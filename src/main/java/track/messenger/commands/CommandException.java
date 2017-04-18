package track.messenger.commands;

/**
 * Created by Oskar on 11.04.17.
 */
public class CommandException extends Exception {
    public CommandException(String msg) {
        super(msg);
    }

    public CommandException(Throwable ex) {
        super(ex);
    }
}
