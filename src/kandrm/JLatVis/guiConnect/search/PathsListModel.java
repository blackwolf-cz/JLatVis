package kandrm.JLatVis.guiConnect.search;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.editing.search.PathSearch;

/**
 * Nalezené cesty - model pro List zobrazující nalezené cesty uživateli, který z nich může vybírat.
 * @author Michal Kandr
 */
public class PathsListModel implements ListModel {
    private PathSearch search;

    protected List<ListDataListener> listDataListeners = new ArrayList<ListDataListener>();

    /**
     * Nový model pro příslušné vyhledávání.
     * @param search
     */
    public PathsListModel(PathSearch search){
        this.search = search;
    }

    public void fireChangeEvent(){
        ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
        for (ListDataListener l : listDataListeners) {
            l.contentsChanged(event);
        }
    }

    @Override
    public int getSize() {
        return search.getFoundPaths().size();
    }

    @Override
    public Object getElementAt(int index) {
        StringBuilder pathString = new StringBuilder();
        int i = 0;
        for(Node node : search.getFoundPaths().get(index)){
            if( i++ != 0 ){
                pathString.append(" - ");
            }
            pathString.append(node.getName());
        }
        return pathString.toString();
    }

    /**
     * Vrátí cestu nacházející se na zadaném indexu modelu.
     * Umožňuje získat cestu v podobě seznamu uzlů na základě uživatelova výběru prvku v listu.
     *
     * @param index
     *
     * @return cesta odpovídající příslušnému indexu
     */
    public List<Node> getPathAt(int index){
        return search.getFoundPaths().get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listDataListeners.add(l);
    }
    @Override
    public void removeListDataListener(ListDataListener l) {
        listDataListeners.remove(l);
    }
}
