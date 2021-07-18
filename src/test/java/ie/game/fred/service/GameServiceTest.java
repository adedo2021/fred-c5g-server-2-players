package ie.game.fred.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ie.game.fred.model.C5Token;

class GameServiceTest {

	@Test
	void givenTheBoardColumns_whenMovingDiscWithinContraint_thenTheMoveIsValid() {

		BoardService c5BoardService = new BoardService();
		GameService gameService = new GameService(c5BoardService);

		assertFalse(gameService.getBoardService().isPositionValid(-1));
		final int columns = GameService.NUMBER_OF_COLUMN;
		for (int i = 0; i < columns; i++) {
			assertTrue(gameService.getBoardService().isPositionValid(i));
		}

	}

	@Test
	void givenTheBoardGame_whenMovingDiscOutsideContraint_thenTheMoveIsInalid() {

		BoardService c5BoardService = new BoardService();
		GameService gameService = new GameService(c5BoardService);

		assertFalse(gameService.getBoardService().isPositionValid(-6));
		assertFalse(gameService.getBoardService().isPositionValid(-3));
		assertFalse(gameService.getBoardService().isPositionValid(20));

	}

	@Test
	void givenTheBoardColumns_whenAddingDiscInConsecutiveColumnPosition_thenTheMoveIsWinning() {

		BoardService c5BoardService = new BoardService();
		GameService gameService = new GameService(c5BoardService);

		gameService.play(C5Token.X, 0);
		gameService.play(C5Token.X, 1);
		gameService.play(C5Token.X, 2);
		gameService.play(C5Token.X, 3);
		gameService.play(C5Token.X, 4);
		assertTrue(gameService.getBoardService().isWinningMove(4));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

	}

	@Test
	void givenTheBoard_whenDiscsInRowOfFive_thenTheMoveIsWinning() {

		BoardService c5BoardService = new BoardService();
		GameService gameService = new GameService(c5BoardService);

		gameService.play(C5Token.X, 0);
		gameService.play(C5Token.X, 1);
		gameService.play(C5Token.X, 3);
		gameService.play(C5Token.X, 4);
		gameService.play(C5Token.X, 2);
		assertTrue(gameService.getBoardService().isWinningMove(2));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

	}

	@Test
	void givenTheBoardColumn_whenDiscsInColumnOfFive_thenTheMoveIsWinning() {

		BoardService c5BoardService = new BoardService();
		GameService gameService = new GameService(c5BoardService);

		gameService.play(C5Token.X, 0);
		gameService.play(C5Token.X, 0);
		gameService.play(C5Token.X, 0);
		gameService.play(C5Token.X, 0);
		gameService.play(C5Token.X, 0);
		assertThat(true, is(equalTo(gameService.getBoardService().isWinningMove(0))));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

	}

	@Test
	void givenTheBoardColumn_whenDiscsInRowOfFiveWithAnotherDisc_thenTheMoveIsStillWinning() {

		BoardService c5BoardService = new BoardService();
		GameService gameService = new GameService(c5BoardService);

		gameService.play(C5Token.O, 3);
		gameService.play(C5Token.X, 3);
		gameService.play(C5Token.X, 3);
		gameService.play(C5Token.X, 3);
		gameService.play(C5Token.X, 3);
		gameService.play(C5Token.X, 3);
		assertTrue(gameService.getBoardService().isWinningMove(3));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

	}

	@Test
	void givenTheBoard_whenDiscsInRowOfFiveInAnyScenario_thenTheMoveIsWinning() {

		BoardService c5BoardService = new BoardService();
		GameService gameService = new GameService(c5BoardService);

		gameService.play(C5Token.X, 0);
		gameService.play(C5Token.X, 0);
		gameService.play(C5Token.O, 0);
		gameService.play(C5Token.X, 0);
		gameService.play(C5Token.X, 0);

		gameService.play(C5Token.X, 2);
		gameService.play(C5Token.O, 2);
		gameService.play(C5Token.X, 2);
		gameService.play(C5Token.O, 2);
		assertFalse(gameService.getBoardService().isWinningMove(2));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

		gameService.play(C5Token.O, 3);
		gameService.play(C5Token.X, 3);
		gameService.play(C5Token.O, 3);
		assertFalse(gameService.getBoardService().isWinningMove(3));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

		gameService.play(C5Token.X, 4);
		assertFalse(gameService.getBoardService().isWinningMove(4));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

		gameService.play(C5Token.O, 1);
		gameService.play(C5Token.X, 1);
		gameService.play(C5Token.O, 1);
		gameService.play(C5Token.X, 1);
		assertTrue(gameService.getBoardService().isWinningMove(1));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

	}

