package sandy.command;

import sandy.TaskList;
import sandy.Ui;
import sandy.storage.Storage;

/**
 * Ends the command loop.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
