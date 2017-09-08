package kandrm.JLatVis.export;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;

/**
 *
 * @author Michal Kandr
 */
public interface IXmlSerializable {
     public XmlWriter toXml(XmlWriter writer) throws IOException;
}
