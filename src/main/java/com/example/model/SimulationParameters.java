package com.example.model;

public class SimulationParameters {
    private double mass;              // Масса грузика (кг)
    private double springConstant;    // Жесткость пружины (Н/м)
    private double naturalLength;     // Естественная длина пружины R0 (м)
    private double initialLength;     // Начальная длина пружины (м)
    private double initialAngle;      // Начальный угол отклонения (рад)
    private double damping;           // Коэффициент затухания
    private double gravity;           // Ускорение свободного падения (м/с²)

    public SimulationParameters() {
        this.mass = 1.0;
        this.springConstant = 50.0;
        this.naturalLength = 1.0;
        this.initialLength = 1.5;
        this.initialAngle = Math.PI / 6;
        this.damping = 0.1;
        this.gravity = 9.81;
    }

    public SimulationParameters(SimulationParameters other) {
        this.mass = other.mass;
        this.springConstant = other.springConstant;
        this.naturalLength = other.naturalLength;
        this.initialLength = other.initialLength;
        this.initialAngle = other.initialAngle;
        this.damping = other.damping;
        this.gravity = other.gravity;
    }

    public double getMass() { return mass; }
    public double getSpringConstant() { return springConstant; }
    public double getNaturalLength() { return naturalLength; }
    public double getInitialLength() { return initialLength; }
    public double getInitialAngle() { return initialAngle; }
    public double getDamping() { return damping; }
    public double getGravity() { return gravity; }

    public void setMass(double mass) { this.mass = mass; }
    public void setSpringConstant(double springConstant) { this.springConstant = springConstant; }
    public void setNaturalLength(double naturalLength) { this.naturalLength = naturalLength; }
    public void setInitialLength(double initialLength) { this.initialLength = initialLength; }
    public void setInitialAngle(double initialAngle) { this.initialAngle = initialAngle; }
    public void setDamping(double damping) { this.damping = damping; }
    public void setGravity(double gravity) { this.gravity = gravity; }

    @Override
    public String toString() {
        return String.format("SimulationParameters[m=%.2f, k=%.2f, R0=%.2f, R=%.2f, fi=%.2f]",
                           mass, springConstant, naturalLength, initialLength, initialAngle);
    }
}
