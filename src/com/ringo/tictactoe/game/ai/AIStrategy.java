package com.ringo.tictactoe.game.ai;

import com.ringo.tictactoe.game.Board;
import com.ringo.tictactoe.game.Location;
import com.ringo.tictactoe.game.Player;

/**
 * An interface to programmatically determine the best next move towards winning
 * a game.
 * @author ringolaw
 *
 */
public interface AIStrategy {
	
	/**
	 * Determines the best next move for the AI player based on the current board.
	 * 
	 * @param board the current game being played by AI player with its human opponents.
	 * @param aiPlayer the AI player.
	 * @param opponent the opponent.
	 * @return the best location for AI player to place the next move.
	 */
	public Location getNextMove(Board board, Player aiPlayer, Player opponent);
}
