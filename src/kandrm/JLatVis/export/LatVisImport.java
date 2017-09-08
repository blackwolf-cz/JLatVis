package kandrm.JLatVis.export;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import kandrm.JLatVis.lattice.logical.Lattice;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.visual.NodeShape;
import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.line.Line2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Michal Kandr
 */
public class LatVisImport {
    private static void loadElements(Lattice l, Element element) throws XmlInvalidException{
        NodeList childs = element.getElementsByTagName("element");
        for(int i=0; i<childs.getLength(); ++i){
            Element el = (Element)childs.item(i);
            String name = el.getAttribute("name");
            String id = el.getAttribute("id");
            if(name.equals("") && id.equals("")){
                throw new XmlInvalidException();
            } else if(name.equals("")){
                id = name;
            } else if(id.equals("")){
                id = name;
            }
            Node node = new Node(id, name);
            node.setShape(new NodeShape(node));
            l.addNode(node);
            
            NodeList vertexList = el.getElementsByTagName("vertex");
            if(vertexList.getLength() == 1){
                NodeList x = ((Element)vertexList.item(0)).getElementsByTagName("x");
                NodeList y = ((Element)vertexList.item(0)).getElementsByTagName("y");
                if(x.getLength() != 1 || y.getLength() != 1){
                    throw new XmlInvalidException();
                }
                node.getShape().setCenter( Integer.parseInt( ((Element)x.item(0)).getTextContent() ), Integer.parseInt( ((Element)y.item(0)).getTextContent() ));
            }
        }
        // vertically flip lattice
        Rectangle bounds = l.getShape().getBounds();
        AffineTransform2D transform = AffineTransform2D.createLineReflection( new Line2D(bounds.x, bounds.x + bounds.height / 2, bounds.x + bounds.width, bounds.x + bounds.height / 2) );
        for(NodeShape node : l.getShape().getNodes()){
            node.setCenter( node.getCenter().transform(transform) );
        }
        
        CoordsRecount.adjustCoords(l);
    }

    private static void loadOrders(Lattice l, Element element) throws XmlInvalidException{
        NodeList childs = element.getElementsByTagName("order");
        for(int i=0; i<childs.getLength(); ++i){
            Element el = (Element)childs.item(i);
            NodeList elements = el.getElementsByTagName("element");
            if(elements.getLength() == 2){ // version 1.0
                String u1 = ((Element)elements.item(0)).getAttribute("name");
                String u2 = ((Element)elements.item(1)).getAttribute("name");
                try {
                    l.addRelation(u2, u1);
                } catch (NoSuchFieldException ex) {
                    throw new XmlInvalidException(ex);
                }
            } else { // version 1.1
                NodeList bigger = el.getElementsByTagName("succ");
                NodeList smaller = el.getElementsByTagName("pred");
                if(bigger.getLength() != 1 || smaller.getLength() != 1){
                    throw new XmlInvalidException();
                }
                String u1 = ((Element)bigger.item(0)).getAttribute("id");
                String u2 = ((Element)smaller.item(0)).getAttribute("id");
                try {
                    l.addRelation(u1, u2);
                } catch (NoSuchFieldException ex) {
                    throw new XmlInvalidException(ex);
                }
            }
        }
    }

    public static Lattice load(Document doc) throws ParserConfigurationException, SAXException, IOException, XmlInvalidException{
        Element docElement = doc.getDocumentElement();
        if( ! docElement.getNodeName().equals("ordered_set")){
            throw new XmlInvalidException();
        }

        Lattice lattice = new Lattice();

        NodeList childs = docElement.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("elements")){
                loadElements(lattice, el);
            } else if(elName.equals("orders")){
                loadOrders(lattice, el);
            } else if(elName.equals("diagram")){
                continue;
            } else {
                throw new XmlInvalidException();
            }
        }
        lattice.deleteTransitiveEdges();
        lattice.getShape().moveTo(new Point2D(0, 0));

        return lattice;
    }
    public static Lattice load(File file) throws ParserConfigurationException, SAXException, IOException, XmlInvalidException{
        return load(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file));
    }
    public static Lattice load(String file) throws ParserConfigurationException, SAXException, IOException, XmlInvalidException{
        return load(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new InputSource(new StringReader(file))) );
    }
}