import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MovieAppGUI {
    private MovieDatabase movieDatabase;
    private UserManager userManager;
    private WatchlistManager watchlistManager;
    private JFrame frame;
    private JTable movieTable;
    private DefaultTableModel movieTableModel;
    private String loggedInUser;
    private boolean isAdmin;

    public MovieAppGUI() {
        movieDatabase = new MovieDatabase();
        userManager = new UserManager();
        watchlistManager = new WatchlistManager();
        initializeMovieTable();
        showLoginScreen();
    }

    private void initializeMovieTable() {
        movieTableModel = new DefaultTableModel(new String[]{"Title", "Director", "Year", "Running Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        movieTable = new JTable(movieTableModel);
        movieTable.setFillsViewportHeight(true);
        movieTable.getTableHeader().setReorderingAllowed(false);
        movieTable.setRowSelectionAllowed(false);
    }

    private void showLoginScreen() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            if (userManager.authenticate(usernameField.getText(), new String(passwordField.getPassword()))) {
                loggedInUser = usernameField.getText();
                isAdmin = userManager.isAdmin(loggedInUser);
                frame.dispose();
                createAndShowGUI();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        constraints.insets = new Insets(10, 0, 5, 0);
        frame.add(new JLabel("Username:"), constraints);
        frame.add(usernameField, constraints);
        frame.add(new JLabel("Password:"), constraints);
        frame.add(passwordField, constraints);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 0, 0, 0);
        frame.add(loginButton, constraints);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createAndShowGUI() {
        frame = new JFrame("Movie Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        JPanel inputPanel = new JPanel(new FlowLayout());
    
        if (isAdmin) {
            inputPanel.add(createButton("Add New Movie", e -> addMovieDialog()));
            inputPanel.add(createButton("Edit Movie", e -> editMovieDialog()));
            inputPanel.add(createButton("Delete Movie", e -> deleteMovieDialog()));
            inputPanel.add(createButton("New User", e -> newUserDialog()));
            inputPanel.add(createButton("Delete User", e -> deleteUserDialog()));
        }
    
        inputPanel.add(createButton("Manage Watchlist", e -> manageWatchlistDialog()));
        inputPanel.add(createButton("Rearrange Columns", e -> rearrangeColumnsDialog()));
        inputPanel.add(createButton("Sort Columns", e -> sortColumnsDialog()));
    
        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(movieTable), BorderLayout.CENTER);
        updateMovieListArea();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton createButton(String title, ActionListener actionListener) {
        JButton button = new JButton(title);
        button.addActionListener(actionListener);
        return button;
    }

    private void addMovieDialog() {
        JTextField titleField = new JTextField(20);
        JTextField directorField = new JTextField(20);
        JTextField yearField = new JTextField(20);
        JTextField timeField = new JTextField(20);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Director:"));
        panel.add(directorField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Running Time:"));
        panel.add(timeField);
        int result = JOptionPane.showConfirmDialog(frame, panel, "Add New Movie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String director = directorField.getText();
                int year = Integer.parseInt(yearField.getText());
                int time = Integer.parseInt(timeField.getText());
                movieDatabase.addMovie(new Movie(title, director, year, time));
                updateMovieListArea();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input: Year and Running Time must be numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editMovieDialog() {
        String movieTitle = JOptionPane.showInputDialog(frame, "Enter the title of the movie to edit:");
        if (movieTitle != null) {
            Movie movieToEdit = movieDatabase.getMovie(movieTitle);
            if (movieToEdit != null) {
                JTextField titleField = new JTextField(movieToEdit.getTitle(), 20);
                JTextField directorField = new JTextField(movieToEdit.getDirector(), 20);
                JTextField yearField = new JTextField(String.valueOf(movieToEdit.getReleaseYear()), 20);
                JTextField timeField = new JTextField(String.valueOf(movieToEdit.getRunningTime()), 20);
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Title:"));
                panel.add(titleField);
                panel.add(new JLabel("Director:"));
                panel.add(directorField);
                panel.add(new JLabel("Year:"));
                panel.add(yearField);
                panel.add(new JLabel("Running Time:"));
                panel.add(timeField);
                int result = JOptionPane.showConfirmDialog(frame, panel, "Edit Movie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        movieDatabase.updateMovie(movieToEdit, new Movie(titleField.getText(), directorField.getText(), Integer.parseInt(yearField.getText()), Integer.parseInt(timeField.getText())));
                        updateMovieListArea();
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Year and Time must be numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Movie not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteMovieDialog() {
        String movieTitle = JOptionPane.showInputDialog(frame, "Enter the title of the movie to delete:");
        if (movieTitle != null) {
            Movie movie = movieDatabase.getMovie(movieTitle);
            if (movie != null) {
                int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete \"" + movieTitle + "\"?", "Delete Movie", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    movieDatabase.removeMovie(movieTitle);
                    updateMovieListArea();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Movie not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void newUserDialog() {
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        int result = JOptionPane.showConfirmDialog(frame, panel, "Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            userManager.addUser(username, password);
        }
    }

    private void deleteUserDialog() {
        String username = JOptionPane.showInputDialog(frame, "Enter the username of the user to delete:");
        userManager.removeUser(username);
    }

    private void manageWatchlistDialog() {
        List<String> watchlist = watchlistManager.getWatchlist(loggedInUser);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JComboBox<String> movieDropdown = new JComboBox<>();
        for (Movie movie : movieDatabase.getAllMoviesAsList()) {
            if (!watchlist.contains(movie.getTitle())) {
                movieDropdown.addItem(movie.getTitle());
            }
        }
        JButton addButton = new JButton("Add to Watchlist");
        addButton.addActionListener(e -> {
            String selectedMovie = (String) movieDropdown.getSelectedItem();
            if (selectedMovie != null) {
                watchlistManager.addMovieToWatchlist(loggedInUser, selectedMovie);
            }
        });

        JList<String> watchlistDisplay = new JList<>(watchlist.toArray(new String[0]));

        JScrollPane listScrollPane = new JScrollPane(watchlistDisplay);
        listScrollPane.setPreferredSize(new Dimension(300, 200));

        panel.add(new JLabel("Add Movie to Watchlist:"));
        panel.add(movieDropdown);
        panel.add(addButton);
        panel.add(new JLabel("Your Watchlist:"));
        panel.add(listScrollPane);
        JButton removeButton = new JButton("Remove from Watchlist");
        removeButton.addActionListener(e -> {
            String selectedMovie = watchlistDisplay.getSelectedValue();
            if (selectedMovie != null) {
                watchlistManager.removeMovieFromWatchlist(loggedInUser, selectedMovie);
                watchlist.remove(selectedMovie);
                watchlistDisplay.setListData(watchlist.toArray(new String[0]));
            }
        });
        panel.add(removeButton);

        JOptionPane.showMessageDialog(frame, panel, "Manage Watchlist", JOptionPane.PLAIN_MESSAGE);
    }

    private void updateMovieListArea() {
        movieTableModel.setRowCount(0);
        for (Movie movie : movieDatabase.getAllMoviesAsList()) {
            movieTableModel.addRow(new Object[]{movie.getTitle(), movie.getDirector(), movie.getReleaseYear(), movie.getRunningTime()});
        }
    }

    private void rearrangeColumnsDialog() {
        String[] columnNames = {"Title", "Director", "Year", "Running Time"};
        JComboBox<String> columnBox1 = new JComboBox<>(columnNames);
        JComboBox<String> columnBox2 = new JComboBox<>(columnNames);
        JComboBox<String> columnBox3 = new JComboBox<>(columnNames);
        JComboBox<String> columnBox4 = new JComboBox<>(columnNames);
    
        columnBox1.setSelectedIndex(-1);
        columnBox2.setSelectedIndex(-1);
        columnBox3.setSelectedIndex(-1);
        columnBox4.setSelectedIndex(-1);
    
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select new order for columns"));
        panel.add(new JLabel("First Column"));
        panel.add(columnBox1);
        panel.add(new JLabel("Second Column"));
        panel.add(columnBox2);
        panel.add(new JLabel("Third Column"));
        panel.add(columnBox3);
        panel.add(new JLabel("Fourth Column"));
        panel.add(columnBox4);
    
        int result = JOptionPane.showConfirmDialog(frame, panel, "Rearrange Columns", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            rearrangeColumns(columnBox1, columnBox2, columnBox3, columnBox4);
        }
    }
    
    private void rearrangeColumns(JComboBox<String> columnBox1, JComboBox<String> columnBox2, JComboBox<String> columnBox3, JComboBox<String> columnBox4) {
        TableColumn[] columns = new TableColumn[4];
        columns[0] = movieTable.getColumnModel().getColumn(getColumnIndex(columnBox1.getSelectedItem().toString()));
        columns[1] = movieTable.getColumnModel().getColumn(getColumnIndex(columnBox2.getSelectedItem().toString()));
        columns[2] = movieTable.getColumnModel().getColumn(getColumnIndex(columnBox3.getSelectedItem().toString()));
        columns[3] = movieTable.getColumnModel().getColumn(getColumnIndex(columnBox4.getSelectedItem().toString()));
    
        for (int i = 0; i < columns.length; i++) {
            movieTable.getColumnModel().moveColumn(movieTable.convertColumnIndexToView(columns[i].getModelIndex()), i);
        }
    }    

    private int getColumnIndex(String columnName) {
        switch (columnName) {
            case "Title": return 0;
            case "Director": return 1;
            case "Year": return 2;
            case "Running Time": return 3;
            default: return -1;
        }
    }

    private void sortColumnsDialog() {
        String[] columnNames = {"Title", "Director", "Year", "Running Time"};
        JComboBox<String> columnDropdown = new JComboBox<>(columnNames);
        String[] sortOptions = {"Ascending", "Descending"};
        JComboBox<String> sortOrderDropdown = new JComboBox<>(sortOptions);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Column to Sort:"));
        panel.add(columnDropdown);
        panel.add(new JLabel("Sort Order:"));
        panel.add(sortOrderDropdown);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Sort Columns", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            sortTable(columnDropdown.getSelectedItem().toString(), sortOrderDropdown.getSelectedItem().toString().equals("Ascending"));
        }
    }

    private void sortTable(String column, boolean isAscending) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(movieTableModel);
        movieTable.setRowSorter(sorter);
    
        int columnIndex = getColumnIndex(column);
        sorter.setComparator(columnIndex, getComparatorForColumn(column));
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(columnIndex, isAscending ? SortOrder.ASCENDING : SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
    }

    private Comparator<?> getComparatorForColumn(String column) {
        if ("Year".equals(column) || "Running Time".equals(column)) {
            return new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int num1 = o1.isEmpty() ? 0 : Integer.parseInt(o1.trim());
                    int num2 = o2.isEmpty() ? 0 : Integer.parseInt(o2.trim());
                    return Integer.compare(num1, num2);
                }
            };
        } else {
            return Comparator.naturalOrder();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieAppGUI());
    }
}
