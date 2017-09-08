package kandrm.JLatVis.lattice.editing.history;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * History manager. Stores history events, supports manipullating with them a moving in history back or forward.
 * 
 * @author Michal Kandr
 */
public class History implements IHistoryEventListener{
    protected List<ChangeListener> changeListeners = new LinkedList<ChangeListener>();
    
    protected List<HistoryEvent> history = new LinkedList<HistoryEvent>();
    
    protected boolean transactionActive = false;
    protected List<HistoryEvent> catchedEvents = new ArrayList<HistoryEvent>();

    /**
     * Actual position in history
     */
    protected int actualHistoryPosition = 0;


    protected void fireChangeEvents(){
        ChangeEvent e = new ChangeEvent(0);
        for(ChangeListener l : changeListeners){
            l.stateChanged(e);
        }
    }

    public void addChangeListener(ChangeListener l){
        changeListeners.add(l);
    }
    public void removeChangeListener(ChangeListener l){
        changeListeners.remove(l);
    }

    /**
     * Delete all inverted events (events after actual position).
     */
    private void clearNextEvents(){
        if(actualHistoryPosition <= history.size()-1){
            history.subList(actualHistoryPosition, history.size()).clear();
        }
    }
    
    public void startTransaction(){
        if(catchedEvents.size() > 0){
            endTransaction();
        }
        transactionActive = true;
    }
    
    public void endTransaction(){
        transactionActive = false;
        if(catchedEvents.size() > 0){
            eventPerformed(new HistoryEventCompound(catchedEvents));
        }
        catchedEvents.clear();
    }

    /**
     * Add new history event.
     * Reverted events are deleted.
     * @param evt added event
     */
    @Override
    public void eventPerformed(HistoryEvent e){
        if(transactionActive){
            catchedEvents.add(e);
        } else {
            if(actualHistoryPosition <= history.size()){
                clearNextEvents();
            }
            history.add(e);
            ++ actualHistoryPosition;
            fireChangeEvents();
        }
    }

    /**
     * Delete all events.
     */
    public void clear(){
        history.clear();
        actualHistoryPosition = 0;
        fireChangeEvents();
    }

    /**
     * Revert last event if present.
     */
    public void backward(){
        if(canBackward()){
            actualHistoryPosition --;
            history.get(actualHistoryPosition).reverse();
            fireChangeEvents();
        }
    }

    /**
     * Do again last reverted event, if exists.
     */
    public void forward(){
        if(canForward()){
            history.get(actualHistoryPosition).doAgain();
            actualHistoryPosition ++;
        }
        fireChangeEvents();
    }

    /**
     * Revert all actions.
     */
    public void toBegining(){
        while(canBackward()){
            backward();
        }
    }

    /**
     * Do again all reverted actions
     */
    public void toEnd(){
        while(canForward()){
            forward();
        }
    }

    /**
     * @return are there any events to revert
     */
    public boolean canBackward(){
        return actualHistoryPosition > 0;
    }

    /**
     * @return are there any reverted events
     */
    public boolean canForward(){
        return actualHistoryPosition < history.size();
    }

    /**
     * @return hash of current position in history
     */
    public int getActualPositionHash(){
        return (actualHistoryPosition + "-" + (history.isEmpty() || ! canBackward() ? 0 : history.get( actualHistoryPosition > 0 ? actualHistoryPosition - 1 : 0).hashCode() )).hashCode();
    }
}
