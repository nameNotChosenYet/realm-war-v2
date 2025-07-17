package org.example;

import org.example.view.GameFrame;
import org.example.view.MainMenu;

import java.security.KeyStore;

public class Main {
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {

            new MainMenu().setVisible(true);

        });
    }
}