package com.example.gui;

import com.example.model.PhysicsState;
import com.example.observer.SimulationCommands;
import com.example.observer.SimulationObserver;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChartWindow extends JFrame implements SimulationObserver {
    
    private final String parameterName;
    
    private XYSeries series;
    private XYLineAndShapeRenderer renderer;
    private static final int MAX_POINTS = 500;
    
    public ChartWindow(SimulationCommands controller, String parameterName) {
        this.parameterName = parameterName;
        
        setTitle("📈 График: " + parameterName);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initChart();
        
        controller.subscribe(this);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.unsubscribe(ChartWindow.this);
            }
        });
    }
    
    private void initChart() {
        series = new XYSeries(parameterName);
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        
        JFreeChart chart = ChartFactory.createXYLineChart(
            parameterName,
            "Время (с)",
            parameterName,
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(0, 100, 200));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(580, 350));
        chartPanel.setMouseWheelEnabled(true);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton clearButton = new JButton("🗑 Очистить");
        clearButton.addActionListener(e -> series.clear());
        controlPanel.add(clearButton);

        controlPanel.add(new JLabel("🎨 Цвет:"));
        JComboBox<String> colorSelector = new JComboBox<>(new String[]{"Синий", "Красный", "Зелёный", "Оранжевый"});
        colorSelector.addActionListener(e -> {
            Color[] colors = {
                new Color(0, 120, 215),
                Color.RED,
                new Color(0, 150, 0),
                Color.ORANGE
            };
            renderer.setSeriesPaint(0, colors[colorSelector.getSelectedIndex()]);
            XYPlot plot1 = (XYPlot) chartPanel.getChart().getPlot();
            plot1.setRenderer(renderer);
            chartPanel.repaint();
        });
        controlPanel.add(colorSelector);
        
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    @Override
    public void onStateUpdate(PhysicsState state) {
        double value = state.getParameter(parameterName);
        double time = state.getTime();
        
        series.add(time, value);
        
        while (series.getItemCount() > MAX_POINTS) {
            series.remove(0);
        }
    }
    
    @Override
    public void onSimulationStarted() {
    }
    
    @Override
    public void onSimulationStopped() {
    }
    
    @Override
    public void onSimulationReset() {
        series.clear();
    }
}
