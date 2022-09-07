package kirby.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import kirby.Storage;
import kirby.TaskList;
import kirby.exceptions.KirbyMissingArgumentException;
import kirby.ui.Ui;

public class TaskTest {
    private Storage storage;
    private Ui ui;
    private TaskList taskList;
    public TaskTest() throws IOException {
        this.storage = new Storage("data/blank.txt", "data/");
        this.ui = new Ui();
        taskList = new TaskList(null);
    }

    @Test
    public void checkNoArguments() {
        try {
            new DeadlineCommand("").execute(taskList, ui, storage);
        } catch (KirbyMissingArgumentException e) {
            String expectedOutput = "Aaaaaaah, the description of a deadline is invalid! Try again!";
            assertEquals(expectedOutput, e.getMessage());
        }
    }
}
