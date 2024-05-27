package sokoban.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExchangePosBoxCommand implements Command {
	private final Board4Play board4Play;
	private final List<int [][]> newPositions;
	private final List<int [][]> oldPositions;

	public ExchangePosBoxCommand(Board4Play board4Play, List<int [][]> oldPositions, List<int [][]> newPositions) {
		this.board4Play = board4Play;
		this.oldPositions = oldPositions;
		this.newPositions = newPositions;

	}

	@Override
	public void execute() {
		board4Play.setMoves(board4Play.getMovesProperty().get() + 1); // Chaque redo d'un déplacement "coûte" 1 mouvements
		board4Play.redoBox(newPositions);
		board4Play.MustSetLabelProperty().set(true);
		board4Play.MustSetLabelProperty().set(false);
	}

	@Override
	public void undo() {
		board4Play.setMoves(board4Play.getMovesProperty().get() + 5); // Chaque annulation d'un déplacement "coûte" 5 mouvements
		board4Play.redoBox(oldPositions);
		board4Play.MustSetLabelProperty().set(true);
		board4Play.MustSetLabelProperty().set(false);
	}
}