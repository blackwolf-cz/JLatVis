package kandrm.JLatVis.lattice.visual;

import kandrm.JLatVis.lattice.editing.Zoom;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;
import kandrm.JLatVis.export.IVectorPaintable;
import kandrm.JLatVis.export.VectorBuilder;
import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;
import kandrm.JLatVis.lattice.visual.settings.NodeNameVisualSettings;
import math.geom2d.Angle2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.line.Line2D;

/**
 *
 * @author Michal Kandr
 */
public class NodeNameShape extends JComponent implements IVectorPaintable {
    private Zoom zoom;

    /**
     * Vizuální reprezentace uzlu, ke ktrému popisek patří.
     */
    private NodeShape node;
    /**
     * Nastavení vizuálních vlastností.
     */
    private NodeNameVisualSettings settings;

    /**
     * Nová viuální reprezentace popisku s defaultním vzhledem.
     *
     * @param node
     */
    public NodeNameShape(NodeShape node){
        this(node, new NodeNameVisualSettings());
    }
    private NodeNameShape(NodeShape node, NodeNameVisualSettings settings){
        this.node = node;
        this.settings = settings;
    }

    public void setZoom(Zoom zoom){
        this.zoom = zoom;
    }

    /**
     * @return Nastavení vizuálních vlastností.
     */
    public NodeNameVisualSettings getVisualSettings() {
        return new NodeNameVisualSettings(settings);
    }
    /**
     * Změní nastavení vizuálních vlastností.
     *
     * @param settings nové nastavení
     * @param saveHistory uložit operaci do historie
     */
    public void setVisualSettings(NodeNameVisualSettings settings, boolean saveHistory) {
        if( ! this.settings.equals(settings)){
            if(saveHistory){
                /*History.add(
                    new HistoryEventTagVisual(
                        this,
                        getVisualSettings(),
                        new TagVisualSettings(settings)
                    )
                );*/
            }
            this.settings = settings;
            if(this.zoom != null){
                if(getParent() != null){
                    getParent().repaint();
                } else {
                    repaint(0, 0, getWidth(), getHeight());
                }
            }
        }
    }
    /**
     * Změní nastavení vizuálních vlastností. Změna bude uložena do historie.
     *
     * @param settings nové nastavení
     */
    public void setVisualSettings(NodeNameVisualSettings settings){
        setVisualSettings(settings, true);
    }

    public NodeShape getNode() {
        return node;
    }

    /**
     * Vypočítá souřadnice bodu, do kterého vede spojnice popisku s vrcholem.
     *
     * @return bodu do kterého vede spojnice popisku s vrcholem
     */
    private Point2D countLineEndPoint(){
        Point2D nodeCenter = node.getCenter();
        double x = 0.0;
        double y = 0.0;
        float angleFromHoriz = settings.getAngleFromHoriz();
        float distanceFromNode = settings.getDistanceFromNode();

        // uhel mezi vodorovnou osou a spojnici vrcholu s popiskem
        float alpha = angleFromHoriz;
        if(angleFromHoriz>90 && angleFromHoriz<180){
            alpha = 180 - alpha;
        } else if(angleFromHoriz>180 && angleFromHoriz<270){
            alpha -= 180;
        } else if(angleFromHoriz>270 && angleFromHoriz<360){
            alpha = 360 - alpha;
        }

        // posun souradnic popisku oproti souradnicim vrcholu
        if(angleFromHoriz == 0){
            x = nodeCenter.x + distanceFromNode;
            y = nodeCenter.y;
        } else if(angleFromHoriz == 90){
            x = nodeCenter.x;
            y = nodeCenter.y - distanceFromNode;
        } else if(angleFromHoriz == 180){
            x = nodeCenter.x - distanceFromNode;
            y = nodeCenter.y;
        } else if(angleFromHoriz == 270){
            x = nodeCenter.x;
            y = nodeCenter.y + distanceFromNode;
        } else {
            double xShift = distanceFromNode * Math.cos(Math.toRadians(alpha));
            double yShift = distanceFromNode * Math.sin(Math.toRadians(alpha));
            if(angleFromHoriz>=0 && angleFromHoriz<=90){
                x = nodeCenter.x + xShift;
                y = nodeCenter.y - yShift;
            } else if(angleFromHoriz>90 && angleFromHoriz<180){
                x = nodeCenter.x - xShift;
                y = nodeCenter.y - yShift;
            } else if(angleFromHoriz>180 && angleFromHoriz<270){
                x = nodeCenter.x - xShift;
                y = nodeCenter.y + yShift;
            } else if(angleFromHoriz>270 && angleFromHoriz<360){
                x = nodeCenter.x + xShift;
                y = nodeCenter.y + yShift;
            }
        }

        return new Point2D(x, y);
    }

