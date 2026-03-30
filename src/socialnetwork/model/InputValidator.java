package socialnetwork.util;

public class InputValidator {
    private InputValidator() {}

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() >= 2;
    }

    public static int readInt(java.util.Scanner sc, int min, int max) {
        while (true) {
            System.out.print("Choose (" + min + "-" + max + "): ");
            String line = sc.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) return val;
            } catch (NumberFormatException ignored) {}
            System.out.println("  Invalid input. Please enter a number.");
        }
    }
}
