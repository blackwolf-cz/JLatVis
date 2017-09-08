package kandrm.JLatVis.lattice.editing.search;

/**
 * Interface pro objekty ktere mohou byt prohledavany.
 * Umoznuje jejich oznacovani a odznacovani jako nalezenych
 *
 * @author Michal Kandr
 */
public interface IFoundabe {
    /**
     * @return zda byl prvek pri poslednim hledani nalezen
     */
    public boolean isFound();

    /**
     * @param found zda byl prvek pri poslednim hledani nalezen
     */
    public void setFound(boolean found);
}
