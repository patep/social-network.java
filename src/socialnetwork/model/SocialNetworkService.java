package socialnetwork.service;

import socialnetwork.exception.AlreadyFriendsException;
import socialnetwork.exception.UserNotFoundException;
import socialnetwork.model.FriendshipGraph;
import socialnetwork.model.User;
import socialnetwork.repository.UserRepository;

import java.util.*;

public class SocialNetworkService {
    private final UserRepository repo = new UserRepository();
    private User loggedInUser;

    public SocialNetworkService() { seedData(); }

    // ── Auth ───────────────────────────────────────────────
    public boolean register(String name) {
        if (repo.exists(name)) return false;
        repo.save(new User(name));
        return true;
    }

    public boolean login(String name) {
        repo.findByNameOptional(name).ifPresent(u -> loggedInUser = u);
        return loggedInUser != null && loggedInUser.getName().equalsIgnoreCase(name);
    }

    public void logout() { loggedInUser = null; }

    public User getLoggedInUser() { return loggedInUser; }

    // ── Friends ────────────────────────────────────────────
    public void addFriend(String targetName) {
        requireLogin();
        User target = repo.findByName(targetName);         // throws if not found
        FriendshipGraph graph = repo.getGraph();
        if (graph.areFriends(loggedInUser, target))
            throw new AlreadyFriendsException(loggedInUser.getName(), target.getName());
        graph.addFriendship(loggedInUser, target);
    }

    public Set<User> getFriends() {
        requireLogin();
        return repo.getGraph().getFriends(loggedInUser);
    }

    // ── Top users (by friend count) ────────────────────────
    public List<User> getTopUsers() {
        FriendshipGraph graph = repo.getGraph();
        List<User> users = new ArrayList<>(graph.getAllUsers());
        users.sort((a, b) -> Integer.compare(
            graph.getFriends(b).size(),
            graph.getFriends(a).size()
        ));
        return users;
    }

    // ── Recommendations (friends-of-friends) ──────────────
    public List<User> getRecommendations() {
        requireLogin();
        FriendshipGraph graph = repo.getGraph();
        Set<User> directFriends = graph.getFriends(loggedInUser);
        Map<User, Integer> score = new HashMap<>();

        for (User friend : directFriends) {
            for (User fof : graph.getFriends(friend)) {
                if (!fof.equals(loggedInUser) && !directFriends.contains(fof)) {
                    score.merge(fof, 1, Integer::sum);
                }
            }
        }

        List<User> result = new ArrayList<>(score.keySet());
        result.sort((a, b) -> Integer.compare(score.get(b), score.get(a)));
        return result;
    }

    // ── Degree of separation (BFS) ─────────────────────────
    public int getDegreeOfSeparation(String targetName) {
        requireLogin();
        User target = repo.findByName(targetName);
        if (loggedInUser.equals(target)) return 0;

        FriendshipGraph graph = repo.getGraph();
        Set<User> visited = new HashSet<>();
        Queue<User> queue = new LinkedList<>();
        Map<User, Integer> distance = new HashMap<>();

        queue.add(loggedInUser);
        visited.add(loggedInUser);
        distance.put(loggedInUser, 0);

        while (!queue.isEmpty()) {
            User current = queue.poll();
            for (User neighbor : graph.getFriends(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    distance.put(neighbor, distance.get(current) + 1);
                    if (neighbor.equals(target)) return distance.get(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return -1; // Not connected
    }

    // ── Seed data ──────────────────────────────────────────
    private void seedData() {
        String[] names = {"Alice Johnson","Bob Smith","Charlie Brown","David Williams","Eva Miller"};
        for (String n : names) repo.save(new User(n));

        FriendshipGraph g = repo.getGraph();
        connect(g, "Alice Johnson",   "Bob Smith");
        connect(g, "Alice Johnson",   "Charlie Brown");
        connect(g, "Bob Smith",       "David Williams");
        connect(g, "Charlie Brown",   "Eva Miller");
        connect(g, "David Williams",  "Eva Miller");
    }

    private void connect(FriendshipGraph g, String a, String b) {
        g.addFriendship(repo.findByName(a), repo.findByName(b));
    }

    private void requireLogin() {
        if (loggedInUser == null)
            throw new IllegalStateException("No user is logged in.");
    }
}
