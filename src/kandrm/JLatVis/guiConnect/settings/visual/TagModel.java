package kandrm.JLatVis.guiConnect.settings.visual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kandrm.JLatVis.lattice.editing.history.History;
import kandrm.JLatVis.lattice.visual.TagShape;
import kandrm.JLatVis.lattice.visual.settings.TagVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.patterns.TagSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class TagModel {
    protected List<TagShape> tags = new ArrayList<TagShape>();

    protected TagSettingPattern settingPattern;

    private History history;
    
    public TagModel(){
        initDefaultData();
    }
    public TagModel(List<TagShape> nodes){
        setTags(nodes);
        if(nodes == null || nodes.size() < 1){
            initDefaultData();
        }
    }

    protected void initDefaultData(){
        initData(new TagVisualSettings());
    }

    public void setTags(List<TagShape> tags){
        this.tags = tags;
        changeDataByTags();
    }
    public void setTags(TagShape tag){
        setTags(Arrays.asList(tag));
    }
    
    public void setHistory(History history){
        this.history = history;
    }

    protected void initData(TagVisualSettings setting){
        settingPattern = new TagSettingPattern(setting);
    }

    protected void changeDataByTags(){
        if(tags == null || tags.size() < 1){
            return;
        }
        initData(tags.get(0).getVisualSettings());
        if(tags.size() > 1){
            for(TagShape n : tags){
                settingPattern.intersect(n.getVisualSettings());
            }
        }
    }

    public void apply(){
        history.startTransaction();
        for(TagShape n : tags){
            TagVisualSettings setting = n.getVisualSettings();
            settingPattern.updateByPattern(setting);
            if( ! setting.equals(n.getVisualSettings())){
                n.setVisualSettings(setting);
            }
        }
        history.endTransaction();
    }

    public TagSettingPattern getSettingPattern() {
        return settingPattern;
    }

    public void setSettingPattern(TagSettingPattern settingPattern) {
        this.settingPattern = settingPattern;
    }

    public int size(){
        return tags.size();
    }
}
