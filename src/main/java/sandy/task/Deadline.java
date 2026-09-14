package sandy.task;

/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete deadline with a description and deadline text.
     *
     * @param description Text describing the deadline.
     * @param by Text describing when the deadline is due.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    /**
     * Returns this deadline in the user-interface format.
     *
     * @return The task type, completion status, description, and deadline.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
