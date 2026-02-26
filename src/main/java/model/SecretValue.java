package model;

public record SecretValue(String value) {

    @Override
    public String toString() {
        return "***";
    }

    // Prevent equals() from exposing the value in assertion failure messages
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecretValue other)) return false;
        return value != null && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return 0; // intentionally constant — don't leak via hash
    }
}