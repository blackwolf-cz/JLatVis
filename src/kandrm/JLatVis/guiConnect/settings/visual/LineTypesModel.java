package kandrm.JLatVis.guiConnect.settings.visual;

import javax.swing.DefaultComboBoxModel;
import kandrm.JLatVis.lattice.visual.DashingPattern;

/**
 *
 * @author Michal Kandr
 */
public class LineTypesModel extends DefaultComboBoxModel {
    public final static DashingPattern DASHING_FULL = new DashingPattern();
    public final static DashingPattern DASHING_DOTTED = new DashingPattern(new float[]{1, 3});
    public final static DashingPattern DASHING_DASHED = new DashingPattern(new float[]{4, 3});
    public final static DashingPattern DASHING_DOT_DASH = new DashingPattern(new float[]{4, 3, 2, 3});

    private static final String[] NAMES = {
        "full",
        "dotted",
        "dashed",
        "dot-and-dash"
    };

    private static final DashingPattern[] DASHING  = {
        DASHING_FULL,
        DASHING_DOTTED,
        DASHING_DASHED,
        DASHING_DOT_DASH
    };

    public LineTypesModel(){
        super(NAMES);
    }

    public DashingPattern getDashing(int index){
        if(index < 0 || index > DASHING.length-1){
            throw new IndexOutOfBoundsException();
        }
        return DASHING[index];
    }

    public String getNameByDashig(DashingPattern dashing){
        int index = 0;
        for( int i=0; i<DASHING.length; ++i ){
            if(dashing == DASHING[i] || dashing.equals(DASHING[i]) ){
                index = i;
                break;
            }
        }
        return NAMES[index];
    }
}
