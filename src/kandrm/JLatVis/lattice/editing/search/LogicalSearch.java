package kandrm.JLatVis.lattice.editing.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kandrm.JLatVis.lattice.logical.Lattice;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.editing.selection.Selector;

/**
 * Vyhledávání podle logické struktury svazu (uspořádaná množina). Implementuje
 * vyhledávání podle relace uspořádání.
 *
 * @author Michal Kandr
 */
public class LogicalSearch extends Highlight{
    /**
     * Typy extrémů (minimum a maximum).
     */
    protected enum Extrems { Min, Max }
    /**
     * Typy vytahů se sousedy (větší a menší sousedé).
     */
    protected enum Neighbours { Smaller, Bigger }
    /**
     * Typy kuželů (horní a dolní).
     */
    protected enum Cones { Upprer, Lower }

    public enum OnFlySearch { Maximum, Minimum, Highest, Lowest, BiggerNeighbours, SmallerNeighbours, UpperCone, LowerCone, Supremum, Infimum }

    /**
     * Vyhledávání cest mezi prvky.
     */
    protected PathSearch pathSearch;

    protected OnFlySearch activeOnFlySearch = null;
    
    /**
     * Nové ligické vyhledávání.
     *
     * @param lattice
     * @param selector
     * @param results
     */
    public LogicalSearch(Lattice lattice, Selector selector, HighlightResults results){
        super(lattice, selector, results);
        pathSearch = new PathSearch(lattice, selector, results);
    }

    /**
     * Smaže aktuální výsledky vyhledávání (vynuluje vyhledávání).
     */
    @Override
    public void reset(){
        super.reset();
        activeOnFlySearch = null;
    }

    /**
     * Provede hledání maximálních/minimálních prvků.
     *
     * @param selectedNodes prvky, pro které se má hledání provést. Je-li null, vezmou se vybrané prvky ze Selectoru.
     * @param type typ maximální/minimální
     * @param save uložit výsledek hledání
     *
     * @return maximální/minimální prvky množiny;
     */
    protected List<Node> findMinMax(List<Node> selectedNodes, Extrems type, boolean save){
        if(selectedNodes == null){
            selectedNodes = getSelectedNodes();
        }
        List<Node> found = new ArrayList<Node>();
        for(Node node : selectedNodes){
            if(Collections.disjoint(selectedNodes, ( type==Extrems.Min ? node.getDescendantsTransitive() : node.getParentsTransitive() ))){
                found.add(node);
            }
        }
        if(save){
            results.saveFoundNodes(found);
        }
        return found;
    }

    /**
     * Provede hledání největšího/nejmenšího prvku.
     *
     * @param selectedNodes prvky, pro které se má hledání provést. Je-li null, vezmou se vybrané prvky ze Selectoru.
     * @param type typ největší/nejmenší
     * @param save uložit výsledek hledání
     *
     * @return  největší/nejmenší prvek množiny; null pokud množina takový prvek nemá
     */
    protected Node lowestHighest(List<Node> selectedNodes, Extrems type, boolean save){
        List<Node> found = findMinMax(selectedNodes, type, false);
        if(found.size() == 1){
            if(save){
                results.saveFoundNodes(found);
            }
            return found.get(0);
        } else {
            if(save){
                results.saveFoundNodes((List<Node>)null);
            }
            return null;
        }
    }

    /**
     * Provede hledání větších/menších sousedů.
     *
     * @param selectedNodes prvky, pro které se má hledání provést. Je-li null, vezmou se vybrané prvky ze Selectoru.
     * @param type typ větší/menší
     * @param save uložit výsledek hledání
     *
     * @return  větších/menších sousedé
     */
    protected List<Node> findNeighbours(List<Node> selectedNodes, Neighbours type, boolean save){
        if(selectedNodes == null){
            selectedNodes = getSelectedNodes();
        }
        List<Node> found = new ArrayList<Node>();
        for(Node node : selectedNodes){
            for(Node descendant : ( type==Neighbours.Smaller ? node.getDescendants() : node.getParents() )){
                if( ! selectedNodes.contains(descendant)){
                    found.add(descendant);
                }
            }
        }
        if(save){
            results.saveFoundNodes(found);
        }
        return found;
    }

    /**
     * Zjistí, zda je jeden prvek ostře větší než jiný.
     *
     * @param bigger potenciálně větší uzel
     * @param smaller potenciálně menší uzel
     *
     * @return bigger je opravu větší než smaller
     */
    protected boolean isSmaller(Node bigger, Node smaller){
        List<Node> descendants = bigger.getDescendants();
        
        if(descendants.size() < 1){
            return false;
        } else if(descendants.contains(smaller)){
            return true;
        }
        
        for(Node node : descendants){
            if(isSmaller(node, smaller)){
                return true;
            }
        }

        return false;
    }

