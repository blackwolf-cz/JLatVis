package kandrm.JLatVis.guiConnect.settings.visual;

import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import kandrm.JLatVis.lattice.visual.NodeShapeTypes;

/**
 *
 * @author Michal Kandr
 */
public class NodeTypesModel extends DefaultComboBoxModel {
    private NodeShapeTypes types;

    public NodeTypesModel(NodeShapeTypes types){
        super(types.getNames());
        this.types = types;
    }

    public NodeShapeTypes.Type getType(int index){
        return types.getTypes()[index];
    }

    public int getTypeIndex(NodeShapeTypes.Type t){
        return Arrays.binarySearch(types.getTypes(),t);
    }
}
