import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import javax.swing.*;

public class MovieAppGUITest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private MovieDatabase movieDatabase;
    private UserManager userManager;
    private WatchlistManager watchlistManager;

    @Override
    protected void onSetUp() {
        movieDatabase = new MovieDatabase();
        userManager = new UserManager();
        watchlistManager = new WatchlistManager();

        MovieAppGUI movieAppGUI = GuiActionRunner.execute(() -> new MovieAppGUI(movieDatabase, userManager, watchlistManager));
        window = new FrameFixture(robot(), movieAppGUI);
        window.show();
    }

    @Test
    public void testAddMovie() {
        window.button("addMovieButton").click();
        window.dialog().textBox("titleField").enterText("New Movie Title");
        window.dialog().textBox("directorField").enterText("New Director");
        window.dialog().textBox("yearField").enterText("2021");
        window.dialog().textBox("runningTimeField").enterText("120");
        window.dialog().button("addButton").click();
        window.table("movieTable").requireRowCount(1);
    }

    @Test
    public void testEditMovie() {
        window.table("movieTable").selectRow(0);
        window.button("editMovieButton").click();
        window.dialog().textBox("directorField").setText("Updated Director");
        window.dialog().button("editButton").click();
        window.table("movieTable").cell(row(0).column("Director")).requireValue("Updated Director");
    }

    @Test
    public void testDeleteMovie() {
        window.table("movieTable").selectRow(0);
        window.button("deleteMovieButton").click();
        window.dialog().button("confirmDeleteButton").click();
        window.table("movieTable").requireRowCount(0);
    }

    @Test
    public void testNewUser() {
        window.button("newUserButton").click();
        window.dialog().textBox("usernameField").enterText("newUser");
        window.dialog().textBox("passwordField").enterText("password");
        window.dialog().button("addUserButton").click();
    }

    @Test
    public void testDeleteUser() {
        window.button("deleteUserButton").click();
        window.dialog().list("userList").selectItem("userToBeDeleted");
        window.dialog().button("confirmDeleteButton").click();
    }

    @Test
    public void testManageWatchlist() {
        window.button("watchlistButton").click();
        window.dialog().list("movieList").selectItem("movieToWatchlist");
        window.dialog().button("addToWatchlistButton").click();
    }

    @Test
    public void testRearrangeColumns() {
        window.button("rearrangeColumnsButton").click();
        window.dialog().comboBox("columnOrderBox").selectItem("Title");
        window.dialog().button("rearrangeButton").click();
    }

    @Test
    public void testSortColumns() {
        window.button("sortColumnsButton").click();
        window.dialog().comboBox("sortColumnBox").selectItem("Year");
        window.dialog().comboBox("sortOrderBox").selectItem("Ascending");
        window.dialog().button("sortButton").click();
    }

    @Override
protected void onTearDown() {
    window.cleanUp();
  }
}