    /**
     * Vypočítá souřadnice levého horního rohu popisku.
     *
     * @param lineEndPoint bod do kterého vede spojnice popisku s vrcholem
     *
     * @return souřadnice levého horního rohu popisku
     */
    private Point2D countTopLeftCorner(Point2D lineEndPoint){
        double x = 0.0;
        double y = 0.0;
        float angleFromHoriz = settings.getAngleFromHoriz();
        Dimension dimensions = getDimensions();

        if(settings.getLineTo() == NodeNameVisualSettings.LineDests.CORNER){
            if(angleFromHoriz>=0 && angleFromHoriz<=90){
                x = lineEndPoint.x;
                y = lineEndPoint.y - dimensions.height;
            } else if(angleFromHoriz>90 && angleFromHoriz<=180){
                x = lineEndPoint.x - dimensions.width;
                y = lineEndPoint.y - dimensions.height;
            } else if(angleFromHoriz>180 && angleFromHoriz<270){
                x = lineEndPoint.x - dimensions.width;
                y = lineEndPoint.y;
            } else if(angleFromHoriz>=270 && angleFromHoriz<360){
                x = lineEndPoint.x;
                y = lineEndPoint.y;
            }
        } else {
            if(angleFromHoriz == 0){
                x = lineEndPoint.x;
                y = lineEndPoint.y - dimensions.height / 2;
            } else if(angleFromHoriz==180){
                x = lineEndPoint.x - dimensions.width;
                y = lineEndPoint.y - dimensions.height / 2;
            } else if(angleFromHoriz>0 && angleFromHoriz<=90){
                x = lineEndPoint.x - dimensions.width / 2;
                y = lineEndPoint.y - dimensions.height;
            } else if(angleFromHoriz>90 && angleFromHoriz<180){
                x = lineEndPoint.x - dimensions.width / 2;
                y = lineEndPoint.y - dimensions.height;
            } else if(angleFromHoriz>180 && angleFromHoriz<=270){
                x = lineEndPoint.x - dimensions.width / 2;
                y = lineEndPoint.y;
            } else if(angleFromHoriz>270 && angleFromHoriz<360){
                x = lineEndPoint.x - dimensions.width / 2;
                y = lineEndPoint.y;
            }
        }

        return new Point2D(x, y);
    }

    /**
     * Vypočítá souřadnice bodu u vrcholu, kde začíná spojnice vrcholu s popiskem.
     *
     * @param lineEndPoint bod popisku do kterého vede spojnice popisku s vrcholem
     *
     * @return bod u vrcholu, kde začíná spojnice vrcholu s popiskem
     */
    private Point2D countLineStartpoint(Point2D lineEndPoint){
        return node.findEdgeIntersection(lineEndPoint, settings.getLineStartDist());
    }

