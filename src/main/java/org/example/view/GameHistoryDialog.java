package org.example.view;

import org.example.database.DatabaseManager;
import org.example.view.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameHistoryDialog extends JDialog {

    public GameHistoryDialog(JFrame owner) {
        super(owner, "Game History", true); // modal
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.PANEL_BACKGROUND);


        String[] columns = {"Game #", "Winner", "Loser", "Winner Score", "Loser Score"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };


        ResultSet rs = DatabaseManager.getGameHistory();
        if (rs != null) {
            try {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("winner_name"),
                            rs.getString("loser_name"),
                            rs.getInt("winner_score"),
                            rs.getInt("loser_score")
                    });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {

                try {
                    if (rs != null) {
                        if (rs.getStatement() != null && rs.getStatement().getConnection() != null) {
                            rs.getStatement().getConnection().close();
                        }
                        rs.close();
                    }
                } catch (SQLException ignore) {}
            }
        }

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(22);
        table.setFont(Theme.getFont(14f));


        table.setBackground(Theme.BACKGROUND);
        table.setForeground(Theme.TEXT_COLOR);
        table.setGridColor(Theme.BORDER_COLOR);


        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.ACCENT_COLOR);
        header.setForeground(Theme.BACKGROUND);
        header.setFont(Theme.getFont(16f));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

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
