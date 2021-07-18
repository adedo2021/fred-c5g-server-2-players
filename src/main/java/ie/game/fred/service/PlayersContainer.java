package ie.game.fred.service;

import java.util.ArrayList;
import java.util.List;

import ie.game.fred.model.Player;

public final class PlayersContainer {

	private static List<Player> players = new ArrayList<>();

	private PlayersContainer() {
	}

	public static final List<Player> getPlayers() {
		return players;
	}

	public static final void addPlayer(Player e) {
		players.add(e);
	}

	public static void emptyPlayersList() {
		players = new ArrayList<>();
	}
}
