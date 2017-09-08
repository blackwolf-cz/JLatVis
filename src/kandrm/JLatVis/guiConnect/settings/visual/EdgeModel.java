package kandrm.JLatVis.guiConnect.settings.visual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kandrm.JLatVis.lattice.editing.history.History;
import kandrm.JLatVis.lattice.visual.EdgeShape;
import kandrm.JLatVis.lattice.visual.settings.EdgeVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.patterns.EdgeSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class EdgeModel {
    protected List<EdgeShape> edges = new ArrayList<EdgeShape>();

    protected EdgeSettingPattern settingPattern;

    private History history;

    public EdgeModel(){
        initDefaultData();
    }
    public EdgeModel(List<EdgeShape> edges){
        setEdges(edges);
        if(edges == null || edges.size() < 1){
            initDefaultData();
        }
    }

    protected void initDefaultData(){
        initData(new EdgeVisualSettings());
    }

    public void setEdges(List<EdgeShape> edges){
        this.edges = edges;
        changeDataByEdges();
    }
    public void setEdges(EdgeShape edges){
        setEdges(Arrays.asList(edges));
    }

    public void setHistory(History history){
        this.history = history;
    }

    protected void initData(EdgeVisualSettings setting){
        settingPattern = new EdgeSettingPattern(setting);
    }

    protected void changeDataByEdges(){
        if(edges == null || edges.size() < 1){
            return;
        }
        initData(edges.get(0).getVisualSettings());
        if(edges.size() > 1){
            for(EdgeShape n : edges){
                settingPattern.intersect(n.getVisualSettings());
            }
        }
    }

    public void apply(){
        history.startTransaction();
         for(EdgeShape n : edges){
            EdgeVisualSettings setting = n.getVisualSettings();
            settingPattern.updateByPattern(setting);
            if( ! setting.equals(n.getVisualSettings())){
                n.setVisualSettings(setting);
            }
        }
        history.endTransaction();
    }

    public EdgeSettingPattern getSettingPattern() {
        return settingPattern;
    }

    public void setSettingPattern(EdgeSettingPattern settingPattern) {
        this.settingPattern = settingPattern;
    }

    public int size() {
        return edges.size();
    }
}
