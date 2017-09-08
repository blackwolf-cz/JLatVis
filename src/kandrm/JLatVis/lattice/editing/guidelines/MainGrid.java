package kandrm.JLatVis.lattice.editing.guidelines;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.settings.GuidelineVisualSettings;
import math.geom2d.Point2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.Rectangle2D;

/**
 * Mřížka vodících linek.
 * Je-li zobrazena, je viditelná vždy.
 * Může být horizontální, vertikální či obojí a může být libovolně natočena.
 * Slouží k obecnému uspořádání všech prvků ve svazu.
 *
 * @author Michal Kandr
 */
public class MainGrid extends JPanel implements IGuideline, PropertyChangeListener {
    private GuidelineVisualSettings settings;
    private LatticeShape lattice;

    /**
     * Jednotlivé vodící linky, ze kterých je mřížka složena.
     */
    private List<Guideline> lines = new ArrayList<Guideline>();
    private List<GuidePoint> points = new ArrayList<GuidePoint>();

    private Point2D centerPoint = new Point2D(0, 0);

    /**
     * Vytvoří novu mřížku vodících linek pro svaz s defaultním vzhledem.
     *
     * @param lattice
     */
    public MainGrid(LatticeShape lattice){
        this(lattice, new GuidelineVisualSettings());
    }
    /**
     * Vytvoří novu mřížku vodících linek pro svaz s nastaveným vzhledem.
     *
     * @param lattice
     * @param settings
     */
    public MainGrid(LatticeShape lattice, GuidelineVisualSettings settings){
        this.lattice = lattice;
        this.settings = settings;
        createShapes();
        lattice.addPropertyChangeListener("preferredSize", this);
        setOpaque(false);
    }

    @Override
    public boolean isInStickRange(Point2D p){
        for(Guideline g : lines){
            if(g.isInStickRange(p)){
                return true;
            }
        }
        for(GuidePoint point : points){
            if(point.isInStickRange(p)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Point2D findNearestPoint(Point2D p){
        List<Point2D> nearestPoints = new ArrayList<Point2D>();
        for(GuidePoint point : points){
            if(point.isInStickRange(p)){
                return point.findNearestPoint(p);
            }
        }
        if(nearestPoints.isEmpty()){
            for(Guideline l : lines){
                if(l.isInStickRange(p)){
                    nearestPoints.add(l.findNearestPoint(p));
                }
            }
        }
        Point2D nearestPoint = null;
        for(Point2D point : nearestPoints){
            if(nearestPoint == null || point.getDistance(p) < nearestPoint.getDistance(p)){
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    public Point2D getCenterPoint() {
        return centerPoint;
    }
    public void setCenterPoint(Point2D centerPoint) {
        this.centerPoint = centerPoint;
    }

    private void addLine(Guideline l){
        lines.add(l);
        add(l);
    }
    private void addPoint(GuidePoint l){
        points.add(l);
        add(l);
    }

    /**
     * Smaže všechny linky které mřížka obsahuje.
     */
    private void clear(){
        for(Guideline l : lines){
            remove(l);
        }
        lines.clear();
        for(GuidePoint p : points){
            remove(p);
        }
        points.clear();
    }

    public void refresh(Point2D shift){
        centerPoint = centerPoint.plus(shift);
        createShapes();
    }

    /**
     * Vytvoří linky mřížky podle nastavení.
     */
    private void createShapes(){
        clear();
        if(settings.isShowGrid()){
            List<Line2D> gridLines = createLines();
            if(settings.isOnlyIntersec() && settings.isGrid()){
                createShapesDots(gridLines);
            } else {
                createShapesLines(gridLines);
                createShapesDots(gridLines);
            }
        }
    }

    private void createShapesLines(List<Line2D> gridLines){
        for(Line2D gridLine : gridLines){
            addLine(new Guideline(lattice, settings, gridLine));
        }
    }

    private void createShapesDots(List<Line2D> gridLines){
        ArrayList<Point2D> intersects = new ArrayList<Point2D>();
        for(Line2D gridLine : gridLines){
            for(Line2D gridLineTest : gridLines){
                Point2D intersec = gridLine.getIntersection(gridLineTest);
                if(intersec != null){
                    intersects.add(intersec);
                }
            }
        }
        for(Point2D p : intersects){
            addPoint(new GuidePoint(lattice, settings, p));
        }
    }


    private List<Line2D> createLinesPart(Rectangle2D bounds, StraightLine2D line, int distance){
        ArrayList<Line2D> gridLines = new ArrayList<Line2D>();
        Line2D guideLine = GuidelinesUtils.getIntersectionsLine(line, bounds);
        while(guideLine != null){
            gridLines.add(guideLine.clone());
            line = line.getParallel(distance);
            guideLine = GuidelinesUtils.getIntersectionsLine(line, bounds);
        }
        return gridLines;
    }
    private List<Line2D> createLinesPartDirection(Rectangle2D bounds, int spacing, float angle){
        StraightLine2D line = new StraightLine2D(centerPoint, - Math.toRadians(angle));
        List<Line2D> gridLines = createLinesPart(bounds, line, spacing);
        line = new StraightLine2D(centerPoint, - Math.toRadians(angle)).getParallel( - spacing);
        gridLines.addAll(createLinesPart(bounds, line, - spacing));
        return gridLines;
    }

    private List<Line2D> createLines(){
        ArrayList<Line2D> gridLines = new ArrayList<Line2D>();
        Rectangle boundsRect = lattice.getBounds();
        Rectangle2D bounds = new Rectangle2D(0, 0, boundsRect.width, boundsRect.height);

        gridLines.addAll(createLinesPartDirection(bounds, settings.getSpacingVert(), settings.getAngle()));
        if(settings.isGrid()){
            gridLines.addAll(createLinesPartDirection(bounds, settings.getSpacingHoriz(), settings.getAngle() + (settings.getAngle() <= 90 || settings.getAngle() >= 270 ? 90 : - 90) ));
        }
        return gridLines;
    }

    @Override
    public GuidelineVisualSettings getVisualSettings(){
        return new GuidelineVisualSettings(settings);
    }
    @Override
    public void setVisualSettings(GuidelineVisualSettings settings){
        this.settings = settings;
        for(Guideline g : lines){
            g.setVisualSettings(settings);
        }
        for(GuidePoint point : points){
            point.setVisualSettings(settings);
        }
        createShapes();
    }

    public boolean isShown(){
        return settings.isShowGrid();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        createShapes();
    }

    
    @Override
    public int getX(){
        return 0;
    }
    @Override
    public int getY(){
        return 0;
    }
    @Override
    public int getWidth(){
        return lattice.getWidth();
    }
    @Override
    public int getHeight(){
        return lattice.getHeight();
    }
}
