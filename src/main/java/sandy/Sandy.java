package sandy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sandy.exception.SandyException;
import sandy.storage.Storage;
import sandy.task.Deadline;
import sandy.task.Event;
import sandy.task.Task;
import sandy.task.Todo;

/**
 * Starts Sandy and responds to commands entered by the user.
 */
public class Sandy {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String TODO_PREFIX = "todo ";
    private static final String DEADLINE_PREFIX = "deadline ";
    private static final String EVENT_PREFIX = "event ";
    private static final String MARK_PREFIX = "mark ";
    private static final String UNMARK_PREFIX = "unmark ";
    private static final String DELETE_PREFIX = "delete ";
    private static final String DEADLINE_BY_DELIMITER = " /by ";
    private static final String EVENT_FROM_DELIMITER = " /from ";
    private static final String EVENT_TO_DELIMITER = " /to ";
    private static final Path DATA_FILE_PATH = Path.of("data", "sandy.txt");
    private static final Storage STORAGE = new Storage(DATA_FILE_PATH);

    /**
     * Starts the Sandy command loop.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        printWelcomeMessage();

        List<Task> tasks = loadTasks();

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                try {
                    if (processCommand(command, tasks)) {
                        break;
                    }
                } catch (SandyException exception) {
                    System.out.println(" Oops! " + exception.getMessage());
                    System.out.println(SEPARATOR);
                }
            }
        }
    }

    /**
     * Prints the application's welcome message.
     */
    private static void printWelcomeMessage() {
        String banner = " ____                  _       \n"
                + "/ ___|  __ _ _ __   __| |_   _ \n"
                + "\\___ \\ / _` | '_ \\ / _` | | | |\n"
                + " ___) | (_| | | | | (_| | |_| |\n"
                + "|____/ \\__,_|_| |_|\\__,_|\\__, |\n"
                + "                         |___/ \n";
        System.out.println(SEPARATOR);
        System.out.print(banner);
        System.out.println("Hello! I'm Sandy.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
    }

    /**
     * Processes one command and reports whether the user requested an exit.
     *
     * @param command The command entered by the user.
     * @param tasks The tasks managed by the application.
     * @return {@code true} when the user exits, or {@code false} otherwise.
     * @throws SandyException If the command is invalid.
     */
    private static boolean processCommand(String command, List<Task> tasks) throws SandyException {
        System.out.println(SEPARATOR);

        if (command.equals("bye")) {
            System.out.println("Bye. Hope to see you again soon!");
            System.out.println(SEPARATOR);
            return true;
        }

        if (command.equals("list")) {
            printTaskList(tasks);
        } else if (command.startsWith(MARK_PREFIX)) {
            markTask(tasks, command);
            saveTasks(tasks);
        } else if (command.startsWith(UNMARK_PREFIX)) {
            unmarkTask(tasks, command);
            saveTasks(tasks);
        } else if (command.startsWith(DELETE_PREFIX)) {
            deleteTask(tasks, command);
            saveTasks(tasks);
        } else {
            addTask(command, tasks);
            saveTasks(tasks);
        }

        System.out.println(SEPARATOR);
        return false;
    }

    /**
     * Prints all tasks currently stored by the application.
     *
     * @param tasks The tasks managed by the application.
     */
    private static void printTaskList(List<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Marks the task selected by the command as completed.
     *
     * @param tasks The tasks managed by the application.
     * @param command The command containing the task number.
     */
    private static void markTask(List<Task> tasks, String command) throws SandyException {
        int taskNumber = parseTaskNumber(command.substring(MARK_PREFIX.length()));
        int taskIndex = taskNumber - 1;
        validateTaskIndex(taskIndex, tasks.size());
        Task task = tasks.get(taskIndex);
        task.markAsDone();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Marks the task selected by the command as incomplete.
     *
     * @param tasks The tasks managed by the application.
     * @param command The command containing the task number.
     */
    private static void unmarkTask(List<Task> tasks, String command) throws SandyException {
        int taskNumber = parseTaskNumber(command.substring(UNMARK_PREFIX.length()));
        int taskIndex = taskNumber - 1;
        validateTaskIndex(taskIndex, tasks.size());
        Task task = tasks.get(taskIndex);
        task.unmarkAsDone();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Deletes the task selected by the command.
     *
     * @param tasks The tasks managed by the application.
     * @param command The command containing the task number.
     * @throws SandyException If the task number is invalid or does not exist.
     */
    private static void deleteTask(List<Task> tasks, String command) throws SandyException {
        int taskNumber = parseTaskNumber(command.substring(DELETE_PREFIX.length()));
        int taskIndex = taskNumber - 1;
        validateTaskIndex(taskIndex, tasks.size());
        Task deletedTask = tasks.remove(taskIndex);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + deletedTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Creates, stores, and displays a task described by a command.
     *
     * @param command The command entered by the user.
     * @param tasks The tasks managed by the application.
     * @throws SandyException If the command does not describe a valid task.
     */
    private static void addTask(String command, List<Task> tasks) throws SandyException {
        boolean isTypedTaskCommand = isTypedTaskCommand(command);
        Task task = createTask(command);
        tasks.add(task);
        if (isTypedTaskCommand) {
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + task);
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        } else {
            System.out.println(" added: " + command);
        }
    }

    /**
     * Creates the task represented by a user command.
     *
     * @param command The command entered by the user.
     * @return The task described by the command.
     */
    private static Task createTask(String command) throws SandyException {
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

    private static String requireDescription(String text, String taskType) throws SandyException {
        if (text.trim().isEmpty()) {
            throw new SandyException("The description of a " + taskType + " cannot be empty.");
        }
        return text;
    }

    private static int parseTaskNumber(String text) throws SandyException {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException exception) {
            throw new SandyException("Please provide a valid task number.");
        }
    }

    private static void validateTaskIndex(int taskIndex, int taskCount) throws SandyException {
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new SandyException("That task number does not exist.");
        }
    }

    /**
     * Loads saved tasks without preventing the application from starting if loading fails.
     *
     * @return The saved tasks, or an empty list if loading fails.
     */
    private static List<Task> loadTasks() {
        try {
            return STORAGE.loadTasks();
        } catch (IOException | SandyException exception) {
            System.out.println(SEPARATOR);
            System.out.println(" Oops! I could not load saved tasks: " + exception.getMessage());
            System.out.println(SEPARATOR);
            return new ArrayList<>();
        }
    }

    /**
     * Saves all current tasks and reports an error without stopping the command loop.
     *
     * @param tasks The tasks managed by the application.
     */
    private static void saveTasks(List<Task> tasks) {
        try {
            STORAGE.saveTasks(tasks);
        } catch (IOException exception) {
            System.out.println(" Oops! I could not save your tasks.");
        }
    }

    /**
     * Returns whether the command creates a task with an explicit type.
     *
     * @param command The command entered by the user.
     * @return Whether the command begins with a supported task-type keyword.
     */
    private static boolean isTypedTaskCommand(String command) {
        return command.startsWith(TODO_PREFIX) || command.startsWith(DEADLINE_PREFIX)
                || command.startsWith(EVENT_PREFIX);
    }
}
