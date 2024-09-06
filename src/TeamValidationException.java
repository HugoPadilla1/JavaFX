/*
Exception: TeamValidationException
This exception is utilized for each attribute that is apart of the Team Object.
This catches and supers the specific exception handling for each attribute.
 */

public class TeamValidationException extends Exception {
    public TeamValidationException(String message) {
        super(message);
    }
}
