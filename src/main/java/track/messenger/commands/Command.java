package track.messenger.commands;

import track.messenger.messages.Message;
import track.messenger.net.Session;

/**
 * Created by Oskar on 11.04.17.
 */
public abstract class Command {





    // @param session - текущая сессия
    protected Session session;

    /**
     * Реализация паттерна Команда. Метод execute() вызывает соответствующую реализацию,
     * для запуска команды нужна сессия, чтобы можно было сгенерить ответ клиенту и провести валидацию
     * сессии.
     * @param message - сообщение для обработки
     * @throws CommandException - все исключения перебрасываются как CommandException
     */
    public abstract void execute(Message message) throws CommandException;
}
