package sandy.command;

import sandy.TaskList;
import sandy.Ui;
import sandy.exception.SandyException;
import sandy.storage.Storage;
import sandy.task.Task;

/**
 * Marks a task as complete.
 */
public class MarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that marks the given task number.
     *
     * @param taskNumber The one-based task number.
     */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SandyException {
        Task task = tasks.getTask(taskNumber);
        task.markAsDone();
        ui.showMarkedTask(task);
        saveTasks(tasks, ui, storage);
    }
}
