package kandrm.JLatVis.lattice.editing.history;

import kandrm.JLatVis.lattice.visual.NodeNameShape;
import kandrm.JLatVis.lattice.visual.settings.NodeNameVisualSettings;

/**
 * Událost změny pozice (přesunu) popisku.
 *
 * @author Michal Kandr
 */
public class HistoryEventNodeNamePosition extends HistoryEvent {
    private NodeNameShape movedTag = null;
    private Float oldDistance = null;
    private Float newDistance = null;
    private Float oldAngle = null;
    private Float newAngle = null;

    public HistoryEventNodeNamePosition(NodeNameShape movedTag, Float oldDistance, Float oldAngle, Float newDistance, Float newAngle){
        super(movedTag);
        this.movedTag = movedTag;
        this.oldDistance = oldDistance;
        this.oldAngle = oldAngle;
        this.newDistance = newDistance;
        this.newAngle = newAngle;
    }

    @Override
    public void reverse() {
        NodeNameVisualSettings settings = movedTag.getVisualSettings();
        settings.setDistanceFromNode(oldDistance);
        settings.setAngleFromHoriz(oldAngle);
        movedTag.setVisualSettings(settings, false);
    }

    @Override
    public void doAgain() {
        NodeNameVisualSettings settings = movedTag.getVisualSettings();
        settings.setDistanceFromNode(newDistance);
        settings.setAngleFromHoriz(newAngle);
        movedTag.setVisualSettings(settings, false);
    }
}
