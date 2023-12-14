import javax.swing.*;
import java.awt.*;

public class MovieAppGUI {
    private MovieDatabase movieDatabase;
    private JFrame frame;
    private JTextField movieTitleField, movieDirectorField, movieYearField, movieTimeField;
    private JTextArea movieListArea;

    public MovieAppGUI() {
        movieDatabase = new MovieDatabase();
        createLoginWindow();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Classic Movie Catalog");
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

    private void createLoginWindow() {
        JFrame loginFrame = new JFrame("Login");
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> handleLogin(usernameField.getText(), new String(passwordField.getPassword()), loginFrame));

        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);

        loginFrame.getContentPane().add(panel);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private void handleLogin(String username, String password, JFrame loginFrame) {
        if (username.equals("admin") && password.equals("password")) {
            loginFrame.dispose();
            createAndShowGUI();
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Wrong username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
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
