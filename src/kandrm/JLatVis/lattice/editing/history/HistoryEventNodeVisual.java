package kandrm.JLatVis.lattice.editing.history;

import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.settings.NodeVisualSettings;

/**
 * Událost změny vizuálního vzhledu uzlu.
 *
 * @author Michal Kandr
 */
public class HistoryEventNodeVisual extends HistoryEvent {
    private NodeShape node;
    private NodeVisualSettings oldSetting,
            newSetting;

    public HistoryEventNodeVisual(NodeShape node, NodeVisualSettings oldSetting, NodeVisualSettings newSetting){
        super(node);
        this.node = node;
        this.oldSetting = oldSetting;
        this.newSetting = newSetting;
    }

    @Override
    public void reverse() {
        node.setVisualSettings(oldSetting, false);
    }

    @Override
    public void doAgain() {
        node.setVisualSettings(newSetting, false);
    }

}
