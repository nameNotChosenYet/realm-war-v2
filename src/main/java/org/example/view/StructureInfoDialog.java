package org.example.view;

import org.example.controller.GameController;
import org.example.model.Grid.Grid;
import org.example.model.structures.Structure;
import javax.swing.*;
import java.awt.*;

public class StructureInfoDialog extends JDialog {

    private Structure structure;
    private JLabel levelLabel;

    public StructureInfoDialog(JFrame owner, Structure structure, int row, int col, GameController gameController) {
        super(owner, "Structure Info", false);
        this.structure = structure;

        setLayout(new BorderLayout());
        setSize(300, 250);
        setLocationRelativeTo(owner);

        JPanel infoPanel = new JPanel(new GridLayout(0, 1));

        JLabel nameLabel = new JLabel("Name: " + structure.getClass().getSimpleName(), SwingConstants.CENTER);
        JLabel ownerLabel = new JLabel("Owner: " + structure.getOwner().getName(), SwingConstants.CENTER);
        levelLabel = new JLabel("Level: " + structure.getLevel(), SwingConstants.CENTER);
        JLabel durabilityLabel = new JLabel("Durability: " + structure.getDurability(), SwingConstants.CENTER);
        JLabel levelUpCostLabel = new JLabel("Level Up Cost: " + structure.getLevelUpCost(), SwingConstants.CENTER);

        infoPanel.add(nameLabel);
        infoPanel.add(ownerLabel);
        infoPanel.add(levelLabel);
        infoPanel.add(durabilityLabel);
        infoPanel.add(levelUpCostLabel);

        if (structure instanceof org.example.model.structures.Market) {
            int goldPerTurn = ((org.example.model.structures.Market) structure).getGoldPerTurn();
            JLabel goldLabel = new JLabel("Gold per Turn: " + goldPerTurn, SwingConstants.CENTER);
            infoPanel.add(goldLabel);
        }

        add(infoPanel, BorderLayout.CENTER);

        JButton levelUpButton = new JButton("Level Up");
        levelUpButton.addActionListener(e -> {
            if (structure.canLevelUp()) {
                structure.levelUp();
                levelLabel.setText("Level: " + structure.getLevel());
                durabilityLabel.setText("Durability: " + structure.getDurability());
                levelUpCostLabel.setText("Level Up Cost: " + structure.getLevelUpCost());
                gameController.updateHUD();
                Grid.getBlockViews()[row][col].updateDisplay();
                JOptionPane.showMessageDialog(this, "Leveled up!");
            } else {
                JOptionPane.showMessageDialog(this, "Already at max level!");
            }
        });

        add(levelUpButton, BorderLayout.SOUTH);

        setVisible(true);
    }
}
