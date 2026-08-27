import java.util.Scanner;

/**
 * Starts Sandy and responds to commands entered by the user.
 */
public class Sandy {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    /**
     * Prints the initial greeting, stores entered tasks, lists stored tasks,
     * and ends the conversation when the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used
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

        String[] tasks = new String[MAX_TASKS];
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
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks[i]);
                    }
                } else {
                    tasks[taskCount] = command;
                    taskCount++;
                    System.out.println(" added: " + command);
                }

                System.out.println(SEPARATOR);
            }
        }
    }
}
