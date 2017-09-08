package kandrm.JLatVis.guiConnect.settings.visual;

import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.settings.NodeNameVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.NodeVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.patterns.NodeNameSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class NodeNameModel {
    protected List<NodeShape> nodes = new ArrayList<NodeShape>();

    protected NodeNameSettingPattern settingPattern;

    public NodeNameModel(){
        initDefaultData();
    }
    public NodeNameModel(List<NodeShape> nodes){
        setNodes(nodes);
        if(nodes == null || nodes.size() < 1){
            initDefaultData();
        }
    }

    protected void initDefaultData(){
        initData(new NodeNameVisualSettings());
    }


    public void setNodes(List<NodeShape> nodes){
        this.nodes = nodes;
        changeDataByNodes();
    }

    protected void initData(NodeNameVisualSettings setting){
        settingPattern = new NodeNameSettingPattern(setting);
    }

    protected void changeDataByNodes(){
        if(nodes == null || nodes.size() < 1){
            return;
        }
        initData(nodes.get(0).getVisualSettings().getNameSettings());
        if(nodes.size() > 1){
            for(NodeShape n : nodes){
                settingPattern.intersect(n.getVisualSettings().getNameSettings());
            }
        }
    }

    public void apply(){
         for(NodeShape n : nodes){
            NodeVisualSettings setting = n.getVisualSettings();
            settingPattern.updateByPattern(setting.getNameSettings());
            if( ! setting.equals(n.getVisualSettings())){
                n.setVisualSettings(setting);
            }
        }
    }

    public NodeNameSettingPattern getSettingPattern() {
        return settingPattern;
    }

    public void setSettingPattern(NodeNameSettingPattern settingPattern) {
        this.settingPattern = settingPattern;
    }

    public int size(){
        return nodes.size();
    }
}