    @Override
    public void paintComponent(Graphics g) {
        if( ! node.isVisible()){
            return ;
        }

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        Point2D position = new Point2D(getX(), getY());
        Point2D lineEndPoint = countLineEndPoint();
        // position je jiz posunuta dle zoomu, takze pro vypocet se musi vratit na puvodni misto
        Point2D topLeftCorner = countTopLeftCorner(lineEndPoint).minus( zoom.reverse(position) );
        Dimension dimensions = getDimensions();
        Box2D tagShape = new Box2D(
            topLeftCorner,
            dimensions.width,
            dimensions.height
        );
        Point2D lineStartPoint = countLineStartpoint(lineEndPoint);

        lineEndPoint = zoom.resize(lineEndPoint);
        tagShape = zoom.resize(tagShape);
        lineStartPoint = zoom.resize(lineStartPoint);

        //pozadi
        g2.setPaint(settings.getBackgroundColor());
        tagShape.fill(g2);

        //border
        BasicStroke borderStroke = settings.getBorderStroke();
        if(borderStroke.getLineWidth() > 0){
            g2.setPaint(settings.getBorderColor());
            borderStroke = zoom.resize(borderStroke);
            g2.setStroke(borderStroke);
            tagShape.draw(g2);
        }

        //spojnice s vrcholem
        if(lineStartPoint != null && settings.getConnectLineStroke().getLineWidth() > 0 && lineStartPoint.distance(lineEndPoint) >= settings.getMinLineDist()){
            Line2D lineFromNode = new Line2D(lineStartPoint.minus(position), lineEndPoint.minus(position));
            g2.setPaint(settings.getConnectLineColor());
            BasicStroke borders = settings.getConnectLineStroke();
            borders = zoom.resize(borders);
            g2.setStroke(borders);
            lineFromNode.draw(g2);
        }

        g2.setColor(settings.getTextColor());
        g2.setFont(zoom.resize(settings.getTextFont()));
        g2.drawString(
            node.getLogicalNode().getName(),
            zoom.resize((int)(topLeftCorner.x + settings.getDimensions().width)),
            zoom.resize((int)(topLeftCorner.y + dimensions.height - settings.getDimensions().height))
        );
    }

    @Override
    public void paintVector(VectorBuilder vb) throws VectorBuilderException {
        if( ! node.isVisible()){
            return ;
        }

        Point2D lineEndPoint = countLineEndPoint();
        // position je jiz posunuta dle zoomu, takze pro vypocet se musi vratit na puvodni misto
        Point2D topLeftCorner = countTopLeftCorner(lineEndPoint);
        Dimension dimensions = getDimensions();
        Box2D tagShape = new Box2D(
            topLeftCorner,
            dimensions.width,
            dimensions.height
        );
        Point2D lineStartPoint = countLineStartpoint(lineEndPoint);

        lineEndPoint = zoom.resize(lineEndPoint);
        tagShape = zoom.resize(tagShape);
        lineStartPoint = zoom.resize(lineStartPoint);

        if(settings.getBackgroundColor() != null){
            vb.fill(tagShape, settings.getBackgroundColor());
        }

        BasicStroke borderStroke = settings.getBorderStroke();
        if(borderStroke.getLineWidth() > 0){
            vb.draw(tagShape, borderStroke, settings.getBorderColor());
        }

        if(lineStartPoint != null && settings.getConnectLineStroke().getLineWidth() > 0 && lineStartPoint.distance(lineEndPoint) >= settings.getMinLineDist()){
            vb.draw(new Line2D(lineStartPoint, lineEndPoint), zoom.resize(settings.getConnectLineStroke()), settings.getConnectLineColor());
        }

        vb.write(
            node.getLogicalNode().getName(),
            zoom.resize((int)(topLeftCorner.x + settings.getDimensions().width)),
            zoom.resize((int)(topLeftCorner.y + dimensions.height - settings.getDimensions().height)),
            zoom.resize(settings.getTextFont()),
            settings.getTextColor());
    }

