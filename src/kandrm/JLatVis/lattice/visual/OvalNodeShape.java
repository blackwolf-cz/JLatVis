package kandrm.JLatVis.lattice.visual;

import kandrm.JLatVis.lattice.editing.Zoom;
import java.awt.Graphics2D;
import java.util.Collection;
import kandrm.JLatVis.export.VectorBuilder;
import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;
import kandrm.JLatVis.lattice.visual.settings.NodeVisualSettings;
import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Ellipse2D;
import math.geom2d.line.Line2D;

/**
 * Vrchol svazu v podobe ovalneho tvaru (elipsa nebo kruznice).
 * 
 */
public class OvalNodeShape implements INodeShape {
    private static final NodeShapeTypes.Type NODE_TYPE = NodeShapeTypes.Type.ELLIPSE;

    private Zoom zoom;

    protected NodeVisualSettings settings;
    protected Point2D center;

    public OvalNodeShape(NodeVisualSettings visualSettings, Point2D center){
        this.settings = visualSettings;
        this.center = center;
    }

    @Override
    public void setZoom(Zoom zoom){
        this.zoom = zoom;
    }

    @Override
    public void setVisualSettings(NodeVisualSettings visualSettings){
        this.settings = visualSettings;
    }


    private Ellipse2D generateShape(Point2D c, int distanceFromNode){
        return new Ellipse2D(
            c,
            settings.getDimensions().width/2 + distanceFromNode,
            settings.getDimensions().height/2 + distanceFromNode,
            settings.getAngle()
        );
    }
    private Ellipse2D generateShape(){
        return generateShape(center, 0);
    }
    
    private Ellipse2D generateShapeInBox(){
        Box2D box = getBoundingBox();
        Ellipse2D shape = generateShape();
        shape = zoom.resize(shape);
        return shape.transform(
            AffineTransform2D.createTranslation(
                - box.getMinX() + settings.getMaxHighlightDistance(),
                - box.getMinY() + settings.getMaxHighlightDistance()
            ));
    }

    @Override
    public Point2D findEdgeIntersection(Point2D p, int distanceFromNode){
        Line2D line = new Line2D(center, p);
        if(line.getLength() == 0){
            return null;
        }
        Ellipse2D ellipse = generateShape(center, distanceFromNode);
        Collection<Point2D> intersections = ellipse.getIntersections(line);
        return ! intersections.isEmpty() ? (Point2D)intersections.toArray()[0] : null;
    }

    @Override
    public NodeShapeTypes.Type getType(){
        return NODE_TYPE;
    }

    @Override
    public Box2D getBoundingBox(){
        Box2D box = generateShape().getBoundingBox();
        float borderWidth = settings.getBorderStroke().getLineWidth();
        return zoom.resize(new Box2D(box.getMinX() - borderWidth, box.getMaxX() + borderWidth, box.getMinY() - borderWidth, box.getMaxY() + borderWidth));
    }

    @Override
    public void draw(Graphics2D g2){
        generateShapeInBox().draw(g2);
    }

    @Override
    public void drawVector(VectorBuilder vb) throws VectorBuilderException {
        vb.draw(zoom.resize(generateShape()));
    }

    @Override
    public void fill(Graphics2D g2){
        generateShapeInBox().fill(g2);
    }

    @Override
    public void fillVector(VectorBuilder vb) throws VectorBuilderException {
        vb.fill(zoom.resize(generateShape()));
    }

    @Override
    public boolean contains(Point2D p){
        return zoom.resize(generateShape()).isInside(p);
    }
}
