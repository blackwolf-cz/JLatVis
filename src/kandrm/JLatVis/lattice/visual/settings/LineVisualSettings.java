package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.awt.Color;
import java.awt.BasicStroke;
import java.io.IOException;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.visual.DashingPattern;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Nastavení vizuální podoby linky (čáry).
 *
 * @author Michal Kandr
 */
public class LineVisualSettings implements IXmlSerializable {
    private Color color;
    private BasicStroke stroke;

    public LineVisualSettings(){
        this(null, null);
    }
    public LineVisualSettings(Color color, BasicStroke stroke){
        this.color = color;
        this.stroke = stroke;
    }
    public LineVisualSettings(LineVisualSettings l){
        this.color = l.color;
        this.stroke = l.stroke;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } else if(o == null || o.getClass() != this.getClass()){
            return false;
        }

        LineVisualSettings l = (LineVisualSettings)o;
        return color.equals(l.color)
            && stroke.equals(l.stroke);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.color != null ? this.color.hashCode() : 0);
        hash = 97 * hash + (this.stroke != null ? this.stroke.hashCode() : 0);
        return hash;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public BasicStroke getStroke() {
        return stroke;
    }
    public void setStroke(BasicStroke stroke) {
        this.stroke = stroke;
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntityWithText("Color", color.getRGB())
        .writeEntityWithText("Width", ((Float)stroke.getLineWidth()).intValue());
        new DashingPattern(stroke.getDashArray()).toXml(writer);
        return writer;
    }

    public static LineVisualSettings fromXml(Element element) throws XmlInvalidException {
        LineVisualSettings settings = new LineVisualSettings();
        if(element == null){
            return settings;
        }
        settings.stroke = new BasicStroke();
        DashingPattern dashingPattern = DashingPattern.fromXml(element);

        NodeList colorNode = element.getElementsByTagName("Color");
        NodeList widthNode = element.getElementsByTagName("Width");
        if(colorNode.getLength() != 1 || widthNode.getLength() != 1){
            throw new XmlInvalidException();
        }
        settings.color = new Color( Integer.parseInt(colorNode.item(0).getTextContent()) );
        settings.stroke = settings.stroke = new BasicStroke(
                Integer.parseInt(widthNode.item(0).getTextContent()),
                settings.stroke.getEndCap(),
                settings.stroke.getLineJoin(),
                settings.stroke.getMiterLimit(),
                dashingPattern.allZero() ? null : dashingPattern.getDashing(),
                settings.stroke.getDashPhase());

        return settings;
    }
}
