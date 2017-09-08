package kandrm.JLatVis.lattice.editing.history;

import java.util.Arrays;
import java.util.List;
import kandrm.JLatVis.lattice.editing.IHidable;

/**
 * Událost změny viditelnsoti uzlu (uzel může být viditelný, nebo skrytý).
 *
 * @author Michal Kandr
 */
public class HistoryEventVisibility extends HistoryEvent {
    private List<IHidable> items;
    private boolean oldVisibility,
            newVisibility;

    public HistoryEventVisibility(List<IHidable> items, boolean oldVisibility, boolean newVisibility){
        super(items);
        this.items = items;
        this.oldVisibility = oldVisibility;
        this.newVisibility = newVisibility;
    }
    public HistoryEventVisibility(IHidable item, boolean oldVisibility, boolean newVisibility){
        this(Arrays.asList(item), oldVisibility, newVisibility);
    }

    @Override
    public void reverse() {
        for(IHidable n : items){
            n.setVisible(oldVisibility, false);
        }
    }

    @Override
    public void doAgain() {
        for(IHidable n : items){
            n.setVisible(newVisibility, false);
        }
    }
}
