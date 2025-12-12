package com.example;

import com.example.controller.PhysicsController;
import com.example.gui.MainWindow;

import javax.swing.*;

/**
 * Главный класс приложения.
 * Лабораторная работа №4: Физическая модель "Грузик на пружине"
 */
public class Main {
    public static void main(String[] args) {
        // Установка системного Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Используем стандартный L&F
        }
        
        // Запуск GUI в потоке событий Swing
        SwingUtilities.invokeLater(() -> {
            PhysicsController controller = new PhysicsController();
            MainWindow mainWindow = new MainWindow(controller);
            
            mainWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    controller.shutdown();
                }
            });
            
            mainWindow.setVisible(true);
        });
    }
}
