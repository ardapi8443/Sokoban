package sokoban.model;

public interface Command {
	void execute();
	void undo();
}