    @Override
    public Rectangle getBounds(){
        Rectangle shapeBounds = getShapeBounds();
        Box2D bounds = new Box2D(
            new Point2D(
                shapeBounds.x,
                shapeBounds.y
            ),
            shapeBounds.width,
            shapeBounds.height
        );
        bounds = zoom.resize(bounds);
        return bounds.getAsAWTRectangle();
    }
    /**
     * @return hranice tvaru popisku bez započítání místa pro zvýraznění vybraného a nalezeného.
     */
    public Rectangle getShapeBounds(){
        Point2D lineEndPoint = countLineEndPoint();
        Point2D topLeftCorner = countTopLeftCorner(lineEndPoint);
        Point2D lineStartPoint = countLineStartpoint(lineEndPoint);
        if(lineStartPoint == null){
            lineStartPoint = lineEndPoint;
        }
        Dimension dimensions = getDimensions();

        int distanceX = (int) (lineStartPoint.getX() - topLeftCorner.getX());
        int width;
        int distanceY = (int) (lineStartPoint.getY() - topLeftCorner.getY());
        int height;

        if(distanceX < 0){
            width = dimensions.width - distanceX;
        } else if(distanceX > dimensions.width){
            width = distanceX;
        } else {
            width = dimensions.width;
        }

        if(distanceY < 0){
            height = dimensions.height - distanceY;
        } else if(distanceY > dimensions.height){
            height = distanceY;
        } else {
            height = dimensions.height;
        }

        float lineWidth = settings.getConnectLineStroke().getLineWidth();
        Box2D bounds = new Box2D(
            new Point2D(
                Math.min(lineStartPoint.getX(), topLeftCorner.getX()),
                Math.min(lineStartPoint.getY(), topLeftCorner.getY())
            ),
            width + lineWidth * 2,
            height + lineWidth * 2
        );
        return bounds.getAsAWTRectangle();
    }
    /**
     * Hranice popisku bez započítání místa pro spojnici s vrcholem a místa pro zvýraznění vybraného a nalezeného.
     *
     * @return hranice tvaru s textem
     */
    public Rectangle getBoundsWithoutLine(){
        Point2D lineEndPoint = countLineEndPoint();
        Point2D topLeftCorner = countTopLeftCorner(lineEndPoint);
        Dimension dimensions = getDimensions();
        Box2D bounds = new Box2D(
            new Point2D(
                topLeftCorner.getX(),
                topLeftCorner.getY()
            ),
            dimensions.width,
            dimensions.height
        );
        bounds = zoom.resize(bounds);

        return bounds.getAsAWTRectangle();
    }

    protected Dimension getDimensions(){
        int width = 0;
        if(getGraphics() != null){
            width = getGraphics().getFontMetrics( settings.getTextFont() ).stringWidth( node.getLogicalNode().getName() ) + settings.getDimensions().width*2;
        }
        return new Dimension(
            width,
            settings.getTextFont().getSize() + settings.getDimensions().height*2
        );
    }


    @Override
    public boolean contains(int x, int y){
        return getBoundsWithoutLine().contains(x, y);
    }

    @Override
    public int getX(){
        return (int) getBounds().getX();
    }

    @Override
    public int getY(){
        return (int) getBounds().getY();
    }

    @Override
    public int getWidth(){
        return (int) getBounds().getWidth();
    }

    @Override
    public int getHeight(){
        return (int) getBounds().getHeight();
    }

    public void moveBy(Point2D p){
        Point2D origin = node.getCenter();
        Point2D p1 = new Point2D(
                settings.getDistanceFromNode() * Math.cos(Math.toRadians(settings.getAngleFromHoriz())),
                settings.getDistanceFromNode() * Math.sin(Math.toRadians(settings.getAngleFromHoriz()))).plus(origin);
        Point2D p2 = new Point2D(p1.x + p.x, p1.y - p.y);

        settings.setAngleFromHoriz((float) Math.toDegrees( Angle2D.getHorizontalAngle(p2.minus(origin)) ));
        settings.setDistanceFromNode((float) Point2D.getDistance(origin, p2));
    }
}
