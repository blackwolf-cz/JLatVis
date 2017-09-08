package kandrm.JLatVis.lattice.editing;

import kandrm.JLatVis.lattice.logical.*;
import kandrm.JLatVis.lattice.visual.NodeShape;
import kandrm.JLatVis.lattice.visual.LatticeShape;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import kandrm.JLatVis.lattice.editing.guidelines.GuidelinesHolder;
import kandrm.JLatVis.lattice.editing.history.HistoryEvent;
import kandrm.JLatVis.lattice.editing.history.HistoryEventNodePosition;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventListener;
import kandrm.JLatVis.lattice.editing.history.IHistoryEventSender;
import math.geom2d.Point2D;

/**
 *
 * @author Michal Kandr
 */
public class NodeMover extends MouseAdapter implements IHistoryEventSender {
    private enum Relatives { Parents, Childs }
    
    private List<IHistoryEventListener> historyListeners = new ArrayList<IHistoryEventListener>();

    private LatticeShape latticeShape;
    private Zoom zoom;

    private List <NodeShape> movedNodes = new ArrayList<NodeShape>();
    private Map <NodeShape, Point2D> originalPositions = new HashMap<NodeShape, Point2D>();
    private Point2D prevPosition = null;
    
    private Point2D stickMovesVector = null;
    private Point2D stickMovesOrigin = null;

