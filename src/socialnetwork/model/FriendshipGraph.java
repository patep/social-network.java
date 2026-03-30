package socialnetwork.model;

import java.util.*;

public class FriendshipGraph {
    private final Map<User, Set<User>> adjacencyList = new HashMap<>();

    public void addUser(User user) {
        adjacencyList.putIfAbsent(user, new HashSet<>());
    }

    public boolean hasUser(User user) {
        return adjacencyList.containsKey(user);
    }

    public void addFriendship(User a, User b) {
        adjacencyList.get(a).add(b);
        adjacencyList.get(b).add(a);
    }

    public boolean areFriends(User a, User b) {
        return adjacencyList.getOrDefault(a, Collections.emptySet()).contains(b);
    }

    public Set<User> getFriends(User user) {
        return Collections.unmodifiableSet(
            adjacencyList.getOrDefault(user, Collections.emptySet())
        );
    }

    public Set<User> getAllUsers() {
        return Collections.unmodifiableSet(adjacencyList.keySet());
    }
}
