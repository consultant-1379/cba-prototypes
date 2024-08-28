/**
 * Created by ecaseal on 06/02/2015.
 */
public class RequestInformation {
    String operation;
    String message;
    int bytesToRead;

    public RequestInformation(final String operation, final String message, final int bytesToRead) {
        this.operation = operation;
        this.message = message;
        this.bytesToRead = bytesToRead;
    }
}

