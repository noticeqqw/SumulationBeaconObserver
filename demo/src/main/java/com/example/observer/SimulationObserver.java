package com.example.observer;

import com.example.model.PhysicsState;

/**
 * Интерфейс наблюдателя (Observer) для получения данных от контроллера симуляции.
 * Реализует паттерн "Наблюдатель".
 */
public interface SimulationObserver {
    
    /**
     * Вызывается при обновлении состояния симуляции.
     * @param state Текущее состояние физической системы
     */
    void onStateUpdate(PhysicsState state);
    
    /**
     * Вызывается при запуске симуляции.
     */
    void onSimulationStarted();
    
    /**
     * Вызывается при остановке симуляции.
     */
    void onSimulationStopped();
    
    /**
     * Вызывается при сбросе симуляции.
     */
    void onSimulationReset();
}
