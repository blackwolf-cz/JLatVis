package kandrm.JLatVis.lattice.editing.search;

import java.util.LinkedList;
import java.util.List;
import kandrm.JLatVis.lattice.logical.Lattice;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.editing.selection.Selector;

/**
 * Obecná třída pro vyhledávání. Implementuje základní funkce společné všem vyhledávacím třídám.
 * Konkrétní třídy pak realizují specifický typ vyhledávání.
 *
 * @author Michal Kandr
 */
public abstract class Search {
    protected Lattice lattice;
    /**
     * Selector využívá hlavně instance třídy SearchResults pro vybrání nalezených prvků.
     */
    protected Selector selector;
    /**
     * Objekt uchovávající výsledky vyhledávání
     */
    protected SearchResults results;

    /**
     * 
     * @param lattice prohledávaný svaz
     * @param selector selector příslušného svazu
     * @param results uchovávání výsledků
     */
    protected Search(Lattice lattice, Selector selector, SearchResults results){
        this.lattice = lattice;
        this.selector = selector;
        this.results = results;
    }

    /**
     * Načte vybrané uzly jako List logických uzlů (Node).
     * 
     * @return kolekce logických reprezentací vybraných uzlů
     */
    protected List<Node> getSelectedNodes() {
        List<Node> selectedNodes = new LinkedList<Node>();
        for (NodeShape node : selector.getSelectedNodes()) {
            selectedNodes.add(node.getLogicalNode());
        }
        return selectedNodes;
    }
    
    /**
     * Smaže aktuální výsledky vyhledávání (vynuluje vyhledávání).
     */
    public void reset(){
        results.reset();
    }
}
