package ie.game.fred.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ie.game.fred.model.C5Token;

@Service
public class BoardService {

	Logger logger = LoggerFactory.getLogger(BoardService.class);

	List<Stack<C5Token>> board = new ArrayList<>();

	static final int NUMBER_OF_COLUMN = 9;
	static final int NUMBER_OF_ROW = 6;

	private int turnCounter = 0;

	public BoardService() {
		super();
	}

	public boolean isPositionValid(int position) {
		boolean isplayerMoveValid = (position >= 0 && position < NUMBER_OF_COLUMN
				&& (getBoard().get(position).size() < NUMBER_OF_ROW));
		if (!isplayerMoveValid) {
			logger.info("last player move {} is invalid", position);
		}
		return isplayerMoveValid;
	}

	boolean isWinningMove(int lastMove) {
		boolean isWinnerMove = (isWinningFiveRow(lastMove) || isWinningFiveRowColumn(lastMove)
				|| isWiningFiveDiagonalRow(lastMove) || isWinningFiveBackwardRowDiagonal(lastMove));
		if (isWinnerMove) {
			logger.info("last move {} is a winner", lastMove);
		}
		return isWinnerMove;

	}

	boolean isDrawConditionMet() {
		boolean isDraw = turnCounter == (NUMBER_OF_COLUMN * NUMBER_OF_ROW);
		logger.info("last move {} has game in a draw ", turnCounter);
		return isDraw;
	}

	void updateCounter() {
		turnCounter = turnCounter + 1;
	}

	void addC5Token(C5Token token, int column) {
		getBoard().get(column).push(token);
		updateCounter();
	}

	boolean isWinningFiveRow(int lastMove) {

		var discCounter = 1;
		var isWiningMove = false;
		int row = board.get(lastMove).size() - 1;
		C5Token cPlayer = board.get(lastMove).peek();

		for (int i = lastMove - 1; i >= 0; i--) {
			Stack<C5Token> columnleft = board.get(i);

			if (columnleft.size() > row && columnleft.get(row).equals(cPlayer)) {
				discCounter++;
			} else {
				break;
			}
		}

		for (int i = lastMove + 1; i < NUMBER_OF_COLUMN; i++) {
			Stack<C5Token> columnRight = board.get(i);

			if (columnRight.size() > row && columnRight.get(row).equals(cPlayer)) {
				discCounter++;
			} else {
				break;
			}
		}

		if (discCounter > 4) {
			isWiningMove = true;
		}

		return isWiningMove;

	}

	boolean isWinningFiveRowColumn(int lastMove) {

		var isWiningMove = false;
		C5Token cPlayer = board.get(lastMove).peek();
		int row = board.get(lastMove).size() - 1;
		var discCounter = 1;

		for (int i = row - 1; i >= 0; i--) {

			if (board.get(lastMove).get(i).equals(cPlayer)) {
				discCounter++;
			} else {
				break;
			}
		}

		if (discCounter > 4) {
			isWiningMove = true;
		}

		return isWiningMove;

	}

	boolean isWiningFiveDiagonalRow(int lastMove) {

		var isWiningMove = false;
		C5Token cd = board.get(lastMove).peek();
		int currentRow = board.get(lastMove).size() - 1;
		int currentColumn = lastMove - 1;
		var discCounter = 1;

		for (int i = currentRow - 1; i >= 0 && currentColumn >= 0; i--) {

			Stack<C5Token> columnleft = board.get(currentColumn);

			if (columnleft.size() > i && columnleft.get(i).equals(cd)) {
				discCounter = discCounter + 1;
			}

			currentColumn = currentColumn - 1;

		}

		for (int i = currentRow + 1; i < NUMBER_OF_ROW; i++) {

			Stack<C5Token> columnRight = board.get(i);

			if (columnRight.size() > i && columnRight.get(i).equals(cd)) {
				discCounter = discCounter + 1;
			}

			currentColumn = currentColumn + 1;

		}

		if (discCounter > 4) {
			isWiningMove = true;
		}

		return isWiningMove;
	}

	boolean isWinningFiveBackwardRowDiagonal(int lastMove) {

		var isWiningMove = false;
		C5Token cPlayer = board.get(lastMove).peek();
		int currentRow = board.get(lastMove).size() - 1;
		int currentColumn = lastMove - 1;
		var discCounter = 1;

		for (int i = currentRow + 1; i < NUMBER_OF_ROW && currentColumn >= 0; i++) {
			Stack<C5Token> columnleft = board.get(currentColumn);
			if (columnleft.size() > i && columnleft.get(i).equals(cPlayer)) {
				discCounter = discCounter + 1;
			} else {
				break;
			}
			currentColumn = currentColumn - 1;
		}

		currentColumn = lastMove + 1;
		for (int i = currentRow - 1; i >= 0 && currentColumn < NUMBER_OF_COLUMN; i--) {
			Stack<C5Token> columnRight = board.get(currentColumn);
			if (columnRight.size() > i && columnRight.get(i).equals(cPlayer)) {
				discCounter = discCounter + 1;
			} else {
				break;
			}
			currentColumn = currentColumn + 1;
		}

		if (discCounter > 4) {
			isWiningMove = true;
		}

		return isWiningMove;
	}

	List<Stack<C5Token>> getBoard() {
		return board;
	}

	void setBoard(List<Stack<C5Token>> board) {
		this.board = board;
	}

	public void initialiseBoard() {
		for (var i = 0; i < NUMBER_OF_COLUMN; i++) {
			getBoard().add(new Stack<>());
		}
		logger.info("board initialised with List of {} Stacks Collection", NUMBER_OF_COLUMN);
	}

	public int getTurnCounter() {
		return turnCounter;
	}

}
