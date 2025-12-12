package com.example.gui;

import com.example.model.SimulationParameters;
import com.example.observer.SimulationCommands;

import javax.swing.*;
import java.awt.*;

/**
 * Окно настроек параметров симуляции.
 */
public class SettingsWindow extends JDialog {
    
    private final SimulationCommands controller;
    
    private JSpinner massSpinner;
    private JSpinner springConstantSpinner;
    private JSpinner naturalLengthSpinner;
    private JSpinner initialLengthSpinner;
    private JSpinner initialAngleSpinner;
    private JSpinner dampingSpinner;
    private JSpinner gravitySpinner;
    
    public SettingsWindow(JFrame parent, SimulationCommands controller) {
        super(parent, "⚙ Настройки параметров", true);
        this.controller = controller;
        
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        initComponents();
        loadCurrentParameters();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Панель параметров
        JPanel paramsPanel = new JPanel(new GridBagLayout());
        paramsPanel.setBorder(BorderFactory.createTitledBorder("📋 Параметры физической модели"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Масса грузика
        gbc.gridx = 0; gbc.gridy = 0;
        paramsPanel.add(new JLabel("Масса грузика (кг):"), gbc);
        gbc.gridx = 1;
        massSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 100.0, 0.1));
        massSpinner.setPreferredSize(new Dimension(100, 25));
        paramsPanel.add(massSpinner, gbc);
        
        // Жёсткость пружины
        gbc.gridx = 0; gbc.gridy = 1;
        paramsPanel.add(new JLabel("Жёсткость пружины (Н/м):"), gbc);
        gbc.gridx = 1;
        springConstantSpinner = new JSpinner(new SpinnerNumberModel(50.0, 1.0, 1000.0, 1.0));
        springConstantSpinner.setPreferredSize(new Dimension(100, 25));
        paramsPanel.add(springConstantSpinner, gbc);
        
        // Естественная длина пружины
        gbc.gridx = 0; gbc.gridy = 2;
        paramsPanel.add(new JLabel("Естественная длина R₀ (м):"), gbc);
        gbc.gridx = 1;
        naturalLengthSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
        naturalLengthSpinner.setPreferredSize(new Dimension(100, 25));
        paramsPanel.add(naturalLengthSpinner, gbc);
        
        // Начальная длина пружины
        gbc.gridx = 0; gbc.gridy = 3;
        paramsPanel.add(new JLabel("Начальная длина R (м):"), gbc);
        gbc.gridx = 1;
        initialLengthSpinner = new JSpinner(new SpinnerNumberModel(1.5, 0.1, 10.0, 0.1));
        initialLengthSpinner.setPreferredSize(new Dimension(100, 25));
        paramsPanel.add(initialLengthSpinner, gbc);
        
        // Начальный угол
        gbc.gridx = 0; gbc.gridy = 4;
        paramsPanel.add(new JLabel("Начальный угол (градусы):"), gbc);
        gbc.gridx = 1;
        initialAngleSpinner = new JSpinner(new SpinnerNumberModel(30.0, -90.0, 90.0, 5.0));
        initialAngleSpinner.setPreferredSize(new Dimension(100, 25));
        paramsPanel.add(initialAngleSpinner, gbc);
        
        // Коэффициент затухания
        gbc.gridx = 0; gbc.gridy = 5;
        paramsPanel.add(new JLabel("Коэффициент затухания:"), gbc);
        gbc.gridx = 1;
        dampingSpinner = new JSpinner(new SpinnerNumberModel(0.1, 0.0, 10.0, 0.01));
        dampingSpinner.setPreferredSize(new Dimension(100, 25));
        paramsPanel.add(dampingSpinner, gbc);
        
        // Ускорение свободного падения
        gbc.gridx = 0; gbc.gridy = 6;
        paramsPanel.add(new JLabel("Ускорение g (м/с²):"), gbc);
        gbc.gridx = 1;
        gravitySpinner = new JSpinner(new SpinnerNumberModel(9.81, 0.1, 100.0, 0.1));
        gravitySpinner.setPreferredSize(new Dimension(100, 25));
        paramsPanel.add(gravitySpinner, gbc);
        
        mainPanel.add(paramsPanel, BorderLayout.CENTER);
        
        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton applyButton = new JButton("✅ Применить");
        applyButton.addActionListener(e -> applyParameters());
        buttonPanel.add(applyButton);
        
        JButton resetButton = new JButton("🔄 По умолчанию");
        resetButton.addActionListener(e -> resetToDefaults());
        buttonPanel.add(resetButton);
        
        JButton cancelButton = new JButton("❌ Отмена");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadCurrentParameters() {
        SimulationParameters params = controller.getParameters();
        
        massSpinner.setValue(params.getMass());
        springConstantSpinner.setValue(params.getSpringConstant());
        naturalLengthSpinner.setValue(params.getNaturalLength());
        initialLengthSpinner.setValue(params.getInitialLength());
        initialAngleSpinner.setValue(Math.toDegrees(params.getInitialAngle()));
        dampingSpinner.setValue(params.getDamping());
        gravitySpinner.setValue(params.getGravity());
    }
    
    private void applyParameters() {
        SimulationParameters params = new SimulationParameters();
        
        params.setMass((Double) massSpinner.getValue());
        params.setSpringConstant((Double) springConstantSpinner.getValue());
        params.setNaturalLength((Double) naturalLengthSpinner.getValue());
        params.setInitialLength((Double) initialLengthSpinner.getValue());
        params.setInitialAngle(Math.toRadians((Double) initialAngleSpinner.getValue()));
        params.setDamping((Double) dampingSpinner.getValue());
        params.setGravity((Double) gravitySpinner.getValue());
        
        controller.setParameters(params);
        
        JOptionPane.showMessageDialog(this, 
            "Параметры применены. Симуляция сброшена.",
            "✅ Успех", JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
    }
    
    private void resetToDefaults() {
        SimulationParameters defaults = new SimulationParameters();
        
        massSpinner.setValue(defaults.getMass());
        springConstantSpinner.setValue(defaults.getSpringConstant());
        naturalLengthSpinner.setValue(defaults.getNaturalLength());
        initialLengthSpinner.setValue(defaults.getInitialLength());
        initialAngleSpinner.setValue(Math.toDegrees(defaults.getInitialAngle()));
        dampingSpinner.setValue(defaults.getDamping());
        gravitySpinner.setValue(defaults.getGravity());
    }
}
