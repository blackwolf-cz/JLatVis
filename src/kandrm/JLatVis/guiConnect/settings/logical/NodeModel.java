package kandrm.JLatVis.guiConnect.settings.logical;

import kandrm.JLatVis.lattice.editing.history.HistoryEventNodeLogical;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.logical.Node;
import math.geom2d.Point2D;

/**
 *
 * @author Michal Kandr
 */
public class NodeModel {
    public class ValidationException extends Exception{
        private ValidationException(String message) {
            super(message);
        }
    }

    private Node node = null;

    private String name = null;
    private String comment = null;
    private Integer x = null;
    private Integer y = null;

    public NodeModel(){}
    
    public NodeModel(Node node){
        setNode(node);
    }

    public final void setNode(Node node) {
        this.node = node;
        name = node.getName();
        comment = node.getComment();
        x = (int)Math.round(node.getShape().getCenter().getX());
        y = (int)Math.round(node.getShape().getCenter().getY());
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getComment(){
        return comment;
    }
    public void setComment(String comment){
        this.comment = comment;
    }

    public Integer getX() {
        return x;
    }
    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }
    public void setY(Integer y) {
        this.y = y;
    }
    
    public int getMinimalAllowedY(){
        return node.getShape().getMinimalAllowedY();
    }
    public int getMaximalAllowedY(){
        return node.getShape().getMaximalAllowedY();
    }

    public void apply(){
        if(node != null && (
                ! node.getName().equals(name)
                || (node.getComment() == null && comment == null)
                || (node.getComment() != null && ! node.getComment().equals(comment))
                || node.getShape().getX() != x
                || node.getShape().getY() != y)){
            Point2D newCenter = new Point2D(x, y);
            HistoryEventNodeLogical e = new HistoryEventNodeLogical(node, node.getName(), name, node.getComment(), comment, node.getShape().getCenter(), newCenter);
            for (IHistoryEventListener l : node.getHistoryListeners()) {
                l.eventPerformed( e );
            }
            node.setName(name);
            node.setComment(comment);
            node.getShape().setCenter(newCenter);
        }
    }
}
