import java.util.HashMap;
import java.util.Map;

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
}
