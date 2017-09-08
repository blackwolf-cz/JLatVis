package kandrm.JLatVis.lattice.logical;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class OrderPair implements IXmlSerializable, IHistoryEventSender {
    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();
    
    protected Node less;
    protected Node greater;
    protected String comment = "";

    public OrderPair(){
        this(null, null);
    }
    public OrderPair(Node less, Node greater) {
        this.less = less;
        this.greater = greater;
    }

    @Override
    public void addHistoryListener(IHistoryEventListener l){
        historyListeners.add(l);
    }
    @Override
    public void removeHistoryListener(IHistoryEventListener l){
        historyListeners.remove(l);
    }
    public List<IHistoryEventListener> getHistoryListeners(){
        return historyListeners;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Node getGreater() {
        return greater;
    }

    public Node getLess() {
        return less;
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        return writer.writeEntity("Pair")
                .writeAttribute("greater", greater.getId())
                .writeAttribute("less", less.getId())
                .writeEntityWithText("Comment", comment)
            .endEntity();
    }

    public static OrderPair fromXml(Lattice lattice, Element element) throws XmlInvalidException {
        String greater = element.getAttribute("greater");
        String less = element.getAttribute("less");
        // no reflexive edges
        if(greater.equals(less)){
            return null;
        }
        if( ! element.getNodeName().equals("Pair") || greater.equals("") || less.equals("")){
            throw new XmlInvalidException();
        }
        OrderPair pair = new OrderPair();
        pair.greater = lattice.getNode(greater);
        pair.less = lattice.getNode(less);

        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Comment")){
                pair.comment = el.getTextContent();
            } else {
                throw new XmlInvalidException();
            }
        }

        return pair;
    }
}
