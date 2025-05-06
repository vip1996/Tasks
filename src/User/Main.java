package User;

public class Main {
    public static void main(String[] args) {
        
        try {            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC დრაივერი ვერ მოიძებნა: " + e.getMessage());
            return;
        }
        
        DbUserWriteRepository repository = new DbUserWriteRepository();
        
        // ახალი მომხმარებლის დამატება
        User user1 = new User(1L, "გიორგი", "giorgi@example.com");
        repository.save(user1);
        
        User user2 = new User(2L, "ნინო", "nino@example.com");
        repository.save(user2);
        
        // მომხმარებლის ინფორმაციის განახლება
        user1.setEmail("giorgi.updated@example.com");
        repository.update(user1);
        
        // მომხმარებლის წაშლა
        repository.delete(2L);
        
        // მონაცემთა ბაზის კავშირის დახურვა
        repository.close();
    }
}