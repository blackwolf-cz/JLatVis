package kandrm.JLatVis.lattice.editing.history;

import java.util.List;
import java.util.Map;
import kandrm.JLatVis.lattice.visual.NodeShape;
import math.geom2d.Point2D;

/**
 * Událost změny pozice (přesunu) uzlu.
 *
 * @author Michal Kandr
 */
public class HistoryEventNodePosition extends HistoryEvent {
    private List<NodeShape> nodes;
    private Map <NodeShape, Point2D> originalPositions;
    private Map <NodeShape, Point2D> newPositions;

    public HistoryEventNodePosition(List<NodeShape> nodes,
            Map <NodeShape, Point2D> originalPositions,
            Map <NodeShape, Point2D> newPositions){
        super(nodes);
        this.nodes = nodes;
        this.originalPositions = originalPositions;
        this.newPositions = newPositions;
    }

    @Override
    public void reverse() {
        for(NodeShape node : nodes){
            node.moveTo(originalPositions.get(node));
        }
    }

    @Override
    public void doAgain() {
        for(NodeShape node : nodes){
            node.moveTo(newPositions.get(node));
        }
    }
}
