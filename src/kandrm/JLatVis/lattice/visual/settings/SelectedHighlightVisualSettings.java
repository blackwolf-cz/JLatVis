package kandrm.JLatVis.lattice.visual.settings;

import java.awt.BasicStroke;
import java.awt.Color;
import kandrm.JLatVis.export.IXmlSerializable;

/**
 *
 * @author Michal Kandr
 */
public class SelectedHighlightVisualSettings extends HighlightVisualSettings implements IXmlSerializable {
    private static final Color DEFAULT_COLOR = Color.GRAY;
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(
        1,
        BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10,
        new float[]{4,2,4,2}, 1
    );
    private static final int DEFAULT_DIST = 4;

    public SelectedHighlightVisualSettings(){
        this(DEFAULT_COLOR, DEFAULT_STROKE, DEFAULT_DIST);
    }
    public SelectedHighlightVisualSettings(Color color, BasicStroke stroke, int distance){
        super(color, stroke, distance);
    }
    public SelectedHighlightVisualSettings(HighlightVisualSettings h){
        super(h);
    }
}
