package ie.game.fred.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import ie.game.fred.model.C5Token;
import ie.game.fred.model.Player;
import ie.game.fred.model.ServerResponse;
import ie.game.fred.service.BoardService;
import ie.game.fred.service.GameService;
import ie.game.fred.service.PlayersContainer;

class GameControllerTest {

	@Test
	void givenPlayerAdd_whenPlayerAdded_thenResponsecontainsPlayerData() {

		PlayersContainer.emptyPlayersList();
		BoardService c5BoardService = new BoardService();
		GameService c5GameService = new GameService(c5BoardService);
		GameController c5Controller = new GameController(c5GameService);

		HttpSession session = new MockHttpSession();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		ResponseEntity<String> result = c5Controller.addPlayer("Fred", request);

		System.out.println("\n  ");
		System.out.println(HttpStatus.OK);
		System.out.println(result.getStatusCode());
		System.out.println(c5GameService.getPlayersContainerSize());
		System.out.println(HttpStatus.OK.equals(result.getStatusCode()));

		assertThat(HttpStatus.OK, is(equalTo(result.getStatusCode())));

		ResponseEntity<ServerResponse> response = c5Controller.getStatus(request);
		assertThat(1, is(equalTo(response.getBody().getPlayers().size())));
		assertThat("Fred", is(equalTo(response.getBody().getPlayers().get(0).getName())));
		assertThat(C5Token.X, is(equalTo(response.getBody().getPlayers().get(0).getToken())));
	}

	@Test
	void givenTwoNewPlyersAdded_whenPlayerMakesStatusRequest_thenStatusIsGameStarted() {

		PlayersContainer.emptyPlayersList();
		BoardService c5BoardService = new BoardService();
		GameService c5GameService = new GameService(c5BoardService);
		GameController c5Controller = new GameController(c5GameService);

		HttpSession sessionOne = new MockHttpSession();
		MockHttpServletRequest requestOne = new MockHttpServletRequest();
		requestOne.setSession(sessionOne);
		ResponseEntity<String> resultOne = c5Controller.addPlayer("Fred", requestOne);
		assertThat(HttpStatus.OK, is(equalTo(resultOne.getStatusCode())));

		HttpSession sessionTwo = new MockHttpSession();
		MockHttpServletRequest requestTwo = new MockHttpServletRequest();
		requestOne.setSession(sessionTwo);
		ResponseEntity<String> resultTwo = c5Controller.addPlayer("Mark", requestTwo);
		assertEquals(HttpStatus.OK, resultTwo.getStatusCode());

		ResponseEntity<ServerResponse> gameStatusDto = c5Controller.getStatus(requestTwo);

		assert (gameStatusDto.getBody().getPlayers().size() == 2);
		assertTrue(gameStatusDto.getBody().isGameStarted());
		assertTrue(!gameStatusDto.getBody().isMyTurn());

	}

	@Test
	void givenTwoPlayersAlready_whenthridPlayerTry_thenServiceUnavailableIsReturned() {

		PlayersContainer.emptyPlayersList();
		BoardService c5BoardService = new BoardService();
		GameService c5GameService = new GameService(c5BoardService);
		GameController c5Controller = new GameController(c5GameService);

		HttpSession sessionOne = new MockHttpSession();
		MockHttpServletRequest requestOne = new MockHttpServletRequest();
		requestOne.setSession(sessionOne);
		ResponseEntity<String> resultOne = c5Controller.addPlayer("Fred", requestOne);
		assertEquals(HttpStatus.OK, resultOne.getStatusCode());

		HttpSession sessionTwo = new MockHttpSession();
		MockHttpServletRequest requestTwo = new MockHttpServletRequest();
		requestOne.setSession(sessionTwo);
		ResponseEntity<String> resultTwo = c5Controller.addPlayer("Stephen", requestTwo);
		assertEquals(HttpStatus.OK, resultTwo.getStatusCode());

		ResponseEntity<ServerResponse> gameStatusDto = c5Controller.getStatus(requestTwo);

		assert (gameStatusDto.getBody().getPlayers().size() == 2);
		assertTrue(gameStatusDto.getBody().isGameStarted());
		assertTrue(!gameStatusDto.getBody().isMyTurn());

		HttpSession sessionThree = new MockHttpSession();
		MockHttpServletRequest requestThree = new MockHttpServletRequest();
		requestOne.setSession(sessionThree);
		ResponseEntity<String> resultThree = c5Controller.addPlayer("Stephen", requestThree);
		assertEquals(HttpStatus.SERVICE_UNAVAILABLE, resultThree.getStatusCode());

	}

