package kandrm.JLatVis.export;

import java.awt.Dimension;
import java.awt.Rectangle;
import kandrm.JLatVis.lattice.logical.Lattice;
import kandrm.JLatVis.lattice.logical.Node;
import math.geom2d.Point2D;

/**
 *
 * @author Michal Kandr
 */
public class CoordsRecount {
    public static void adjustCoords(Lattice l){
        int minX = 0, minY = 0;
        for(Node node : l.getNodes().values()){
            Rectangle bounds = node.getShape().getRealBounds();
            Dimension dim = node.getShape().getVisualSettings().getDimensions();
            if(bounds.getX() - dim.width < minX){
                minX = (int) bounds.getX() - dim.width;
            }
            if(bounds.getY() - dim.height < minY){
                minY = (int) bounds.getY() - dim.height;
            }
        }
        if(minX < 0 || minY < 0){
            for(Node node : l.getNodes().values()){
                node.getShape().setCenter( node.getShape().getCenter().plus( new Point2D(- minX, - minY) ) );
            }
        }
    }
}
