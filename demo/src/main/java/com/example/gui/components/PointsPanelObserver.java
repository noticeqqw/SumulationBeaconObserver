package com.example.gui.components;

/**
 * Интерфейс Observer из ЛБ3.
 * Используется для получения данных компонентом PointsPanel.
 */
public interface PointsPanelObserver {
    void update(double x, double y);
}
