package kandrm.JLatVis.lattice.editing.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kandrm.JLatVis.lattice.logical.Lattice;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.editing.selection.Selector;
import kandrm.JLatVis.gui.search.PathSelector;

/**
 * Vyhledávání cest mezi vrcholy. Umí najít vechny cesty vedoucí z jednoho vrcholu do jiného 
 * nebo všechny cesty mezi všemi vrcholy ve skupině. Se svazem tedy pracuje jako
 * s uspořádanou množinou, resp. orientovaným grafem.
 *
 * @author Michal Kandr
 */
public class PathSearch extends Highlight {
    /**
     * Nalezené cesty mezi vrcholy.
     */
    List< List<Node> > paths = new ArrayList< List<Node> >();
    
    public PathSearch(Lattice lattice, Selector selector, HighlightResults results){
        super(lattice, selector, results);
    }

    /**
     * Najde všechny cesty mezi dvěma uzly.
     *
     * @param from
     * @param to
     * @param pathToActual pomocná kolekce do které se ukládá cesta od výchozího uzlu k aktuálně testovanému uzlu
     * @param foundPaths pomocká kolekce sloužící pro ukládání doposud nalezených cest
     */
    private void findPaths(Node from, Node to, List<Node> pathToActual, List < List<Node> > foundPaths){
        pathToActual.add(from);
        if(from == to){
            foundPaths.add(new ArrayList<Node>(pathToActual));
        } else {
            for(Node node : from.getDescendants()){
                findPaths(node, to, pathToActual, foundPaths);
            }
        }
        pathToActual.remove(from);
    }

    /**
     * Najde všecny cesty spojující uzly ze zadané množiny.
     *
     * @param selectedNodes
     *
     * @return nalezené cesty
     */
    public List < List<Node> > findPaths(List<Node> selectedNodes){
        if(selectedNodes == null){
            selectedNodes = getSelectedNodes();
        }
        List < List<Node> > foundPaths = new ArrayList < List<Node> >();
        // only one node - show it
        if(selectedNodes.size() == 1){
            foundPaths.add(new ArrayList<Node>(Arrays.asList(selectedNodes.get(0))));
        // find paths between nodes
        } else {
            for(Node n1 : selectedNodes){
                for(Node n2 : selectedNodes){
                    if(n1 != n2){
                        findPaths(n1, n2, new ArrayList<Node>(), foundPaths);
                    }
                }
            }
        }
        return foundPaths;
    }

    /**
     * @return cesty nalezené při posledním hledání
     */
    public List<List<Node>> getFoundPaths(){
        return Collections.unmodifiableList(paths);
    }

    /**
     * Uloží jako výsledek hledání uzly obsažené v cestě, která se nachází v kolekci
     * nalezených cest na zadaném indexu.
     *
     * @param index
     */
    public void showPathAt(int index){
        results.saveFoundNodes(paths.get(index));
    }
    
    public void hidePath(){
        results.saveFoundNodes(null);
    }

    public void refreshPaths(){
        paths = findPaths(null);
    }

    /**
     * Provede hledání cest mezi uzly které uživatel vybral.
     * Předpokládá se volání z gui na příní uživatele.
     * 
     * Nejsou-li nalezeny žádné cesty, nebo je-li nalezena cesta jen jediná, výsledek
     * (nalezené uzly) se označí jako výsledek hledání. Je-li nalezeno více cest,
     * otevře se okno pro výběr z nalezených cest.
     */
    public void findPaths(){
        refreshPaths();
        PathSelector pathSelector = new PathSelector(this, selector);
        pathSelector.setVisible(true);
    }
}
