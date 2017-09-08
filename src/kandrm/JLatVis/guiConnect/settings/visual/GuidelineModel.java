package kandrm.JLatVis.guiConnect.settings.visual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kandrm.JLatVis.lattice.editing.guidelines.IGuideline;
import kandrm.JLatVis.lattice.visual.settings.GuidelineVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.patterns.GuidelineSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class GuidelineModel {
    protected List<IGuideline> guidelines = new ArrayList<IGuideline>();

    protected GuidelineSettingPattern settingPattern;


    public GuidelineModel(){
        initDefaultData();
    }
    public GuidelineModel(List<IGuideline> guidelines){
        setGuidelines(guidelines);
        if(guidelines == null || guidelines.size() < 1){
            initDefaultData();
        }
    }

    protected void initDefaultData(){
        initData(new GuidelineVisualSettings());
    }


    public void setGuidelines(List<IGuideline> edges){
        this.guidelines = edges;
        changeDataByGuidelines();
    }
    public void setGuidelines(IGuideline edges){
        setGuidelines(Arrays.asList(edges));
    }

    protected void initData(GuidelineVisualSettings setting){
        settingPattern = new GuidelineSettingPattern(setting);
    }

    protected void changeDataByGuidelines(){
        if(guidelines == null || guidelines.size() < 1){
            return;
        }
        initData(guidelines.get(0).getVisualSettings());
        if(guidelines.size() > 1){
            for(IGuideline n : guidelines){
                settingPattern.intersect(n.getVisualSettings());
            }
        }
    }

    public void apply(){
         for(IGuideline n : guidelines){
            GuidelineVisualSettings setting = n.getVisualSettings();
            settingPattern.updateByPattern(setting);
            if( ! setting.equals(n.getVisualSettings())){
                n.setVisualSettings(setting);
            }
        }
    }

    public GuidelineSettingPattern getSettingPattern() {
        return settingPattern;
    }

    public void setSettingPattern(GuidelineSettingPattern settingPattern) {
        this.settingPattern = settingPattern;
    }

    public int size() {
        return guidelines.size();
    }
}
