package view;

import controller.GameController;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GameController gameController;
    private GamePanel gamePanel;
    private HUDPanel hudPanel;
    private JPanel bottomPanel;

    public GameFrame() {
        this.gameController = new GameController();
        initializeFrame();
        createComponents();
        layoutComponents();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Realm War - Kingdom Strategy Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void createComponents() {
        gamePanel = new GamePanel(gameController);
        hudPanel = new HUDPanel(gameController);
        gameController.setHudPanel(hudPanel);
        bottomPanel = createBottomPanel();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        add(gamePanel, BorderLayout.CENTER);

        add(hudPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Game Controls"));
        panel.setPreferredSize(new Dimension(0, 100)); // Smaller height
        panel.setBackground(new Color(240, 240, 240));

        JPanel controlsGrid = new JPanel(new GridLayout(2, 2, 5, 5));
        controlsGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton endTurnButton = new JButton("End Turn");
        JButton saveGameButton = new JButton("Save Game");
        JButton loadGameButton = new JButton("Load Game");
        JButton newGameButton = new JButton("New Game");

        endTurnButton.addActionListener(e -> {
            if (gameController != null) {
                gameController.endTurn();
                gamePanel.clearSelectionHighlights();
                repaint();
                updateDisplay();
            }
        });

        newGameButton.addActionListener(e -> {
            if (gameController != null) {
                new GameFrame();
                dispose();
            }
        });

        saveGameButton.addActionListener(e -> {
            hudPanel.addLogMessage("Game saved");
        });

        loadGameButton.addActionListener(e -> {
            hudPanel.addLogMessage("Game loaded");
        });

        JButton[] buttons = {endTurnButton, newGameButton, saveGameButton, loadGameButton};
        for (JButton button : buttons) {
            button.setFont(new Font("Arial", Font.BOLD, 11));
            button.setPreferredSize(new Dimension(100, 30));
        }

        controlsGrid.add(endTurnButton);
        controlsGrid.add(newGameButton);
        controlsGrid.add(saveGameButton);
        controlsGrid.add(loadGameButton);

        panel.add(controlsGrid, BorderLayout.CENTER);

        return panel;
    }

    public void updateDisplay() {
        gamePanel.repaint();
        hudPanel.updateStats();
    }

}