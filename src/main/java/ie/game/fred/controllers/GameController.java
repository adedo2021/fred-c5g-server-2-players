package ie.game.fred.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ie.game.fred.model.Player;
import ie.game.fred.model.ServerResponse;
import ie.game.fred.service.GameService;
import ie.game.fred.service.PlayersContainer;

@RestController
@RequestMapping("/game")
public class GameController {

	Logger logger = LoggerFactory.getLogger(GameController.class);

	static final String PLAYER_SESSION_ATTRIBUTE = "player";
	static final String UNAUTHORISE_TO_PLAY = "invalid player not authorised to play";

	private final GameService gameService;

	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	@PostMapping("/players")
	@ResponseBody
	public ResponseEntity<String> addPlayer(@RequestParam String name, HttpServletRequest request) {
		List<Player> players = PlayersContainer.getPlayers();

		if (players.size() < 2) {

			request.getSession().setAttribute(PLAYER_SESSION_ATTRIBUTE, gameService.addPlayer(name));

			logger.info(" {} you have been added to the game ", name);
			return new ResponseEntity<>(name + " you have been added to the game ", HttpStatus.OK);
		}

		logger.info(" maximum players for this game reached");
		return new ResponseEntity<>("max players for this game", HttpStatus.SERVICE_UNAVAILABLE);

	}

	@GetMapping("/status")
	@ResponseBody
	public ResponseEntity<ServerResponse> getStatus(HttpServletRequest request) {
		Object attribute = request.getSession().getAttribute(PLAYER_SESSION_ATTRIBUTE);

		if (attribute instanceof Player) {
			var serverResponse = gameService.getGameStatus((Player) attribute);
			logger.info("send game status after request by {}", ((Player) attribute).getName());
			return ResponseEntity.ok(serverResponse);
		}
		logger.info(UNAUTHORISE_TO_PLAY);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/play")
	@ResponseBody
	public ResponseEntity<String> play(@RequestParam String position, HttpServletRequest request) {

		Object attribute = request.getSession().getAttribute(PLAYER_SESSION_ATTRIBUTE);
		if (attribute instanceof Player) {
			var player = (Player) attribute;

			Integer pos = Integer.parseInt(position);
			boolean isValid = gameService.getBoardService().isPositionValid(pos - 1);

			if (isValid) {

				gameService.play(player.getToken(), pos - 1);

				logger.info("{} has taken their turn", player.getName());
				return new ResponseEntity<>(player.getName() + " has taken their turn", HttpStatus.OK);

			} else {
				logger.info("not user {} turn to play, wrong call", player.getName());
				return new ResponseEntity<>(player.getName() + ", this is not your turn.", HttpStatus.BAD_REQUEST);
			}
		} else {
			logger.info(UNAUTHORISE_TO_PLAY);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@GetMapping("/leave")
	@ResponseBody
	public ResponseEntity<String> exitCleanly(HttpServletRequest request) {
		Object attribute = request.getSession().getAttribute(PLAYER_SESSION_ATTRIBUTE);
		if (attribute instanceof Player) {
			String disconnectedPlayerName = ((Player) attribute).getName();
			expireUserSessions(request);
			logger.info("user {} has disconnected and this game is now over.", disconnectedPlayerName);
			return new ResponseEntity<>(disconnectedPlayerName + " has disconnected and this game is now over.",
					HttpStatus.OK);
		}
		logger.info(UNAUTHORISE_TO_PLAY);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	public void expireUserSessions(HttpServletRequest request) {
		Object attribute = request.getSession().getAttribute(PLAYER_SESSION_ATTRIBUTE);
		if (attribute instanceof Player) {
			request.getSession().removeAttribute(PLAYER_SESSION_ATTRIBUTE);
			PlayersContainer.emptyPlayersList();
			gameService.getBoardService().initialiseBoard();
		}
	}

}
