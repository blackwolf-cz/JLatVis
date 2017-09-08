package kandrm.JLatVis.guiConnect.selection;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import kandrm.JLatVis.lattice.editing.selection.ISavedSelectionsEventListener;
import kandrm.JLatVis.lattice.editing.selection.SavedSelections;
import kandrm.JLatVis.lattice.editing.selection.SavedSelectionsChangeEvent;

/**
 *
 * @author Michal Kandr
 */
public class SavedSelectionsModel implements ListModel, ISavedSelectionsEventListener {
    private List<ListDataListener> listDataListeners = new ArrayList<ListDataListener>();
    private SavedSelections selections;

    public SavedSelectionsModel(SavedSelections selections){
        this.selections = selections;
        selections.addHistoryEventListener(this);
    }

    private void fireListDataEvent(int type, int startIndex, int endIndex){
        for(ListDataListener l : listDataListeners){
            l.intervalRemoved(new ListDataEvent(this, type, startIndex, endIndex));
        }
    }

    public void deleteSelectionAt(int index){
        selections.deleteSelectionAt(index);
    }

    @Override
    public int getSize() {
        return selections.getSize();
    }

    @Override
    public Object getElementAt(int index) {
        return selections.getSelectionAt(index).getName();
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listDataListeners.add(l);
    }
    @Override
    public void removeListDataListener(ListDataListener l) {
        listDataListeners.remove(l);
    }

    @Override
    public void savedSelectionsEvenOccurred(SavedSelectionsChangeEvent evt) {
        fireListDataEvent(
            evt.getType()==SavedSelectionsChangeEvent.Type.Add ? ListDataEvent.INTERVAL_ADDED : ListDataEvent.INTERVAL_REMOVED,
            evt.getStartIndex(),
            evt.getEndIndex()
        );
    }
}