    /**
     * Provede hledání horního/dolního kužele.
     *
     * @param selectedNodes prvky, pro které se má hledání provést. Je-li null, vezmou se vybrané prvky ze Selectoru.
     * @param type typ horního/dolního
     * @param save uložit výsledek hledání
     *
     * @return prvky patřící do kužele
     */
    protected List<Node> findCone(List<Node> selectedNodes, Cones type, boolean save){
        if(selectedNodes == null){
            selectedNodes = getSelectedNodes();
        }
        List<Node> found = new ArrayList<Node>();

        for(Node node : lattice.getNodes().values()){
            boolean notFound = false;
            for(Node selected : selectedNodes){
                if( (type == Cones.Lower && ! isSmaller(selected, node) && selected != node) || (type == Cones.Upprer && ! isSmaller(node, selected) && selected != node)){
                    notFound = true;
                }
                if(notFound){
                    break;
                }
            }
            if( ! notFound){
                found.add(node);
            }
        }

        if(save){
            results.saveFoundNodes(found);
        }
        
        return found;
    }


    /**
     * Najde menší sousedy vybraných uzlů, kteří nejsou vybráni.
     */
    public void smallerNeighbours(){
        findNeighbours(null, Neighbours.Smaller, true);
    }
    public List<Node> getSmallerNeighbours(List<Node> selectedNodes){
        return findNeighbours(selectedNodes, Neighbours.Smaller, false);
    }
    public List<Node> getSmallerNeighbours(Node node){
        return getSmallerNeighbours(Arrays.asList(node));
    }
    /**
     * Najde větší sousedy vybraných uzlů, kteří nejsou vybráni.
     */
    public void biggerNeighbours(){
        findNeighbours(null, Neighbours.Bigger, true);
    }
    public List<Node> getBiggerNeighbours(List<Node> selectedNodes){
        return findNeighbours(selectedNodes, Neighbours.Bigger, false);
    }
    public List<Node> getBiggerNeighbours(Node node){
        return getBiggerNeighbours(Arrays.asList(node));
    }
    
    /**
     * Najde maximální prvky z množiny vybraných prvků.
     */
    public void minimum(){
        findMinMax(null, Extrems.Min, true);
    }
    /**
     * Najde minimální prvky z množiny vybraných prvků.
     */
    public void maximum(){
        findMinMax(null, Extrems.Max, true);
    }

    /**
     * Najde nejmenší prvek z množiny vybraných prvků.
     */
    public void lowest(){
        lowestHighest(null, Extrems.Min, true);
    }
    public Node lowest(List<Node> selectedNodes){
        return lowestHighest(selectedNodes, Extrems.Min, false);
    }
    /**
     * Najde největší prvek z množiny vybraných prvků.
     */
    public void highest(){
        lowestHighest(null, Extrems.Max, true);
    }
    public Node highest(List<Node> selectedNodes){
        return lowestHighest(selectedNodes, Extrems.Max, false);
    }

    /**
     * Najde prvky tvořící dolní kužel vybraných prvků.
     */
    public void lowerCone(){
        findCone(null, Cones.Lower, true);
    }
    public List<Node> getLowerCone(List<Node> selectedNodes){
        return findCone(selectedNodes, Cones.Lower, false);
    }
    public List<Node> getLowerCone(Node node){
        return getLowerCone(Arrays.asList(node));
    }
    /**
     * Najde prvky tvořící horní kužel vybraných prvků.
     */
    public void upperCone(){
        findCone(null, Cones.Upprer, true);
    }
    public List<Node> getUpperCone(List<Node> selectedNodes){
        return findCone(selectedNodes, Cones.Upprer, false);
    }
    public List<Node> getUpperCone(Node node){
        return getUpperCone( Arrays.asList(node) );
    }

    /**
     * Najde supremum vybraných prvků.
     */
    public void supremum(){
        findMinMax(findCone(null, Cones.Upprer, false), Extrems.Min, true);
    }
    public Node getSupremum(List<Node> selectedNodes){
        List<Node> supremum = findMinMax(findCone(selectedNodes, Cones.Upprer, false), Extrems.Min, false);
        return supremum.size() == 1 ? supremum.get(0) : null;
    }
    /**
     * Najde infimum vybraných prvků.
     */
    public void infimum(){
        findMinMax(findCone(null, Cones.Lower, false), Extrems.Max, true);
    }
    public Node getInfimum(List<Node> selectedNodes){
        List<Node> infimum =  findMinMax(findCone(selectedNodes, Cones.Lower, false), Extrems.Max, false);
        return infimum.size() == 1 ? infimum.get(0) : null;
    }

    /**
     * Najde všechny cesty mezi vybranými prvky.
     */
    public void paths(){
        pathSearch.findPaths();
    }


    public void setActiveSearch(OnFlySearch search){
        if(activeOnFlySearch != null){
            reset();
        }
        activeOnFlySearch = search;
        executeActiveSearch();
    }

    public void executeActiveSearch(){
        if(activeOnFlySearch == null){
            return;
        }
        switch(activeOnFlySearch){
            case Maximum:
                maximum();
                break;
            case Minimum:
                minimum();
                break;
            case Highest:
                highest();
                break;
            case Lowest:
                lowest();
                break;
            case BiggerNeighbours:
                biggerNeighbours();
                break;
            case SmallerNeighbours:
                smallerNeighbours();
                break;
            case UpperCone:
                upperCone();
                break;
            case LowerCone:
                lowerCone();
                break;
            case Supremum:
                supremum();
                break;
            case Infimum:
                infimum();
                break;
        }
    }
}
