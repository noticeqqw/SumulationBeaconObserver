package com.example.controller;

import com.example.model.PhysicsState;
import com.example.model.SimulationParameters;
import com.example.observer.SimulationCommands;
import com.example.observer.SimulationObserver;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PhysicsController implements SimulationCommands, Runnable {

    private final List<SimulationObserver> observers = new CopyOnWriteArrayList<>();
    private SimulationParameters parameters;
    
    private double time;
    private double angle;           // fi - угол отклонения от вертикали
    private double angularVelocity; // d(fi)/dt
    private double springLength;    // R - текущая длина пружины
    private double radialVelocity;  // dR/dt
    
    private volatile boolean running = false;
    private volatile boolean alive = true;
    private double simulationSpeed = 1.0;
    private Thread simulationThread;
    
    private static final double DT = 0.001;        // Шаг интегрирования (с)
    private static final int UPDATE_INTERVAL = 16; // Интервал обновления GUI (мс) ~60 FPS
    
    public PhysicsController() {
        this.parameters = new SimulationParameters();
        resetState();
        
        simulationThread = new Thread(this, "PhysicsSimulation");
        simulationThread.setDaemon(true);
        simulationThread.start();
    }
    
    private void resetState() {
        time = 0;
        angle = parameters.getInitialAngle();
        angularVelocity = 0;
        springLength = parameters.getInitialLength();
        radialVelocity = 0;
    }
    
    @Override
    public void run() {
        long lastUpdateTime = System.currentTimeMillis();
        
        while (alive) {
            if (running) {
                long currentTime = System.currentTimeMillis();
                long elapsed = currentTime - lastUpdateTime;
                
                if (elapsed >= UPDATE_INTERVAL) {

                    double simulatedTime = elapsed / 1000.0 * simulationSpeed;
                    int steps = (int) (simulatedTime / DT);
                    
                    for (int i = 0; i < steps; i++) {
                        integrate(DT);
                    }
                    
                    notifyStateUpdate();
                    lastUpdateTime = currentTime;
                }
            } else {
                lastUpdateTime = System.currentTimeMillis();
            }
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void integrate(double dt) {
        
        double m = parameters.getMass();
        double k = parameters.getSpringConstant();
        double R0 = parameters.getNaturalLength();
        double g = parameters.getGravity();
        double c = parameters.getDamping();
        
        double[] y = {springLength, radialVelocity, angle, angularVelocity};
        
        double[] k1 = derivatives(y, m, k, R0, g, c);
        double[] y1 = add(y, mult(k1, dt / 2));
        
        double[] k2 = derivatives(y1, m, k, R0, g, c);
        double[] y2 = add(y, mult(k2, dt / 2));
        
        double[] k3 = derivatives(y2, m, k, R0, g, c);
        double[] y3 = add(y, mult(k3, dt));
        
        double[] k4 = derivatives(y3, m, k, R0, g, c);
        
        double[] sum = add(k1, mult(k2, 2));
        sum = add(sum, mult(k3, 2));
        sum = add(sum, k4);
        
        y = add(y, mult(sum, dt / 6));
        
        springLength = Math.max(0.1, y[0]);
        radialVelocity = y[1];
        angle = y[2];
        angularVelocity = y[3];
        time += dt;
    }
    
    private double[] derivatives(double[] y, double m, double k, double R0, double g, double c) {
        double R = y[0];
        double dR = y[1];
        double fi = y[2];
        double dfi = y[3];
        
        double dR_dt = dR;
        
        double d2R_dt2 = R * dfi * dfi - (k / m) * (R - R0) + g * Math.cos(fi) - (c / m) * dR;
        
        double dfi_dt = dfi;
        
        double d2fi_dt2 = -g * Math.sin(fi) / R - 2 * (dR / R) * dfi - (c / m) * dfi;
        
        return new double[] {dR_dt, d2R_dt2, dfi_dt, d2fi_dt2};
    }
    
    private double[] add(double[] a, double[] b) {
        return new double[] {a[0] + b[0], a[1] + b[1], a[2] + b[2], a[3] + b[3]};
    }
    
    private double[] mult(double[] a, double s) {
        return new double[] {a[0] * s, a[1] * s, a[2] * s, a[3] * s};
    }
    

    private PhysicsState createCurrentState() {
        double m = parameters.getMass();
        double k = parameters.getSpringConstant();
        double R0 = parameters.getNaturalLength();
        double g = parameters.getGravity();
        
        double x = springLength * Math.sin(angle);
        double y = springLength * Math.cos(angle);
        
        double vx = radialVelocity * Math.sin(angle) + springLength * angularVelocity * Math.cos(angle);
        double vy = radialVelocity * Math.cos(angle) - springLength * angularVelocity * Math.sin(angle);
        double velocity = Math.sqrt(vx * vx + vy * vy);
        
        double springForce = k * (springLength - R0);
        double gravityForce = m * g;
        
        double radialAcc = springLength * angularVelocity * angularVelocity 
                          - (k / m) * (springLength - R0) + g * Math.cos(angle);
        double tangentialAcc = -g * Math.sin(angle) * springLength;
        double acceleration = Math.sqrt(radialAcc * radialAcc + tangentialAcc * tangentialAcc);
        
        return new PhysicsState(time, angle, angularVelocity, springLength, radialVelocity,
                               x, y, velocity, springForce, gravityForce, acceleration);
    }
    
    private void notifyStateUpdate() {
        PhysicsState state = createCurrentState();
        SwingUtilities.invokeLater(() -> {
            for (SimulationObserver observer : observers) {
                observer.onStateUpdate(state);
            }
        });
    }
    
    private void notifySimulationStarted() {
        SwingUtilities.invokeLater(() -> {
            for (SimulationObserver observer : observers) {
                observer.onSimulationStarted();
            }
        });
    }
    
    private void notifySimulationStopped() {
        SwingUtilities.invokeLater(() -> {
            for (SimulationObserver observer : observers) {
                observer.onSimulationStopped();
            }
        });
    }
    
    private void notifySimulationReset() {
        SwingUtilities.invokeLater(() -> {
            for (SimulationObserver observer : observers) {
                observer.onSimulationReset();
            }
        });
    }
    
    @Override
    public void start() {
        if (!running) {
            running = true;
            notifySimulationStarted();
        }
    }
    
    @Override
    public void stop() {
        if (running) {
            running = false;
            notifySimulationStopped();
        }
    }
    
    @Override
    public void reset() {
        boolean wasRunning = running;
        running = false;
        resetState();
        notifySimulationReset();
        notifyStateUpdate();
        if (wasRunning) {
            running = true;
        }
    }
    
    @Override
    public void setParameters(SimulationParameters params) {
        this.parameters = new SimulationParameters(params);
        reset();
    }
    
    @Override
    public SimulationParameters getParameters() {
        return new SimulationParameters(parameters);
    }
    
    @Override
    public boolean isRunning() {
        return running;
    }
    
    @Override
    public void setSimulationSpeed(double speed) {
        this.simulationSpeed = Math.max(0.1, Math.min(10.0, speed));
    }
    
    @Override
    public double getSimulationSpeed() {
        return simulationSpeed;
    }
    
    @Override
    public void subscribe(SimulationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    @Override
    public void unsubscribe(SimulationObserver observer) {
        observers.remove(observer);
    }
  
    public void shutdown() {
        alive = false;
        running = false;
        if (simulationThread != null) {
            simulationThread.interrupt();
        }
    }
}
