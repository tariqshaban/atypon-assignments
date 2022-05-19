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
                + TO_STRING_EXCEPTION_INFO_HOLDER.get(0)
                + isSuccess
                + TO_STRING_EXCEPTION_INFO_HOLDER.get(1)
                + reason;
    }
}