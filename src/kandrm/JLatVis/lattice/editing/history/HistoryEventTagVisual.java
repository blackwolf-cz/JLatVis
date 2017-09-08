package kandrm.JLatVis.lattice.editing.history;

import kandrm.JLatVis.lattice.visual.TagShape;
import kandrm.JLatVis.lattice.visual.settings.TagVisualSettings;

/**
 * Událost změny vizuálního vzhledu popisku uzlu.
 *
 * @author Michal Kandr
 */
public class HistoryEventTagVisual extends HistoryEvent {
    private TagShape tag;
    private TagVisualSettings oldSetting,
            newSetting;

    public HistoryEventTagVisual(TagShape tag, TagVisualSettings oldSetting, TagVisualSettings newSetting){
        super(tag);
        this.tag = tag;
        this.oldSetting = oldSetting;
        this.newSetting = newSetting;
    }

    @Override
    public void reverse() {
        tag.setVisualSettings(oldSetting, false);
    }

    @Override
    public void doAgain() {
        tag.setVisualSettings(newSetting, false);
    }

}
