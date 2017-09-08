package kandrm.JLatVis.guiConnect.search;

import javax.swing.JList;
import kandrm.JLatVis.lattice.editing.search.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Posluchač událostí pro změnu výběru nalezené cesty.
 * Má být navázán na List zobrazující seznam všechn nalezených cest.
 *
 * @author Michal Kandr
 */
public class PathSelectionListener implements ListSelectionListener {
    private PathSearch search;
    private PathsListModel model;

    /**
     * Nový posluchač událostí.
     * @param search
     * @param model
     */
    public PathSelectionListener(PathSearch search, PathsListModel model){
        this.search = search;
        this.model = model;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();
        search.showPathAt(list.getSelectedIndex());
    }
}
