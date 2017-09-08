package kandrm.JLatVis.lattice.editing.history;

import kandrm.JLatVis.lattice.logical.Node;
import math.geom2d.Point2D;

/**
 *
 * @author Michal Kandr
 */
public class HistoryEventNodeLogical extends HistoryEvent{
    private Node node;
    private String oldName;
    private String newName;
    private String oldComment;
    private String newComment;
    private Point2D oldCenter;
    private Point2D newCenter;

    public HistoryEventNodeLogical(Node node,
            String oldName, String newName,
            String oldComment, String newComment,
            Point2D oldCenter, Point2D newCenter){
        super(node);
        this.node = node;
        this.oldName = oldName;
        this.newName = newName;
        this.oldComment = oldComment;
        this.newComment = newComment;
        this.oldCenter = oldCenter.clone();
        this.newCenter = newCenter.clone();
    }

    @Override
    public void reverse() {
        node.setName(oldName);
        node.setComment(oldComment);
        node.getShape().setCenter(oldCenter.clone());
    }

    @Override
    public void doAgain() {
        node.setName(newName);
        node.setComment(newComment);
        node.getShape().setCenter(newCenter.clone());
    }
}
