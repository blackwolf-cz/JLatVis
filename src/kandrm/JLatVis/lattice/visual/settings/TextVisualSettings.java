package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class TextVisualSettings implements IXmlSerializable {
    public enum TextAlignment { LEFT, CENTER, RIGHT }

    private Color color;
    private Font font;
    private TextAlignment alignment;

    public TextVisualSettings(){
        this(null, null, null);
    }
    public TextVisualSettings(Color color, Font font, TextAlignment alignment){
        this.color = color;
        this.font = font;
        this.alignment = alignment;
    }
    public TextVisualSettings(TextVisualSettings t){
        this.color = t.color;
        this.font = t.font;
        this.alignment = t.alignment;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } else if(o == null || o.getClass() != this.getClass()){
            return false;
        }

        TextVisualSettings t = (TextVisualSettings)o;
        return color.equals(t.color)
            && font.equals(t.font)
            && alignment == t.alignment;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.color != null ? this.color.hashCode() : 0);
        hash = 53 * hash + (this.font != null ? this.font.hashCode() : 0);
        hash = 53 * hash + (this.alignment != null ? this.alignment.hashCode() : 0);
        return hash;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }
    public void setFont(Font font) {
        this.font = font;
    }

    public TextAlignment getAlignment() {
        return alignment;
    }
    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }


    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        return writer.writeEntityWithText("Color", color.getRGB())
        .writeEntity("Font")
            .writeAttribute("size", font.getSize())
            .writeAttribute("bold", font.isBold())
            .writeAttribute("italic", font.isItalic())
            .writeAttribute("underline", font.getAttributes().get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON)
            .writeText(font.getName())
        .endEntity()
        .writeEntityWithText("Alignment", alignment);
    }

    public static TextVisualSettings fromXml(Element element) throws XmlInvalidException {
        TextVisualSettings settings = new TextVisualSettings();
        if(element == null){
            return settings;
        }

        NodeList colorNode = element.getElementsByTagName("Color");
        NodeList fontNode = element.getElementsByTagName("Font");
        NodeList alignmentNode = element.getElementsByTagName("Alignment");
        if(colorNode.getLength() != 1 || fontNode.getLength() != 1 || alignmentNode.getLength() != 1){
            throw new XmlInvalidException();
        }
        settings.color = new Color( Integer.parseInt(colorNode.item(0).getTextContent()) );
        
        Element font = (Element)fontNode.item(0);
        for(String attr : Arrays.asList("size", "bold", "italic", "underline")){
            if( ! font.hasAttribute(attr)){
                throw new XmlInvalidException();
            }
        }
        Map fontAttributes = new HashMap();
        fontAttributes.put(TextAttribute.FAMILY, font.getTextContent());
        fontAttributes.put(TextAttribute.SIZE, Integer.parseInt(font.getAttribute("size")));
        if(Boolean.parseBoolean(font.getAttribute("bold"))){
            fontAttributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        }
        if(Boolean.parseBoolean(font.getAttribute("italic"))){
            fontAttributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
        }
        if(Boolean.parseBoolean(font.getAttribute("underline"))){
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        }
        settings.font = new Font(fontAttributes);
        
        try{
            settings.alignment = TextAlignment.valueOf(alignmentNode.item(0).getTextContent());
        } catch (IllegalArgumentException e){
            settings.alignment = TextAlignment.LEFT;
        }

        return settings;
    }
}