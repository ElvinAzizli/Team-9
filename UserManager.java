import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserManager {
    private Map<String, String> users = new HashMap<>();
    private final String filePath = "UserDataBase.csv";

    public UserManager() {
        loadUsersFromFile();
    }

    public boolean authenticate(String username, String password) {
        String correctPassword = users.get(username);
        return correctPassword != null && correctPassword.equals(password);
    }

    public void addUser(String username, String password) {
        users.put(username, password);
        saveUsersToFile();
    }

    public void removeUser(String username) {
        users.remove(username);
        saveUsersToFile();
    }

    private void loadUsersFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating new file: " + e.getMessage());
                return;
            }
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] userDetails = scanner.nextLine().split(",");
                if (userDetails.length == 2) {
                    users.put(userDetails[0], userDetails[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    private void saveUsersToFile() {
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            for (Map.Entry<String, String> user : users.entrySet()) {
                writer.println(user.getKey() + "," + user.getValue());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
