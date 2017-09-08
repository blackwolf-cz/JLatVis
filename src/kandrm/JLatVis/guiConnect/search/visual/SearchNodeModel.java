package kandrm.JLatVis.guiConnect.search.visual;

import kandrm.JLatVis.guiConnect.settings.visual.NodeModel;
import kandrm.JLatVis.lattice.editing.search.VisualSearch;
import kandrm.JLatVis.lattice.visual.settings.patterns.NodeSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class SearchNodeModel extends NodeModel {
    private VisualSearch search;

    public SearchNodeModel(VisualSearch search){
        super();
        this.search = search;
    }

    @Override
    protected void initDefaultData(){
        settingPattern = new NodeSettingPattern();
    }

    @Override
    public void apply(){
        search.findNodes(settingPattern);
    }
}
