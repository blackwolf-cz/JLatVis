package kandrm.JLatVis.lattice.editing.history;

import kandrm.JLatVis.lattice.logical.Tag;

/**
 *
 * @author Michal Kandr
 */
public class HistoryEventTagLogical extends HistoryEvent {
    private Tag tag;
    private String oldName;
    private String newName;
    private String oldText;
    private String newText;

    public HistoryEventTagLogical(Tag tag, String oldName, String newName, String oldText, String newText){
        super(tag);
        this.tag = tag;
        this.oldName = oldName;
        this.newName = newName;
        this.oldText = oldText;
        this.newText = newText;
    }

    @Override
    public void reverse() {
        tag.setName(oldName);
        tag.setText(oldText);
    }

    @Override
    public void doAgain() {
        tag.setName(newName);
        tag.setText(newText);
    }
}
