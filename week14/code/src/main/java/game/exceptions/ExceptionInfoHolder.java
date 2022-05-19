package game.exceptions;

import static game.assets.StringValues.*;

public class ExceptionInfoHolder {
    private final boolean isSuccess;
    private final String reason;

    public ExceptionInfoHolder(boolean isSuccess, String reason) {
        this.isSuccess = isSuccess;
        this.reason = reason;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + translate("TO_STRING_EXCEPTION_INFO_HOLDER",0)
                + isSuccess
                + translate("TO_STRING_EXCEPTION_INFO_HOLDER",1)
                + reason;
    }
}