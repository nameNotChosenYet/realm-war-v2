// In a new file: view/Theme.java
package view;

import java.awt.*;

public class Theme {
    public static final Color BACKGROUND = new Color(45, 50, 60);
    public static final Color PANEL_BACKGROUND = new Color(60, 65, 75);
    public static final Color TEXT_COLOR = new Color(220, 220, 220);
    public static final Color BORDER_COLOR = new Color(30, 30, 40);
    public static final Color ACCENT_COLOR = new Color(180, 160, 110);

    public static Font getFont(float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, GameFrame.class.getResourceAsStream("resources/fonts/Cinzel-Regular.ttf"));
            return font.deriveFont(size);
        } catch (Exception e) {
            return new Font("Garamond", Font.PLAIN, (int)size);
        }
    }
}