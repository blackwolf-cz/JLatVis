package kandrm.JLatVis.lattice.editing.history;

import kandrm.JLatVis.lattice.visual.EdgeShape;
import kandrm.JLatVis.lattice.visual.settings.EdgeVisualSettings;

/**
 * Událost změny vizuálního vzhledu hrany mezi uzly.
 *
 * @author Michal Kandr
 */
public class HistoryEventEdgeVisual extends HistoryEvent {
    private EdgeShape edge;
    private EdgeVisualSettings oldSetting,
            newSetting;

    public HistoryEventEdgeVisual(EdgeShape edge, EdgeVisualSettings oldSetting, EdgeVisualSettings newSetting){
        super(edge);
        this.edge = edge;
        this.oldSetting = oldSetting;
        this.newSetting = newSetting;
    }

    @Override
    public void reverse() {
        edge.setVisualSettings(oldSetting, false);
    }

    @Override
    public void doAgain() {
        edge.setVisualSettings(newSetting, false);
    }

}
