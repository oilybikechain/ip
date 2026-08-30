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
                int updatedTaskCount = processCommand(command, tasks, taskCount);
                if (updatedTaskCount == EXIT_REQUESTED) {
                    break;
                }
                taskCount = updatedTaskCount;
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
     */
    private static int processCommand(String command, Task[] tasks, int taskCount) {
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
    private static void markTask(Task[] tasks, String command) {
        int taskNumber = Integer.parseInt(command.substring(MARK_PREFIX.length()));
        int taskIndex = taskNumber - 1;
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
    private static void unmarkTask(Task[] tasks, String command) {
        int taskNumber = Integer.parseInt(command.substring(UNMARK_PREFIX.length()));
        int taskIndex = taskNumber - 1;
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
    private static int addTask(String command, Task[] tasks, int taskCount) {
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
    private static Task createTask(String command) {
        if (command.startsWith(TODO_PREFIX)) {
            return new Todo(command.substring(TODO_PREFIX.length()));
        }

        if (command.startsWith(DEADLINE_PREFIX)) {
            int byIndex = command.indexOf(DEADLINE_BY_DELIMITER);
            String description = command.substring(DEADLINE_PREFIX.length(), byIndex);
            String by = command.substring(byIndex + DEADLINE_BY_DELIMITER.length());
            return new Deadline(description, by);
        }

        if (command.startsWith(EVENT_PREFIX)) {
            int fromIndex = command.indexOf(EVENT_FROM_DELIMITER);
            int toIndex = command.indexOf(EVENT_TO_DELIMITER);
            String description = command.substring(EVENT_PREFIX.length(), fromIndex);
            String from = command.substring(fromIndex + EVENT_FROM_DELIMITER.length(), toIndex);
            String to = command.substring(toIndex + EVENT_TO_DELIMITER.length());
            return new Event(description, from, to);
        }

        return new Task(command);
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
