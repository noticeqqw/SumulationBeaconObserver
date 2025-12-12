package com.example.observer;

import com.example.model.SimulationParameters;

public interface SimulationCommands {
    
    void start();
    void stop();
    void reset();
    
    /** @param params */
    void setParameters(SimulationParameters params);
    
    /** @return */
    SimulationParameters getParameters();
    
    /** @return */
    boolean isRunning();
    
    /** @param speed */
    void setSimulationSpeed(double speed);
    
    /** @return */
    double getSimulationSpeed();
    
    /** @param observer */
    void subscribe(SimulationObserver observer);
    
    /** @param observer */
    void unsubscribe(SimulationObserver observer);
}
