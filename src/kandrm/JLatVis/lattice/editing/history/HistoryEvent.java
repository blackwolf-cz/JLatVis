package kandrm.JLatVis.lattice.editing.history;

import java.util.EventObject;

/**
 * Událost historie generovaná při jakékoli příležitosti, jež má být uložena do historie
 * a má být možno ji vracet.
 *
 * @author Michal Kandr
 */
public abstract class HistoryEvent extends EventObject {
    /**
     * Nová událost historie
     * @param source objekt, ve kterém k události došlo
     */
    protected HistoryEvent(Object source){
        super(source);
    }

    /**
     * Vrácení operace - obnovení stavu, jenž byl před provedením operace.
     */
    public abstract void reverse();

    /**
     * Opětovné provedení operace.
     */
    public abstract void doAgain();
}
