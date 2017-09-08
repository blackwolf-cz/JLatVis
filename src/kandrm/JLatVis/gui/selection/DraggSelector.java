package kandrm.JLatVis.gui.selection;

import kandrm.JLatVis.lattice.visual.EdgeShape;
import kandrm.JLatVis.lattice.editing.selection.ISelectable;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.TagShape;
import kandrm.JLatVis.lattice.editing.selection.Selector;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.gui.DraggSquare;
import kandrm.JLatVis.lattice.visual.settings.SelectRectVisualSettings;
import math.geom2d.Box2D;
import math.geom2d.polygon.Rectangle2D;

/**
 * Ovladač výběru tažením myši.
 *
 * Zajišťuje výběr prvků uživatelem na základě tažení myši. Uživatel tažením myši
 * označí určitou oblast svazu a vše co tato oblast obsahuje se označí.
 * Označená oblast je vizuálně znázorněna obdélníkem.
 *
 * @author Michal Kandr
 */
public class DraggSelector extends DraggSquare {
    private Selector selector;

    private boolean selectNodes = true;
    private boolean selectEdges = false;
    private boolean selectTags = false;

    /**
     * Nový ovladač výběru tažením myši.
     *
     * @param lattice
     * @param selector
     * @param settings vizuální vzhled znázornění oblasti (obdélníku)
     */
    public DraggSelector(LatticeShape lattice, Selector selector, SelectRectVisualSettings visualSettings){
        super(lattice, visualSettings);
        this.selector = selector;
    }

    /**
     * Nový ovladač výběru tažením myši.
     * 
     * @param lattice
     * @param selector
     */
    public DraggSelector(LatticeShape lattice, Selector selector){
        this(lattice, selector, new SelectRectVisualSettings());
    }

    public boolean isSelectTags() {
        return selectTags;
    }
    public void setSelectTags(boolean selectTags) {
        this.selectTags = selectTags;
    }

    public boolean isSelectEdges() {
        return selectEdges;
    }
    public void setSelectEdges(boolean selectEdges) {
        this.selectEdges = selectEdges;
    }

    public boolean isSelectNodes() {
        return selectNodes;
    }
    public void setSelectNodes(boolean selectNodes) {
        this.selectNodes = selectNodes;
    }

    /**
     * Najde uzly nacházející se uvnitř výběru.
     *
     * @param shape hranice výběru
     * @param itemsInside seznam nalezených prvků, do kterého se uzly
     */
    private void findNodesInside(Box2D shape, List<ISelectable> itemsInside){
        for(NodeShape node : lattice.getNodes()){
            if(shape.containsBounds(new Rectangle2D(node.getShapeBounds()))){
                itemsInside.add(node);
            }
        }        
    }

    /**
     * Najde popisky nacházející se uvnitř výběru.
     *
     * @param shape hranice výběru
     * @param itemsInside seznam nalezených prvků, do kterého se popisky přidají
     */
    private void findTagsInside(Box2D shape, List<ISelectable> itemsInside){
        for(TagShape tag : lattice.getTags()){
            if(shape.containsBounds(new Rectangle2D(tag.getBoundsWithoutLine()))){
                itemsInside.add(tag);
            }
        }
    }
    
    /**
     * Najde hrany nacházející se uvnitř výběru.
     *
     * @param shape hranice výběru
     * @param itemsInside seznam nalezených prvků, do kterého se hrany přidají
     */
    private void findEdgesInside(Box2D shape, List<ISelectable> itemsInside){
        for(EdgeShape edge : lattice.getEdges()){
            if(shape.containsBounds(new Rectangle2D(edge.getBounds()))){
                itemsInside.add(edge);
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if(startPoint != null && endPoint != null){
            Box2D shape = new Rectangle2D(getX(), getY(), getWidth(), getHeight()).getBoundingBox();
            List<ISelectable> itemsInside = new ArrayList<ISelectable>();

            if(selectNodes){
                findNodesInside(shape, itemsInside);
            }
            if(selectTags){
                findTagsInside(shape, itemsInside);
            }
            if(selectEdges){
                findEdgesInside(shape, itemsInside);
            }

            if(itemsInside.size() > 0){
                selector.selectItems(itemsInside,  ! e.isControlDown());
            } else if( ! e.isControlDown()){
                selector.unselectAll();
            }
        }
        super.mouseReleased(e);
    }
}