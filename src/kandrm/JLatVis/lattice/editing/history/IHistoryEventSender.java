package kandrm.JLatVis.lattice.editing.history;

/**
 *
 * @author Michal Kandr
 */
public interface IHistoryEventSender {
    public void addHistoryListener(IHistoryEventListener l);
    public void removeHistoryListener(IHistoryEventListener l);
}
