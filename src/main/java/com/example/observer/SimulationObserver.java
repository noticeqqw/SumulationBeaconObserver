package com.example.observer;

import com.example.model.PhysicsState;

public interface SimulationObserver {
    
    /** @param state */
    void onStateUpdate(PhysicsState state);
    
    void onSimulationStarted();

    void onSimulationStopped();
    
    void onSimulationReset();
}
