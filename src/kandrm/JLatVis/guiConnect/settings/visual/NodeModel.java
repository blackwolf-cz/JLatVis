package kandrm.JLatVis.guiConnect.settings.visual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kandrm.JLatVis.lattice.editing.history.History;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.NodeShapeTypes;
import kandrm.JLatVis.lattice.visual.settings.NodeVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.patterns.NodeSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class NodeModel {
    protected List<NodeShape> nodes = new ArrayList<NodeShape>();

    protected NodeSettingPattern settingPattern;
    
    private History history;

    public NodeModel(){
        initDefaultData();
    }
    public NodeModel(List<NodeShape> nodes){
        setNodes(nodes);
        if(nodes == null || nodes.size() < 1){
            initDefaultData();
        }
    }

    protected void initDefaultData(){
        initData(new NodeVisualSettings());
    }

    public void setNodes(List<NodeShape> nodes){
        this.nodes = nodes;
        changeDataByNodes();
    }
    public void setNodes(NodeShape node){
        setNodes(Arrays.asList(node));
    }
    
    public void setHistory(History history){
        this.history = history;
    }

    protected void initData(NodeVisualSettings setting){
        settingPattern = new NodeSettingPattern(setting);
    }

    protected void changeDataByNodes(){
        if(nodes == null || nodes.size() < 1){
            return;
        }
        initData(nodes.get(0).getVisualSettings());
        if(nodes.size() > 1){
            for(NodeShape n : nodes){
                settingPattern.intersect(n.getVisualSettings());
            }
        }
    }

    public void apply(){
        history.startTransaction();
        for(NodeShape n : nodes){
            NodeVisualSettings setting = n.getVisualSettings();
            settingPattern.updateByPattern(setting);
            if( ! setting.equals(n.getVisualSettings())){
                n.setVisualSettings(setting);
            }
        }
        history.endTransaction();
    }


    public NodeSpecialModel getSpecialModel(NodeShapeTypes.Type t){
        if(nodes != null && nodes.size() > 1){
            return NodeShapeTypes.getInstance().getSettingModelInstance(t);
        } else {
            return NodeShapeTypes.getInstance().getSettingModelInstance(t, nodes);
        }
    }

    public NodeNameModel getNodeNameModel(){
        NodeNameModel model = new NodeNameModel(nodes);
        if(nodes.size() < 1){
            model.setSettingPattern(settingPattern.getNameSettings());
        }
        return model;
    }

    public NodeSettingPattern getSettingPattern() {
        return settingPattern;
    }

    public void setSettingPattern(NodeSettingPattern settingPattern) {
        this.settingPattern = settingPattern;
    }

    public int size() {
        return nodes.size();
    }
}
