package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import kandrm.JLatVis.export.IXmlSerializable;

/**
 *
 * @author Michal Kandr
 */
public abstract class ShapeSpecialVisualSettings implements Cloneable, IXmlSerializable {
    @Override
    public abstract ShapeSpecialVisualSettings clone();

    public abstract XmlWriter toXml(XmlWriter writer) throws IOException;
}