    public NodeMover(LatticeShape latticeShape){
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

    private boolean fitPositionForRelatives(Point2D shift, Relatives type){
        for(NodeShape node : movedNodes){
            List <Node> relativeNodes = null;
            if(type == Relatives.Parents){
                relativeNodes = node.getLogicalNode().getParents();
            } else {
                relativeNodes = node.getLogicalNode().getDescendants();
            }

            for(Node logicalRelative : relativeNodes){
                NodeShape relative = logicalRelative.getShape();
                double newY = node.getCenter().getY() + shift.getY();
                if(( type==Relatives.Parents && newY <= relative.getCenter().getY() + LatticeShape.MIN_DESC_PARENT_DISTANCE )
                        || ( type==Relatives.Childs && newY >= relative.getCenter().getY() - LatticeShape.MIN_DESC_PARENT_DISTANCE )){
                    shift.setLocation(shift.getX(), 0);
                    return true;
                }
            }
        }
        return false;
    }

    private void fitPositionByOrder(Point2D shift){
        if(fitPositionForRelatives(shift, Relatives.Parents)){
            return;
        }
        fitPositionForRelatives(shift, Relatives.Childs);
    }

    private void moveOtherNodes(Point2D shift){
        List <NodeShape> otherNodes = new ArrayList<NodeShape>(latticeShape.getNodes());
        otherNodes.removeAll(movedNodes);
        if( ! originalPositions.keySet().containsAll(otherNodes)){
            for(NodeShape node : otherNodes){
                 originalPositions.put(node, node.getCenter());
            }
        }
        for(NodeShape node : otherNodes){
            node.moveBy(shift);
        }
        latticeShape.getGuidelines().refresh(movedNodes, shift);
    }

    private void fitPositionToBorders(Point2D shift){
        boolean moveOthers = false;
        Point2D newShift = new Point2D(0, 0);
        //osetreni posunu za levy ci horni okraj plochy
        for(NodeShape node : movedNodes){
            Rectangle nodeBounds = node.getRealBounds();
            // move outside containner, or first node from left
            if( nodeBounds.getX() + shift.getX() < 0){
                int newXShift = (int) Math.max(0, - nodeBounds.getX());
                if(newXShift == 0){
                    moveOthers = true;
                    newShift = new Point2D(- shift.getX(), newShift.getY());
                }
                shift.setLocation(newXShift, shift.getY());
            }
            if( ! moveOthers){
                if( nodeBounds.getY() + shift.getY() < 0){
                    int newYShift = (int) Math.max(0, - nodeBounds.getY());
                    if(newYShift == 0){
                        moveOthers = true;
                        newShift = new Point2D(newShift.getX(), - shift.getY());
                    }
                    shift.setLocation(shift.getX(), newYShift);
                }
            }
        }
        if(moveOthers){
            moveOtherNodes(newShift);
        }
    }

    private void resetSticking(){
        stickMovesVector = null;
        stickMovesOrigin = null;
    }

    private void stickToGrids(Point2D shift){
        GuidelinesHolder grid = latticeShape.getGuidelines();
        if( ! grid.isShown()){
            return;
        }

        int minDistance = Integer.MAX_VALUE;
        Point2D minCenter = null;
        Point2D nearestGridPoint = null;
        Point2D minCenterOriginal = null;

        // nalezeni nejblizsiho bodu na voditku, na ktery se bude protahovat
        for(NodeShape node : movedNodes){
            Point2D newCenter = zoom.resize(node.getCenter()).translate( shift.getX(), shift.getY() );
            if( grid.isInStickRange(newCenter) ){
                Point2D gridPoint = grid.findNearestPoint(newCenter);
                if(gridPoint == null){
                    continue;
                }
                int distance = (int) newCenter.distance(gridPoint);
                if(distance < minDistance){
                    minDistance = distance;
                    minCenter = newCenter;
                    minCenterOriginal = zoom.resize(node.getCenter());
                    nearestGridPoint = gridPoint;
                }
            }
        }

        if(minDistance != Integer.MAX_VALUE){
            if(stickMovesVector == null){
                stickMovesVector = new Point2D(0,0);
                stickMovesOrigin = new Point2D(minCenter);
            } else {
                stickMovesVector = stickMovesVector.plus(shift);
            }

            if( stickMovesVector != null && ! grid.isInStickRange(minCenter.plus(stickMovesVector))){
                shift.setLocation( stickMovesOrigin.plus(stickMovesVector).minus(nearestGridPoint) );
                resetSticking();
                //System.out.println("out");
                return;
            } else if(stickMovesVector.x == 0 && stickMovesVector.y == 0){
                //System.out.println("catch");
            } else {
                //System.out.println("stay");
            }

            Point2D newShift = zoom.reverse(new Point2D( nearestGridPoint.getX() - minCenterOriginal.getX(), nearestGridPoint.getY() - minCenterOriginal.getY() ));
            shift.setLocation( newShift );

            //System.out.println(" stickMovesVector: " + stickMovesVector + ",  newShift: " + shift + ",  minDistance: " + minDistance);
            
        } else if( minDistance == 0 ){
            shift.setLocation(0, 0);
        }
    }

    private void mouseDraggedNoNodes(MouseEvent e){
        if(e.getSource() instanceof NodeShape){
            NodeShape movedNode = (NodeShape) e.getSource();
            if(movedNode.isSelected()){
                movedNodes.addAll(latticeShape.getSelector().getSelectedNodes());
            } else {
                movedNodes.add((NodeShape) e.getSource());
            }
            ListIterator listIterator = movedNodes.listIterator();
            while(listIterator.hasNext()){
                NodeShape n = (NodeShape) listIterator.next();
                if( ! n.isVisible()){
                    listIterator.remove();
                }
            }
            for(NodeShape node : movedNodes){
                originalPositions.put(node, node.getCenter());
            }
            latticeShape.getGuidelines().getLatticeGrid().create(movedNodes);
        }
    }

    private void mouseDraggedNodes(MouseEvent e){
        Point2D shift = new Point2D(e.getX() - prevPosition.getX(), e.getY() - prevPosition.getY());
        shift = zoom.reverse(shift);

        
        if(movedNodes.size() == latticeShape.getNodes().size()){
            Rectangle bounds = latticeShape.getRealBounds();
            if(bounds.getX() + shift.x < 0){
                shift.x = - latticeShape.getX();
            }
            if(bounds.getY() + shift.y < 0){
                shift.y = - latticeShape.getY();
            }
        } else {
            fitPositionByOrder(shift);
            fitPositionToBorders(shift);
        }
        stickToGrids(shift);

        for(NodeShape node : movedNodes){
            node.moveBy(shift);
        }

        latticeShape.recountPreferredSize();
        latticeShape.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(movedNodes.size() > 0){
            Map <NodeShape, Point2D> newPositions = new HashMap<NodeShape, Point2D>();
            for(NodeShape node : originalPositions.keySet()){
                newPositions.put(node, node.getCenter());
            }
            fireHistoryEvent(new HistoryEventNodePosition(new ArrayList <NodeShape>(originalPositions.keySet()), new HashMap<NodeShape, Point2D>(originalPositions), newPositions));
        }
        movedNodes.clear();
        originalPositions.clear();
        prevPosition = null;
        resetSticking();
        latticeShape.getGuidelines().getLatticeGrid().destroy();
        latticeShape.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(movedNodes.size() < 1){
            mouseDraggedNoNodes(e);
        } else {
            mouseDraggedNodes(e);
        }
        
        prevPosition = new Point2D(e.getX(), e.getY());
    }
}
