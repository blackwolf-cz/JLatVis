package kandrm.JLatVis.lattice.visual.settings.patterns;

import java.awt.BasicStroke;
import java.awt.Color;
import kandrm.JLatVis.lattice.visual.DashingPattern;
import kandrm.JLatVis.lattice.visual.settings.LineVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class LineSettingPattern {
    private Color color = null;
    private Integer width;
    private DashingPattern dashing;

    public LineSettingPattern(Color color, Integer width, DashingPattern dashing) {
        this.color = color;
        this.width = width;
        this.dashing = dashing;
    }
    public LineSettingPattern(LineVisualSettings settings){
        this(settings.getColor(), (int) settings.getStroke().getLineWidth(), new DashingPattern(settings.getStroke().getDashArray()));
    }

    public boolean match(LineVisualSettings settings){
        boolean sameColor = color == null || color.equals(settings.getColor());
        boolean sameWidth = width == null || width.equals((int) settings.getStroke().getLineWidth());
        boolean sameDashing = dashing == null || dashing.equals(new DashingPattern(settings.getStroke().getDashArray()));
        
        return sameColor
            && sameWidth
            && sameDashing;
    }

    public BasicStroke createStrokeByPattern(BasicStroke oldStroke){
        if (width == null && dashing == null) {
            return oldStroke;
        }
         int newLineWidth = (width != null) ? width : (int) oldStroke.getLineWidth();
         
         DashingPattern newDashing = dashing;
         if(newDashing == null){
             newDashing = new DashingPattern(oldStroke.getDashArray());
         }
         if(newDashing != null && newDashing.allZero()){
            newDashing = null;
         }

         BasicStroke newStroke = new BasicStroke(
             newLineWidth, oldStroke.getEndCap(),
             oldStroke.getLineJoin(), oldStroke.getMiterLimit(),
             (newDashing!=null ? newDashing.getRecountedDashing(newLineWidth) : null), oldStroke.getDashPhase()
         );

         return newStroke;
    }

    public void updateByPattern(LineVisualSettings settings){
        if(color != null){
            settings.setColor(color);
        }
        settings.setStroke(createStrokeByPattern(settings.getStroke()));
    }

    public void intersect(LineVisualSettings settings){
        if(color == null || ! color.equals(settings.getColor())){
            color = null;
        }
        if (width == null || width != (int) settings.getStroke().getLineWidth()) {
            width = null;
        }
        if (dashing == null || ! dashing.equals(new DashingPattern(settings.getStroke().getDashArray()))) {
            dashing = null;
        }
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public DashingPattern getDashing() {
        return dashing;
    }
    public void setDashing(DashingPattern dashing) {
        this.dashing = dashing;
    }

    public Integer getWidth() {
        return width;
    }
    public void setWidth(Integer width) {
        this.width = width;
    }
}
