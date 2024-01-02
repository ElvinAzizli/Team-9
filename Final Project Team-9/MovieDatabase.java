import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class MovieDatabase {
    private Map<String, Movie> movies;
    private final String filePath = "DataBase.csv";

    public MovieDatabase() {
        this.movies = new HashMap<>();
        loadMoviesFromCSV();
    }

    public void addMovie(Movie movie) {
        movies.put(movie.getTitle(), movie);
        saveMoviesToCSV();
    }

    public void removeMovie(String title) {
        movies.remove(title);
        saveMoviesToCSV();
    }

    public Movie getMovie(String title) {
        return movies.get(title);
    }

    public List<Movie> getAllMoviesAsList() {
        return new ArrayList<>(movies.values());
    }

    public void updateMovie(Movie oldMovie, Movie newMovie) {
        if (oldMovie != null && movies.containsKey(oldMovie.getTitle())) {
            movies.put(oldMovie.getTitle(), newMovie);
            saveMoviesToCSV();
        }
    }

    private void loadMoviesFromCSV() {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] details = line.split(",");
                if (details.length == 4) {
                    try {
                        addMovie(new Movie(details[0], details[1], Integer.parseInt(details[2].trim()), Integer.parseInt(details[3].trim())));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException ignored) {
        }
    }

    private void saveMoviesToCSV() {
        try {
            PrintWriter writer = new PrintWriter(new File(filePath));
            writer.println("Title,Director,ReleaseYear,RunningTime");
            for (Movie movie : movies.values()) {
                writer.println(movie.getTitle() + "," + movie.getDirector() + "," + movie.getReleaseYear() + "," + movie.getRunningTime());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
