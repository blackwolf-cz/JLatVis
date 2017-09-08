package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Nastavení vizuální podoby geometrického tvaru.
 * 
 * @author Michal Kandr
 */
public class ShapeVisualSettings implements IXmlSerializable {
    private Color color;
    private Dimension dimensions;
    private Integer angle;

    public ShapeVisualSettings(){
        this(null, new Dimension(), 0);
    }
    public ShapeVisualSettings(Color color, Dimension dimensions, Integer angle){
        this.color = color;
        this.dimensions = dimensions;
        this.angle = angle;
    }
    public ShapeVisualSettings(ShapeVisualSettings s){
        color = s.color;
        dimensions = s.dimensions;
        angle = s.angle;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } else if(o == null || o.getClass() != this.getClass()){
            return false;
        }

        ShapeVisualSettings s = (ShapeVisualSettings)o;
        return (color == s.color || (color != null && s.color != null && color.equals(s.color)))
            && (dimensions == s.dimensions || (dimensions != null && s.dimensions != null && dimensions.equals(s.dimensions)))
            && angle == s.angle;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.color != null ? this.color.hashCode() : 0);
        hash = 89 * hash + (this.dimensions != null ? this.dimensions.hashCode() : 0);
        hash = 89 * hash + Float.floatToIntBits(this.angle);
        return hash;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public Dimension getDimensions() {
        return dimensions;
    }
    public void setDimensions(Dimension dimensions) {
        this.dimensions = dimensions;
    }

    public Integer getAngle() {
        return angle;
    }
    public void setAngle(int angle) {
        this.angle = angle;
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntityWithText("Color", color != null ? color.getRGB() : null);
        if(dimensions != null){
            writer.writeEntityWithText("Width", dimensions != null ? dimensions.width : null);
            writer.writeEntityWithText("Height", dimensions != null ? dimensions.height : null);
        }
        writer.writeEntityWithText("Angle", angle);
        return writer;
    }

    public static ShapeVisualSettings fromXml(Element element) throws XmlInvalidException {
        ShapeVisualSettings settings = new ShapeVisualSettings();
        if(element == null){
            return settings;
        }
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Color")){
                settings.color = el.getTextContent().equals("null") ? null : new Color( Integer.parseInt(el.getTextContent()) );
            } else if(elName.equals("Width")){
                settings.dimensions.width = el.getTextContent().equals("null") ? 0 : Integer.parseInt(el.getTextContent());
            } else if(elName.equals("Height")){
                settings.dimensions.height = el.getTextContent().equals("null") ? 0 : Integer.parseInt(el.getTextContent());
            } else if(elName.equals("Angle")){
                settings.angle = Integer.parseInt(el.getTextContent());
            } else {
                throw new XmlInvalidException();
            }
        }
        return settings;
    }
}
