package com.example.model;

public class PhysicsState {
    private double time;           // Время симуляции (с)
    private double angle;          // Угол отклонения fi (рад)
    private double angularVelocity; // Угловая скорость (рад/с)
    private double springLength;   // Текущая длина пружины R (м)
    private double radialVelocity; // Радиальная скорость (м/с)
    private double x;              // Координата X грузика
    private double y;              // Координата Y грузика
    private double velocity;       // Полная скорость (м/с)
    private double springForce;    // Сила пружины (Н)
    private double gravityForce;   // Сила тяжести (Н)
    private double acceleration;   // Полное ускорение (м/с²)

    public PhysicsState() {}

    public PhysicsState(double time, double angle, double angularVelocity, 
                       double springLength, double radialVelocity,
                       double x, double y, double velocity,
                       double springForce, double gravityForce, double acceleration) {
        this.time = time;
        this.angle = angle;
        this.angularVelocity = angularVelocity;
        this.springLength = springLength;
        this.radialVelocity = radialVelocity;
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.springForce = springForce;
        this.gravityForce = gravityForce;
        this.acceleration = acceleration;
    }

    public double getTime() { return time; }
    public double getAngle() { return angle; }
    public double getAngularVelocity() { return angularVelocity; }
    public double getSpringLength() { return springLength; }
    public double getRadialVelocity() { return radialVelocity; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getVelocity() { return velocity; }
    public double getSpringForce() { return springForce; }
    public double getGravityForce() { return gravityForce; }
    public double getAcceleration() { return acceleration; }

    public void setTime(double time) { this.time = time; }
    public void setAngle(double angle) { this.angle = angle; }
    public void setAngularVelocity(double angularVelocity) { this.angularVelocity = angularVelocity; }
    public void setSpringLength(double springLength) { this.springLength = springLength; }
    public void setRadialVelocity(double radialVelocity) { this.radialVelocity = radialVelocity; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setVelocity(double velocity) { this.velocity = velocity; }
    public void setSpringForce(double springForce) { this.springForce = springForce; }
    public void setGravityForce(double gravityForce) { this.gravityForce = gravityForce; }
    public void setAcceleration(double acceleration) { this.acceleration = acceleration; }

    public double getParameter(String parameterName) {
        return switch (parameterName) {
            case "Время (с)" -> time;
            case "Угол (рад)" -> angle;
            case "Угловая скорость (рад/с)" -> angularVelocity;
            case "Длина пружины (м)" -> springLength;
            case "Радиальная скорость (м/с)" -> radialVelocity;
            case "Координата X (м)" -> x;
            case "Координата Y (м)" -> y;
            case "Скорость (м/с)" -> velocity;
            case "Сила пружины (Н)" -> springForce;
            case "Сила тяжести (Н)" -> gravityForce;
            case "Ускорение (м/с²)" -> acceleration;
            default -> 0;
        };
    }

    public static String[] getAvailableParameters() {
        return new String[] {
            "Угол (рад)",
            "Угловая скорость (рад/с)",
            "Длина пружины (м)",
            "Радиальная скорость (м/с)",
            "Координата X (м)",
            "Координата Y (м)",
            "Скорость (м/с)",
            "Сила пружины (Н)",
            "Сила тяжести (Н)",
            "Ускорение (м/с²)"
        };
    }

    @Override
    public String toString() {
        return String.format("PhysicsState[t=%.3f, angle=%.3f, R=%.3f, v=%.3f]", 
                           time, angle, springLength, velocity);
    }
}
