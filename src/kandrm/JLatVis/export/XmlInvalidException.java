package kandrm.JLatVis.export;

/**
 *
 * @author Michal Kandr
 */
public class XmlInvalidException extends Exception {
    public XmlInvalidException() {
	super();
    }

    public XmlInvalidException(String message) {
	super(message);
    }

    public XmlInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlInvalidException(Throwable cause) {
        super(cause);
    }
}
