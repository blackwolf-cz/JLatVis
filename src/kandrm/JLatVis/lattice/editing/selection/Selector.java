package kandrm.JLatVis.lattice.editing.selection;

import com.generationjava.io.xml.XmlWriter;
import java.awt.event.MouseAdapter;
import kandrm.JLatVis.gui.selection.DraggSelector;
import kandrm.JLatVis.lattice.visual.EdgeShape;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.TagShape;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.lattice.visual.NodeNameShape;
import org.w3c.dom.Element;

/**
 *
 * @author Michal Kandr
 */
public class Selector extends MouseAdapter implements IXmlSerializable {
    /**
     * Typy grafickych objektu, ktere mouhou byt vybrany.
     */
    public enum ItemType { Node, Edge, Tag }

    protected List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

    private LatticeShape latticeShape;
    private Selection selection = new Selection();
    private DraggSelector dSelector;

    public Selector(LatticeShape lattice){
        this.latticeShape = lattice;
        dSelector = new DraggSelector(lattice, this);
    }

    public DraggSelector getDraggSelector(){
        return dSelector;
    }

    public void addChangeListener(ChangeListener listener){
        changeListeners.add(listener);
    }
    public void removeChangeListener(ChangeListener listener){
        changeListeners.remove(listener);
    }
    protected void fireChangeEvent(){
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : changeListeners) {
            listener.stateChanged(event);
        }
    }

    private List getItemsByType(ItemType type) throws IllegalArgumentException{
        List items = null;
        switch(type){
            case Node:
                items = latticeShape.getNodes();
                break;
            case Edge:
                items = latticeShape.getEdges();
                break;
            case Tag:
                items = latticeShape.getTags();
                break;
            default:
                throw new IllegalArgumentException();
        }
        return items;
    }


    public List<ISelectable> getSelectedItems(){
        return selection.getSelectedItems();
    }

    public List<NodeShape> getSelectedNodes(){
        return selection.getSelectedNodes();
    }

    public List<EdgeShape> getSelectedEdges(){
        return selection.getSelectedEdges();
    }

    public List<TagShape> getSelectedTags(){
        return selection.getSelectedTags();
    }

    public void selectItem(ISelectable item, boolean unselectOthers){
        if(unselectOthers){
            unselectAll();
        }
        item.setSelected(true);
        selection.add(item);
        latticeShape.getLogicalSearch().executeActiveSearch();
        latticeShape.repaint();
        fireChangeEvent();
    }

    public void selectItems(Collection<? extends ISelectable> items, boolean unselectOthers){
        if(items == null || items.size()<1){
            return;
        }
        if(unselectOthers){
            unselectAll();
        }
        for(ISelectable i : items){
            selectItem(i, false);
        }
    }


    private void selectItemsAll(ItemType type, List<ISelectable> selected) throws ClassCastException, IllegalArgumentException {
        for(Object iObj : getItemsByType(type)){
            ISelectable i = (ISelectable) iObj;
            if( ! i.isSelected()){
                selected.add(i);
                selectItem(i, false);
            }
        }
    }
    
    public void selectAll(){
        List<ISelectable> selected = new ArrayList<ISelectable>();

        selectItemsAll(ItemType.Node, selected);
        selectItemsAll(ItemType.Edge, selected);
        selectItemsAll(ItemType.Tag, selected);
    }

    public void selectAllNodes(){
        List<ISelectable> selected = new ArrayList<ISelectable>();
        selectItemsAll(ItemType.Node, selected);
    }

    public void selectAllEdges(){
        List<ISelectable> selected = new ArrayList<ISelectable>();
        selectItemsAll(ItemType.Edge, selected);
    }

    public void selectAllTags(){
        List<ISelectable> selected = new ArrayList<ISelectable>();
        selectItemsAll(ItemType.Tag, selected);
    }


    public void unselectItem(final ISelectable item){
        item.setSelected(false);
        selection.remove(item);
        latticeShape.getLogicalSearch().executeActiveSearch();
        latticeShape.repaint();
        fireChangeEvent();
    }

    public void unselectItems(Collection<ISelectable> items){
        if(items == null || items.size()<1){
            return;
        }
        List<ISelectable> prevSelected = new ArrayList<ISelectable>();
        for(ISelectable i : items){
            if(i.isSelected()){
                prevSelected.add(i);
                unselectItem(i);
            }
        }
    }

    public void unselectAll(){
        if(selection.size() <= 0){
            return;
        }
        for(ISelectable i : selection){
            i.setSelected(false);
        }
        selection.clear();
        latticeShape.getLogicalSearch().executeActiveSearch();
        latticeShape.repaint();
        fireChangeEvent();
    }

    public void unselectAllNodes(){
       unselectItems(getItemsByType(ItemType.Node));
    }

    public void unselectAllEdges(){
       unselectItems(getItemsByType(ItemType.Edge));
    }

    public void unselectAllTags(){
       unselectItems(getItemsByType(ItemType.Tag));
    }

    private void invertItemsSelection(ItemType type, List<ISelectable> selected, List<ISelectable> unselected)
            throws ClassCastException, IllegalArgumentException {
        List items = getItemsByType(type);
        
        for(Object iObj : items){
            ISelectable i = (ISelectable) iObj;
            if(i.isSelected()){
                unselected.add(i);
                unselectItem(i);
            } else {
                selected.add(i);
                selectItem(i, false);
            }
        }
    }
    
    public void invertSelection(){
        List<ISelectable> selected = new ArrayList<ISelectable>();
        List<ISelectable> unselected = new ArrayList<ISelectable>();

        invertItemsSelection(ItemType.Node, selected, unselected);
        invertItemsSelection(ItemType.Edge, selected, unselected);
        invertItemsSelection(ItemType.Tag, selected, unselected);
    }

    public void invertSelectionNodes(){
        List<ISelectable> selected = new ArrayList<ISelectable>();
        List<ISelectable> unselected = new ArrayList<ISelectable>();
        invertItemsSelection(ItemType.Node, selected, unselected);
    }

    public void invertSelectionEdges(){
        List<ISelectable> selected = new ArrayList<ISelectable>();
        List<ISelectable> unselected = new ArrayList<ISelectable>();
        invertItemsSelection(ItemType.Edge, selected, unselected);
    }
    public void invertSelectionTags(){
        List<ISelectable> selected = new ArrayList<ISelectable>();
        List<ISelectable> unselected = new ArrayList<ISelectable>();
        invertItemsSelection(ItemType.Tag, selected, unselected);
    }
    
    
    public List<ISelectable> addEdgesBetweenNodes(boolean ret){
        List<ISelectable> select = new ArrayList<ISelectable>();
        for(EdgeShape edge : latticeShape.getEdges()){
            if(edge.getU1().isSelected() && edge.getU2().isSelected()){
                select.add(edge);
            }
        }
        if(ret){
            return select;
        }
        selectItems(select, false);
        return null;
    }
    public void addEdgesBetweenNodes(){
        addEdgesBetweenNodes(false);
    }
    
    public List<ISelectable> addNodesTags(boolean ret){
        List<ISelectable> select = new ArrayList<ISelectable>();
        for(NodeShape node : getSelectedNodes()){
            select.addAll(node.getTags());
        }
        if(ret){
            return select;
        }
        selectItems(select, false);
        return null;
    }
    public void addNodesTags(){
        addNodesTags(false);
    }
    
    public List<ISelectable> addEdgesNodes(boolean ret){
        List<ISelectable> select = new ArrayList<ISelectable>();
        for(EdgeShape edge : getSelectedEdges()){
            select.add(edge.getU1());
            select.add(edge.getU2());
        }
        if(ret){
            return select;
        }
        selectItems(select, false);
        return null;
    }
    public void addEdgesNodes(){
        addEdgesNodes(false);
    }
    
    public List<ISelectable> addTagsNodes(boolean ret){
        List<ISelectable> select = new ArrayList<ISelectable>();
        for(TagShape tag : getSelectedTags()){
            select.add(tag.getNode());
        }
        if(ret){
            return select;
        }
        selectItems(select, false);
        return null;
    }
    public void addTagsNodes(){
        addTagsNodes(false);
    }
    
    public void addAllRelated(){
        List<ISelectable> select = new ArrayList<ISelectable>();
        select.addAll(addEdgesBetweenNodes(true));
        select.addAll(addNodesTags(true));
        select.addAll(addEdgesNodes(true));
        select.addAll(addTagsNodes(true));
        selectItems(select, false);
    }
    

    @Override
    public void mouseClicked(MouseEvent evt) {
        if(evt.getButton() != MouseEvent.BUTTON1){
            return;
        }
        Object source = evt.getSource();
        boolean sourceSelectabele = false;
        if(source instanceof ISelectable){
            sourceSelectabele = true;
        } else if(source instanceof NodeNameShape){
            source = ((NodeNameShape)source).getNode();
            sourceSelectabele = true;
        } else if(source == latticeShape){
            for (EdgeShape e : latticeShape.getEdges()){
                if(e.containsWithTolerance(evt.getX(), evt.getY())){
                    source = e;
                    sourceSelectabele = true;
                    break;
                }
            }
        }
        if(sourceSelectabele){
            ISelectable sourceSelectable = (ISelectable) source;
            if(evt.isControlDown() && sourceSelectable.isSelected()){
                unselectItem(sourceSelectable);
            } else if(sourceSelectable.isSelected() && selection.getSelectedItems().size() == 1 && selection.getSelectedItems().get(0) == sourceSelectable){
                unselectItem(sourceSelectable);
            } else {
                selectItem(sourceSelectable, ! evt.isControlDown());
            }
        } else if( ! evt.isControlDown() ) {
            unselectAll();
        }
    }

    public void fromXml(Element element) throws XmlInvalidException {
        selection = Selection.fromXml(element, latticeShape);
        for(ISelectable i : selection){
            i.setSelected(true);
        }
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        return selection.toXml(writer);
    }
}