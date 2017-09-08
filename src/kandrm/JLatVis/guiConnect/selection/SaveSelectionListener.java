package kandrm.JLatVis.guiConnect.selection;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import kandrm.JLatVis.lattice.editing.selection.SavedSelections;

/**
 *
 * @author Michal Kandr
 */
public class SaveSelectionListener implements ListSelectionListener {
    private SavedSelections savedSelections;

    public SaveSelectionListener(SavedSelections savedSelections){
        this.savedSelections = savedSelections;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();

        if(list.getSelectedIndex() >=0 && list.getSelectedIndex() < savedSelections.getSize()){
            savedSelections.loadSelectionAt(list.getSelectedIndex());
        }
    }
}