	@Test
	void givenTheBoard_whenDiscsInRowOfFiveInDiagonalScenario_thenTheMoveIsWinning() {

		BoardService c5BoardService = new BoardService();
		GameService gameService = new GameService(c5BoardService);

		gameService.play(C5Token.X, 4);
		assertFalse(gameService.getBoardService().isWinningMove(4));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

		gameService.play(C5Token.O, 5);
		gameService.play(C5Token.X, 5);
		gameService.play(C5Token.O, 5);
		gameService.play(C5Token.X, 5);
		assertFalse(gameService.getBoardService().isWinningMove(5));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

		gameService.play(C5Token.X, 6);
		gameService.play(C5Token.O, 6);
		gameService.play(C5Token.X, 6);
		gameService.play(C5Token.O, 6);
		gameService.play(C5Token.X, 6);
		assertFalse(gameService.getBoardService().isWinningMove(6));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

		gameService.play(C5Token.O, 7);
		gameService.play(C5Token.O, 7);
		gameService.play(C5Token.X, 7);
		gameService.play(C5Token.X, 7);
		assertFalse(gameService.getBoardService().isWinningMove(7));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

		gameService.play(C5Token.X, 8);
		gameService.play(C5Token.O, 8);
		gameService.play(C5Token.X, 8);
		gameService.play(C5Token.O, 8);
		gameService.play(C5Token.X, 8);
		assertTrue(gameService.getBoardService().isWinningMove(8));
		assertFalse(gameService.getBoardService().isDrawConditionMet());

	}

	@Test
	void testDrawCondition() {

		BoardService c5BoardService = new BoardService();
		GameService gameService = new GameService(c5BoardService);

		for (int i = 0; i < 9; i++) {
			if (i % 2 == 0) {
				gameService.play(C5Token.X, i);
				gameService.play(C5Token.X, i);
				gameService.play(C5Token.O, i);
				gameService.play(C5Token.O, i);
				gameService.play(C5Token.X, i);
				gameService.play(C5Token.X, i);

			} else {
				gameService.play(C5Token.O, i);
				gameService.play(C5Token.O, i);
				gameService.play(C5Token.X, i);
				gameService.play(C5Token.X, i);
				gameService.play(C5Token.O, i);
				gameService.play(C5Token.O, i);
			}
		}

		assertTrue(gameService.getBoardService().isDrawConditionMet());

	}

	@Test
	void givenTheBoard_whenDiscsInValidPosition_thenTheMoveIsValid() {

		BoardService c5BoardService = new BoardService();
		GameService gameService = new GameService(c5BoardService);

		assertTrue(gameService.getBoardService().isPositionValid(0));
		assertTrue(gameService.getBoardService().isPositionValid(1));
		assertTrue(gameService.getBoardService().isPositionValid(2));
		assertTrue(gameService.getBoardService().isPositionValid(3));
		assertTrue(gameService.getBoardService().isPositionValid(4));
		assertTrue(gameService.getBoardService().isPositionValid(5));
		assertTrue(gameService.getBoardService().isPositionValid(6));
		assertTrue(gameService.getBoardService().isPositionValid(7));
		assertTrue(gameService.getBoardService().isPositionValid(8));

	}

	@Test
	void givenTheBoard_whenDiscsInInValidPosition_thenTheMoveIsInValid() {

		BoardService c5BoardService = new BoardService();
		GameService gameService = new GameService(c5BoardService);

		assertFalse(gameService.getBoardService().isPositionValid(9));
		assertFalse(gameService.getBoardService().isPositionValid(-1));
		assertFalse(gameService.getBoardService().isPositionValid(20));
		assertFalse(gameService.getBoardService().isPositionValid(30));
		assertFalse(gameService.getBoardService().isPositionValid(44));
		assertFalse(gameService.getBoardService().isPositionValid(54));
		assertFalse(gameService.getBoardService().isPositionValid(66));
		assertFalse(gameService.getBoardService().isPositionValid(77));
		assertFalse(gameService.getBoardService().isPositionValid(88));

	}

}
