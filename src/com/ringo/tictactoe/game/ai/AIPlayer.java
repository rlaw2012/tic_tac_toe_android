package com.ringo.tictactoe.game.ai;

import com.ringo.tictactoe.game.Player;

public class AIPlayer extends Player {
	private static final long serialVersionUID = 6335019079773369366L;
	
	public static final String AI_PLAYER_NAME = "Computer";

	public AIPlayer() {
		super(AI_PLAYER_NAME, true);
	}

}
