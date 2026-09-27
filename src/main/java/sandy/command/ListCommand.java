package sandy.command;

import sandy.TaskList;
import sandy.Ui;
import sandy.storage.Storage;

/**
 * Displays all tasks in the task list.
 */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
