package kandrm.JLatVis.lattice.visual.settings.patterns;

import java.awt.Color;
import kandrm.JLatVis.lattice.visual.DashingPattern;
import kandrm.JLatVis.lattice.visual.settings.NodeNameVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.NodeNameVisualSettings.LineDests;
import kandrm.JLatVis.lattice.visual.settings.TextVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class NodeNameSettingPattern extends TextShapePattern {
    private LineSettingPattern connectLine;

    private Boolean visible;

    private Float distanceFromNode;
    private Float angleFromHoriz;
    private LineDests lineTo;
    private Integer lineStartDist;
    private Boolean nameOutside;
    private Integer minLineDist;

    public NodeNameSettingPattern(){
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
    public NodeNameSettingPattern(
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

            // poloha popisku
            Float distanceFromNode,
            Float angleFromHoriz,
            LineDests lineTo,
            Integer lineStartDist,
            Boolean nameOutside,
            Integer minLineDist){
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

            distanceFromNode,
            angleFromHoriz,
            lineTo,
            lineStartDist,
            nameOutside,
            minLineDist
        );
    }
    public NodeNameSettingPattern(NodeNameVisualSettings settings){
        super(settings);
        initFields(
            settings.getConnectLineColor(),
            (int) settings.getConnectLineStroke().getLineWidth(),
            new DashingPattern(settings.getConnectLineStroke().getDashArray()),

            settings.getDistanceFromNode(),
            settings.getAngleFromHoriz(),
            settings.getLineTo(),
            settings.getLineStartDist(),
            settings.isNameOutside(),
            settings.getMinLineDist()
        );
    }

    private void initFields(
            // spojnice s vrcholem
            Color connectLineColor,
            Integer connectLineWidth,
            DashingPattern connectLineDashing,

            // poloha popisku
            Float distanceFromNode,
            Float angleFromHoriz,
            LineDests lineTo,
            Integer lineStartDist,
            Boolean nameOutside,
            Integer minLineDist){
        this.connectLine = new LineSettingPattern(connectLineColor, connectLineWidth, connectLineDashing);
        this.distanceFromNode = distanceFromNode;
        this.angleFromHoriz = angleFromHoriz;
        this.lineTo = lineTo;
        this.lineStartDist = lineStartDist;
        this.nameOutside = nameOutside;
        this.minLineDist = minLineDist;
    }


    private boolean matchConnectLine(NodeNameVisualSettings settings){
        boolean sameLineColor = connectLine.getColor() == null || connectLine.getColor().equals(settings.getConnectLineColor());
        boolean sameLineWidth = connectLine.getWidth() == null || connectLine.getWidth().equals((int) settings.getConnectLineStroke().getLineWidth());
        boolean sameLineDashing = connectLine.getDashing() == null || connectLine.getDashing().equals(new DashingPattern(settings.getConnectLineStroke().getDashArray()));

        return sameLineColor
            && sameLineWidth
            && sameLineDashing;
    }

    public boolean match(NodeNameVisualSettings settings){
        return super.match(settings)
            && matchConnectLine(settings)
            && (distanceFromNode == null || distanceFromNode.equals(settings.getDistanceFromNode()))
            && (angleFromHoriz == null || angleFromHoriz.equals(settings.getAngleFromHoriz()))
            && (lineTo == null || lineTo.equals(settings.getLineTo()))
            && (lineStartDist == null || lineStartDist.equals(settings.getLineStartDist()))
            && (nameOutside == null || nameOutside.equals(settings.isNameOutside()))
            && (minLineDist == null || minLineDist.equals(settings.getMinLineDist()));
    }



    private void updateConnectLineByPattern(NodeNameVisualSettings settings){
        if(connectLine.getColor() != null){
            settings.setConnectLineColor(connectLine.getColor());
        }
        settings.setConnectLineStroke( connectLine.createStrokeByPattern(settings.getConnectLineStroke()) );
    }

    public void updateByPattern(NodeNameVisualSettings settings){
        super.updateByPattern(settings);

        updateConnectLineByPattern(settings);

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
        if (nameOutside != null) {
            settings.setNameOutside(nameOutside);
        }
        if (minLineDist != null) {
            settings.setMinLineDist(minLineDist);
        }
    }



    private void intersectConnectLine(NodeNameVisualSettings settings){
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
    
    public void intersect(NodeNameVisualSettings settings){
        super.intersect(settings);

        intersectConnectLine(settings);

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
        if(nameOutside == null || ! nameOutside.equals(settings.isNameOutside())){
            nameOutside = null;
        }
        if(minLineDist == null || ! minLineDist.equals(settings.getMinLineDist())){
            minLineDist = null;
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

    public Boolean isNameOutside(){
        return nameOutside;
    }

    public Integer getMinLineDist(){
        return minLineDist;
    }
}
