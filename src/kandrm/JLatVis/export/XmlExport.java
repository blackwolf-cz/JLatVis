package kandrm.JLatVis.export;

import com.generationjava.io.xml.PrettyPrinterXmlWriter;
import com.generationjava.io.xml.SimpleXmlWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import kandrm.JLatVis.lattice.editing.Zoom;
import kandrm.JLatVis.lattice.logical.Lattice;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Michal Kandr
 */
public class XmlExport {
    public static void save(Lattice lattice, File file) throws IOException{
        Writer writer = new java.io.StringWriter();
        PrettyPrinterXmlWriter pretty = new PrettyPrinterXmlWriter( new SimpleXmlWriter(writer) );
        pretty.setIndent("\t");
        pretty.setNewline("\n\n");

        pretty.writeEntity("LatVis").writeAttribute("version", "2.0");
            pretty.writeEntity("Lattices");
                lattice.toXml(pretty);
            pretty.endEntity()
            .writeEntity("AppSpecificData");
                lattice.getShape().toXmlAppSpecific(pretty);
                lattice.getShape().getZoom().toXml(pretty);
            pretty.endEntity()
        .endEntity();

        pretty.close();

        FileWriter fileWriter = new FileWriter( file );
        fileWriter.write(writer.toString());
        fileWriter.close();
    }

    public static Lattice load(Document doc) throws ParserConfigurationException, SAXException, IOException, XmlInvalidException{
        Element docElement = doc.getDocumentElement();
        if( ! docElement.getNodeName().equals("LatVis")){
            throw new XmlInvalidException();
        }
        
        Lattice l = null;

        NodeList childs = docElement.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Lattices")){
                l = Lattice.fromXml(el);
            } else if(elName.equals("AppSpecificData")){
                l.getShape().fromXmlAppSpecific(el);
                l.getShape().setZoom(Zoom.fromXml(el));
            } else {
                throw new XmlInvalidException();
            }
        }
        
        return l;
    }
    public static Lattice load(File file) throws ParserConfigurationException, SAXException, IOException, XmlInvalidException{
        return load(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file));
    }
    public static Lattice load(String file) throws ParserConfigurationException, SAXException, IOException, XmlInvalidException{
        return load(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new InputSource(new StringReader(file))) );
    }
}