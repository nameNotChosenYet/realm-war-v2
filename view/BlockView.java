package view;

import model.blocks.Block;
import model.blocks.EmptyBlock;
import model.blocks.ForestBlock;
import model.blocks.VoidBlock;
import model.structures.Structures;
import model.units.Units;

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

//        addActionListener(e -> {
//            JOptionPane.showMessageDialog(this,
//                    "Type: " + block.getType() +
//                            "\nOwner: " + (block.isOwned() ? block.getOwner() : "None") +
//                            "\nGold: " + block.getGoldGeneration() +
//                            "\nFood: " + block.getFoodGeneration()
//            );
//
//            if (block instanceof ForestBlock forest) {
//                forest.cutForest();
//            }
//
//            updateDisplay();
//        });
    }

    public BlockView(Block block, boolean useSimpleView) {
        this.block = block;
        this.highlighted = false;
        this.selected = false;

    }

    public void updateDisplay() {
        if (block.hasUnit()) {
            displayUnit(block.getUnit());
        } else if (block.hasStructure()) {
            displayStructure(block.getStructure());
        } else {
            displayBlock();
        }

        if (selected) {
            setBackground(Color.YELLOW);
        } else if (highlighted) {
            setBackground(Color.LIGHT_GRAY);
        } else {
            setBackground(Color.decode("#7EA56E"));
        }

        setOpaque(true);
        setBorderPainted(true);
    }

    private void displayUnit(Units unit) {
        String unitType = unit.getClass().getSimpleName().toLowerCase();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Images/" + unitType + ".png"));
            setIcon(resizeIcon(icon, 64, 64));
        } catch (Exception e) {
            setText(unitType.substring(0, 1).toUpperCase());
            setIcon(null);
        }
    }

    private void displayStructure(Structures structure) {
        String structureType = structure.getClass().getSimpleName().toLowerCase();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Images/" + structureType + ".png"));
            setIcon(resizeIcon(icon, 64, 64));
        } catch (Exception e) {
            setText(structureType.substring(0, 1).toUpperCase());
            setIcon(null);
        }
    }

    private void displayBlock() {
        setText("");

        if (block instanceof ForestBlock forest) {
            if (forest.hasForest()) {
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/Images/forest.png"));
                    setIcon(resizeIcon(icon, 64, 64));
                } catch (Exception e) {
                    setText("F");
                    setIcon(null);
                }
            } else {
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/Images/cutForest.png"));
                    setIcon(resizeIcon(icon, 64, 64));
                } catch (Exception e) {
                    setText("C");
                    setIcon(null);
                }
            }
        } else if (block instanceof VoidBlock) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/Images/void.png"));
                setIcon(resizeIcon(icon, 64, 64));
            } catch (Exception e) {
                setText("V");
                setIcon(null);
                setBackground(Color.DARK_GRAY);
            }
        } else if (block instanceof EmptyBlock) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/Images/empty.png"));
                setIcon(resizeIcon(icon, 64, 64));
            } catch (Exception e) {
                setIcon(null);
            }
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

    private ImageIcon resizeIcon(ImageIcon icon, int w, int h) {
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }
}
