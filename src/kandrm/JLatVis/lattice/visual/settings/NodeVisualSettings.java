package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BasicStroke;
import java.io.IOException;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.visual.NodeShapeTypes;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class NodeVisualSettings extends SelectabeleTextShapeVisualSettings {
    private static final Dimension DEFAULT_DIMENSIONS = new Dimension(10, 10);
    private static final boolean DEFAULT_VISIBLE = true;
    private static final boolean DEFAULT_NAME_VISIBLE = true;
    private static final NodeShapeTypes.Type DEFAULT_SHAPE = NodeShapeTypes.Type.ELLIPSE;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;

    private NodeShapeTypes.Type nodeType;
    private boolean visible;
    private boolean nameVisible;
    private ShapeSpecialVisualSettings specialSettings;
    private NodeNameVisualSettings nameSettings;

    public NodeVisualSettings(){
        super(DEFAULT_DIMENSIONS);
        setDefault();
    }
    public NodeVisualSettings(NodeShapeTypes.Type nodeType,
            Dimension dimensions, int angle,
            Color backgroundColor,
            Color borderColor, BasicStroke borderStroke,
            Color textColor, Font textFont,
            boolean visible, boolean nameVisible){
        this(
            nodeType,
            dimensions, angle,
            backgroundColor,
            borderColor, borderStroke,
            textColor, textFont,
            visible,
            nameVisible,
            NodeShapeTypes.getInstance().getVisualSettingInstance(nodeType),
            new NodeNameVisualSettings()
        );
    }
    public NodeVisualSettings(NodeShapeTypes.Type nodeType,
            Dimension dimensions, int angle,
            Color backgroundColor,
            Color borderColor, BasicStroke borderStroke,
            Color textColor, Font textFont,
            boolean visible,
            boolean nameVisible,
            ShapeSpecialVisualSettings specialSettings,
            NodeNameVisualSettings nameSettings){
        super(dimensions, angle, backgroundColor, borderColor, borderStroke, textColor, textFont, null);
        this.nodeType = nodeType;
        this.visible = visible;
        this.nameVisible = nameVisible;
        this.specialSettings = specialSettings;
        this.nameSettings = nameSettings;
    }

    public NodeVisualSettings(NodeVisualSettings n){
        super(n);
        nodeType = n.nodeType;
        visible = n.visible;
        nameVisible = n.nameVisible;
        if(n.specialSettings == null){
            specialSettings = null;
        } else {
            specialSettings = n.specialSettings.clone();
        }
        nameSettings = new NodeNameVisualSettings(n.getNameSettings());
    }
    public NodeVisualSettings(SelectabeleTextShapeVisualSettings n){
        super(n);
        setDefault();
    }

    private void setDefault(){
        nodeType = DEFAULT_SHAPE;
        visible = DEFAULT_VISIBLE;
        nameVisible = DEFAULT_NAME_VISIBLE;
        specialSettings = NodeShapeTypes.getInstance().getVisualSettingInstance(DEFAULT_SHAPE);
        nameSettings = new NodeNameVisualSettings();
        if(getDimensions() == null){
            setDimensions(DEFAULT_DIMENSIONS);
        }
        if(getBackgroundColor() == null){
            setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        }
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } else if(o == null || o.getClass() != this.getClass()){
            return false;
        }

        NodeVisualSettings nv = (NodeVisualSettings)o;
        return super.equals(o)
            && nodeType.equals(nv.nodeType)
            && visible == nv.visible
            && nameVisible == nv.nameVisible
            && ((specialSettings != null
                    && nv.specialSettings != null
                    && specialSettings.equals(nv.specialSettings))
               || (specialSettings == null
                    && nv.specialSettings == null))
            && nameSettings.equals(nv.getNameSettings());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + (this.nodeType != null ? this.nodeType.hashCode() : 0);
        hash = 31 * hash + (this.visible ? 1 : 0);
        hash = 31 * hash + (this.nameVisible ? 1 : 0);
        hash = 31 * hash + (this.specialSettings != null ? this.specialSettings.hashCode() : 0);
        hash = 31 * hash + this.nameSettings.hashCode();
        return hash;
    }

    public NodeShapeTypes.Type getNodeType() {
        return nodeType;
    }
    public void setNodeType(NodeShapeTypes.Type nodeType) {
        this.nodeType = nodeType;
    }
    
    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isNameVisible() {
        return nameVisible;
    }
    public void setNameVisible(boolean nameVisible) {
        this.nameVisible = nameVisible;
    }

    public ShapeSpecialVisualSettings getSpecialSettings() {
        if(specialSettings == null){
            specialSettings = NodeShapeTypes.getInstance().getVisualSettingInstance(nodeType);
        }
        return specialSettings;
    }
    public void setSpecialSettings(ShapeSpecialVisualSettings specialSettings) {
        this.specialSettings = specialSettings;
    }

    public NodeNameVisualSettings getNameSettings() {
        return nameSettings;
    }
    public void setNameSettings(NodeNameVisualSettings nameSettings) {
        this.nameSettings = nameSettings;
    }


    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Style");
            super.toXml(writer);
            writer.writeEntityWithText("NodeType", nodeType)
            .writeEntityWithText("Visible", visible)
            .writeEntityWithText("NameVisible", nameVisible)
            .writeEntity("Special");
                if(specialSettings != null){
                    specialSettings.toXml(writer);
                }
            writer.endEntity();
            writer.writeEntity("NodeName");
                nameSettings.toXml(writer);
            writer.endEntity();
        return writer.endEntity();
    }

    public static NodeVisualSettings fromXml(Element element) throws XmlInvalidException {
        NodeVisualSettings settings = new NodeVisualSettings( SelectabeleTextShapeVisualSettings.fromXml(element) );
        if(element == null){
            return settings;
        }
        if( ! element.getNodeName().equals("Style")){
            throw new XmlInvalidException();
        }
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("NodeType")){
                try{
                    settings.nodeType = NodeShapeTypes.Type.valueOf(el.getTextContent());
                } catch(IllegalArgumentException e){
                    throw new XmlInvalidException();
                }
            } else if(elName.equals("Visible")){
                settings.visible = Boolean.parseBoolean(el.getTextContent());
            } else if(elName.equals("NameVisible")){
                settings.nameVisible = Boolean.parseBoolean(el.getTextContent());
            } else if(elName.equals("Special")){
                Class specialClass = NodeShapeTypes.getInstance().getVisualSettingClass(settings.nodeType);
                if(specialClass != null){
                    try {
                        settings.specialSettings = (ShapeSpecialVisualSettings) specialClass.getMethod("fromXml", Element.class).invoke(null, el);
                    } catch (Exception ex) {
                        throw new XmlInvalidException(ex);
                    }
                }
            } else if(elName.equals("NodeName")){
                settings.nameSettings = (NodeNameVisualSettings) NodeNameVisualSettings.fromXml(el);
            }
        }
        if(settings.nameSettings == null){
            settings.nameSettings = new NodeNameVisualSettings();
        }
        return settings;
    }
}
