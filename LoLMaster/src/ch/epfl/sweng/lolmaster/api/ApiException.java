package ch.epfl.sweng.lolmaster.api;

import main.java.riotapi.RiotApiException;
import ch.epfl.sweng.lolmaster.api.mashape.MashapeApiException;

/**
 * @author fKunstner
 */
public class ApiException extends Exception {

    private static final long serialVersionUID = 5118421643273424195L;

    public ApiException(RiotApiException e) {
        super(e);
    }

    public ApiException(MashapeApiException e) {
        super(e);
    }

    public ApiException(String string) {
        super(string);
    }

    public ApiException(String string, Exception e) {
        super(string, e);
    }
}
