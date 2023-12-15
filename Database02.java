import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MovieDatabase {
    private Map<String, Movie> movies;

    public MovieDatabase() {
        this.movies = new HashMap<>();
        loadMoviesFromCSV("DataBase.csv");
    }

    private void loadMoviesFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] movieData = line.split(",");
                String title = movieData[0].trim();
                String director = movieData[1].trim();
                int releaseYear = Integer.parseInt(movieData[2].trim());
                int runningTime = Integer.parseInt(movieData[3].trim());
                addMovie(new Movie(title, director, releaseYear, runningTime));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMovie(Movie movie) {
        movies.putIfAbsent(movie.getTitle(), movie);
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
}
