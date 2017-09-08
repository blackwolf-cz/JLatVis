package kandrm.JLatVis.guiConnect.search.visual;

import kandrm.JLatVis.guiConnect.settings.visual.EdgeModel;
import kandrm.JLatVis.lattice.editing.search.VisualSearch;
import kandrm.JLatVis.lattice.visual.settings.patterns.EdgeSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class SearchEdgeModel extends EdgeModel {
    private VisualSearch search;

    public SearchEdgeModel(VisualSearch search){
        super();
        this.search = search;
    }

    @Override
    protected void initDefaultData(){
        settingPattern = new EdgeSettingPattern();
    }

    @Override
    public void apply(){
        search.findEdges(settingPattern);
    }
}
