import javax.swing.*;
import java.awt.*;

public class MovieAppGUI {
    private MovieDatabase movieDatabase;
    private JFrame frame;
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

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

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
        constraints.insets = new Insets(10, 0, 0, 0);
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

        JButton addButton = new JButton("Add New Movie");
        movieListArea = new JTextArea(10, 40);
        movieListArea.setEditable(false);

        addButton.addActionListener(e -> addMovieDialog());

        JPanel inputPanel = new JPanel();
        inputPanel.add(addButton);
        inputPanel.setLayout(new FlowLayout());

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(movieListArea), BorderLayout.CENTER);

        updateMovieListArea();
        frame.setVisible(true);
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

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add a New Movie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String director = directorField.getText();
                int year = Integer.parseInt(yearField.getText());
                int time = Integer.parseInt(timeField.getText());

                Movie newMovie = new Movie(title, director, year, time);
                movieDatabase.addMovie(newMovie);
                updateMovieListArea();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Year and Time must be numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateMovieListArea() {
        movieListArea.setText(movieDatabase.getAllMovies());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieAppGUI());
    }
}
