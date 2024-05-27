package sokoban.model;

import javafx.scene.control.skin.TextInputControlSkin;

public class MovePlayerCommand implements Command {
	private final Board4Play board4Play;
	private final Backup preMoveBackup;
	private final TextInputControlSkin.Direction direction;

	public MovePlayerCommand(Board4Play board4Play, TextInputControlSkin.Direction direction) {
		this.board4Play = board4Play;
		this.direction = direction;
		this.preMoveBackup = new Backup(board4Play.phrase());
	}

	@Override
	public void execute() {
	 	board4Play.setMoves(board4Play.getMovesProperty().get() + 1); // Chaque redo d'un déplacement "coûte" 1 mouvements
		board4Play.MovePlayerGrid(direction);
	}

	@Override
	public void undo() {
		board4Play.setMoves(board4Play.getMovesProperty().get() + 5); // Chaque annulation d'un déplacement "coûte" 5 mouvements
		board4Play.restoreState(preMoveBackup);
	}
}