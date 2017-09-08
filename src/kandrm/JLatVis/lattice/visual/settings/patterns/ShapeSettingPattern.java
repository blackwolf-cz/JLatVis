package kandrm.JLatVis.lattice.visual.settings.patterns;

import java.awt.Color;

/**
 *
 * @author Michal Kandr
 */
public class ShapeSettingPattern {
    private Color color = null;
    private Integer width = null;
    private Integer height = null;
    private Integer angle = null;

    public ShapeSettingPattern(Color color, Integer width, Integer height, Integer angle){
        this.color = color;
        this.width = width;
        this.height = height;
        this.angle = angle;
    }

    public Integer getAngle() {
        return angle;
    }
    public void setAngle(Integer angle) {
        this.angle = angle;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getWidth() {
        return width;
    }
    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }
}
