import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class WatchlistManager {
    private Map<String, List<String>> userWatchlists;
    private final String filePath = "Watchlist.csv";

    public WatchlistManager() {
        userWatchlists = new HashMap<>();
        loadWatchlistsFromFile();
    }

    public List<String> getWatchlist(String username) {
        return userWatchlists.getOrDefault(username, new ArrayList<>());
    }

    public void addMovieToWatchlist(String username, String movieTitle) {
        userWatchlists.computeIfAbsent(username, k -> new ArrayList<>()).add(movieTitle);
        saveWatchlistsToFile();
    }

    public void removeMovieFromWatchlist(String username, String movieTitle) {
        List<String> watchlist = userWatchlists.get(username);
        if (watchlist != null) {
            watchlist.remove(movieTitle);
            saveWatchlistsToFile();
        }
    }

    private void loadWatchlistsFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String username = parts[0];
                    List<String> movies = Arrays.asList(parts[1].split(","));
                    userWatchlists.put(username, new ArrayList<>(movies));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading watchlist file: " + e.getMessage());
        }
    }

    private void saveWatchlistsToFile() {
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            for (Map.Entry<String, List<String>> entry : userWatchlists.entrySet()) {
                String username = entry.getKey();
                String movies = String.join(",", entry.getValue());
                writer.println(username + ":" + movies);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error writing to watchlist file: " + e.getMessage());
        }
    }
}
