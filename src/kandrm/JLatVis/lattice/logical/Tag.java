package kandrm.JLatVis.lattice.logical;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;

import kandrm.JLatVis.lattice.visual.TagShape;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Logiská reprezentace popisku uzlu. Obsahuje logické vlastnosti - název, text -
 * a odkaz na vizuální reprezentaci.
 * Vizuální reprezentaci si sám vytváří.
 *
 * @author Michal Kandr
 */
public class Tag implements IXmlSerializable, IHistoryEventSender {
    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();

    private Node node;
    private Integer id;
    private String name = "";
    private String text = "";
    
    private TagShape shape;

    public Tag(Node node){
        this(node, null, null, null);
    }
    /**
     * Nový popiske uzlu.
     *
     * @param node
     * @param name
     * @param text
     */
    public Tag(Node node, Integer id, String name, String text){
        this.node = node;
        this.id = id;
        this.name = name;
        this.text = text;
        this.shape = new TagShape(this);
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

    public int getId(){
        return id;
    }

    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public Node getNode() {
        return node;
    }
    public void setNode(Node node){
        this.node = node;
    }

    public TagShape getShape() {
        return shape;
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        return writer.writeEntity("Tag").writeAttribute("id", id)
            .writeEntityWithText("Name", name)
            .writeEntityWithText("Text", text)
        .endEntity();
    }

    public static Tag fromXml(Element element, Node node) throws XmlInvalidException {
        Tag tag = new Tag(node);
        String tagId = element.getAttribute("id");
        if( ! element.getNodeName().equals("Tag") || tagId.equals("")){
            throw new XmlInvalidException();
        }
        tag.id = Integer.parseInt(tagId);

        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Name")){
                tag.name = el.getTextContent();
            } else if(elName.equals("Text")){
                tag.text = el.getTextContent();
            } else {
                throw new XmlInvalidException();
            }
        }
        return tag;
    }

    public void fromXmlVisual(Element element) throws XmlInvalidException {
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Style")){
                shape = TagShape.fromXml(el, this);
            } else {
                throw new XmlInvalidException();
            }
        }
    }
}
