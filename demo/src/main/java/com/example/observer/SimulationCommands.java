package com.example.observer;

import com.example.model.SimulationParameters;

/**
 * Интерфейс команд для управления контроллером симуляции.
 * Предоставляет полный набор возможностей управления физическим объектом.
 */
public interface SimulationCommands {
    
    /**
     * Запустить симуляцию.
     */
    void start();
    
    /**
     * Остановить (пауза) симуляцию.
     */
    void stop();
    
    /**
     * Сбросить симуляцию в начальное состояние.
     */
    void reset();
    
    /**
     * Установить параметры симуляции.
     * @param params Новые параметры симуляции
     */
    void setParameters(SimulationParameters params);
    
    /**
     * Получить текущие параметры симуляции.
     * @return Текущие параметры
     */
    SimulationParameters getParameters();
    
    /**
     * Проверить, запущена ли симуляция.
     * @return true если симуляция запущена
     */
    boolean isRunning();
    
    /**
     * Установить скорость симуляции (множитель времени).
     * @param speed Множитель скорости (1.0 = реальное время)
     */
    void setSimulationSpeed(double speed);
    
    /**
     * Получить текущую скорость симуляции.
     * @return Множитель скорости
     */
    double getSimulationSpeed();
    
    /**
     * Подписаться на события симуляции.
     * @param observer Наблюдатель
     */
    void subscribe(SimulationObserver observer);
    
    /**
     * Отписаться от событий симуляции.
     * @param observer Наблюдатель
     */
    void unsubscribe(SimulationObserver observer);
}
