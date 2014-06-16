package com.ringo.tictactoe.game.algorithms;

import java.util.Vector;

import com.ringo.tictactoe.game.Board;
import com.ringo.tictactoe.game.Cell;
import com.ringo.tictactoe.game.Player;

public interface BoardAlgorithm {
	
	/**
	 * Determines whether the game is drawn by analyzing the given board.
	 * 
	 * @param board the board to be analyzed.
	 * @return true if the game is drawn; false otherwise.
	 */
	public boolean isDrawn(Board board);
	
	/**
	 * Determines whether the game has a winner by analyzing the given board.
	 * 
	 * @param board the board to be analyzed.
	 * @return true if the game has a winner; false otherwise.
	 */
	public boolean isWinning(Board board);
	
	/**
	 * Determines whether the given player is a winning player for the given board.
	 * 
	 * @param board the board to be analyzed.
	 * @param player player that just made a move.
	 * @return true if player is the winner; false otherwise.
	 */
	public boolean isPlayerWon(Board board, Player player);
	
	/**
	 * Gets the cells from the given board that the player wins the game.
	 * 
	 * @param board the board to be analyzed.
	 * @param player the winning player.
	 * @return a vector of cells that represents the winning game or an empty 
	 *         vector if player has won the game yet; or null if player hasn't 
	 *         won the game yet.
	 */
	public Vector<Cell> getWinningCells(Board board, Player player);
	
	/**
	 * Gets the best score for the given player to win by analyzing the given board.
	 * 
	 * @param board the board to be analyzed.
	 * @param player the current player that made a move.
	 * @return the best score for player to win.
	 */
	public int getBoardScore(Board board, Player player);
}
