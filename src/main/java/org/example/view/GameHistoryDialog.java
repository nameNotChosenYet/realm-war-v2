package org.example.view;

import org.example.database.DatabaseManager;

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


        table.setBackground(new Color(186, 186, 186));
        table.setForeground(Color.BLACK);
        table.setGridColor(Color.GRAY);


        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(124, 124, 124));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        JPanel bottom = new JPanel();
        bottom.add(closeBtn);
        add(bottom, BorderLayout.SOUTH);
    }
}
