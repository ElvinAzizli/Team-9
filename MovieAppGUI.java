import javax.swing.*;
import java.awt.*;

public class MovieAppGUI {
    private MovieDatabase movieDatabase;
    private JFrame frame;
    private JTextField movieTitleField, movieDirectorField, movieYearField, movieTimeField, usernameField;
    private JPasswordField passwordField;
    private JTextArea movieListArea;

    public MovieAppGUI() {
        movieDatabase = new MovieDatabase();
        showLoginScreen();
    }

    private void showLoginScreen() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            if (authenticate(usernameField.getText(), new String(passwordField.getPassword()))) {
                frame.dispose();
                populateSampleData();
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
        constraints.insets = new Insets(10, 0, 10, 0);
        frame.add(loginButton, constraints);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean authenticate(String username, String password) {
        return "admin".equals(username) && "password".equals(password);
    }

    private void populateSampleData() {
        movieDatabase.loadMoviesFromCSV("DataBase.csv");
    }

    private void createAndShowGUI() {
        frame = new JFrame("Movie Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        movieTitleField = new JTextField(20);
        movieDirectorField = new JTextField(20);
        movieYearField = new JTextField(20);
        movieTimeField = new JTextField(20);
        JButton addButton = new JButton("Add New Movie");
        movieListArea = new JTextArea(10, 40);
        movieListArea.setEditable(false);

        addButton.addActionListener(e -> addMovie());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(movieTitleField);
        inputPanel.add(new JLabel("Director:"));
        inputPanel.add(movieDirectorField);
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(movieYearField);
        inputPanel.add(new JLabel("Running Time:"));
        inputPanel.add(movieTimeField);
        inputPanel.add(addButton);
        inputPanel.setLayout(new FlowLayout());

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(movieListArea), BorderLayout.CENTER);

        updateMovieListArea();
        frame.setVisible(true);
    }

    private void addMovie() {
        String title = movieTitleField.getText();
        String director = movieDirectorField.getText();
        int year;
        int time;
        try {
            year = Integer.parseInt(movieYearField.getText());
            time = Integer.parseInt(movieTimeField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Year and Time must be numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Movie newMovie = new Movie(title, director, year, time);
        movieDatabase.addMovie(newMovie);
        updateMovieListArea();
    }

    private void updateMovieListArea() {
        movieListArea.setText(movieDatabase.getAllMovies());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieAppGUI());
    }
}
