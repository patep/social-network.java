package socialnetwork.model;

import java.util.Objects;

public class User {
    private final String name;

    public User(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Username cannot be empty.");
        this.name = name.trim();
    }

    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return name.equalsIgnoreCase(((User) o).name);
    }

    @Override
    public int hashCode() { return Objects.hash(name.toLowerCase()); }

    @Override
    public String toString() { return name; }
}
