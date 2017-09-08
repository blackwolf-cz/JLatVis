package kandrm.JLatVis.lattice.visual.settings.patterns;

import java.awt.Color;
import kandrm.JLatVis.lattice.visual.settings.TextVisualSettings.TextAlignment;

/**
 *
 * @author Michal Kandr
 */
public class TextSettingPattern {
    private Color color;

    private String fontName;
    private Integer fontSize;
    private Boolean fontBold;
    private Boolean fontItalic;
    private Boolean fontUnderline;

    private TextAlignment alignment;

    public TextSettingPattern(Color color,
            String fontName, Integer fontSize,
            Boolean fontBold, Boolean fontItalic, Boolean fontUnderline,
            TextAlignment alignment) {
        this.color = color;
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.fontBold = fontBold;
        this.fontItalic = fontItalic;
        this.fontUnderline = fontUnderline;
        this.alignment = alignment;
    }

    public TextAlignment getAlignment() {
        return alignment;
    }
    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    
    public Boolean isFontBold() {
        return fontBold;
    }
    public void setFontBold(Boolean fontBold) {
        this.fontBold = fontBold;
    }

    public Boolean isFontItalic() {
        return fontItalic;
    }
    public void setFontItalic(Boolean fontItalic) {
        this.fontItalic = fontItalic;
    }

    public String getFontName() {
        return fontName;
    }
    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Integer getFontSize() {
        return fontSize;
    }
    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Boolean isFontUnderline() {
        return fontUnderline;
    }
    public void setFontUnderline(Boolean fontUnderline) {
        this.fontUnderline = fontUnderline;
    }

}
