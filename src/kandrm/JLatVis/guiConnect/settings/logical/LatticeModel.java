package kandrm.JLatVis.guiConnect.settings.logical;

import kandrm.JLatVis.lattice.editing.history.HistoryEventLatticeLogical;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.logical.Lattice;

/**
 *
 * @author Michal Kandr
 */
public class LatticeModel {
    private Lattice lattice = null;

    private String name = null;
    private String comment = null;

    public LatticeModel(){}
    
    public LatticeModel(Lattice lattice){
        setLattice(lattice);
    }

    public final void setLattice(Lattice lattice) {
        this.lattice = lattice;
        name = lattice.getName();
        comment = lattice.getComment();
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public void apply(){
        if(lattice != null && ( ! name.equals(lattice.getName()) || ! comment.equals(lattice.getComment()) )){
            HistoryEventLatticeLogical e = new HistoryEventLatticeLogical(lattice, lattice.getName(), name, lattice.getComment(), comment);
            for (IHistoryEventListener l : lattice.getHistoryListeners()) {
                l.eventPerformed( e );
            }
            
            lattice.setName(name);
            lattice.setComment(comment);
        }
    }
}
