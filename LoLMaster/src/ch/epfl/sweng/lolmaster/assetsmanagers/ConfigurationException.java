package ch.epfl.sweng.lolmaster.assetsmanagers;

/**
 * Thrown to indicate that the configuration of the program makes it impossible
 * to operate.
 * 
 * @author fKunstner
 */
public class ConfigurationException extends RuntimeException {
    private static final long serialVersionUID = 3730437812980232020L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String arg0) {
        super(arg0);
    }

    public ConfigurationException(Throwable arg0) {
        super(arg0);
    }

    public ConfigurationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
