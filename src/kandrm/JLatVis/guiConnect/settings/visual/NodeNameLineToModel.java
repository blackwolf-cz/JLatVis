package kandrm.JLatVis.guiConnect.settings.visual;

import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import kandrm.JLatVis.lattice.visual.settings.NodeNameVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class NodeNameLineToModel extends DefaultComboBoxModel {
    private static final String[] NAMES = {
        "shape corner",
        "edge center"
    };

    private static final NodeNameVisualSettings.LineDests[] DESTS = {
        NodeNameVisualSettings.LineDests.CORNER,
        NodeNameVisualSettings.LineDests.CENTER
    };
    
    public NodeNameLineToModel(){
        super(NAMES);
    }

    public NodeNameVisualSettings.LineDests getLineTo(int index){
        return DESTS[index];
    }

    public String getNameByLineTo(NodeNameVisualSettings.LineDests dest){
        return NAMES[Arrays.binarySearch(DESTS, dest)];
    }
}
