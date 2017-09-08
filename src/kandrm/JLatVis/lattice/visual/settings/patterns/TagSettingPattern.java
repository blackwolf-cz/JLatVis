package kandrm.JLatVis.lattice.visual.settings.patterns;

import java.awt.Color;
import kandrm.JLatVis.lattice.visual.DashingPattern;
import kandrm.JLatVis.lattice.visual.settings.TagVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.TagVisualSettings.LineDests;
import kandrm.JLatVis.lattice.visual.settings.TextVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class TagSettingPattern extends TextShapePattern {
    private LineSettingPattern connectLine;

    private Boolean visible;

    private Float distanceFromNode;
    private Float angleFromHoriz;

	private LineDests lineTo;
	private Integer lineStartDist;

    public TagSettingPattern(){
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
    public TagSettingPattern(
            //tvar
            Integer width,
            Integer height,
            Integer angle,
            Color backgroundColor,
            
            // okraje
            Color borderColor,
            Integer borderWidth,
            DashingPattern borderDashing,

            // text
            Color textColor,
            String fontName,
            Integer fontSize,
            Boolean fontBold,
            Boolean fontItalic,
            Boolean fontUnderline,
            TextVisualSettings.TextAlignment textAlignment,

            // spojnice s vrcholem
            Color connectLineColor,
            Integer connectLineWidth,
            DashingPattern connectLineDashing,

            // zobrazeni popisku
            Boolean visible,

            // poloha popisku
            Float distanceFromNode,
            Float angleFromHoriz,
            LineDests lineTo,
            Integer lineStartDist){

        super(
            width,
            height,
            angle,
            backgroundColor,

            borderColor,
            borderWidth,
            borderDashing,

            textColor,
            fontName,
            fontSize,
            fontBold,
            fontItalic,
            fontUnderline,
            null
        );
        initFields(
            connectLineColor,
            connectLineWidth,
            connectLineDashing,

            visible,

            distanceFromNode,
            angleFromHoriz,
            lineTo,
            lineStartDist
        );
    }
    public TagSettingPattern(TagVisualSettings settings){
        super(settings);
        initFields(
            settings.getConnectLineColor(),
            (int) settings.getConnectLineStroke().getLineWidth(),
            new DashingPattern(settings.getConnectLineStroke().getDashArray()),

            settings.isVisible(),

            settings.getDistanceFromNode(),
            settings.getAngleFromHoriz(),
            settings.getLineTo(),
            settings.getLineStartDist()
        );
    }

    private void initFields(
            // spojnice s vrcholem
            Color connectLineColor,
            Integer connectLineWidth,
            DashingPattern connectLineDashing,

            // zobrazeni popisku
            Boolean visible,

            // poloha popisku
            Float distanceFromNode,
            Float angleFromHoriz,
            LineDests lineTo,
            Integer lineStartDist){

        this.connectLine = new LineSettingPattern(connectLineColor, connectLineWidth, connectLineDashing);
        this.visible = visible;
        this.distanceFromNode = distanceFromNode;
        this.angleFromHoriz = angleFromHoriz;
        this.lineTo = lineTo;
        this.lineStartDist = lineStartDist;
    }


    private boolean matchConnectLine(TagVisualSettings settings){
        boolean sameLineColor = connectLine.getColor() == null || connectLine.getColor().equals(settings.getConnectLineColor());
        boolean sameLineWidth = connectLine.getWidth() == null || connectLine.getWidth().equals((int) settings.getConnectLineStroke().getLineWidth());
        boolean sameLineDashing = connectLine.getDashing() == null || connectLine.getDashing().equals(new DashingPattern(settings.getConnectLineStroke().getDashArray()));

        return sameLineColor
            && sameLineWidth
            && sameLineDashing;
    }

    public boolean match(TagVisualSettings settings){
        return super.match(settings)
            && matchConnectLine(settings)
            && (visible == null || visible == settings.isVisible())
            && (distanceFromNode == null || distanceFromNode.equals(settings.getDistanceFromNode()))
            && (angleFromHoriz == null || angleFromHoriz.equals(settings.getAngleFromHoriz()))
            && (lineTo == null || lineTo.equals(settings.getLineTo()))
            && (lineStartDist == null || lineStartDist.equals(settings.getLineStartDist()));
    }



    private void updateConnectLineByPattern(TagVisualSettings settings){
        if(connectLine.getColor() != null){
            settings.setConnectLineColor(connectLine.getColor());
        }
        settings.setConnectLineStroke( connectLine.createStrokeByPattern(settings.getConnectLineStroke()) );
    }

    public void updateByPattern(TagVisualSettings settings){
        super.updateByPattern(settings);

        updateConnectLineByPattern(settings);

        if (visible != null) {
            settings.setVisible(visible);
        }
        if (distanceFromNode != null) {
            settings.setDistanceFromNode(distanceFromNode);
        }
        if (angleFromHoriz != null) {
            settings.setAngleFromHoriz(angleFromHoriz);
        }
        if (lineTo != null) {
            settings.setLineTo(lineTo);
        }
        if (lineStartDist != null) {
            settings.setLineStartDist(lineStartDist);
        }
    }



    private void intersectConnectLine(TagVisualSettings settings){
        if(connectLine.getColor() == null || ! connectLine.getColor().equals(settings.getConnectLineColor())){
            connectLine.setColor(null);
        }
        if (connectLine.getWidth() == null || connectLine.getWidth() != (int)settings.getConnectLineStroke().getLineWidth()) {
            connectLine.setWidth(null);
        }
        if(connectLine.getDashing() == null || ! connectLine.getDashing().equals(new DashingPattern(settings.getConnectLineStroke().getDashArray()))){
            connectLine.setDashing(null);
        }
    }
    
    public void intersect(TagVisualSettings settings){
        super.intersect(settings);

        intersectConnectLine(settings);

        if(visible == null || ! visible.equals(settings.isVisible())){
            visible = null;
        }
        if(distanceFromNode == null || ! distanceFromNode.equals(settings.getDistanceFromNode())){
            distanceFromNode = null;
        }
        if(angleFromHoriz == null || ! angleFromHoriz.equals(settings.getAngleFromHoriz())){
            angleFromHoriz = null;
        }
        if(lineTo == null || ! lineTo.equals(settings.getLineTo())){
            lineTo = null;
        }
        if(lineStartDist == null || ! lineStartDist.equals(settings.getLineStartDist())){
            lineStartDist = null;
        }
    }


    public Color getConnectLineColor() {
        return connectLine.getColor();
    }
    
    public Integer getConnectLineWidth() {
        return connectLine.getWidth();
    }

    public DashingPattern getConnectLineDashing() {
        return connectLine.getDashing();
    }


    public Boolean isVisible() {
        return visible;
    }


    public Float getAngleFromHoriz() {
        return angleFromHoriz;
    }

    public Float getDistanceFromNode() {
        return distanceFromNode;
    }

    public Integer getLineStartDist() {
        return lineStartDist;
    }

    public LineDests getLineTo() {
        return lineTo;
    }
}
