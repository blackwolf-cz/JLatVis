package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import kandrm.JLatVis.export.IXmlSerializable;

/**
 *
 * @author Michal Kandr
 */
public class HighlightVisualSettings extends LineVisualSettings implements IXmlSerializable {
    private int distance;

    public HighlightVisualSettings(Color color, BasicStroke stroke, int distance){
        super(color, stroke);
        this.distance = distance;
    }
    public HighlightVisualSettings(HighlightVisualSettings h){
        super(h);
        this.distance = h.distance;
    }

    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        super.toXml(writer);
        return writer.writeEntityWithText("Distance", distance);
    }
}
