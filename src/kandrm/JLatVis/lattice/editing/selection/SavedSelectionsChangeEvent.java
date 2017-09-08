package kandrm.JLatVis.lattice.editing.selection;

/**
 *
 * @author Michal Kandr
 */
public class SavedSelectionsChangeEvent {
    public enum Type { Add, Delete }

    private Type type;
    private int startIndex;
    private int endIndex;

    public SavedSelectionsChangeEvent(Type type, int startIndex, int endIndex) {
        this.type = type;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public SavedSelectionsChangeEvent(Type type, int startIndex){
        this(type, startIndex, startIndex+1);
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public Type getType() {
        return type;
    }
}
