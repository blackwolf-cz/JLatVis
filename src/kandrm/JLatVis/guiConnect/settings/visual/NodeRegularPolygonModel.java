package kandrm.JLatVis.guiConnect.settings.visual;

import java.util.List;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.settings.ShapeRegularPolygonVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.ShapeSpecialVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.patterns.ShapeRegularPolygonSettingPattern;

/**
 *
 * @author Michal Kandr
 */
public class NodeRegularPolygonModel extends NodeSpecialModel {

    public NodeRegularPolygonModel(){
        initDefaultData();
    }
    public NodeRegularPolygonModel(List<NodeShape> nodes){
        super(nodes);
        if(nodes == null || nodes.size() < 1){
            initDefaultData();
        }
    }

    private void initDefaultData(){
        initData(new ShapeRegularPolygonVisualSettings());
    }

    @Override
    protected void initData(ShapeSpecialVisualSettings setting) {
        settingPattern = new ShapeRegularPolygonSettingPattern(setting);
    }

    @Override
    public ShapeRegularPolygonSettingPattern getSettingPattern() {
        return (ShapeRegularPolygonSettingPattern) settingPattern;
    }
}
