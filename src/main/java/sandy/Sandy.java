package sandy;

import java.util.Scanner;

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
    private static final String DEADLINE_BY_DELIMITER = " /by ";
    private static final String EVENT_FROM_DELIMITER = " /from ";
    private static final String EVENT_TO_DELIMITER = " /to ";
    private static final int MAX_TASKS = 100;
    private static final int EXIT_REQUESTED = -1;

    /**
     * Starts the Sandy command loop.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        printWelcomeMessage();

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                try {
                    int updatedTaskCount = processCommand(command, tasks, taskCount);
                    if (updatedTaskCount == EXIT_REQUESTED) {
                        break;
                    }
                    taskCount = updatedTaskCount;
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
     * Processes one command and returns the resulting number of tasks.
     *
     * @param command The command entered by the user.
     * @param tasks The tasks managed by the application.
     * @param taskCount The number of tasks currently stored.
     * @return The updated task count, or {@value #EXIT_REQUESTED} when the user exits.
     * @throws SandyException If the command is invalid.
     */
    private static int processCommand(String command, Task[] tasks, int taskCount) throws SandyException {
        System.out.println(SEPARATOR);

        if (command.equals("bye")) {
            System.out.println("Bye. Hope to see you again soon!");
            System.out.println(SEPARATOR);
            return EXIT_REQUESTED;
        }

        if (command.equals("list")) {
            printTaskList(tasks, taskCount);
        } else if (command.startsWith(MARK_PREFIX)) {
            markTask(tasks, command);
        } else if (command.startsWith(UNMARK_PREFIX)) {
            unmarkTask(tasks, command);
        } else {
            taskCount = addTask(command, tasks, taskCount);
        }

        System.out.println(SEPARATOR);
        return taskCount;
    }

    /**
     * Prints all tasks currently stored by the application.
     *
     * @param tasks The tasks managed by the application.
     * @param taskCount The number of tasks currently stored.
     */
    private static void printTaskList(Task[] tasks, int taskCount) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + "." + tasks[i]);
        }
    }

    /**
     * Marks the task selected by the command as completed.
     *
     * @param tasks The tasks managed by the application.
     * @param command The command containing the task number.
     */
    private static void markTask(Task[] tasks, String command) throws SandyException {
        int taskNumber = parseTaskNumber(command.substring(MARK_PREFIX.length()));
        int taskIndex = taskNumber - 1;
        validateTaskIndex(taskIndex, taskCount(tasks));
        tasks[taskIndex].markAsDone();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + tasks[taskIndex]);
    }

    /**
     * Marks the task selected by the command as incomplete.
     *
     * @param tasks The tasks managed by the application.
     * @param command The command containing the task number.
     */
    private static void unmarkTask(Task[] tasks, String command) throws SandyException {
        int taskNumber = parseTaskNumber(command.substring(UNMARK_PREFIX.length()));
        int taskIndex = taskNumber - 1;
        validateTaskIndex(taskIndex, taskCount(tasks));
        tasks[taskIndex].unmarkAsDone();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + tasks[taskIndex]);
    }

    /**
     * Creates, stores, and displays a task described by a command.
     *
     * @param command The command entered by the user.
     * @param tasks The tasks managed by the application.
     * @param taskCount The number of tasks currently stored.
     * @return The number of tasks after adding the new task.
     */
    private static int addTask(String command, Task[] tasks, int taskCount) throws SandyException {
        if (taskCount == MAX_TASKS) {
            throw new SandyException("Your task list is full.");
        }
        boolean isTypedTaskCommand = isTypedTaskCommand(command);
        tasks[taskCount] = createTask(command);
        int updatedTaskCount = taskCount + 1;
        if (isTypedTaskCommand) {
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + tasks[taskCount]);
            System.out.println(" Now you have " + updatedTaskCount + " tasks in the list.");
        } else {
            System.out.println(" added: " + command);
        }
        return updatedTaskCount;
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

    private static int taskCount(Task[] tasks) {
        int count = 0;
        while (count < tasks.length && tasks[count] != null) {
            count++;
        }
        return count;
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
