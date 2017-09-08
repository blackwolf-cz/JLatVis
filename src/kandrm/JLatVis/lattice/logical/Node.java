package kandrm.JLatVis.lattice.logical;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import kandrm.JLatVis.lattice.visual.NodeShape;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Reprezentace prvku svazu (uzel v grafu tvořeném Hasseovým diagramem).
 *
 * @author Michal Kandr
 */
public class Node implements IXmlSerializable, IHistoryEventSender{
    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();

    private String id = null;
    private String name = "";
    private String comment = "";
    
    private NodeShape shape = null;
    private List<Tag> tags = null;
    private List<Node> parents = null;
    private List<Node> descendants = null;

    public Node(){
        this(null);
    }
    public Node(String id){
        this(id, id);
    }
    public Node(String id, String name){
        this.id = id;
        this.name = name;
        tags = new ArrayList<Tag>();
        parents = new ArrayList<Node>();
        descendants = new ArrayList<Node>();
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

    public List<Tag> getTags(){
        return Collections.unmodifiableList(tags);
    }
    public Tag getTagById(int id){
        for(Tag tag : tags){
            if(tag.getId() == id){
                return tag;
            }
        }
        return null;
    }

    public Tag addTag(String name, String text){
        int tagId = 1;
        for(Tag tag : tags){
            if(tag.getId() >= tagId){
                tagId = tag.getId() + 1;
            }
        }
        Tag tag = new Tag(this, tagId, name, text);
        addTag(tag);
        return tag;
    }
    public void addTag(Tag tag){
        if(tag == null){
            throw new NullPointerException();
        }
        if(tag.getNode() != this){
            throw new IllegalArgumentException("Tag is not belong to this node.");
        }
        tags.add(tag);
        shape.addTag(tag.getShape());
    }
    public void removeTag(Tag tag){
        tags.remove(tag);
        shape.removeTag(tag.getShape());
    }

    public void addParent(Node n){
        parents.add(n);
    }
    public List<Node> getParents(){
        return Collections.unmodifiableList(parents);
    }
    public List<Node> getParentsTransitive(){
        ArrayList<Node> parentsTr = new ArrayList<Node>(parents);
        for(Node child : parents){
            parentsTr.addAll(child.getParentsTransitive());
        }
        return Collections.unmodifiableList(parentsTr);
    }
    public void removeParent(Node n){
        parents.remove(n);
    }

    public void addDescendant(Node n){
        descendants.add(n);
    }
    public List<Node> getDescendants(){
        return Collections.unmodifiableList(descendants);
    }
    public List<Node> getDescendantsTransitive(){
        ArrayList<Node> childs = new ArrayList<Node>(descendants);
        for(Node child : descendants){
            childs.addAll(child.getDescendantsTransitive());
        }
        return Collections.unmodifiableList(childs);
    }
    public void removeDescendant(Node n){
        descendants.remove(n);
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public NodeShape getShape(){
        return shape;
    }
    public void setShape(NodeShape shape){
        this.shape = shape;
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Element").writeAttribute("id", getId())
                .writeEntityWithText("Name", getName())
                .writeEntityWithText("Comment", comment);
        for(Tag tag : tags){
            tag.toXml(writer);
        }
        return writer.endEntity();
    }

    public static Node fromXml(Element element) throws XmlInvalidException {
        Node node = new Node();
        String id = element.getAttribute("id");
        String name = element.getAttribute("name");
        if( ! element.getNodeName().equals("Element") || ( id.equals(""))){
            throw new XmlInvalidException();
        }
        node.id = id;
        node.name = name.equals("") ? id : name;

        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Name") && ! el.getTextContent().equals("")){
                node.name = el.getTextContent();
            } else if(elName.equals("Comment")){
                node.comment = el.getTextContent();
            } else if(elName.equals("Tag")){
                node.tags.add( Tag.fromXml(el, node) );
            } else {
                throw new XmlInvalidException();
            }
        }

        return node;
    }
}
