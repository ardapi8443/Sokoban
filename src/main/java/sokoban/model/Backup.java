package sokoban.model;

public class Backup {

    private final String state;

    public Backup(String currentState) {
        this.state = currentState;
    }
    public String getState() {
        return this.state;
    }
}
