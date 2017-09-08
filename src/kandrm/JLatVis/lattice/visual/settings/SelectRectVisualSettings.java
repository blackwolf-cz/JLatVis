package kandrm.JLatVis.lattice.visual.settings;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 *
 * @author Michal Kandr
 */
public class SelectRectVisualSettings extends LineVisualSettings {
    private static final Color DEFAULT_COLOR = Color.RED;
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1);

    public SelectRectVisualSettings(){
        this(DEFAULT_COLOR, DEFAULT_STROKE);
    }
    
    public SelectRectVisualSettings(Color color, BasicStroke stroke){
        super(color, stroke);
    }

    public SelectRectVisualSettings(SelectRectVisualSettings l){
        super(l);
    }
}
