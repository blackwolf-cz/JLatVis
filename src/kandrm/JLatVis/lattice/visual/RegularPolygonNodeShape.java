package kandrm.JLatVis.lattice.visual;

import kandrm.JLatVis.lattice.editing.Zoom;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Collection;
import kandrm.JLatVis.export.VectorBuilder;
import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;
import kandrm.JLatVis.lattice.visual.settings.NodeVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.ShapeRegularPolygonVisualSettings;
import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.SimplePolygon2D;

/**
 * Vrchol svazu v podobe pravidelneho n-uhelniku.
 *
 */
public class RegularPolygonNodeShape implements INodeShape{
    private static final NodeShapeTypes.Type NODE_TYPE = NodeShapeTypes.Type.REGULAR_POLYGON;

    private Zoom zoom;

    protected NodeVisualSettings settings;
    protected Point2D center;

    public RegularPolygonNodeShape(NodeVisualSettings settings, Point2D center){
        this.settings = settings;
        this.center = center;
    }

    @Override
    public void setZoom(Zoom zoom){
        this.zoom = zoom;
    }

    @Override
    public void setVisualSettings(NodeVisualSettings settings){
        this.settings = settings;
    }

    private SimplePolygon2D generateShape(){
        Dimension dimensions = settings.getDimensions();
        int n = ((ShapeRegularPolygonVisualSettings)settings.getSpecialSettings()).getN();
        int initAngle = ((n-2)*90)/n;
        int r = (int)((dimensions.width>dimensions.height ? dimensions.height : dimensions.width)
                / (2*Math.cos(Math.toRadians(initAngle))));


        SimplePolygon2D polygon = new SimplePolygon2D();
        for(int i=initAngle; i<360+initAngle; i+=360/n){
            polygon.addVertex(new Point2D(-r*Math.cos(Math.toRadians(i))+center.x, r*Math.sin(Math.toRadians(i))+center.y));
        }

        if(dimensions.width != dimensions.height){
            polygon = polygon.transform(AffineTransform2D.createScaling(
                center,
                dimensions.width>dimensions.height ? dimensions.width/dimensions.height : 1,
                dimensions.height>dimensions.width ? dimensions.height/dimensions.width : 1
            ));
        }
        
        if(settings.getAngle() > 0){
            polygon = polygon.transform(AffineTransform2D.createRotation(center, settings.getAngle()));
        }

        return polygon;
    }

    private SimplePolygon2D generateShapeInBox(){
        Box2D box = getBoundingBox();
        SimplePolygon2D shape = generateShape();
        shape = zoom.resize(shape);
        return shape.transform(
            AffineTransform2D.createTranslation(
                - box.getMinX() + settings.getMaxHighlightDistance(),
                - box.getMinY() + settings.getMaxHighlightDistance()
            ));
    }

    @Override
    public Point2D findEdgeIntersection(Point2D p, int distanceFromNode) {
        Line2D line = new Line2D(center, p);
        Collection<LineSegment2D> edges = generateShape().getEdges();

        Point2D result = null;

        for (LineSegment2D edge : edges) {
            Point2D edgeIntersec = edge.getIntersection(line);
            if(edgeIntersec != null){
                result = edgeIntersec;
                break;
            }
        }
       
        if(result!=null && distanceFromNode>0){
            result = result.transform(AffineTransform2D.createTranslation(new Vector2D(center, p).getNormalizedVector().times(distanceFromNode)));
        }
        return result;
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
        SimplePolygon2D shape = zoom.resize(generateShape());
        //Implementace contains v knihovne je chybna a nepocita s tim, ze winding number muze byt 1 i -1 a pri -1 vrati chybne false.
        return Math.abs(shape.getWindingNumber(p.x, p.y))==1 || shape.getBoundary().contains(p.x, p.y);
    }
}
