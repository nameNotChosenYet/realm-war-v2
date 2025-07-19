package org.example.view;

import javax.swing.table.JTableHeader;
import javax.swing.*;
import java.awt.*;

import org.example.view.Theme;

public class GameResultDialog extends JDialog {
    public GameResultDialog(JFrame owner, String winner, String loser, int winnerScore, int loserScore) {
        super(owner, "Game Results", true);
        setSize(300, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.PANEL_BACKGROUND);

        JLabel resultLabel = new JLabel(winner + " wins!", SwingConstants.CENTER);
        resultLabel.setFont(Theme.getFont(18f));
        resultLabel.setForeground(Theme.TEXT_COLOR);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(resultLabel, BorderLayout.NORTH);

        String[] columns = {"Player", "Score"};
        Object[][] data = {
                {winner, winnerScore},
                {loser, loserScore}
        };
        JTable table = new JTable(data, columns);
        table.setEnabled(false);
        table.setFont(Theme.getFont(14f));
        table.setBackground(Theme.BACKGROUND);
        table.setForeground(Theme.TEXT_COLOR);
        table.setGridColor(Theme.BORDER_COLOR);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.ACCENT_COLOR);
        header.setForeground(Theme.BACKGROUND);
        header.setFont(Theme.getFont(16f));
        add(new JScrollPane(table), BorderLayout.CENTER);



        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(Theme.getFont(14f));
        closeBtn.setBackground(Theme.PANEL_BACKGROUND);
        closeBtn.setForeground(Theme.TEXT_COLOR);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        closeBtn.addActionListener(e -> dispose());
        JPanel bottom = new JPanel();
        bottom.setBackground(Theme.PANEL_BACKGROUND);
        bottom.add(closeBtn);
        add(bottom, BorderLayout.SOUTH);
    }
}