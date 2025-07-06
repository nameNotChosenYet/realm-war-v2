package view;

import model.blocks.Block;
import model.blocks.EmptyBlock;
import model.blocks.ForestBlock;
import model.blocks.VoidBlock;
import model.structures.Structure;
import model.units.Unit;

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
        if (structure.getLevel() == 1) {
            switch (structure.getDurability()) {
                case 40:
                    structureType += "Durability40";
                    break;
                case 30:
                    structureType += "Durability30";
                    break;
                case 20:
                    structureType += "Durability20";
                    break;
                case 10:
                    structureType += "Durability10";
                    break;
                default:
                    break;
            }
        }
        if (structure.getLevel() == 2) {
            structureType += "2";
            switch (structure.getDurability()) {
                case 50:
                    structureType += "durability50";
                case 40:
                    structureType += "durability40";
                    break;
                case 30:
                    structureType += "durability30";
                    break;
                case 20:
                    structureType += "durability20";
                    break;
                case 10:
                    structureType += "durability10";
                    break;
                default:
                    break;
            }
        }
        if (structure.getLevel() == 3) {
            structureType += "3";
            switch (structure.getDurability()) {
                case 60:
                    structureType += "durability60";
                case 50:
                    structureType += "durability50";
                case 40:
                    structureType += "durability40";
                    break;
                case 30:
                    structureType += "durability30";
                    break;
                case 20:
                    structureType += "durability20";
                    break;
                case 10:
                    structureType += "durability10";
                    break;
                default:
                    break;
            }
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
