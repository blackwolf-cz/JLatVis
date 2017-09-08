package kandrm.JLatVis.lattice.editing.selection;

/**
 * Interface pro třídy reprezentující objekty, které mohou být vybrány uživatelem.
 *
 * @author Michal Kandr
 */
public interface ISelectable {

    /**
     * @return zda je uzel označen uživatelem
     */
    public boolean isSelected();

    /**
     * @param selected zda je uzel označen uživatelem
     */
    public void setSelected(boolean selected);
}
