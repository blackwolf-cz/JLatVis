package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BasicStroke;
import java.io.IOException;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class TextShapeVisualSettings implements IXmlSerializable {
    private static final int DEFAULT_ANGLE = 0;
    private static final Color DEFAULT_BACKGROUND_COLOR = null;
    private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final BasicStroke DEFAULT_BORDER_STROKE = new BasicStroke(1);
    private static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final Font DEFAULT_TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private static final TextVisualSettings.TextAlignment DEFAULT_TEXT_ALIGNMENT = TextVisualSettings.TextAlignment.LEFT;

    private ShapeVisualSettings shape;
    private LineVisualSettings border;
    private TextVisualSettings text;

    public TextShapeVisualSettings(){
        this((Dimension)null);
    }
    public TextShapeVisualSettings(Dimension dimensions){
        this(
            dimensions, DEFAULT_ANGLE,
            DEFAULT_BACKGROUND_COLOR,
            DEFAULT_BORDER_COLOR, DEFAULT_BORDER_STROKE,
            DEFAULT_TEXT_COLOR, DEFAULT_TEXT_FONT, DEFAULT_TEXT_ALIGNMENT
        );
    }
    public TextShapeVisualSettings(Dimension dimensions, Color backgroundColor){
        this(
            dimensions, DEFAULT_ANGLE,
            backgroundColor,
            DEFAULT_BORDER_COLOR, DEFAULT_BORDER_STROKE,
            DEFAULT_TEXT_COLOR, DEFAULT_TEXT_FONT, DEFAULT_TEXT_ALIGNMENT
        );
    }

    public TextShapeVisualSettings(Dimension dimensions, int angle,
            Color backgroundColor,
            Color borderColor, BasicStroke borderStroke,
            Color textColor, Font textFont, TextVisualSettings.TextAlignment textAlignment){
        this.shape = new ShapeVisualSettings(backgroundColor, dimensions, angle);
        this.border = new LineVisualSettings(borderColor, borderStroke);
        this.text = new TextVisualSettings(textColor, textFont, textAlignment);
    }

    public TextShapeVisualSettings(TextShapeVisualSettings t){
        this.shape = new ShapeVisualSettings(t.shape);
        this.border = new LineVisualSettings(t.border);
        this.text = new TextVisualSettings(t.text);
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } else if(o == null || o.getClass() != this.getClass()){
            return false;
        }

        TextShapeVisualSettings t = (TextShapeVisualSettings)o;
        return shape.equals(t.shape)
            && border.equals(t.border)
            && text.equals(t.text);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.shape != null ? this.shape.hashCode() : 0);
        hash = 53 * hash + (this.border != null ? this.border.hashCode() : 0);
        hash = 53 * hash + (this.text != null ? this.text.hashCode() : 0);
        return hash;
    }


    public Color getBackgroundColor(){
        return shape.getColor();
    }
    public void setBackgroundColor(Color color){
        shape.setColor(color);
    }


    public Color getBorderColor() {
        return border.getColor();
    }
    public void setBorderColor(Color color) {
        border.setColor(color);
    }

    public BasicStroke getBorderStroke() {
        return border.getStroke();
    }
    public void setBorderStroke(BasicStroke stroke) {
        border.setStroke(stroke);
    }


    public Color getTextColor() {
        return text.getColor();
    }
    public void setTextColor(Color color) {
        text.setColor(color);
    }

    public Font getTextFont() {
        return text.getFont();
    }
    public void setTextFont(Font font) {
        text.setFont(font);
    }

    public TextVisualSettings.TextAlignment getTextAlignment() {
        return text.getAlignment();
    }
    public void setTextAlignment(TextVisualSettings.TextAlignment alignment) {
        text.setAlignment(alignment);
    }

    public int getAngle() {
        return shape.getAngle();
    }
    public void setAngle(int angle) {
        shape.setAngle(angle);
    }

    public Dimension getDimensions() {
        return shape.getDimensions();
    }
    public void setDimensions(Dimension dimensions) {
        shape.setDimensions(dimensions);
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Shape");
            shape.toXml(writer)
        .endEntity()
        .writeEntity("Border");
            border.toXml(writer)
        .endEntity()
        .writeEntity("Text");
            text.toXml(writer);
        return writer.endEntity();
    }

    public static TextShapeVisualSettings fromXml(Element element) throws XmlInvalidException {
        TextShapeVisualSettings settings = new TextShapeVisualSettings();
        if(element == null){
            return settings;
        }
        NodeList shapeNode = element.getElementsByTagName("Shape");
        NodeList borderNode = element.getElementsByTagName("Border");
        NodeList textNode = element.getElementsByTagName("Text");

        if(shapeNode.getLength() > 0){
            settings.shape = ShapeVisualSettings.fromXml((Element)shapeNode.item(0));
        } else {
            settings.shape = new ShapeVisualSettings();
        }
        if(borderNode.getLength() > 0){
            settings.border = LineVisualSettings.fromXml((Element)borderNode.item(0));
        } else {
            settings.border = new LineVisualSettings();
        }
        if(textNode.getLength() > 0){
            settings.text = TextVisualSettings.fromXml((Element)textNode.item(0));
        } else {
            settings.text = new TextVisualSettings();
        }
        return settings;
    }
}
