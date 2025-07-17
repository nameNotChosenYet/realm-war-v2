package org.example.view;

import org.example.controller.GameController;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private GameController gameController;
    private GamePanel gamePanel;
    private HUDPanel hudPanel;
    private JPanel bottomPanel;

    private Timer turnTimer;
    private JLabel timerLabel;
    private int timeLeft;
    private static final int TURN_DURATION = 30;

    private JLabel buildImageLabel;
    private JLabel buildNameLabel;
    private JLabel entityOwnerLabel;
    private JLabel entityLevelLabel;
    private JLabel entityLevelUpCostLabel;
    private JProgressBar entityHealthBar;
    private JPanel centerInfoPanel;
    private JButton upgradeButton;

    public GameFrame() {
        this.gameController = new GameController();
        this.gameController.setGameFrame(this);

        initializeFrame();
        createComponents();
        layoutComponents();
        setVisible(true);

        resetTurnTimer();
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
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setPreferredSize(new Dimension(0, 120));
        panel.setBackground(Theme.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Theme.BORDER_COLOR));

        JPanel leftPanel = new JPanel(new BorderLayout(0, 5));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buildImageLabel = new JLabel();
        buildImageLabel.setPreferredSize(new Dimension(64, 64));
        buildImageLabel.setOpaque(true);
        buildImageLabel.setBackground(Theme.BACKGROUND);
        buildImageLabel.setBorder(BorderFactory.createLineBorder(Theme.ACCENT_COLOR, 1));
        buildImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buildNameLabel = new JLabel("No Selection", SwingConstants.CENTER);
        buildNameLabel.setFont(Theme.getFont(18f));
        buildNameLabel.setForeground(Theme.TEXT_COLOR);
        leftPanel.add(buildImageLabel, BorderLayout.CENTER);
        leftPanel.add(buildNameLabel, BorderLayout.SOUTH);

        centerInfoPanel = new JPanel(new BorderLayout(0, 5));
        centerInfoPanel.setOpaque(false);
        centerInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        entityHealthBar = new JProgressBar();
        entityHealthBar.setForeground(new Color(180, 50, 50));
        entityHealthBar.setBorderPainted(false);
        entityHealthBar.setStringPainted(true);
        entityHealthBar.setFont(Theme.getFont(12f));

        entityOwnerLabel = new JLabel("Owner: N/A");
        entityLevelLabel = new JLabel("Level: N/A");
        Font infoFont = Theme.getFont(14f);
        entityOwnerLabel.setFont(infoFont);
        entityOwnerLabel.setForeground(Theme.TEXT_COLOR);
        entityLevelLabel.setFont(infoFont);
        entityLevelLabel.setForeground(Theme.TEXT_COLOR);

        JPanel statsGrid = new JPanel(new GridLayout(1, 2, 10, 0));
        statsGrid.setOpaque(false);
        statsGrid.add(entityOwnerLabel);
        statsGrid.add(entityLevelLabel);

        upgradeButton = createThemedButton("Upgrade");
        upgradeButton.addActionListener(e -> gameController.upgradeSelectedEntity());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(entityHealthBar, BorderLayout.NORTH);
        topPanel.add(statsGrid, BorderLayout.CENTER);

        centerInfoPanel.add(topPanel, BorderLayout.NORTH);
        centerInfoPanel.add(upgradeButton, BorderLayout.SOUTH);
        centerInfoPanel.setVisible(false);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JButton endTurnButton = new JButton("End Turn");
        endTurnButton.setFont(Theme.getFont(20f));
        endTurnButton.setBackground(Theme.ACCENT_COLOR);
        endTurnButton.setForeground(Theme.BACKGROUND);
        endTurnButton.setFocusPainted(false);
        endTurnButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        endTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endTurnButton.addActionListener(e -> gameController.endTurn());
        timerLabel = new JLabel("00:30");
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        timerLabel.setForeground(Theme.TEXT_COLOR);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(endTurnButton);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(timerLabel);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(centerInfoPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    public void updateBuildSelectionInfo(String name, String iconPath) {
        if (name == null || iconPath == null) {
            clearBuildSelectionInfo();
            return;
        }
        buildNameLabel.setText(name);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            buildImageLabel.setIcon(new ImageIcon(img));
            buildImageLabel.setText("");
        } catch (Exception e) {
            buildImageLabel.setIcon(null);
            buildImageLabel.setText("?");
        }
    }


    private JButton createThemedButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Theme.getFont(14f));
        button.setBackground(Theme.PANEL_BACKGROUND);
        button.setForeground(Theme.TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        return button;
    }
    public void clearBuildSelectionInfo() {
        buildNameLabel.setText("No Selection");
        buildImageLabel.setIcon(null);
        buildImageLabel.setText("");
    }

    public void updateEntityInfo(String name, String iconPath, String owner, int durability, int maxDurability, int level, boolean canUpgrade, int upgradeCost) {
        centerInfoPanel.setVisible(true);
        updateBuildSelectionInfo(name, iconPath);
        entityOwnerLabel.setText("Owner: " + owner);
        entityLevelLabel.setText("Level: " + level);
        entityHealthBar.setMaximum(maxDurability);
        entityHealthBar.setValue(durability);
        entityHealthBar.setString(durability + " / " + maxDurability + " HP");

        if (canUpgrade) {
            upgradeButton.setEnabled(true);
            upgradeButton.setText("Upgrade (" + upgradeCost + "g)");
        } else {
            upgradeButton.setEnabled(false);
            upgradeButton.setText("Max Level");
        }
    }

    public void clearEntityInfo() {
        centerInfoPanel.setVisible(false);
        clearBuildSelectionInfo();
    }

    public void onTurnEnded() { resetTurnTimer(); }
    private void resetTurnTimer() {
        if (turnTimer != null && turnTimer.isRunning()) turnTimer.stop();
        timeLeft = TURN_DURATION;
        updateTimerLabel();
        turnTimer = new Timer(1000, e -> {
            timeLeft--;
            updateTimerLabel();
            if (timeLeft < 0) ((Timer)e.getSource()).stop();
        });
        turnTimer.setInitialDelay(0);
        turnTimer.start();
    }
    private void updateTimerLabel() {
        timerLabel.setText(String.format(java.util.Locale.US, "%02d:%02d", timeLeft / 60, timeLeft % 60));
    }
    public void updateDisplay() {
        gamePanel.repaint();
        hudPanel.updateStats();
    }
}
