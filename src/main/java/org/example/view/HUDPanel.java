package org.example.view;

import org.example.controller.GameController;
import org.example.model.Kingdom;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HUDPanel extends JPanel {
    private GameController gameController;
    private JLabel player1GoldLabel, player1FoodLabel, player1UnitsLabel, player1StructuresLabel;
    private JLabel player2GoldLabel, player2FoodLabel, player2UnitsLabel, player2StructuresLabel;
    private JLabel currentPlayerLabel;
    private JTextArea gameLogArea;

    private static final Map<String, Integer> UNIT_COSTS = new HashMap<>();
    private static final Map<String, Integer> STRUCTURE_COSTS = new HashMap<>();

    static {
        UNIT_COSTS.put("Knight", 20);
        UNIT_COSTS.put("Peasant", 5);
        UNIT_COSTS.put("SpearMan", 10);
        UNIT_COSTS.put("SwordMan", 15);

        STRUCTURE_COSTS.put("Barrack", 20);
        STRUCTURE_COSTS.put("Farm", 10);
        STRUCTURE_COSTS.put("Market", 10);
        STRUCTURE_COSTS.put("Tower", 10);
    }

    public HUDPanel(GameController gameController) {
        this.gameController = gameController;
        initializePanel();
        layoutComponents();
        updateStats();
    }

    private void initializePanel() {
        setPreferredSize(new Dimension(280, 0));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Theme.BORDER_COLOR));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void layoutComponents() {
        add(createCurrentPlayerPanel());
        add(Box.createVerticalStrut(15));

        player1GoldLabel = new JLabel("0");
        player1FoodLabel = new JLabel("0");
        player1UnitsLabel = new JLabel("0");
        player1StructuresLabel = new JLabel("0");
        player2GoldLabel = new JLabel("0");
        player2FoodLabel = new JLabel("0");
        player2UnitsLabel = new JLabel("0");
        player2StructuresLabel = new JLabel("0");

        JPanel player1StatsPanel = createPlayerStatsPanel("Player 1", new Color(220, 80, 80), player1GoldLabel, player1FoodLabel, player1UnitsLabel, player1StructuresLabel);
        player1StatsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(player1StatsPanel);
        add(Box.createVerticalStrut(10));

        JPanel player2StatsPanel = createPlayerStatsPanel("Player 2", new Color(60, 120, 220), player2GoldLabel, player2FoodLabel, player2UnitsLabel, player2StructuresLabel);
        player2StatsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(player2StatsPanel);

        add(Box.createVerticalStrut(15));

        JButton clearUnitButton = createClearButton();
        clearUnitButton.addActionListener(e -> {
            gameController.clearSelectedUnitType();
            addLogMessage("Unit selection cleared.");
        });

        JButton clearStructureButton = createClearButton();
        clearStructureButton.addActionListener(e -> {
            gameController.clearSelectedStructureType();
            addLogMessage("Structure selection cleared.");
        });

        add(createTitledPanel("Build Units", createUnitSelectionPanel(), clearUnitButton));
        add(Box.createVerticalStrut(10));
        add(createTitledPanel("Build Structures", createStructureSelectionPanel(), clearStructureButton));
        add(Box.createVerticalStrut(15));

        add(createTitledPanel("Game Log", createGameLogPanel(), null));

        add(Box.createVerticalGlue());
    }

    private JPanel createTitledPanel(String title, JComponent content, JButton actionButton) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setOpaque(false);
        titleContainer.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 5));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.getFont(16f));
        titleLabel.setForeground(Theme.ACCENT_COLOR);
        titleContainer.add(titleLabel, BorderLayout.WEST);

        if (actionButton != null) {
            titleContainer.add(actionButton, BorderLayout.EAST);
        }

        panel.add(titleContainer, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCurrentPlayerPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        currentPlayerLabel = new JLabel("Current Turn: Player 1");
        currentPlayerLabel.setFont(Theme.getFont(18f));
        currentPlayerLabel.setForeground(Theme.TEXT_COLOR);
        panel.add(currentPlayerLabel);
        return panel;
    }

    private JPanel createPlayerStatsPanel(String playerName, Color playerColor,
                                          JLabel gold, JLabel food, JLabel units, JLabel structures) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JLabel nameLabel = new JLabel(playerName);
        nameLabel.setFont(Theme.getFont(16f));
        nameLabel.setForeground(playerColor);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(5));

        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 8, 8));
        statsGrid.setOpaque(false);

        statsGrid.add(createStatDisplay("gold_icon.png", gold));
        statsGrid.add(createStatDisplay("food_icon.png", food));
        statsGrid.add(createStatDisplay("units_icon.png", units));
        statsGrid.add(createStatDisplay("structure_icon.png", structures));
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(statsGrid);
        return panel;
    }

    private JPanel createStatDisplay(String iconName, JLabel label) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Images/" + iconName));
            JLabel iconLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
            panel.add(iconLabel);
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconName);
        }

        label.setFont(Theme.getFont(14f));
        label.setForeground(Theme.TEXT_COLOR);
        panel.add(label);
        return panel;
    }

    private JScrollPane createGameLogPanel() {
        gameLogArea = new JTextArea(4, 20);
        gameLogArea.setEditable(false);
        gameLogArea.setFont(Theme.getFont(11f));
        gameLogArea.setBackground(Theme.PANEL_BACKGROUND);
        gameLogArea.setForeground(Theme.TEXT_COLOR);
        gameLogArea.setLineWrap(true);
        gameLogArea.setWrapStyleWord(true);
        gameLogArea.setMargin(new Insets(5, 5, 5, 5));
        gameLogArea.setText("Game Started!\nPlayer 1's turn.\n");

        JScrollPane scrollPane = new JScrollPane(gameLogArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private JPanel createUnitSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        panel.setOpaque(false);

        String[] units = {"Knight", "Peasant", "SpearMan", "SwordMan"};

        for (String unit : units) {
            int cost = UNIT_COSTS.getOrDefault(unit, 0);
            JButton unitButton = createThemedButton(unit + " (" + cost + "g)");
            unitButton.addActionListener(e -> {
                gameController.selectUnitType(unit);
                addLogMessage("Selected unit: " + unit + " (Cost: " + cost + "g)");
            });
            panel.add(unitButton);
        }
        return panel;
    }

    private JPanel createStructureSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        panel.setOpaque(false);

        String[] structures = {"Barrack", "Farm", "Market", "Tower"};
        for (String structure : structures) {
            int cost = STRUCTURE_COSTS.getOrDefault(structure, 0);
            JButton structureButton = createThemedButton(structure + " (" + cost + "g)");
            structureButton.addActionListener(e -> {
                gameController.selectStructureType(structure);
                addLogMessage("Selected structure: " + structure + " (Cost: " + cost + "g)");
            });
            panel.add(structureButton);
        }
        return panel;
    }

    private JButton createClearButton() {
        JButton button = new JButton("Clear");
        button.setFont(Theme.getFont(10f));
        button.setBackground(Theme.PANEL_BACKGROUND);
        button.setForeground(Theme.TEXT_COLOR);
        button.setFocusPainted(false);
        button.setMargin(new Insets(1, 4, 1, 4));
        button.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1));
        return button;
    }

    private JButton createThemedButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Theme.getFont(12f));
        button.setBackground(Theme.PANEL_BACKGROUND);
        button.setForeground(Theme.TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return button;
    }

    public void updateStats() {
        if (gameController != null && gameController.getKingdom1() != null && gameController.getKingdom2() != null) {
            Kingdom kingdom1 = gameController.getKingdom1();
            Kingdom kingdom2 = gameController.getKingdom2();

            player1GoldLabel.setText(String.valueOf(kingdom1.getGold()));
            player1FoodLabel.setText(String.valueOf(kingdom1.getFood()));
            player1UnitsLabel.setText(String.valueOf(kingdom1.getUnits().size()));
            player1StructuresLabel.setText(String.valueOf(kingdom1.getStructures().size()));

            player2GoldLabel.setText(String.valueOf(kingdom2.getGold()));
            player2FoodLabel.setText(String.valueOf(kingdom2.getFood()));
            player2UnitsLabel.setText(String.valueOf(kingdom2.getUnits().size()));
            player2StructuresLabel.setText(String.valueOf(kingdom2.getStructures().size()));
        }
    }

    public void addLogMessage(String message) {
        gameLogArea.append(message + "\n");
        gameLogArea.setCaretPosition(gameLogArea.getDocument().getLength());
    }

    public void setCurrentPlayer(String playerName) {
        currentPlayerLabel.setText("Turn: " + playerName);
    }
}
