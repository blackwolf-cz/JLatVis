package kandrm.JLatVis.lattice.editing.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Michal Kandr
 */
public class HistoryEventCompound extends HistoryEvent {
    private List<HistoryEvent> historyEvents;

    public HistoryEventCompound(HistoryEvent ... events){
        super(events);
        historyEvents = Arrays.asList(events);
    }
    
    public HistoryEventCompound(List<HistoryEvent> events){
        super(events);
        historyEvents = new ArrayList<HistoryEvent>(events);
    }

    @Override
    public void reverse() {
        for(HistoryEvent event : historyEvents){
            event.reverse();
        }
    }

    @Override
    public void doAgain() {
        for(HistoryEvent event : historyEvents){
            event.doAgain();
        }
    }
}
