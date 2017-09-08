package kandrm.JLatVis.lattice.editing.selection;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.visual.TagShape;
import kandrm.JLatVis.lattice.visual.EdgeShape;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.NodeShape;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class Selection implements Iterable<ISelectable>, IXmlSerializable{
    private String name;
    private List<ISelectable> selectedItems;

    public class SelectionIterator implements Iterator<ISelectable>{
        private List<ISelectable> selectedItems;
        private int count = 0;

        public SelectionIterator(Selection selection){
            selectedItems = selection.getSelectedItems();
        }

        @Override
        public boolean hasNext() {
            return count < selectedItems.size();
        }
        @Override
        public ISelectable next() {
            if(count >= selectedItems.size()){
                throw new NoSuchElementException();
            }
            return selectedItems.get(count ++ );
        }
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public Selection(){
        this(null, null);
    }
    public Selection(String name, List selectedItems){
        this.name = name == null ? null : new String(name);
        this.selectedItems = selectedItems == null ? new ArrayList() : new ArrayList(selectedItems);
    }

    public String getName() {
        return new String(name);
    }

    public List<ISelectable> getSelectedItems() {
        return Collections.unmodifiableList(selectedItems);
    }

    public List<NodeShape> getSelectedNodes(){
        if(selectedItems != null){
            List<NodeShape> selectedNodes = new ArrayList<NodeShape>();
            for(ISelectable i : selectedItems){
                if(i instanceof NodeShape){
                    selectedNodes.add((NodeShape)i);
                }
            }
            return selectedNodes;
        } else {
            return null;
        }
    }

    public List<EdgeShape> getSelectedEdges(){
        if(selectedItems != null){
            List<EdgeShape> selectedEdges = new ArrayList<EdgeShape>();
            for(ISelectable i : selectedItems){
                if(i instanceof EdgeShape){
                    selectedEdges.add((EdgeShape)i);
                }
            }
            return selectedEdges;
        } else {
            return null;
        }
    }

    public List<TagShape> getSelectedTags(){
        if(selectedItems != null){
            List<TagShape> selectedTags = new ArrayList<TagShape>();
            for(ISelectable i : selectedItems){
                if(i instanceof TagShape){
                    selectedTags.add((TagShape)i);
                }
            }
            return selectedTags;
        } else {
            return null;
        }
    }

    public void add(ISelectable item){
        selectedItems.add(item);
    }

    public void remove(ISelectable item){
        selectedItems.remove(item);
    }

    public void clear(){
        selectedItems.clear();
    }

    public int size(){
        return selectedItems.size();
    }
    
    @Override
    public Iterator<ISelectable> iterator() {
        return new SelectionIterator(this);
    }
   
    
    public static Selection fromXml(Element element, LatticeShape lattice) throws XmlInvalidException {
        Selection selection = new Selection();
        if( ! element.getNodeName().equals("Selection")){
            throw new XmlInvalidException();
        }
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Name")){
                selection.name = el.getTextContent();
            } else if(elName.equals("Node")){
                selection.add(lattice.getNodeById(el.getTextContent()));
            } else if(elName.equals("Edge")){
                String u1 = el.getAttribute("u1");
                String u2 = el.getAttribute("u2");
                if(u1.equals("") || u2.equals("")){
                    throw new XmlInvalidException();
                }
                selection.add(lattice.getEdgeByNodes(u1, u2));
            } else if(elName.equals("Tag")){
                selection.add(lattice.getTagByName(el.getTextContent()));
            } else {
                throw new XmlInvalidException();
            }
        }
        return selection;
    }
    
    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Selection");
            if(name != null){
                writer.writeEntityWithText("Name", name);
            }
            for(ISelectable i : this){
                if(i instanceof NodeShape){
                    writer.writeEntityWithText("Node", ((NodeShape)i).getLogicalNode().getId());
                } else if(i instanceof EdgeShape){
                    writer.writeEntity("Edge")
                        .writeAttribute("u1", ((EdgeShape)i).getU1().getLogicalNode().getId())
                        .writeAttribute("u2", ((EdgeShape)i).getU2().getLogicalNode().getId())
                    .endEntity();
                } else if(i instanceof TagShape){
                    writer.writeEntityWithText("Tag", ((TagShape)i).getLogicalTag().getName());
                }
            }
        writer.endEntity();
        return writer;
    }
}
