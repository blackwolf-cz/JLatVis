package kandrm.JLatVis.lattice.logical;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import java.util.Map;
import java.util.TreeMap;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;
import kandrm.JLatVis.lattice.editing.search.PathSearch;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Logická reprezentace (konceptualniho) svazu. Obsahuje logické reprezentace prvků.
 * Vizuální podoba je reprezentována třídou <code>LatticeShape</code>.
 *
 * @author Michal Kandr
 */
public class Lattice implements IXmlSerializable, IHistoryEventSender {
    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();
    /**
     * Logické reprezentace prvků.
     */
    private Map<String, Node> nodes = new TreeMap<String,Node>();
    private List<OrderPair> ordering = new ArrayList<OrderPair>();
    /**
     * Vizuální reprezentace svazu.
     */
    LatticeShape shape = null;
    private String name = "";
    private String comment = "";

    public Lattice(){
        shape = new LatticeShape(this);
    }

    @Override
    public void addHistoryListener(IHistoryEventListener l){
        historyListeners.add(l);
        for(OrderPair op : ordering){
            op.addHistoryListener(l);
        }
    }
    @Override
    public void removeHistoryListener(IHistoryEventListener l){
        historyListeners.remove(l);
        for(OrderPair op : ordering){
            op.removeHistoryListener(l);
        }
    }
    public List<IHistoryEventListener> getHistoryListeners(){
        return historyListeners;
    }

    /**
     * Přidá do svazu nový prvek.
     *
     * @param name jméno prvku
     * @param x x souřadnice středu prvku
     * @param y y souřadnice středu uzlu
     */
    public Node addNode(String name){
        return addNode(new Node(name));
    }
    public Node addNode(Node n) throws IllegalArgumentException{
        if(nodes.containsKey(n.getId())){
            throw new IllegalArgumentException("Duplicate node ID.");
        }
        nodes.put(n.getId(), n);
        shape.addNode(n);
        return n;
    }

    /**
     * Vytvoří relaci mezi dvěma prvky.
     *
     * @param bigger jméno většího prvku
     * @param smaller jméno menšího prvku
     * @throws java.lang.NoSuchFieldException pokud prvek s takovým názvem neexistuje
     */
    public void addRelation(String bigger, String smaller) throws NoSuchFieldException{
        // reflective edges are not allowed in Hasse diagram
        if(bigger.equals(smaller)){
            return;
        }
        Node biggerNode = nodes.get(bigger);
        Node smallerNode = nodes.get(smaller);
        if(biggerNode == null || smallerNode == null){
            throw new NoSuchFieldException();
        }
        addOrderPair(new OrderPair(smallerNode, biggerNode));
    }

    public void addOrderPair(OrderPair pair){
        pair.getGreater().addDescendant(pair.getLess());
        pair.getLess().addParent(pair.getGreater());
        ordering.add(pair);
        shape.addEdge(pair);
        for(IHistoryEventListener l : historyListeners){
            pair.addHistoryListener(l);
        }
    }

    /**
     * @return vizuální reprezentace svazu
     */
    public LatticeShape getShape(){
        return this.shape;
    }

    /**
     * @return prvkzy svazu
     */
    public Map<String,Node> getNodes(){
        return new TreeMap<String, Node>(nodes);
    }

    /**
     * Najde prvek svazu podle jeho jména.
     *
     * @param name jméno hledaného prvku
     *
     * @return logická reprezentace prvku; null pokud takový prvek neexistuje
     */
    public Node getNode(String name){
        return nodes.get(name);
    }
    
    public List<OrderPair> getOrderPairs(){
        return new ArrayList<OrderPair>(ordering);
    }

    public OrderPair getOrderPair(String u1, String u2){
        for(OrderPair p : ordering){
            if(p.getGreater().getName().equals(u1) && p.getLess().getName().equals(u2)
                    || p.getGreater().getName().equals(u2) && p.getLess().getName().equals(u1)){
                return p;
            }
        }
        return null;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Node getLowest(){
        return shape.getLogicalSearch().lowest(new ArrayList(nodes.values()));
    }
    
    public Node getHighest(){
        return shape.getLogicalSearch().highest(new ArrayList<Node>(nodes.values()));
    }
    
    
    public void deleteTransitiveEdges(){
        PathSearch pathSearch = new PathSearch(this, shape.getSelector(), shape.getHighlightResults());
        List<OrderPair> pairs = new ArrayList<OrderPair>(ordering);
        for(OrderPair order : pairs){
            List < List<Node> > paths = pathSearch.findPaths( Arrays.asList( order.getGreater(), order.getLess()) );
            if(paths.size() > 1){
                ordering.remove(order);
                order.getGreater().removeDescendant( order.getLess() );
                order.getLess().removeParent( order.getGreater() );
                shape.removeEdge( order );
            }
        }
    }
    

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Lattice")
            .writeEntityWithText("Name", name)
            .writeEntityWithText("Comment", comment)
            .writeEntity("Elements");
                for(Node node : nodes.values()){
                    node.toXml(writer);
                }
            writer.endEntity()
            .writeEntity("Order");
                for(OrderPair orderPair : ordering){
                    orderPair.toXml(writer);
                }
            writer.endEntity();
            shape.toXml(writer);
        writer.endEntity();
        return writer;
    }

    private static void fromXmlElements(Lattice lattice, Element element) throws XmlInvalidException {
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            if(el.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE){
                try{
                    lattice.addNode( Node.fromXml(el));
                } catch(IllegalArgumentException e){
                    throw new XmlInvalidException(e);
                }
            }
        }
    }
    private static void fromXmlOrder(Lattice lattice, Element element) throws XmlInvalidException {
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            OrderPair pair = OrderPair.fromXml(lattice, el);
            if(pair != null){
                lattice.addOrderPair( pair );
            }
        }
    }

    public static Lattice fromXml(Element element) throws XmlInvalidException {
        NodeList elements = element.getElementsByTagName("Lattice");
        if(elements.getLength() < 1){
            throw new XmlInvalidException();
        }
        element = (Element) elements.item(0);

        Lattice lattice = new Lattice();
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Name")){
                lattice.name = el.getTextContent();
            } else if(elName.equals("Comment")){
                lattice.comment = el.getTextContent();
            } else if(elName.equals("Elements")){
                fromXmlElements(lattice, el);
            } else if(elName.equals("Order")){
                fromXmlOrder(lattice, el);
            } else if(elName.equals("Diagram")) {
                lattice.shape = LatticeShape.fromXml(el, lattice);
            } else {
                throw new XmlInvalidException();
            }
        }
        lattice.deleteTransitiveEdges();
        return lattice;
    }
}