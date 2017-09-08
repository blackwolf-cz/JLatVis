package kandrm.JLatVis.lattice.editing.guidelines;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.visual.EdgeShape;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.settings.GuidelineVisualSettings;
import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.Line2D;
import math.geom2d.polygon.Rectangle2D;

/**
 *
 * @author Michal Kandr
 */
public class LatticeGrid extends JPanel implements IGuideline {
    private GuidelineVisualSettings settings;
    private LatticeShape lattice;

    /**
     * Jednotlivé vodící linky, ze kterých je mřížka složena.
     */
    private List<Guideline> lines = new ArrayList<Guideline>();
    private List<GuidePoint> points = new ArrayList<GuidePoint>();

    /**
     * Vytvoří novu mřížku vodících linek pro svaz s defaultním vzhledem.
     *
     * @param lattice
     */
    public LatticeGrid(LatticeShape lattice){
        this(lattice, new GuidelineVisualSettings());
    }
    /**
     * Vytvoří novu mřížku vodících linek pro svaz s nastaveným vzhledem.
     *
     * @param lattice
     * @param visualSettings
     */
    public LatticeGrid(LatticeShape lattice, GuidelineVisualSettings visualSettings){
        this.lattice = lattice;
        this.settings = visualSettings;
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
        for(GuidePoint point : points){
            if(point.isInStickRange(p)){
                return point.findNearestPoint(p);
            }
        }
        for(Guideline l : lines){
            if(l.isInStickRange(p)){
                return l.findNearestPoint(p);
            }
        }
        return null;
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
        removeAll();
        lines.clear();
        points.clear();
    }

    /**
     * Vytvoří linky mřížky podle nastavení.
     */
    private void createShapes(List<NodeShape> exclude){
        clear();
        if(settings.isShowEdge()){
            createEdgeGuidelines(exclude);
        }
        if(settings.isShowParallel()){
            createParallelogramGuidelines(exclude);
        }
    }

    private int getMinY(List<NodeShape> nodes){
        int minY = 0;
        if(nodes != null){
            for(NodeShape n : nodes){
                int nMinY = n.getMinimalAllowedY();
                if(nMinY > minY){
                    minY = nMinY;
                }
            }
        }
        return minY;
    }
    private int getMaxY(List<NodeShape> nodes){
        int maxY = Integer.MAX_VALUE;
        if(nodes != null){
            for(NodeShape n : nodes){
                int nMaxY = n.getMaximalAllowedY();
                if(nMaxY < maxY){
                    maxY = nMaxY;
                }
            }
        }
        return maxY;
    }


    private List<EdgeShape> getEdgesForGuidelines(List<NodeShape> movedNodes){
        List<NodeShape> parents = new ArrayList<NodeShape>();
        List<NodeShape> childs = new ArrayList<NodeShape>();
        for(NodeShape node : movedNodes){
            for(Node parent : node.getLogicalNode().getParents()){
                parents.add(parent.getShape());
            }
            for(Node child : node.getLogicalNode().getDescendants()){
                childs.add(child.getShape());
            }
        }

        List<EdgeShape> edges = new ArrayList<EdgeShape>();
        for(EdgeShape edge : lattice.getEdges()){
            if((
                    (parents.contains(edge.getU1()) && edge.getU1().getLogicalNode().getParents().contains( edge.getU2().getLogicalNode() ))
                    || (parents.contains(edge.getU2()) && edge.getU2().getLogicalNode().getParents().contains( edge.getU1().getLogicalNode() ))
                    || (childs.contains(edge.getU1()) && edge.getU1().getLogicalNode().getDescendants().contains( edge.getU2().getLogicalNode() ))
                    || (childs.contains(edge.getU2()) && edge.getU2().getLogicalNode().getDescendants().contains( edge.getU1().getLogicalNode() ))
                )
                && ! movedNodes.contains(edge.getU1())
                && ! movedNodes.contains(edge.getU2())){
                edges.add(edge);
            }
        }
        
        return edges;
    }

