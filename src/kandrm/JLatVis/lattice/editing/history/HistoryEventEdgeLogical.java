package kandrm.JLatVis.lattice.editing.history;

import kandrm.JLatVis.lattice.logical.OrderPair;

/**
 *
 * @author Michal Kandr
 */
public class HistoryEventEdgeLogical extends HistoryEvent {
    private OrderPair edge;
    private String oldComment;
    private String newComment;

    public HistoryEventEdgeLogical(OrderPair edge, String oldComment, String newComment){
        super(edge);
        this.edge = edge;
        this.oldComment = oldComment;
        this.newComment = newComment;
    }

    @Override
    public void reverse() {
        edge.setComment(oldComment);
    }

    @Override
    public void doAgain() {
        edge.setComment(newComment);
    }
}
