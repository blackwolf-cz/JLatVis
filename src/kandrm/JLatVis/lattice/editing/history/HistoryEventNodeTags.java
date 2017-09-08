package kandrm.JLatVis.lattice.editing.history;

import java.util.List;
import kandrm.JLatVis.lattice.logical.Tag;

/**
 *
 * @author Michal Kandr
 */
public class HistoryEventNodeTags extends HistoryEvent {
    private List<Tag> added;
    private List<Tag> removed;

    public HistoryEventNodeTags(List<Tag> added, List<Tag> removed){
        super(added);
        this.added = added;
        this.removed = removed;
    }

    @Override
    public void reverse() {
        for(Tag c : added){
            c.getNode().removeTag(c);
        }
        for(Tag c : removed){
            c.getNode().addTag(c);
        }
    }

    @Override
    public void doAgain() {
        for(Tag c : added){
            c.getNode().addTag(c);
        }
        for(Tag c : removed){
            c.getNode().removeTag(c);
        }
    }
}