    private void createEdgeGuidelines(List<NodeShape> movedNodes){
        int minY = getMinY(movedNodes);
        int maxY = getMaxY(movedNodes);
        Rectangle bounds = lattice.getBounds();
        int boundsRectY = minY > bounds.y ? minY : bounds.y;
        int boundsRectHeight =  ( maxY < Integer.MAX_VALUE ? maxY : bounds.height) - boundsRectY;
        Rectangle2D boundsRect = new Rectangle2D(bounds.x, boundsRectY , bounds.width, boundsRectHeight);
        for(EdgeShape edge : getEdgesForGuidelines(movedNodes)){
            if(movedNodes == null || ( ! movedNodes.contains(edge.getU1()) && ! movedNodes.contains(edge.getU2()))){
                Line2D guideLine = GuidelinesUtils.getIntersectionsLine(new Line2D(edge.getU1().getCenter(), edge.getU2().getCenter()).getSupportingLine(), boundsRect);
                if(guideLine != null){
                    addLine(new Guideline(lattice, settings, guideLine));
                }

                Point2D guidePoint = edge.getU1().getCenter().transform( AffineTransform2D.createPointReflection(edge.getU2().getCenter()) );
                if(guidePoint.getY() >= minY && guidePoint.getY() <= maxY){
                    addPoint(new GuidePoint(lattice, settings, guidePoint));
                }
                guidePoint =  edge.getU2().getCenter().transform(AffineTransform2D.createPointReflection(edge.getU1().getCenter()));
                if(guidePoint.getY() >= minY && guidePoint.getY() <= maxY){
                    addPoint(new GuidePoint(lattice, settings, guidePoint));
                }
            }
        }
    }

    private void createParallelogramGuideline(int minY, int maxY, NodeShape origin, NodeShape n1, NodeShape n2){
        Vector2D vector = new Vector2D(origin.getCenter(), n1.getCenter()).plus( new Vector2D(origin.getCenter(), n2.getCenter()));
        Point2D guidePoint = new Point2D(vector.getX(), vector.getY()).plus(origin.getCenter());
        if(guidePoint.x > 0 && guidePoint.y >= minY && guidePoint.y <= maxY){
            addPoint(new GuidePoint(lattice, settings, guidePoint));
        }
    }
    
    private void createParallelogramGuidelines(List<NodeShape> movedNodes){
        int minY = getMinY(movedNodes);
        int maxY = getMaxY(movedNodes);

        for(NodeShape node : movedNodes){
            for(final Node n1 : node.getLogicalNode().getParents()){
                for(final Node n2 : node.getLogicalNode().getParents()){
                    if(n2 == n1){
                        continue;
                    }
                    Node n3 = lattice.getLogicalSearch().getSupremum( new ArrayList<Node>(){{ add(n1); add(n2); }} );
                    if(n3 == null){
                        continue;
                    }
                    createParallelogramGuideline(minY, maxY, n1.getShape(), n2.getShape(), n3.getShape());
                    createParallelogramGuideline(minY, maxY, n2.getShape(), n1.getShape(), n3.getShape());
                    createParallelogramGuideline(minY, maxY, n3.getShape(), n2.getShape(), n1.getShape());
                }
            }
            for(final Node n1 : node.getLogicalNode().getDescendants()){
                for(final Node n2 : node.getLogicalNode().getDescendants()){
                    if(n2 == n1){
                        continue;
                    }
                    Node n3 = lattice.getLogicalSearch().getInfimum( new ArrayList<Node>(){{ add(n1); add(n2); }} );
                    if(n3 == null){
                        continue;
                    }
                    createParallelogramGuideline(minY, maxY, n1.getShape(), n2.getShape(), n3.getShape());
                    createParallelogramGuideline(minY, maxY, n2.getShape(), n1.getShape(), n3.getShape());
                    createParallelogramGuideline(minY, maxY, n3.getShape(), n2.getShape(), n1.getShape());
                }
            }
        }
    }

    public void create(List<NodeShape> movedNodes){
        createShapes(movedNodes);
    }
    public void create(){
        create(null);
    }

    public void destroy(){
        clear();
    }

    public void refresh(List<NodeShape> movedNodes){
        clear();
        create(movedNodes);
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
    }

    public boolean isShown(){
        return settings.isShowEdge() || settings.isShowParallel();
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
