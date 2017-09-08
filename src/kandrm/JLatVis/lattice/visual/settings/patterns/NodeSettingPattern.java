package kandrm.JLatVis.lattice.visual.settings.patterns;

import java.awt.Color;
import kandrm.JLatVis.lattice.visual.DashingPattern;
import kandrm.JLatVis.lattice.visual.NodeShapeTypes;
import kandrm.JLatVis.lattice.visual.settings.NodeVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class NodeSettingPattern extends TextShapePattern {
    private NodeShapeTypes.Type nodeType;
    private ShapeSpecialSettingPattern specialSettings;
    private Boolean visible;
    private Boolean nameVisible;
    private NodeNameSettingPattern nameSettings;

    public NodeSettingPattern(){
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new NodeNameSettingPattern());
    }
    public NodeSettingPattern(
            // typ uzlu
            NodeShapeTypes.Type nodeType,

            //tvar
            Integer width,
            Integer height,
            Integer angle,
            Color backgroundColor,

            // okraje
            Color borderColor,
            Integer borderWidth,
            DashingPattern borderDashing,

            // text
            Color textColor,
            String fontName,
            Integer fontSize,
            Boolean fontBold,
            Boolean fontItalic,
            Boolean fontUnderline,
            
            // zobrazeni popisku
            Boolean visible,
            Boolean nameVisible,

            // specialni nastaveni
            ShapeSpecialSettingPattern specialSettings,
            NodeNameSettingPattern nameSettings){
        super(
            width,
            height,
            angle,
            backgroundColor,

            borderColor,
            borderWidth,
            borderDashing,

            textColor,
            fontName,
            fontSize,
            fontBold,
            fontItalic,
            fontUnderline,
            null
        );
        this.nodeType = nodeType;
        this.visible = visible;
        this.nameVisible = nameVisible;
        this.specialSettings = specialSettings;
        this.nameSettings = nameSettings;
    }
    public NodeSettingPattern(NodeVisualSettings settings){
        super(settings);
        this.nodeType = settings.getNodeType();
        this.visible = settings.isVisible();
        this.nameVisible = settings.isNameVisible();
        this.specialSettings = NodeShapeTypes.getInstance().getSettingPatternInstance(
            settings.getNodeType(), settings.getSpecialSettings());
        this.nameSettings = new NodeNameSettingPattern(settings.getNameSettings());
    }

    public boolean match(NodeVisualSettings settings){
        return super.match(settings)
            && (nodeType == null || nodeType.equals(settings.getNodeType()))
            && (visible == null || visible.equals(settings.isVisible()))
            && (nameVisible == null || nameVisible.equals(settings.isNameVisible()))
            && (specialSettings == null || specialSettings.match(settings.getSpecialSettings()))
            && (nameSettings == null || nameSettings.match(settings.getNameSettings()));
    }

    public void updateByPattern(NodeVisualSettings settings){
        super.updateByPattern(settings);
        if(nodeType != null){
            settings.setNodeType(nodeType);
        }
        if(visible != null){
            settings.setVisible(visible);
        }
        if(nameVisible != null){
            settings.setNameVisible(nameVisible);
        }
        if(specialSettings != null){
            specialSettings.updateByPattern(settings.getSpecialSettings());
        }
        if(nameSettings != null){
            nameSettings.updateByPattern(settings.getNameSettings());
        }
    }
    
    public void intersect(NodeVisualSettings settings){
        super.intersect(settings);
        if(nodeType == null || ! nodeType.equals(settings.getNodeType())){
            nodeType = null;
        }
        if(visible == null || ! visible.equals(settings.isVisible())){
            visible = null;
        }
        if(nameVisible == null || ! nameVisible.equals(settings.isNameVisible())){
            nameVisible = null;
        }
        if(specialSettings != null){
            specialSettings.intersect(settings.getSpecialSettings());
        }
        if(nameSettings != null){
            nameSettings.intersect(settings.getNameSettings());
        }
    }

    public NodeShapeTypes.Type getNodeType() {
        return nodeType;
    }
    
    public Boolean isVisible() {
        return visible;
    }

    public Boolean isNameVisible() {
        return nameVisible;
    }

    public ShapeSpecialSettingPattern getSpecialSettings() {
        if(specialSettings == null){
            specialSettings = NodeShapeTypes.getInstance().getSettingPatternInstance(nodeType);
        }
        return specialSettings;
    }

    public NodeNameSettingPattern getNameSettings() {
        if(nameSettings == null){
            nameSettings = new NodeNameSettingPattern();
        }
        return nameSettings;
    }
}
