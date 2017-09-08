package kandrm.JLatVis.lattice.editing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import kandrm.JLatVis.lattice.editing.history.HistoryEvent;
import kandrm.JLatVis.lattice.editing.history.HistoryEventTagPosition;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;
import kandrm.JLatVis.lattice.visual.TagShape;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import kandrm.JLatVis.lattice.visual.NodeShape;
import math.geom2d.Point2D;

/**
 *
 * @author Michal Kandr
 */
public class TagMover extends MouseAdapter implements IHistoryEventSender {
    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();

    private LatticeShape latticeShape;
    private Zoom zoom;
    private Point2D prevPosition = null;
    private TagShape movedTag = null;
    private Float originalDistance = null;
    private Float originalAngle = null;

    public TagMover(LatticeShape latticeShape){
        this.latticeShape = latticeShape;
        zoom = latticeShape.getZoom();
    }

    @Override
    public void addHistoryListener(IHistoryEventListener l){
        historyListeners.add(l);
    }
    @Override
    public void removeHistoryListener(IHistoryEventListener l){
        historyListeners.remove(l);
    }
    private void fireHistoryEvent(HistoryEvent e){
        for (IHistoryEventListener l : historyListeners) {
            l.eventPerformed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(movedTag != null){
            fireHistoryEvent(new HistoryEventTagPosition(movedTag, originalDistance, originalAngle,
                    movedTag.getVisualSettings().getDistanceFromNode(), movedTag.getVisualSettings().getAngleFromHoriz()));

            movedTag = null;
            prevPosition = null;
            originalDistance = null;
            originalAngle = null;
        }
    }

    private void selectMovedTag(MouseEvent e){
        movedTag = (TagShape) e.getSource();
        if( ! movedTag.getNode().isVisible()){
            movedTag = null;
            return;
        }
        originalDistance = movedTag.getVisualSettings().getDistanceFromNode();
        originalAngle = movedTag.getVisualSettings().getAngleFromHoriz();
    }
    
    private void moveTag(MouseEvent e){
        Point2D shift = new Point2D(e.getX() - prevPosition.getX(), e.getY() - prevPosition.getY());
        shift = zoom.reverse(shift);
        Point2D newShift = new Point2D(0, 0);
        if(movedTag.getX() + shift.x < 0 || movedTag.getY() + shift.y < 0){
            if( movedTag.getX() + shift.x < 0){
                int newXShift = (int) Math.max(0, - movedTag.getX());
                if(newXShift == 0){
                    newShift = new Point2D(- shift.getX(), newShift.getY());
                }
                shift.setLocation(newXShift, shift.getY());
            }
            if( movedTag.getY() + shift.y < 0){
                int newYShift = (int) Math.max(0, - movedTag.getY());
                if(newYShift == 0){
                    newShift = new Point2D(newShift.getX(), - shift.getY());
                }
                shift.setLocation(shift.getX(), newYShift);
            }
            for(NodeShape n : latticeShape.getNodes()){
                n.moveBy(newShift);
            }
        }

        movedTag.moveBy(shift);

        latticeShape.recountPreferredSize();
        latticeShape.repaint();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if( ! ( e.getSource() instanceof TagShape)){
            return;
        }
        if(movedTag == null){
            selectMovedTag(e);
        } else {
            moveTag(e);
        }
        prevPosition = new Point2D(e.getX(), e.getY());
    }
}
