package view;

import controller.GameController;
import model.Kingdom;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HUDPanel extends JPanel {
    private GameController gameController;
    private JLabel player1GoldLabel, player1FoodLabel, player1UnitsLabel, player1StructuresLabel;
    private JLabel player2GoldLabel, player2FoodLabel, player2UnitsLabel, player2StructuresLabel;
    private JLabel currentPlayerLabel;
    private JTextArea gameLogArea;

    public HUDPanel(GameController gameController) {
        this.gameController = gameController;
        initializePanel();
        createComponents();
        layoutComponents();
        updateStats();
    }

    private void initializePanel() {
        setPreferredSize(new Dimension(300, 0));
        setBackground(new Color(248, 248, 255));
        setBorder(BorderFactory.createTitledBorder("Game Statistics"));
    }

    private void createComponents() {
        currentPlayerLabel = new JLabel("Current Turn: Player 1");
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        currentPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        player1GoldLabel = new JLabel("Gold: 100");
        player1FoodLabel = new JLabel("Food: 50");
        player1UnitsLabel = new JLabel("Units: 0");
        player1StructuresLabel = new JLabel("Structures: 1");

        player2GoldLabel = new JLabel("Gold: 100");
        player2FoodLabel = new JLabel("Food: 50");
        player2UnitsLabel = new JLabel("Units: 0");
        player2StructuresLabel = new JLabel("Structures: 1");

        gameLogArea = new JTextArea(4, 20);
        gameLogArea.setEditable(false);
        gameLogArea.setFont(new Font("Courier", Font.PLAIN, 9));
        gameLogArea.setText("Game Started!\nPlayer 1's turn\n");
    }

    private void layoutComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(createCurrentPlayerPanel());
        add(Box.createVerticalStrut(5));

        add(createHorizontalPlayerStatsPanel());
        add(Box.createVerticalStrut(5));

        add(createGameLogPanel());
        add(Box.createVerticalStrut(5));

        add(createUnitSelectionPanel());
        add(Box.createVerticalStrut(5));

        add(createStructureSelectionPanel());

        add(Box.createVerticalGlue());
    }

    private JPanel createCurrentPlayerPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Current Turn"));
        panel.add(currentPlayerLabel);
        return panel;
    }

    private JPanel createHorizontalPlayerStatsPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 0, 5));

        JPanel player1Panel = createHorizontalPlayerStats("Player 1", Color.BLUE,
                player1GoldLabel, player1FoodLabel, player1UnitsLabel, player1StructuresLabel);

        JPanel player2Panel = createHorizontalPlayerStats("Player 2", Color.RED,
                player2GoldLabel, player2FoodLabel, player2UnitsLabel, player2StructuresLabel);

        mainPanel.add(player1Panel);
        mainPanel.add(player2Panel);

        return mainPanel;
    }

    private JPanel createHorizontalPlayerStats(String playerName, Color borderColor,
                                               JLabel goldLabel, JLabel foodLabel,
                                               JLabel unitsLabel, JLabel structuresLabel) {
        JPanel panel = new JPanel(new GridLayout(1, 4, 5, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(borderColor, 2), playerName),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));

        JLabel[] labels = {goldLabel, foodLabel, unitsLabel, structuresLabel};
        for (JLabel label : labels) {
            label.setFont(new Font("Arial", Font.PLAIN, 10));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(label);
        }

        return panel;
    }

    private JPanel createGameLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Game Log"));
        panel.setPreferredSize(new Dimension(0, 100)); // Fixed smaller height

        JScrollPane scrollPane = new JScrollPane(gameLogArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }


    private JPanel createUnitSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 3, 3));
        panel.setBorder(BorderFactory.createTitledBorder("Units"));
        panel.setPreferredSize(new Dimension(0, 120));

        String[] units = {"Knight", "Peasant", "SpearMan", "SwordMan"};

        for (String unit : units) {
            JButton unitButton = new JButton(unit);
            unitButton.setFont(new Font("Arial", Font.PLAIN, 9));
            unitButton.setPreferredSize(new Dimension(60, 25));

            panel.add(unitButton);

            unitButton.addActionListener(e -> {
                gameController.selectUnitType(unit);
                addLogMessage("Selected unit: " + unit);
            });

        }

        return panel;
    }

    private JPanel createStructureSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 3, 3));
        panel.setBorder(BorderFactory.createTitledBorder("Structures"));
        panel.setPreferredSize(new Dimension(0, 150));

        String[] structures = {"Barrack", "Farm", "Market", "Tower"};


        for (String structure : structures) {
            JButton structureButton = new JButton(structure);
            structureButton.setFont(new Font("Arial", Font.PLAIN, 9));
            structureButton.setPreferredSize(new Dimension(60, 25));

            panel.add(structureButton);

            structureButton.addActionListener(e -> {
                gameController.selectStructureType(structure);
                addLogMessage("Selected structure: " + structure);
            });



        }

        panel.add(new JPanel());




        return panel;
    }

    public void updateStats() {
        if (gameController != null && gameController.getKingdom1() != null && gameController.getKingdom2() != null) {
            Kingdom kingdom1 = gameController.getKingdom1();
            Kingdom kingdom2 = gameController.getKingdom2();

            player1GoldLabel.setText("Gold: " + kingdom1.getGold());
            player1FoodLabel.setText("Food: " + kingdom1.getFood());
            player1UnitsLabel.setText("Units: " + kingdom1.getUnits().size());
            player1StructuresLabel.setText("Structures: " + kingdom1.getStructures().size());

            player2GoldLabel.setText("Gold: " + kingdom2.getGold());
            player2FoodLabel.setText("Food: " + kingdom2.getFood());
            player2UnitsLabel.setText("Units: " + kingdom2.getUnits().size());
            player2StructuresLabel.setText("Structures: " + kingdom2.getStructures().size());
        }

        repaint();
    }

    public void addLogMessage(String message) {
        gameLogArea.append(message + "\n");
        gameLogArea.setCaretPosition(gameLogArea.getDocument().getLength());
    }

    public void setCurrentPlayer(String playerName) {
        currentPlayerLabel.setText("Current Turn: " + playerName);
    }



}