	@Test
	void givenTwoPlayersAdded_whenStatusRequest_thenResponseIsGameStarted() {

		PlayersContainer.emptyPlayersList();
		BoardService c5BoardService = new BoardService();
		GameService c5GameService = new GameService(c5BoardService);
		GameController c5Controller = new GameController(c5GameService);

		MockHttpServletRequest requestOne = new MockHttpServletRequest();
		ResponseEntity<String> resultOne = c5Controller.addPlayer("Fred", requestOne);
		assertEquals(HttpStatus.OK, resultOne.getStatusCode());

		MockHttpServletRequest requestTwo = new MockHttpServletRequest();
		ResponseEntity<String> resultTwo = c5Controller.addPlayer("Stephen", requestTwo);
		assertEquals(HttpStatus.OK, resultTwo.getStatusCode());

		HttpSession sessionOne = new MockHttpSession();
		sessionOne.setAttribute("player", new Player("Fred", C5Token.X));
		MockHttpServletRequest requestThree = new MockHttpServletRequest();
		requestThree.setSession(sessionOne);
		ResponseEntity<ServerResponse> gameStatusDto = c5Controller.getStatus(requestThree);
		ServerResponse body = gameStatusDto.getBody();
		assertTrue(body.isGameStarted());
		assertTrue(body.isMyTurn());
		assertFalse(body.isWinningConditionMet());
		assertFalse(body.isDrawConditionMet());
		assertEquals(2, body.getPlayers().size());
		assertEquals("Fred", body.getCurrentPlayer().getName());
		assertEquals(C5Token.X, body.getCurrentPlayer().getToken());
		assertEquals("Stephen", body.getNextPlayer().getName());
		assertEquals(C5Token.O, body.getNextPlayer().getToken());
	}

	@Test
	void givenTwoPlayersAdded_whenPlayersPlaying_thenStatusIsGameStarted() {

		PlayersContainer.emptyPlayersList();
		BoardService c5BoardService = new BoardService();
		GameService c5GameService = new GameService(c5BoardService);
		GameController c5Controller = new GameController(c5GameService);

		HttpSession sessionOne = new MockHttpSession();
		sessionOne.setAttribute("player", new Player("Fred", C5Token.X));

		HttpSession sessionTwo = new MockHttpSession();
		sessionTwo.setAttribute("player", new Player("Stephen", C5Token.O));

		MockHttpServletRequest requestOne = new MockHttpServletRequest();
		ResponseEntity<String> resultOne = c5Controller.addPlayer("Fred", requestOne);
		assertEquals(HttpStatus.OK, resultOne.getStatusCode());

		MockHttpServletRequest requestTwo = new MockHttpServletRequest();
		ResponseEntity<String> resultTwo = c5Controller.addPlayer("Stephen", requestTwo);
		assertEquals(HttpStatus.OK, resultTwo.getStatusCode());

		MockHttpServletRequest requestThree = new MockHttpServletRequest();
		requestThree.setSession(sessionOne);
		c5Controller.play("1", requestThree);

		MockHttpServletRequest requestFour = new MockHttpServletRequest();
		requestFour.setSession(sessionTwo);
		c5Controller.play("1", requestFour);

		ResponseEntity<ServerResponse> gameStatusDto = c5Controller.getStatus(requestFour);
		ServerResponse body = gameStatusDto.getBody();
		assertTrue(body.isGameStarted());
		assertFalse(body.isMyTurn());
		assertFalse(body.isWinningConditionMet());
		assertFalse(body.isDrawConditionMet());
		assertEquals(2, body.getPlayers().size());
		assertEquals("Fred", body.getCurrentPlayer().getName());
		assertEquals(C5Token.X, body.getCurrentPlayer().getToken());
		assertEquals("Stephen", body.getNextPlayer().getName());
		assertEquals(C5Token.O, body.getNextPlayer().getToken());
		assertEquals(2, body.getBoard().get(0).size());

	}

	@Test
	void givenPlayerAdded_whenTryingToPlay_ThenBadRequest() {

		PlayersContainer.emptyPlayersList();
		HttpSession session = new MockHttpSession();
		session.setAttribute("player", new Player("Fred", C5Token.X));
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);

		BoardService c5BoardService = new BoardService();
		GameService c5GameService = new GameService(c5BoardService);
		GameController c5Controller = new GameController(c5GameService);

		ResponseEntity<String> result = c5Controller.play("-1", request);
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
	}

	@Test
	void givenClienttHttpRequestWithSession_whencontrollerhandlRequestAndDisconnect_thenHttpStatusIsOk() {

		PlayersContainer.emptyPlayersList();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("player", new Player("Fred", C5Token.X));
		request.setSession(session);

		BoardService c5BoardService = new BoardService();
		GameService c5GameService = new GameService(c5BoardService);
		GameController c5Controller = new GameController(c5GameService);

		ResponseEntity<String> result = c5Controller.exitCleanly(request);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

}
