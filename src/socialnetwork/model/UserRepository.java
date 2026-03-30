package socialnetwork.repository;

import socialnetwork.exception.UserNotFoundException;
import socialnetwork.model.FriendshipGraph;
import socialnetwork.model.User;

import java.util.Optional;

public class UserRepository {
    private final FriendshipGraph graph = new FriendshipGraph();

    public void save(User user) {
        graph.addUser(user);
    }

    public boolean exists(String name) {
        return graph.getAllUsers().stream()
                    .anyMatch(u -> u.getName().equalsIgnoreCase(name));
    }

    public User findByName(String name) {
        return graph.getAllUsers().stream()
                    .filter(u -> u.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow(() -> new UserNotFoundException(name));
    }

    public Optional<User> findByNameOptional(String name) {
        return graph.getAllUsers().stream()
                    .filter(u -> u.getName().equalsIgnoreCase(name))
                    .findFirst();
    }

    public FriendshipGraph getGraph() { return graph; }
}
