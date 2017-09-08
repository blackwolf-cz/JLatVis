package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import kandrm.JLatVis.export.XmlInvalidException;
import kandrm.JLatVis.guiConnect.settings.visual.LineTypesModel;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal Kandr
 */
public class NodeNameVisualSettings extends TextShapeVisualSettings {
    /**
     * Možná místa popisku, do kterých vede hrana z uzlu.
     */
    public enum LineDests {
        /**
         * Spojnice vede do nejbližšího z rohů popisku.
         */
        CORNER,
        /**
         * Spojnice vede do středu nejbližší hrany popisku.
         */
        CENTER
    }
    private static final Color DEFAULT_CONNECT_LINE_COLOR = Color.BLACK;
    private static final BasicStroke DEFAULT_CONNECT_LINE_STROKE = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, LineTypesModel.DASHING_DASHED.getDashing(), 0);
    private static final int DEFAULT_DISTANCE_FROM_NODE = 20;
    private static final int DEFAULT_ANGLE_FROM_HORIZ = 20;
    private static final int DEFAULT_LINE_START_DIST = 5;
    private static final LineDests DEFAULT_LINE_TO = LineDests.CORNER;
    private static final Dimension DEFAULT_PADDING = new Dimension(0, 0);

    private static final int DEFAULT_ANGLE = 0;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final BasicStroke DEFAULT_BORDER_STROKE = new BasicStroke(0);
    private static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final Font DEFAULT_TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private static final boolean DEFAULT_NAME_OUTSIDE = true;
    private static final int DEFAULT_MIN_LINE_DIST = 10;

    private LineVisualSettings connectLine;
    private float distanceFromNode;
    private float angleFromHoriz;
    /**
     * Vzrálenost začátku spojovací čáry od vrcholu
     */
    private int lineStartDist;
    private LineDests lineTo;
    private boolean nameOutside;
    private int minLineDist = 0;

    public NodeNameVisualSettings(){
        super(DEFAULT_PADDING, DEFAULT_ANGLE, DEFAULT_BACKGROUND_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_STROKE, DEFAULT_TEXT_COLOR, DEFAULT_TEXT_FONT, null);
        setDefault();
    }

    public NodeNameVisualSettings(Color backgroundColor,
            Color borderColor, BasicStroke borderStroke,
            Color textColor, Font textFont, TextVisualSettings.TextAlignment textAlignment,
            Color connectLineColor, BasicStroke connectLineStroke,
            int distanceFromNode, int angleFromHoriz, LineDests lineTo, int lineStartDist, Dimension padding, boolean nameOutside, int minLineDist){
        super(padding, 0, backgroundColor, borderColor, borderStroke, textColor, textFont, textAlignment);
        this.connectLine = new LineVisualSettings(connectLineColor, connectLineStroke);
        this.distanceFromNode = distanceFromNode;
        this.angleFromHoriz = angleFromHoriz;
        this.lineTo = lineTo;
        this.lineStartDist = lineStartDist;
        this.nameOutside = nameOutside;
        this.minLineDist = minLineDist;
    }

    public NodeNameVisualSettings(NodeNameVisualSettings c){
        super(c);
        connectLine = new LineVisualSettings(c.connectLine);
        distanceFromNode = c.distanceFromNode;
        angleFromHoriz = c.angleFromHoriz;
        lineTo = c.lineTo;
        lineStartDist = c.lineStartDist;
        nameOutside = c.nameOutside;
        minLineDist = c.minLineDist;
    }
    public NodeNameVisualSettings(TextShapeVisualSettings c){
        super(c);
        setDefault();
    }

    private void setDefault(){
        this.connectLine = new LineVisualSettings(DEFAULT_CONNECT_LINE_COLOR,
                DEFAULT_CONNECT_LINE_STROKE);
        distanceFromNode = DEFAULT_DISTANCE_FROM_NODE;
        angleFromHoriz = DEFAULT_ANGLE_FROM_HORIZ;
        lineStartDist = DEFAULT_LINE_START_DIST;
        lineTo = DEFAULT_LINE_TO;
        nameOutside = DEFAULT_NAME_OUTSIDE;
        minLineDist = DEFAULT_MIN_LINE_DIST;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } else if(o == null || o.getClass() != this.getClass()){
            return false;
        }
        NodeNameVisualSettings c = (NodeNameVisualSettings)o;
        return super.equals(o)
            && connectLine.equals(c.connectLine)
            && distanceFromNode == c.distanceFromNode
            && angleFromHoriz == c.angleFromHoriz
            && lineStartDist == c.lineStartDist
            && nameOutside == c.nameOutside
            && minLineDist == c.minLineDist;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + super.hashCode();
        hash = 41 * hash + (this.connectLine != null ? this.connectLine.hashCode() : 0);
        hash = (int) (41 * hash + this.distanceFromNode);
        hash = (int) (41 * hash + this.angleFromHoriz);
        hash = 41 * hash + this.lineStartDist;
        hash = 41 * hash + (this.nameOutside ? 1 : 0);
        hash = 41 * hash + this.minLineDist;
        return hash;
    }


    public Color getConnectLineColor() {
        return connectLine.getColor();
    }
    public void setConnectLineColor(Color color) {
        connectLine.setColor(color);
    }

    public BasicStroke getConnectLineStroke() {
        return connectLine.getStroke();
    }
    public void setConnectLineStroke(BasicStroke stroke) {
        connectLine.setStroke(stroke);
    }

    public float getAngleFromHoriz() {
        return angleFromHoriz;
    }
    public void setAngleFromHoriz(float angleFromHoriz) {
        this.angleFromHoriz = angleFromHoriz;
    }

    public float getDistanceFromNode() {
        return distanceFromNode;
    }
    public void setDistanceFromNode(float distanceFromNode) {
        this.distanceFromNode = distanceFromNode;
    }

    public int getLineStartDist() {
        return lineStartDist;
    }
    public void setLineStartDist(int lineStartDist) {
        this.lineStartDist = lineStartDist;
    }

    public LineDests getLineTo() {
        return lineTo;
    }
    public void setLineTo(LineDests lineTo) {
        this.lineTo = lineTo;
    }

    public boolean isNameOutside() {
        return nameOutside;
    }
    public void setNameOutside(boolean nameOutside) {
        this.nameOutside = nameOutside;
    }

    public int getMinLineDist() {
        return minLineDist;
    }
    public void setMinLineDist(int minLineDist) {
        this.minLineDist = minLineDist;
    }


    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        super.toXml(writer);
        writer.writeEntity("Line");
            connectLine.toXml(writer)
        .endEntity()
        .writeEntityWithText("DistanceFromNode", distanceFromNode)
        .writeEntityWithText("AngleFromHoriz", angleFromHoriz)
        .writeEntityWithText("LineTo", lineTo)
        .writeEntityWithText("LineStartDist", lineStartDist)
        .writeEntityWithText("NameOutside", nameOutside)
        .writeEntityWithText("MinLineDist", minLineDist);
        return writer;
    }

    public static NodeNameVisualSettings fromXml(Element element) throws XmlInvalidException {
        NodeNameVisualSettings settings = new NodeNameVisualSettings( TextShapeVisualSettings.fromXml(element) );
        if( element == null){
            return settings;
        }
        if(! element.getNodeName().equals("NodeName")){
            throw new XmlInvalidException();
        }
        NodeList distance = element.getElementsByTagName("DistanceFromNode");
        if(distance.getLength() != 1){
            throw new XmlInvalidException();
        }
        NodeList childs = element.getChildNodes();
        for(int i=0; i<childs.getLength(); ++i){
            org.w3c.dom.Node elNode = childs.item(i);
            if(elNode.getNodeType() != Element.ELEMENT_NODE){
                continue;
            }
            Element el = (Element)elNode;
            String elName = el.getNodeName();
            if(elName.equals("Line")){
                settings.connectLine = LineVisualSettings.fromXml(el);
            } else if(elName.equals("NameOutside")){
                settings.nameOutside = Boolean.parseBoolean(el.getTextContent());
            } else if(elName.equals("DistanceFromNode")){
                settings.distanceFromNode = Float.parseFloat(el.getTextContent());
            } else if(elName.equals("AngleFromHoriz")){
                settings.angleFromHoriz = Float.parseFloat(el.getTextContent());
            } else if(elName.equals("LineTo")){
                try{
                    settings.lineTo = LineDests.valueOf(el.getTextContent());
                } catch(IllegalArgumentException e){
                    throw new XmlInvalidException();
                }
            } else if(elName.equals("LineStartDist")){
                settings.lineStartDist = Integer.parseInt(el.getTextContent());
            } else if(elName.equals("MinLineDist")){
                settings.minLineDist = Integer.parseInt(el.getTextContent());
            }
        }
        return settings;
    }
}
