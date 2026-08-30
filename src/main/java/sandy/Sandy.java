package sandy;

import java.util.Scanner;

/**
 * Starts Sandy and responds to commands entered by the user.
 */
public class Sandy {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    /**
     * Starts the Sandy command loop.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
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

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                System.out.println(SEPARATOR);

                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(SEPARATOR);
                    break;
                } else if (command.equals("list")) {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }
                } else if (command.startsWith("mark ")) {
                    int taskNumber = Integer.parseInt(command.substring(5));
                    int taskIndex = taskNumber - 1;
                    tasks[taskIndex].markAsDone();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[taskIndex]);
                } else if (command.startsWith("unmark ")) {
                    int taskNumber = Integer.parseInt(command.substring(7));
                    int taskIndex = taskNumber - 1;
                    tasks[taskIndex].unmarkAsDone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[taskIndex]);
                } else {
                    boolean isTypedTaskCommand = isTypedTaskCommand(command);
                    tasks[taskCount] = createTask(command);
                    taskCount++;
                    if (isTypedTaskCommand) {
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + tasks[taskCount - 1]);
                        System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    } else {
                        System.out.println(" added: " + command);
                    }
                }

                System.out.println(SEPARATOR);
            }
        }
    }

    /**
     * Creates the task represented by a user command.
     *
     * @param command The command entered by the user.
     * @return The task described by the command.
     */
    private static Task createTask(String command) {
        if (command.startsWith("todo ")) {
            return new Todo(command.substring(5));
        }

        if (command.startsWith("deadline ")) {
            int byIndex = command.indexOf(" /by ");
            String description = command.substring(9, byIndex);
            String by = command.substring(byIndex + 5);
            return new Deadline(description, by);
        }

        if (command.startsWith("event ")) {
            int fromIndex = command.indexOf(" /from ");
            int toIndex = command.indexOf(" /to ");
            String description = command.substring(6, fromIndex);
            String from = command.substring(fromIndex + 7, toIndex);
            String to = command.substring(toIndex + 5);
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
        return command.startsWith("todo ") || command.startsWith("deadline ") || command.startsWith("event ");
    }
}
