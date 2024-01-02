import javax.swing.*;
import java.util.List;

public class ManageWatchlistDialog extends JDialog {
    private final WatchlistManager watchlistManager;
    private final MovieDatabase movieDatabase;
    private final String loggedInUser;
    private JComboBox<String> movieDropdown;
    private JList<String> watchlistDisplay;
    private DefaultListModel<String> watchlistModel;

    public ManageWatchlistDialog(JFrame parent, WatchlistManager watchlistManager, MovieDatabase movieDatabase, String loggedInUser) {
        super(parent, "Manage Watchlist", true);
        this.watchlistManager = watchlistManager;
        this.movieDatabase = movieDatabase;
        this.loggedInUser = loggedInUser;

        initializeComponents();
        loadWatchlist();
        setupLayout();
    }

    private void initializeComponents() {
        movieDropdown = new JComboBox<>();
        watchlistModel = new DefaultListModel<>();
        watchlistDisplay = new JList<>(watchlistModel);
        JButton addButton = new JButton("Add to Watchlist");
        addButton.addActionListener(e -> addToWatchlist());
        JButton removeButton = new JButton("Remove from Watchlist");
        removeButton.addActionListener(e -> removeFromWatchlist());

        for (Movie movie : movieDatabase.getAllMoviesAsList()) {
            movieDropdown.addItem(movie.getTitle());
        }
    }

    private void loadWatchlist() {
        List<String> watchlist = watchlistManager.getWatchlist(loggedInUser);
        watchlistModel.clear();
        for (String movieTitle : watchlist) {
            watchlistModel.addElement(movieTitle);
        }
    }

    private void setupLayout() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Add Movie to Watchlist:"));
        add(movieDropdown);
        add(new JButton("Add to Watchlist"));
        add(new JLabel("Your Watchlist:"));
        add(new JScrollPane(watchlistDisplay));
        add(new JButton("Remove from Watchlist"));
        pack();
        setLocationRelativeTo(getParent());
    }

    private void addToWatchlist() {
        String selectedMovie = (String) movieDropdown.getSelectedItem();
        if (selectedMovie != null && !watchlistModel.contains(selectedMovie)) {
            watchlistManager.addMovieToWatchlist(loggedInUser, selectedMovie);
            watchlistModel.addElement(selectedMovie);
        }
    }

    private void removeFromWatchlist() {
        String selectedMovie = watchlistDisplay.getSelectedValue();
        if (selectedMovie != null) {
            watchlistManager.removeMovieFromWatchlist(loggedInUser, selectedMovie);
            watchlistModel.removeElement(selectedMovie);
        }
    }
}
