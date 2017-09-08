package kandrm.JLatVis.guiConnect.settings.visual;

import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.settings.ShapeSpecialVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.patterns.ShapeSpecialSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public abstract class NodeSpecialModel {
    protected List<NodeShape> nodes = new ArrayList<NodeShape>();
    protected ShapeSpecialSettingPattern settingPattern;

    public NodeSpecialModel(){}
    public NodeSpecialModel(List<NodeShape> nodes){
        this.nodes = nodes;
        changeDataByNodes();
    }

    protected abstract void initData(ShapeSpecialVisualSettings setting);

    protected void changeDataByNodes(){
        if(nodes == null || nodes.size() < 1){
            return;
        }
        initData(nodes.get(0).getVisualSettings().getSpecialSettings());
        if(nodes.size() > 1){
            for(NodeShape n : nodes){
                settingPattern.intersect(n.getVisualSettings().getSpecialSettings());
            }
        }
    }

    public ShapeSpecialSettingPattern getSettingPattern() {
        return settingPattern;
    }

    public void setSettingPattern(ShapeSpecialSettingPattern settingPattern) {
        this.settingPattern = settingPattern;
    }
}
