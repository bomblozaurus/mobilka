package teamE.users.exceptions;

public class EmailAlreadyUsedException extends UserException {
    public EmailAlreadyUsedException() {
        super();
    }

    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}
