package track.messenger.commands;

import track.messenger.messages.Message;
import track.messenger.net.Session;

/**
 * Created by Oskar on 11.04.17.
 */
public class LoginCmd implements Command {
    @Override
    public void execute(Session session, Message message) throws CommandException {

    }
}

// what server should do with text message? send to all users or just save in db?  //send to all
// message for each command? // yep
// need command register? //yep
// bean for client? // yep, use spring bean
// architecture ok? (nio instead of sockets)
// is select.register thread safe?
// если логин не указан, то авторизоваться <- что это значит? в чём разница между залогиниться и авторизоваться?