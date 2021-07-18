package ie.game.fred.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import ie.game.fred.model.C5Token;

class BoardServiceTest {

	@Test
	void givenBoardService_whenPlayBoard_thenCounterIncremented() {
		BoardService c5BoardService = new BoardService();
		c5BoardService.initialiseBoard();

		c5BoardService.addC5Token(C5Token.X, 0);
		assertThat(true, is(c5BoardService.isPositionValid(0)));
		assertThat(false, is(c5BoardService.isWiningFiveDiagonalRow(0)));
		assertThat(false, is(c5BoardService.isWinningFiveBackwardRowDiagonal(0)));
		assertThat(false, is(c5BoardService.isWinningFiveRow(0)));
		assertThat(false, is(c5BoardService.isWinningMove(0)));
		assertThat(false, is(c5BoardService.isWinningFiveRowColumn(0)));
		assertThat(false, is(c5BoardService.isDrawConditionMet()));
		assertThat(1, is(equalTo(c5BoardService.getTurnCounter())));
	}
}
