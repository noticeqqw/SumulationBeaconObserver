package com.example;

import com.example.controller.PhysicsController;
import com.example.gui.MainWindow;

import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
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
