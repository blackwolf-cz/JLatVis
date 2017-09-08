package kandrm.JLatVis.lattice.editing.search;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.visual.NodeShape;

/**
 *
 * @author Michal Kandr
 */
public class OnlineHighlight extends MouseAdapter {
    public static final int CONE_UPPER = 0;
    public static final int CONE_LOWER = 1;
    public static final int NEIGHBORS_UPPER = 2;
    public static final int NEIGHBORS_LOWER = 3;
    
    protected LogicalSearch search;
    
    protected List<NodeShape> upper = new ArrayList<NodeShape>();
    protected List<NodeShape> lower = new ArrayList<NodeShape>();
    
    protected Integer activeHighlight = null;
    
    public OnlineHighlight(LogicalSearch search){
        this.search = search;
    }
    
    public void setActiveHighlight(Integer activeHighlight){
        if(activeHighlight != null
                && activeHighlight != CONE_UPPER && activeHighlight != CONE_LOWER
                && activeHighlight != NEIGHBORS_UPPER && activeHighlight != NEIGHBORS_LOWER){
            throw new IllegalArgumentException();
        }
        this.activeHighlight = activeHighlight;
    }
    public void reset(){
        setActiveHighlight(null);
        clear();
    }
    
    protected void mark(boolean marked){
        for(NodeShape node : upper){
            node.setHighlightedUpper(marked);
            node.repaint();
        }
        for(NodeShape node : lower){
            node.setHighlightedLower(marked);
            node.repaint();
        }
    }
    
    protected void clear(){
        mark(false);
        upper.clear();
        lower.clear();
    }
    
    @Override
    public void mouseEntered(MouseEvent e){
        if( ! (e.getSource() instanceof NodeShape) || activeHighlight == null){
            return;
        }
        clear();
        if(activeHighlight == CONE_UPPER){
            for(Node node : search.getUpperCone(((NodeShape)e.getSource()).getLogicalNode())){
                upper.add(node.getShape());
            }
        } else if(activeHighlight == CONE_LOWER){
            for(Node node : search.getLowerCone(((NodeShape)e.getSource()).getLogicalNode())){
                lower.add(node.getShape());
            }
        } else if(activeHighlight == NEIGHBORS_UPPER){
            for(Node node : search.getBiggerNeighbours(((NodeShape)e.getSource()).getLogicalNode())){
                upper.add(node.getShape());
            }
        } else if(activeHighlight == NEIGHBORS_LOWER){
            for(Node node : search.getSmallerNeighbours(((NodeShape)e.getSource()).getLogicalNode())){
                lower.add(node.getShape());
            }
        }
        mark(true);
    }
    
    @Override
    public void mouseExited(MouseEvent e){
        if( ! (e.getSource() instanceof NodeShape)){
            return;
        }
        clear();
    }
}
