package sokoban.model;

public class MoveMushroomCommand implements Command {
    private Board4Play board;
    private int oldRow, oldCol;
    private int newRow, newCol;

    public MoveMushroomCommand(Board4Play board, int oldRow, int oldCol, int newRow, int newCol) {
        this.board = board;
        this.oldRow = oldRow;
        this.oldCol = oldCol;
        this.newRow = newRow;
        this.newCol = newCol;
    }

    @Override
    public void execute() {
        board.moveMushroomTo(newRow, newCol);
    }

    @Override
    public void undo() { board.moveMushroomTo(oldRow, oldCol);}
}