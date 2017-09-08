package kandrm.JLatVis.lattice.visual.settings.patterns;

import java.awt.Color;
import kandrm.JLatVis.lattice.visual.DashingPattern;
import kandrm.JLatVis.lattice.visual.settings.EdgeVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class EdgeSettingPattern extends LineSettingPattern {
    private Boolean visible;
    private Integer distanceFromNode;

    public EdgeSettingPattern(){
        this(null, null, null, null, null);
    }
    public EdgeSettingPattern(Color color, Integer width, DashingPattern dashing, Boolean visible, Integer distanceFromNode){
        super(color, width, dashing);
        this.visible = visible;
        this.distanceFromNode = distanceFromNode;
    }
    public EdgeSettingPattern(EdgeVisualSettings settings){
        this(
            settings.getColor(),
            (int) settings.getStroke().getLineWidth(),
            new DashingPattern(settings.getStroke().getDashArray()),
            settings.isVisible(),
            settings.getDistanceFromNode()
       );
    }
    
    public boolean match(EdgeVisualSettings settings){
        boolean sameDistance = (distanceFromNode == null || distanceFromNode.equals((int)settings.getDistanceFromNode()));
        return super.match(settings)
            && (visible == null || visible.equals(settings.isVisible()))
            && sameDistance;
    }

    public void updateByPattern(EdgeVisualSettings settings){
        super.updateByPattern(settings);
        if(distanceFromNode != null){
            settings.setDistanceFromNode(distanceFromNode);
        }
        if(visible != null){
            settings.setVisible(visible);
        }
    }

    public void intersect(EdgeVisualSettings settings){
        super.intersect(settings);
        
        if(distanceFromNode == null || ! distanceFromNode.equals(settings.getDistanceFromNode())){
            distanceFromNode = null;
        }
        if(visible == null || ! visible.equals(settings.isVisible())){
            visible = null;
        }
    }

    public Integer getDistanceFromNode() {
        return distanceFromNode;
    }

    public Boolean getVisible() {
        return visible;
    }
}
