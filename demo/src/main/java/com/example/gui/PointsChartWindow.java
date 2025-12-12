package com.example.gui;

import com.example.model.PhysicsState;
import com.example.observer.SimulationCommands;
import com.example.observer.SimulationObserver;
import com.example.gui.components.PointsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Окно с компонентом PointsPanel из ЛБ3 для отображения точек.
 */
public class PointsChartWindow extends JFrame implements SimulationObserver {
    
    private final String parameterName;
    private final PointsPanel pointsPanel;
    
    // Для автоматического масштабирования
    private double minTime = 0, maxTime = 10;
    private double minValue = -5, maxValue = 5;
    private boolean autoScale = true;
    
    public PointsChartWindow(SimulationCommands controller, String parameterName) {
        this.parameterName = parameterName;
        
        setTitle("📊 ЛБ3 PointsPanel: " + parameterName);
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Создание компонента PointsPanel из ЛБ3
        pointsPanel = new PointsPanel(550, 350, minTime, maxTime, minValue, maxValue, 500);
        
        // Панель управления
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JButton clearButton = new JButton("🗑 Очистить");
        clearButton.addActionListener(e -> {
            pointsPanel.clearPoints();
            minTime = 0; maxTime = 10;
            minValue = -5; maxValue = 5;
            pointsPanel.setRange(minTime, maxTime, minValue, maxValue);
        });
        controlPanel.add(clearButton);
        
        JCheckBox autoScaleBox = new JCheckBox("Авто-масштаб", true);
        autoScaleBox.addActionListener(e -> autoScale = autoScaleBox.isSelected());
        controlPanel.add(autoScaleBox);
        
        controlPanel.add(new JLabel("🎨 Цвет:"));
        JComboBox<String> colorSelector = new JComboBox<>(new String[]{"Синий", "Красный", "Зелёный", "Оранжевый"});
        colorSelector.addActionListener(e -> {
            Color[] colors = {new Color(0, 120, 215), Color.RED, new Color(0, 150, 0), Color.ORANGE};
            pointsPanel.setPointColor(colors[colorSelector.getSelectedIndex()]);
        });
        controlPanel.add(colorSelector);
        
        // Заголовок
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("📊 Компонент PointsPanel из ЛБ3: " + parameterName);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        headerPanel.add(titleLabel);
        
        // Компоновка
        setLayout(new BorderLayout(5, 5));
        add(headerPanel, BorderLayout.NORTH);
        add(pointsPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        // Подписка на контроллер
        controller.subscribe(this);
        
        // Отписка при закрытии
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.unsubscribe(PointsChartWindow.this);
            }
        });
    }
    
    @Override
    public void onStateUpdate(PhysicsState state) {
        double value = state.getParameter(parameterName);
        double time = state.getTime();
        
        // Автоматическое масштабирование диапазона
        if (autoScale) {
            boolean rangeChanged = false;
            
            if (time > maxTime) {
                maxTime = time * 1.2;
                minTime = Math.max(0, time - (maxTime - minTime) * 0.8);
                rangeChanged = true;
            }
            
            if (value < minValue) {
                minValue = value * 1.2;
                rangeChanged = true;
            }
            if (value > maxValue) {
                maxValue = value * 1.2;
                rangeChanged = true;
            }
            
            if (rangeChanged) {
                pointsPanel.setRange(minTime, maxTime, minValue, maxValue);
            }
        }
        
        // Добавление точки через метод update (Observer паттерн из ЛБ3)
        pointsPanel.update(time, value);
    }
    
    @Override
    public void onSimulationStarted() {
    }
    
    @Override
    public void onSimulationStopped() {
    }
    
    @Override
    public void onSimulationReset() {
        pointsPanel.clearPoints();
        minTime = 0; maxTime = 10;
        minValue = -5; maxValue = 5;
        pointsPanel.setRange(minTime, maxTime, minValue, maxValue);
    }
}
