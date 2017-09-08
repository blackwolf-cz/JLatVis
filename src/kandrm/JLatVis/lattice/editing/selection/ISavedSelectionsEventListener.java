package kandrm.JLatVis.lattice.editing.selection;

import java.util.EventListener;

/**
 *
 * @author Michal Kandr
 */
public interface ISavedSelectionsEventListener extends EventListener {
    public void savedSelectionsEvenOccurred(SavedSelectionsChangeEvent evt);
}
