package ie.game.fred.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ie.game.fred.model.C5Token;
import ie.game.fred.model.Player;
import ie.game.fred.model.ServerResponse;

@Service
public class GameService {

	Logger logger = LoggerFactory.getLogger(GameService.class);

	Player currentPlayer;
	Player nextPlayer;
	int lastMove = 0;
	public static final int NUMBER_OF_COLUMN = 9;
	public static final int NUMBER_OF_ROW = 6;

	private final BoardService boardService;

	public GameService(BoardService boardService) {
		this.boardService = boardService;
		this.boardService.initialiseBoard();
	}

	public Player addPlayer(String playerName) {

		var newPlayer = new Player();
		newPlayer.setName(playerName);
		if (PlayersContainer.getPlayers().isEmpty()) {
			newPlayer.setToken(C5Token.X);
			setCurrentPlayer(newPlayer);
		} else {
			newPlayer.setToken(C5Token.O);
			setNextPlayer(newPlayer);
		}

		PlayersContainer.getPlayers().add(newPlayer);
		logger.info("{} added as new Player with token {}", newPlayer.getName(), newPlayer.getToken());
		return newPlayer;

	}

	public void play(C5Token token, int column) {

		lastMove = column;

		boardService.addC5Token(token, column);
		final List<Player> players = PlayersContainer.getPlayers();
		for (Player player : players) {
			if (player.getToken().equals(token)) {
				currentPlayer = player;
			} else {
				nextPlayer = player;
			}
		}

		// switch around player
		var temp = currentPlayer;
		currentPlayer = nextPlayer;
		nextPlayer = temp;

		logger.info("Players have been switched around ");
	}

	/* This method returns the current status of the game */

	public ServerResponse getGameStatus(Player player) {

		var response = new ServerResponse();
		response.setMyTurn(player.equals(getCurrentPlayer()));
		response.setBoard(getBoardService().getBoard());

		if (getLastMove() != 0) {
			response.setWinningConditionMet(boardService.isWinningMove(getLastMove()));
		} else {
			response.setWinningConditionMet(false);
		}
		response.setGameStarted(PlayersContainer.getPlayers().size() == 2);
		response.setCurrentPlayer(getCurrentPlayer());
		response.setNextPlayer(getNextPlayer());
		response.setPlayers(PlayersContainer.getPlayers());

		return response;
	}

	public int getLastMove() {
		return lastMove;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Player getNextPlayer() {
		return nextPlayer;
	}

	public void setNextPlayer(Player nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	public BoardService getBoardService() {
		return boardService;
	}

	public List<Player> getPlayersContainerSize() {
		return PlayersContainer.getPlayers();
	}

}
