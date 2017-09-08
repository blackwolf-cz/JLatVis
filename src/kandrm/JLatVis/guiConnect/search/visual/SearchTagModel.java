package kandrm.JLatVis.guiConnect.search.visual;

import kandrm.JLatVis.guiConnect.settings.visual.TagModel;
import kandrm.JLatVis.lattice.editing.search.VisualSearch;
import kandrm.JLatVis.lattice.visual.settings.patterns.TagSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class SearchTagModel extends TagModel {
    private VisualSearch search;

    public SearchTagModel(VisualSearch search){
        super();
        this.search = search;
    }

    @Override
    protected void initDefaultData(){
        settingPattern = new TagSettingPattern();
    }

    @Override
    public void apply(){
        search.findTags(settingPattern);
    }
}
