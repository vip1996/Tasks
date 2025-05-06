package User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbUserWriteRepository implements UserWriteRepository {
    private Connection connection;
    
    public DbUserWriteRepository() {
        try {
        	String url = "jdbc:sqlserver://localhost:1433;" +
                    "databaseName=NikasDB;" +
                    "user=test_user;" +
                    "password=1234;" +
                    "encrypt=true;" +
                    "trustServerCertificate=true;";
            
            connection = DriverManager.getConnection(url);
            createTableIfNotExists();
            System.out.println("მონაცემთა ბაზასთან კავშირი წარმატებით დამყარდა");
        } catch (SQLException e) {
            System.err.println("მონაცემთა ბაზასთან კავშირის დამყარების შეცდომა: " + e.getMessage());
        }
    }
    
    private void createTableIfNotExists() {
        try {
            boolean tableExists = false;
            ResultSet tables = connection.getMetaData().getTables(null, null, "users", null);
            if (tables.next()) {
                tableExists = true;
                System.out.println("'users' ცხრილი უკვე არსებობს");
            }
            
            if (!tableExists) {
                String createTableSQL = "CREATE TABLE users (" +
                        "id BIGINT PRIMARY KEY," +
                        "name VARCHAR(255) NOT NULL," +
                        "email VARCHAR(255) NOT NULL" +
                        ")";
                
                try (Statement statement = connection.createStatement()) {
                    statement.execute(createTableSQL);
                    System.out.println("'users' ცხრილი წარმატებით შეიქმნა");
                }
            }
        } catch (SQLException e) {
            System.err.println("ცხრილის შექმნის შეცდომა: " + e.getMessage());
        }
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("მომხმარებელი წარმატებით დაემატა: " + user);
            }
        } catch (SQLException e) {
            System.err.println("მომხმარებლის დამატების შეცდომა: " + e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET name=?, email=? WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setLong(3, user.getId());
            
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("მომხმარებლის ინფორმაცია განახლდა წარმატებით: " + user);
            } else {
                System.out.println("მომხმარებელი ID-ით " + user.getId() + " ვერ მოიძებნა");
            }
        } catch (SQLException e) {
            System.err.println("მომხმარებლის განახლების შეცდომა: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("მომხმარებელი ID-ით " + id + " წარმატებით წაიშალა");
            } else {
                System.out.println("მომხმარებელი ID-ით " + id + " ვერ მოიძებნა");
            }
        } catch (SQLException e) {
            System.err.println("მომხმარებლის წაშლის შეცდომა: " + e.getMessage());
        }
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("მონაცემთა ბაზასთან კავშირი დახურულია");
            }
        } catch (SQLException e) {
            System.err.println("მონაცემთა ბაზის დახურვის შეცდომა: " + e.getMessage());
        }
    }
}