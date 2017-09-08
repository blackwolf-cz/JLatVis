package kandrm.JLatVis.lattice.editing.history;

import kandrm.JLatVis.lattice.logical.Lattice;

/**
 *
 * @author Michal Kandr
 */
public class HistoryEventLatticeLogical extends HistoryEvent {
    private Lattice lattice;
    private String oldName;
    private String newName;
    private String oldComment;
    private String newComment;

    public HistoryEventLatticeLogical(Lattice lattice, String oldName, String newName, String oldComment, String newComment){
        super(lattice);
        this.lattice = lattice;
        this.oldName = oldName;
        this.newName = newName;
        this.oldComment = oldComment;
        this.newComment = newComment;
    }

    @Override
    public void reverse() {
        lattice.setName(oldName);
        lattice.setComment(oldComment);
    }

    @Override
    public void doAgain() {
        lattice.setName(newName);
        lattice.setComment(newComment);
    }
}
