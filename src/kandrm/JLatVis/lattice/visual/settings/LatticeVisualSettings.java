package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.awt.Color;
import java.io.IOException;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class LatticeVisualSettings implements IXmlSerializable {
    public static final float FOUND_HIDDEN_TRANSPARENCY = 0.2f;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    private Color backgroundColor;

    public LatticeVisualSettings(){
        this(DEFAULT_BACKGROUND_COLOR);
    }
    public LatticeVisualSettings(LatticeVisualSettings l){
        this.backgroundColor = new Color(l.backgroundColor.getRGB());
    }
    public LatticeVisualSettings(Color backgroundColor){
        this.backgroundColor = backgroundColor;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } else if(o == null || o.getClass() != this.getClass()){
            return false;
        }
        LatticeVisualSettings l = (LatticeVisualSettings)o;
        return backgroundColor.equals(l.getBackgroundColor());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.backgroundColor != null ? this.backgroundColor.hashCode() : 0);
        return hash;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }


    public XmlWriter toXml(XmlWriter writer) throws IOException {
        return writer.writeEntity("Style").writeEntityWithText("BackgroundColor", backgroundColor.getRGB()).endEntity();
    }

    public static LatticeVisualSettings fromXml(Element element) throws XmlInvalidException {
        LatticeVisualSettings settings = new LatticeVisualSettings();
        if(element != null){
            if( ! element.getNodeName().equals("Style")){
                throw new XmlInvalidException();
            }
            NodeList childs = element.getChildNodes();
            for(int i=0; i<childs.getLength(); ++i){
                org.w3c.dom.Node elNode = childs.item(i);
                if(elNode.getNodeType() != Element.ELEMENT_NODE){
                    continue;
                }
                if(elNode.getNodeName().equals("BackgroundColor")){
                    settings.backgroundColor = new Color( Integer.parseInt(elNode.getTextContent()) );
                } else {
                    throw new XmlInvalidException();
                }
            }
        }
        return settings;
    }
}
