package kandrm.JLatVis.lattice.visual.settings;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 *
 * @author Michal Kandr
 */
public class HighlightedHighlightVisualSettings extends HighlightVisualSettings{
    private static final Color DEFAULT_COLOR = Color.BLUE;
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(
        2,
        BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10,
        new float[]{2, 3}, 1
    );
    private static final int DEFAULT_DIST = 10;

    public HighlightedHighlightVisualSettings(){
        this(DEFAULT_COLOR, DEFAULT_STROKE, DEFAULT_DIST);
    }
    public HighlightedHighlightVisualSettings(Color color, BasicStroke stroke, int distance){
        super(color, stroke, distance);
    }
    public HighlightedHighlightVisualSettings(HighlightVisualSettings h){
        super(h);
    }
}
