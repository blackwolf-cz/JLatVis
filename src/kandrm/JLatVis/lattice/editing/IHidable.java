package kandrm.JLatVis.lattice.editing;

/**
 *
 * @author Michal Kandr
 */
public interface IHidable {
    public void setVisible(boolean visible, boolean saveHistory);
    public void setVisible(boolean visible);
    public boolean isVisible();
}
