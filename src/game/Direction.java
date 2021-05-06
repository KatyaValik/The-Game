package game;

public enum Direction {
    FORWARD(1),
    BACK(-1),
    NO(0),
    CONTINUE(1);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
