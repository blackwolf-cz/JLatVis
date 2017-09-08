package kandrm.JLatVis.guiConnect.settings.logical;

import kandrm.JLatVis.lattice.editing.history.HistoryEventEdgeLogical;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.logical.OrderPair;

/**
 *
 * @author Michal Kandr
 */
public class EdgeModel {
    private OrderPair pair = null;

    private String comment = null;

    public EdgeModel(){}
    
    public EdgeModel(OrderPair edge){
        setEdge(edge);
    }

    public final void setEdge(OrderPair edge) {
        this.pair = edge;
        comment = edge.getComment();
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public void apply(){
        if(pair != null && ! comment.equals(pair.getComment())){
            HistoryEventEdgeLogical e = new HistoryEventEdgeLogical(pair, pair.getComment(), comment);
            for (IHistoryEventListener l : pair.getHistoryListeners()) {
                l.eventPerformed( e );
            }
            pair.setComment(comment);
        }
    }
}
