package User;

public interface UserWriteRepository {
    void save(User user);
    void update(User user);
    void delete(Long id);
}
