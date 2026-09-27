package sandy;

import sandy.command.AddCommand;
import sandy.command.Command;
import sandy.command.DeleteCommand;
import sandy.command.ExitCommand;
import sandy.command.ListCommand;
import sandy.command.MarkCommand;
import sandy.command.UnmarkCommand;
import sandy.exception.SandyException;
import sandy.task.Deadline;
import sandy.task.Event;
import sandy.task.Task;
import sandy.task.Todo;

/**
 * Interprets user input and creates tasks from task commands.
 */
public final class Parser {
    private static final String TODO_PREFIX = "todo ";
    private static final String DEADLINE_PREFIX = "deadline ";
    private static final String EVENT_PREFIX = "event ";
    private static final String DEADLINE_BY_DELIMITER = " /by ";
    private static final String EVENT_FROM_DELIMITER = " /from ";
    private static final String EVENT_TO_DELIMITER = " /to ";
    private static final String MARK_PREFIX = "mark ";
    private static final String UNMARK_PREFIX = "unmark ";
    private static final String DELETE_PREFIX = "delete ";

    private Parser() {
    }

    /**
     * Converts a complete user command into an executable command object.
     *
     * @param command The command entered by the user.
     * @return The operation represented by the command.
     * @throws SandyException If the command is invalid.
     */
    public static Command parse(String command) throws SandyException {
        if (command.equals("bye")) {
            return new ExitCommand();
        }
        if (command.equals("list")) {
            return new ListCommand();
        }
        if (command.startsWith(MARK_PREFIX)) {
            return new MarkCommand(parseTaskNumber(command.substring(MARK_PREFIX.length())));
        }
        if (command.startsWith(UNMARK_PREFIX)) {
            return new UnmarkCommand(parseTaskNumber(command.substring(UNMARK_PREFIX.length())));
        }
        if (command.startsWith(DELETE_PREFIX)) {
            return new DeleteCommand(parseTaskNumber(command.substring(DELETE_PREFIX.length())));
        }
        return new AddCommand(parseTask(command));
    }

    private static Task parseTask(String command) throws SandyException {
        if (command.equals("todo") || command.startsWith(TODO_PREFIX)) {
            String description = command.equals("todo") ? "" : command.substring(TODO_PREFIX.length());
            return new Todo(requireDescription(description, "todo"));
        }

        if (command.startsWith(DEADLINE_PREFIX)) {
            int byIndex = command.indexOf(DEADLINE_BY_DELIMITER);
            if (byIndex < DEADLINE_PREFIX.length()) {
                throw new SandyException("A deadline must have a description and a /by value.");
            }
            String description = command.substring(DEADLINE_PREFIX.length(), byIndex);
            String by = command.substring(byIndex + DEADLINE_BY_DELIMITER.length());
            requireDescription(description, "deadline");
            requireDescription(by, "deadline");
            return new Deadline(description, by);
        }

        if (command.startsWith(EVENT_PREFIX)) {
            int fromIndex = command.indexOf(EVENT_FROM_DELIMITER);
            int toIndex = command.indexOf(EVENT_TO_DELIMITER);
            if (fromIndex < EVENT_PREFIX.length() || toIndex < fromIndex) {
                throw new SandyException("An event must have a description, /from value, and /to value.");
            }
            String description = command.substring(EVENT_PREFIX.length(), fromIndex);
            String from = command.substring(fromIndex + EVENT_FROM_DELIMITER.length(), toIndex);
            String to = command.substring(toIndex + EVENT_TO_DELIMITER.length());
            requireDescription(description, "event");
            requireDescription(from, "event");
            requireDescription(to, "event");
            return new Event(description, from, to);
        }

        throw new SandyException("I do not recognize that command.");
    }

    /**
     * Parses the task number included in a command.
     *
     * @param text The text containing the task number.
     * @return The parsed task number.
     * @throws SandyException If the text is not a valid integer.
     */
    private static int parseTaskNumber(String text) throws SandyException {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException exception) {
            throw new SandyException("Please provide a valid task number.");
        }
    }

    private static String requireDescription(String text, String taskType) throws SandyException {
        if (text.trim().isEmpty()) {
            throw new SandyException("The description of a " + taskType + " cannot be empty.");
        }
        return text;
    }
}
