package org.example.view;

import javax.swing.*;
import java.awt.*;

public class GameResultDialog extends JDialog {
    public GameResultDialog(JFrame owner, String winner, String loser, int winnerScore, int loserScore) {
        super(owner, "Game Results", true);
        setSize(300, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JLabel resultLabel = new JLabel(winner + " wins!", SwingConstants.CENTER);
        resultLabel.setFont(Theme.getFont(18f));
        resultLabel.setForeground(Theme.TEXT_COLOR);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(resultLabel, BorderLayout.NORTH);

        String[] columns = {"Player", "Score"};
        Object[][] data = {
                {winner, winnerScore},
                {loser, loserScore}
        };
        JTable table = new JTable(data, columns);
        table.setEnabled(false);
        add(table, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        JPanel bottom = new JPanel();
        bottom.add(closeBtn);
        add(bottom, BorderLayout.SOUTH);
    }
}