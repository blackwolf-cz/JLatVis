package kandrm.JLatVis.guiConnect.settings.visual;

import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import kandrm.JLatVis.lattice.visual.settings.TagVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class TagLineToModel extends DefaultComboBoxModel {
    private static final String[] NAMES = {
        "shape corner",
        "edge center"
    };

    private static final TagVisualSettings.LineDests[] DESTS = {
        TagVisualSettings.LineDests.CORNER,
        TagVisualSettings.LineDests.CENTER
    };
    
    public TagLineToModel(){
        super(NAMES);
    }

    public TagVisualSettings.LineDests getLineTo(int index){
        return DESTS[index];
    }

    public String getNameByLineTo(TagVisualSettings.LineDests dest){
        return NAMES[Arrays.binarySearch(DESTS, dest)];
    }
}
