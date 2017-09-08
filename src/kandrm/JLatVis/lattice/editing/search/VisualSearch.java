package kandrm.JLatVis.lattice.editing.search;

import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.lattice.visual.TagShape;
import kandrm.JLatVis.lattice.visual.EdgeShape;
import kandrm.JLatVis.lattice.logical.Lattice;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.settings.patterns.TagSettingPattern;
import kandrm.JLatVis.lattice.editing.selection.Selector;
import kandrm.JLatVis.lattice.visual.settings.patterns.EdgeSettingPattern;
import kandrm.JLatVis.lattice.visual.settings.patterns.NodeSettingPattern;

/**
 * Vizuální vyhledávání. Hledá uzly, popisky a hrany podle vizuálních vlastností.
 *
 * @author Michal Kandr
 */
public class VisualSearch extends Search {

    public VisualSearch(Lattice lattice, Selector selector, SearchResults results){
        super(lattice, selector, results);
    }

    /**
     * Vyhledá uzly s odpovídajícími vizuálními vlastnostmi.
     *
     * @param searched hledané vlastnosti. Vlastnosti s hodnotou null
     * nejsou do hledání zařazeny
     */
    public void findNodes(NodeSettingPattern searched){
        List<IFoundabe> foundItems = new ArrayList<IFoundabe>();
        for(NodeShape node : lattice.getShape().getNodes()){
            if(searched.match(node.getVisualSettings())){
                foundItems.add(node);
            }
        }
        results.saveFoundItems(foundItems);
    }

    /**
     * Vyhledá popisky uzlů s odpovídajícími vizuálními vlastnostmi.
     *
     * @param searchedSetting hledané vlastnosti. Vlastnosti s hodnotou null
     * nejsou do hledání zařazeny
     */
    public void findTags(TagSettingPattern searched){
        List<IFoundabe> foundItems = new ArrayList<IFoundabe>();
        for(TagShape tag : lattice.getShape().getTags()){
            if(searched.match(tag.getVisualSettings())){
                foundItems.add(tag);
            }
        }
        results.saveFoundItems(foundItems);
    }

    /**
     * Vyhledá hrany s odpovídajícími vizuálními vlastnostmi.
     *
     * @param searchedSetting hledané vlastnosti. Vlastnosti s hodnotou null
     * nejsou do hledání zařazeny
     */
    public void findEdges(EdgeSettingPattern searched){
        List<IFoundabe> foundItems = new ArrayList<IFoundabe>();
        for(EdgeShape edge : lattice.getShape().getEdges()){
            if(searched.match(edge.getVisualSettings())){
                foundItems.add(edge);
            }
        }
        results.saveFoundItems(foundItems);
    }
}
