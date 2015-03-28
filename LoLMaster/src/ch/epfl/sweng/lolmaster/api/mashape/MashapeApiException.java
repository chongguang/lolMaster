package ch.epfl.sweng.lolmaster.api.mashape;

import ch.epfl.sweng.lolmaster.api.ApiException;

/**
 * @author fKunstner
 */
public class MashapeApiException extends ApiException {

    private static final long serialVersionUID = -2804925851283969772L;

    private final MashapeError mError;

    public MashapeApiException(MashapeError error) {
        super(error.getErrorMessage());
        this.mError = error;
    }

    public MashapeApiException(MashapeError error, Exception e) {
        super(error.getErrorMessage(), e);
        this.mError = error;
    }

    public MashapeError getError() {
        return this.mError;
    }

    /**
     * Mashape know error enum
     * 
     * @author fKunstner
     */
    public enum MashapeError {
        UNKNOWN_ERROR(0, "The error is unkown"),
        BAD_REQUEST(400, "Bad request"),
        FORBIDDEN(403, "Forbidden"),
        DATA_NOT_FOUND(404, "Requested data not found"),
        UNAUTHORIZED(401, "Unauthorized"),
        RATE_LIMITED(429, "Rate limit exceeded"),
        SERVER_ERROR(500, "Internal server error"),
        UNAVAILABLE(503, "Service unavaiable"),
        PARSE_FAILURE(600, "Failed to parse JSON response"),
        OK(200, "OK"),
        NO_GAME_FOUND(601, "No game found");

        private int mErrorCode;
        private String mErrorMessage;

        private MashapeError(int errorCode, String errorMessage) {
            mErrorCode = errorCode;
            mErrorMessage = errorMessage;
        }

        public int getCode() {
            return this.mErrorCode;
        }

        public String getErrorMessage() {
            return this.mErrorMessage;
        }

        @Override
        public String toString() {
            return "[" + this.getClass().getName() + "." + this.name() + " "
                + mErrorMessage + "(" + mErrorCode + ")]";
        }

        public static MashapeError getMashapeError(int errorCode) {
            for (MashapeError ec : MashapeError.values()) {
                if (ec.getCode() == errorCode) {
                    return ec;
                }
            }
            return MashapeError.UNKNOWN_ERROR;
        }
    }
}