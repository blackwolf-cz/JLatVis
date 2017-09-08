package kandrm.JLatVis.lattice.visual.settings;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 *
 * @author Michal Kandr
 */
public class HighlightedLowerHighlightVisualSettings extends HighlightVisualSettings {
    private static final Color DEFAULT_COLOR = Color.GREEN;
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(
        3,
        BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10,
        new float[]{2, 2}, 1
    );
    private static final int DEFAULT_DIST = 10;

    public HighlightedLowerHighlightVisualSettings(){
        this(DEFAULT_COLOR, DEFAULT_STROKE, DEFAULT_DIST);
    }
    public HighlightedLowerHighlightVisualSettings(Color color, BasicStroke stroke, int distance){
        super(color, stroke, distance);
    }
    public HighlightedLowerHighlightVisualSettings(HighlightVisualSettings h){
        super(h);
    }   
}
