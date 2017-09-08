package kandrm.JLatVis.lattice.editing.history;

import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.settings.LatticeVisualSettings;

/**
 * Událost změny vizuálního vzhledu svazu.
 *
 * @author Michal Kandr
 */
public class HistoryEventLatticeVisual extends HistoryEvent {
    private LatticeShape lattice;
    private LatticeVisualSettings oldSetting,
            newSetting;

    public HistoryEventLatticeVisual(LatticeShape Lattice, LatticeVisualSettings oldSetting, LatticeVisualSettings newSetting){
        super(Lattice);
        this.lattice = Lattice;
        this.oldSetting = oldSetting;
        this.newSetting = newSetting;
    }

    @Override
    public void reverse() {
        lattice.setVisualSettings(oldSetting, false);
    }

    @Override
    public void doAgain() {
        lattice.setVisualSettings(newSetting, false);
    }
}
