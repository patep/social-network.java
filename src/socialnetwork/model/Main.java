package socialnetwork;

import socialnetwork.exception.AlreadyFriendsException;
import socialnetwork.exception.UserNotFoundException;
import socialnetwork.model.User;
import socialnetwork.service.SocialNetworkService;
import socialnetwork.util.InputValidator;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final SocialNetworkService service = new SocialNetworkService();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=============================");
        System.out.println("  Welcome to SocialNet CLI  ");
        System.out.println("=============================");

        boolean running = true;
        while (running) {
            System.out.println("\n-- Main Menu --");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Exit");

            int choice = InputValidator.readInt(sc, 1, 3);
            switch (choice) {
                case 1 -> handleSignUp();
                case 2 -> { if (handleLogin()) showUserMenu(); }
                case 3 -> running = false;
            }
        }
        System.out.println("Goodbye!");
        sc.close();
    }

    private static void handleSignUp() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();
        if (!InputValidator.isValidName(name)) {
            System.out.println("  Name must be at least 2 characters.");
            return;
        }
        System.out.println(service.register(name)
            ? "  Signed up successfully as \"" + name + "\"."
            : "  That name is already taken.");
    }

    private static boolean handleLogin() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();
        if (service.login(name)) {
            System.out.println("  Logged in as \"" + name + "\".");
            return true;
        }
        System.out.println("  User not found. Please sign up first.");
        return false;
    }

    private static void showUserMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n-- User Menu [" + service.getLoggedInUser() + "] --");
            System.out.println("1. View Friends");
            System.out.println("2. Add Friend");
            System.out.println("3. View Top Users");
            System.out.println("4. Friend Recommendations");
            System.out.println("5. Degree of Separation");
            System.out.println("6. Log Out");

            int choice = InputValidator.readInt(sc, 1, 6);
            switch (choice) {
                case 1 -> viewFriends();
                case 2 -> addFriend();
                case 3 -> viewTopUsers();
                case 4 -> viewRecommendations();
                case 5 -> degreeOfSeparation();
                case 6 -> { service.logout(); active = false; }
            }
        }
    }

    private static void viewFriends() {
        Set<User> friends = service.getFriends();
        if (friends.isEmpty()) { System.out.println("  You have no friends yet."); return; }
        System.out.println("\n  Your friends:");
        System.out.println("  " + "-".repeat(25));
        friends.forEach(f -> System.out.println("   • " + f.getName()));
    }

    private static void addFriend() {
        System.out.print("  Enter user's name: ");
        String name = sc.nextLine().trim();
        try {
            service.addFriend(name);
            System.out.println("  You are now friends with \"" + name + "\".");
        } catch (UserNotFoundException e) {
            System.out.println("  User not found: " + name);
        } catch (AlreadyFriendsException e) {
            System.out.println("  " + e.getMessage());
        }
    }

    private static void viewTopUsers() {
        System.out.println("\n  Top users by friend count:");
        System.out.println("  " + "-".repeat(30));
        List<User> top = service.getTopUsers();
        for (int i = 0; i < top.size(); i++) {
            User u = top.get(i);
            int count = service.getFriendsOf(u);  // see note below
            System.out.printf("   %d. %-20s (%d friends)%n", i + 1, u.getName(), count);
        }
    }

    private static void viewRecommendations() {
        List<User> recs = service.getRecommendations();
        if (recs.isEmpty()) { System.out.println("  No recommendations yet. Add more friends!"); return; }
        System.out.println("\n  Recommended friends:");
        recs.forEach(u -> System.out.println("   • " + u.getName()));
    }

    private static void degreeOfSeparation() {
        System.out.print("  Check separation from: ");
        String name = sc.nextLine().trim();
        try {
            int deg = service.getDegreeOfSeparation(name);
            if (deg == 0)      System.out.println("  That's you!");
            else if (deg == 1) System.out.println("  \"" + name + "\" is your direct friend.");
            else if (deg == -1) System.out.println("  No connection found with \"" + name + "\".");
            else               System.out.println("  Degree of separation: " + deg);
        } catch (UserNotFoundException e) {
            System.out.println("  User not found: " + name);
        }
    }
}
