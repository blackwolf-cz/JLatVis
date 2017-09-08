
package kandrm.JLatVis.lattice.editing.guidelines;

import java.util.ArrayList;
import java.util.List;
import math.geom2d.Point2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.Rectangle2D;

/**
 *
 * @author Michal Kandr
 */
public class GuidelinesUtils {
    public static Line2D getIntersectionsLine(StraightLine2D line, Rectangle2D rect){
        List<Point2D> intersections = new ArrayList<Point2D>();
        for(LineSegment2D lineSegment : rect.getEdges()){
            Point2D intersect = lineSegment.getIntersection(line);
            if(intersect != null && ! intersections.contains(intersect)){
                intersections.add(intersect);
            } else if(lineSegment.getSupportingLine().equals(line)){
                return new Line2D(lineSegment.getFirstPoint(), lineSegment.getLastPoint());
            }
        }
        return intersections.size() == 2 ? new Line2D(intersections.get(0), intersections.get(1)) : null;
    }
}
