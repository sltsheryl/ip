package kirby;

import kirby.commands.Command;
import kirby.commands.DeadlineCommand;
import kirby.commands.DeleteCommand;
import kirby.commands.EventCommand;
import kirby.commands.ExitCommand;
import kirby.commands.FindCommand;
import kirby.commands.GetCommand;
import kirby.commands.HelpCommand;
import kirby.commands.MarkCommand;
import kirby.commands.ShowListCommand;
import kirby.commands.ToDoCommand;
import kirby.commands.UnmarkCommand;
import kirby.exceptions.KirbyInvalidArgumentException;
import kirby.exceptions.KirbyInvalidCommandException;
import kirby.exceptions.KirbyMissingArgumentException;
import kirby.exceptions.KirbyOutOfRangeException;

/**
 * Parser class handles reading user input and then translating it to command instances.
 */
public class Parser {
    /**
     * Returns the name of a Command based on users' input.
     *
     * @param inputString the entire input by the user.
     * @return a String representing command name.
     */
    public static String getCommandName(String inputString) {
        if (inputString.split(" ").length == 1) {
            return inputString;
        }
        return inputString.substring(0, inputString.indexOf(" "));
    }

    /**
     * Returns the placeholder of a Command based on users' input.
     *
     * @param taskType the type of task input by the user.
     * @return placeholder word.
     */
    public static String getPlaceholder(String taskType) {
        if (taskType.equals("event")) {
            return "/at";
        } else if (taskType.equals("deadline")) {
            return "/by";
        }
        return null;
    }

    /**
     * Returns the arguments of a Command based on users' input.
     *
     * @param inputString the entire input by the user.
     * @return an array of Strings of arguments.
     */
    public static String[] getArguments(String inputString, String taskType) {
        String taskName = null;
        String taskTime = null;
        int beginIndex = inputString.indexOf(taskType) + taskType.length() + 1;
        String placeholder = getPlaceholder(taskType);
        assert placeholder != null;
        int afterPlaceholderIndex = inputString.indexOf(placeholder) + placeholder.length() + 1;
        boolean taskNameExists = beginIndex < inputString.indexOf(placeholder);
        boolean timeExists = inputString.length() - 1 >= afterPlaceholderIndex;
        if (taskNameExists && timeExists) {
            taskName = inputString.substring(beginIndex,
                    inputString.indexOf(placeholder) - 1);
            taskTime = inputString.substring(afterPlaceholderIndex);
        }
        return new String[] {taskName, taskTime};
    }

    /**
     * Returns the argument of a Command based on users' input.
     *
     * @param inputString the entire input by the user.
     * @return an array of Strings of arguments.
     */
    static String getArgument(String inputString) {
        if (inputString.split(" ").length == 1) {
            return null;
        }
        return inputString.substring(inputString.indexOf(" ") + 1);
    }

    /**
     * Returns the specific type of valid Command based on users' input.
     *
     * @param inputString the entire input by the user.
     * @return a Command instance based on what type of Command is entered.
     * @throws KirbyInvalidCommandException if the command entered is undefined.
     * @throws KirbyMissingArgumentException if the command entered is defined but missing arguments.
     */
    public static Command parseValidCommand(String inputString, TaskList tasks)
            throws KirbyInvalidCommandException, KirbyMissingArgumentException, KirbyOutOfRangeException,
            KirbyInvalidArgumentException {

        String commandString = Parser.getCommandName(inputString);
        String[] arguments;
        String argument = Parser.getArgument(inputString);

        switch (commandString) {
        case "todo":
            return new ToDoCommand(argument);

        case "event":
            arguments = Parser.getArguments(inputString, "event");
            return new EventCommand(arguments);

        case "deadline":
            arguments = Parser.getArguments(inputString, "deadline");
            return new DeadlineCommand(arguments);

        case "mark":
            return new MarkCommand(argument, tasks);

        case "unmark":
            return new UnmarkCommand(argument, tasks);

        case "delete":
            return new DeleteCommand(argument, tasks);

        case "bye":
            return new ExitCommand();

        case "list":
            return new ShowListCommand();

        case "get":
            return new GetCommand(argument);

        case "find":
            return new FindCommand(argument);

        case "help":
            return new HelpCommand();

        default:
            throw new KirbyInvalidCommandException();
        }
    }

    /**
     * Returns a valid Command based on users' input if possible.
     * Else return InvalidCommand.
     *
     * @param inputString the entire input by the user.
     * @return a Command instance based on users' input.
     */
    public static Command parse(String inputString, TaskList tasks) {
        try {
            return Parser.parseValidCommand(inputString, tasks);
        } catch (KirbyInvalidCommandException | KirbyMissingArgumentException
                | KirbyOutOfRangeException | KirbyInvalidArgumentException exception) {
            return new kirby.commands.InvalidCommand(exception);
        }
    }
}
