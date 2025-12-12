package com.example.gui;

import com.example.model.PhysicsState;
import com.example.observer.SimulationCommands;
import com.example.observer.SimulationObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/**
 * Главное окно приложения с визуализацией грузика на пружине.
 */
public class MainWindow extends JFrame implements SimulationObserver {
    
    private final SimulationCommands controller;
    private final SimulationPanel simulationPanel;
    
    public MainWindow(SimulationCommands controller) {
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        simulationPanel = new SimulationPanel();
        simulationPanel.setPreferredSize(new Dimension(600, 500));
        simulationPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        mainPanel.add(simulationPanel, BorderLayout.CENTER);
        
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.EAST);
        
        add(mainPanel);
        
        controller.subscribe(this);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.unsubscribe(MainWindow.this);
            }
        });
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        panel.setPreferredSize(new Dimension(300, 0));
        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("🎮 Управление"));
        
        JButton start = new JButton("▶ Старт");
        start.addActionListener(e -> controller.start());
        buttonPanel.add(start);
        
        JButton stop = new JButton("⏸ Стоп");
        stop.addActionListener(e -> controller.stop());
        buttonPanel.add(stop);
        
        JButton reset = new JButton("↺ Сброс");
        reset.addActionListener(e -> controller.reset());
        buttonPanel.add(reset);
        
        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(10));
        
        JButton settingsButton = new JButton("⚙ Настройки параметров");
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.addActionListener(e -> openSettingsWindow());
        panel.add(settingsButton);
        
        panel.add(Box.createVerticalStrut(20));
        
        JPanel subscribePanel = new JPanel();
        subscribePanel.setLayout(new BoxLayout(subscribePanel, BoxLayout.Y_AXIS));
        subscribePanel.setBorder(BorderFactory.createTitledBorder("📊 Подписка на параметр"));
        subscribePanel.setMaximumSize(new Dimension(200, 200));
        
        subscribePanel.add(Box.createVerticalStrut(5));
        
        subscribePanel.add(new JLabel("Параметр:"));
        JComboBox<String> paramSelector = new JComboBox<>(PhysicsState.getAvailableParameters());
        paramSelector.setMaximumSize(new Dimension(180, 25));
        subscribePanel.add(paramSelector);
        
        subscribePanel.add(Box.createVerticalStrut(8));
        
        subscribePanel.add(new JLabel("Тип графика:"));
        JComboBox<String> typeSelector = new JComboBox<>(new String[] {
            "JFreeChart график",
            "PointsPanel (ЛБ3)"
        });
        typeSelector.setMaximumSize(new Dimension(180, 25));
        subscribePanel.add(typeSelector);
        
        subscribePanel.add(Box.createVerticalStrut(10));
        
        JButton subscribeButton = new JButton("📈 Подписаться");
        subscribeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        subscribeButton.addActionListener(e -> createSubscriber(
            (String) paramSelector.getSelectedItem(),
            typeSelector.getSelectedIndex()
        ));
        subscribePanel.add(Box.createVerticalStrut(5));
        subscribePanel.add(subscribeButton);
        
        panel.add(subscribePanel);
        
        panel.add(Box.createVerticalStrut(20));
        
        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        speedPanel.setBorder(BorderFactory.createTitledBorder("⚡ Скорость"));
        speedPanel.add(new JLabel("Множитель:"));
        JSlider speedSlider = new JSlider(1, 50, 10);
        speedSlider.addChangeListener(e -> {
            double speed = speedSlider.getValue() / 10.0;
            controller.setSimulationSpeed(speed);
        });
        speedPanel.add(speedSlider);
        panel.add(speedPanel);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private void openSettingsWindow() {
        SettingsWindow settingsWindow = new SettingsWindow(this, controller);
        settingsWindow.setVisible(true);
    }
    
    private void createSubscriber(String parameter, int type) {
        switch (type) {
            case 0 -> { // JFreeChart график
                ChartWindow chartWindow = new ChartWindow(controller, parameter);
                chartWindow.setVisible(true);
            }
            case 1 -> { // Собственный компонент (точки)
                PointsChartWindow pointsWindow = new PointsChartWindow(controller, parameter);
                pointsWindow.setVisible(true);
            }
        }
    }
    
    @Override
    public void onStateUpdate(PhysicsState state) {
        simulationPanel.setState(state);
        simulationPanel.repaint();
    }
    
    @Override
    public void onSimulationStarted() {
    }
    
    @Override
    public void onSimulationStopped() {
    }
    
    @Override
    public void onSimulationReset() {
    }
    
    private static class SimulationPanel extends JPanel {
        private PhysicsState state;
        private static final int SCALE = 150;
        
        public void setState(PhysicsState state) {
            this.state = state;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int centerX = getWidth() / 2;
            int pivotY = 50;
            
            g2d.setColor(new Color(240, 248, 255));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(centerX - 30, pivotY - 10, 60, 10);
            
            if (state == null) {
                drawSpring(g2d, centerX, pivotY, centerX, pivotY + 150, 10);
                g2d.setColor(Color.RED);
                g2d.fill(new Ellipse2D.Double(centerX - 20, pivotY + 150 - 20, 40, 40));
                return;
            }
            
            int massX = centerX + (int) (state.getX() * SCALE);
            int massY = pivotY + (int) (state.getY() * SCALE);
            
            drawSpring(g2d, centerX, pivotY, massX, massY, 15);
            
            int massRadius = 20;
            g2d.setColor(Color.RED);
            g2d.fill(new Ellipse2D.Double(massX - massRadius, massY - massRadius, 
                                          massRadius * 2, massRadius * 2));
            g2d.setColor(Color.DARK_GRAY);
            g2d.draw(new Ellipse2D.Double(massX - massRadius, massY - massRadius, 
                                          massRadius * 2, massRadius * 2));
        }
        
        private void drawSpring(Graphics2D g2d, int x1, int y1, int x2, int y2, int coils) {
            double dx = x2 - x1;
            double dy = y2 - y1;
            double length = Math.sqrt(dx * dx + dy * dy);
            double angle = Math.atan2(dy, dx);
            
            Path2D spring = new Path2D.Double();
            spring.moveTo(x1, y1);
            
            double coilWidth = 10;
            double segmentLength = length / (coils * 2 + 2);
            
            double perpX = -Math.sin(angle);
            double perpY = Math.cos(angle);
            
            for (int i = 0; i <= coils * 2; i++) {
                double t = (i + 1) * segmentLength;
                double px = x1 + t * Math.cos(angle);
                double py = y1 + t * Math.sin(angle);
                
                if (i > 0 && i <= coils * 2 - 1) {
                    double offset = (i % 2 == 1) ? coilWidth : -coilWidth;
                    px += perpX * offset;
                    py += perpY * offset;
                }
                
                spring.lineTo(px, py);
            }
            
            spring.lineTo(x2, y2);
            
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(spring);
        }
    }
}
