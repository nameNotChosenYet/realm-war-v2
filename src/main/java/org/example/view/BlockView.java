package org.example.view;

import org.example.model.blocks.Block;
import org.example.model.blocks.EmptyBlock;
import org.example.model.blocks.ForestBlock;
import org.example.model.blocks.VoidBlock;
import org.example.model.structures.Structure;
import org.example.model.units.Unit;

import javax.swing.*;
import java.awt.*;

public class BlockView extends JButton {

    private Block block;
    private boolean highlighted = false;
    private boolean selected = false;

    public BlockView(Block block) {
        this.block = block;

        setPreferredSize(new Dimension(64, 64));
        setMinimumSize(new Dimension(64, 64));
        setMaximumSize(new Dimension(64, 64));
        setBackground(Color.decode("#7EA56E"));
        setContentAreaFilled(false);
        setFocusPainted(false);

        setBorder(BorderFactory.createLineBorder(new Color(92, 120, 80), 1));
        setBorderPainted(true);

        updateDisplay();
    }

    public void updateDisplay() {
        if (block.hasUnit())
            displayUnit(block.getUnit());
        else if (block.hasStructure())
            displayStructure(block.getStructure());
        else
            displayBlock();

        setOpaque(true);
        setBorderPainted(true);
        repaint();
    }

    private void displayUnit(Unit unit) {
        String unitType = unit.getClass().getSimpleName().toLowerCase();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Images/" + unitType + ".png"));
            setIcon(resizeIcon(icon, 64, 64));
        } catch (Exception e) {
            setText(unitType.substring(0, 1).toUpperCase());
            setIcon(null);
        }
    }

    private void displayStructure(Structure structure) {
        String structureType = structure.getClass().getSimpleName().toLowerCase();
        if (structure.getLevel() == 2) {
            structureType += "2";
        }
        if (structure.getLevel() == 3) {
            structureType += "3";
        }


        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Images/" + structureType + ".png"));
            setIcon(resizeIcon(icon, 64, 64));
        } catch (Exception e) {
            setText(structureType.substring(0, 1).toUpperCase());
            setIcon(null);
        }
    }

    private void displayBlock() {
        String blockType = block.getClass().getSimpleName().toLowerCase();
        if (blockType.equals("forestblock") && block instanceof ForestBlock && !((ForestBlock) block).hasForest())
            blockType = "emptyblock";
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Images/" + blockType + ".png"));
            setIcon(resizeIcon(icon, 64, 64));
        } catch (Exception e) {
            setText(blockType.substring(0, 1).toUpperCase());
            setIcon(null);
        }
    }

    private ImageIcon resizeIcon(ImageIcon icon, int w, int h) {
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (highlighted) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setColor(new Color(0, 0, 255, 80));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            g2d.dispose();
        }
    }


    public Block getBlock() {
        return block;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
