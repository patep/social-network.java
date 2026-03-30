package socialnetwork.exception;

public class AlreadyFriendsException extends RuntimeException {
    public AlreadyFriendsException(String a, String b) {
        super(a + " and " + b + " are already friends.");
    }
}
