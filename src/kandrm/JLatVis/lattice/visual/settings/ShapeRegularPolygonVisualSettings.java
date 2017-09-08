package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import kandrm.JLatVis.export.XmlInvalidException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class ShapeRegularPolygonVisualSettings extends ShapeSpecialVisualSettings {
    private static final int DEFAULT_N = 4;
    private int n;

    public ShapeRegularPolygonVisualSettings(int n){
        this.n = n;
    }
    public ShapeRegularPolygonVisualSettings(){
        this(DEFAULT_N);
    }

    @Override
    public ShapeSpecialVisualSettings clone() {
        return new ShapeRegularPolygonVisualSettings(n);
    }

    public int getN() {
        return n;
    }
    public void setN(int n) {
        this.n = n;
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        return writer.writeEntityWithText("N", n);
    }

    public static ShapeRegularPolygonVisualSettings fromXml(Element element) throws XmlInvalidException {
        ShapeRegularPolygonVisualSettings setting = new ShapeRegularPolygonVisualSettings();
        NodeList n = element.getElementsByTagName("N");
        if(n.getLength() == 1){
            setting.n = Integer.parseInt(n.item(0).getTextContent());
        }
        return setting;
    }
}
