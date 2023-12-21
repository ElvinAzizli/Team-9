import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MovieDatabase {
    private Map<String, Movie> movies;

    public MovieDatabase() {
        this.movies = new HashMap<>();
    }

    public void addMovie(Movie movie) {
        movies.putIfAbsent(movie.getTitle(), movie);
    }

    public void removeMovie(String title) {
        movies.remove(title);
    }

    public Movie getMovie(String title) {
        return movies.get(title);
    }

    public String getAllMovies() {
        StringBuilder sb = new StringBuilder();
        for (Movie movie : movies.values()) {
            sb.append(movie.getTitle()).append(" - ")
              .append(movie.getDirector()).append(" - ")
              .append(movie.getReleaseYear()).append(" - ")
              .append(movie.getRunningTime()).append("min\n");
        }
        return sb.toString();
    }

    public void loadMoviesFromCSV(String filePath) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            boolean isFirstLine = true;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

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

    public void saveMoviesToCSV(String filePath) {
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

    public void updateMovie(Movie oldMovie, Movie newMovie) {
        movies.remove(oldMovie.getTitle());
        movies.put(newMovie.getTitle(), newMovie);
    }
}



