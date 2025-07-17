package org.example.view;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Realm War - Main Menu");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(45, 50, 60));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 110), 7),
                BorderFactory.createEmptyBorder(40, 60, 40, 60)
        ));


        JLabel titleLabel = new JLabel("Realm War");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(50, 50, 100));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        centerPanel.add(titleLabel);


        centerPanel.add(createMenuButton("New Game"));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        centerPanel.add(createMenuButton("Continue Game"));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        centerPanel.add(createMenuButton("Game History"));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        centerPanel.add(createMenuButton("Exit"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(centerPanel, gbc);

        add(mainPanel);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text, getButtonIcon(text));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(15);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBackground(new Color(92, 120, 80));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(250, 50));
        button.setMaximumSize(new Dimension(300, 60));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(112, 150, 90));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(92, 120, 80));
            }
        });

        button.addActionListener(e -> handleButtonClick(text));
        return button;
    }

    private Icon getButtonIcon(String action) {
        String iconPath = "/Images/";

        switch (action) {
            case "New Game":
                iconPath += "sword.png";
                break;
            case "Continue Game":
                iconPath += "hourglass.png";
                break;
            case "Game History":
                iconPath += "scroll.png";
                break;
            case "Exit":
                iconPath += "exit.png";
                break;
            default:
                return null;
        }

        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void handleButtonClick(String action) {
        switch (action) {
            case "New Game":
                dispose();
                new GameFrame().setVisible(true);
                break;
            case "Continue Game":
                JOptionPane.showMessageDialog(this, "Continue Game clicked");
                break;
            case "Game History":
                new GameHistoryDialog(this).setVisible(true);
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }

}