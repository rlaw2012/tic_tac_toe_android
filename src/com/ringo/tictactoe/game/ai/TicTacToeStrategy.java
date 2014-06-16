package com.ringo.tictactoe.game.ai;

import java.util.Vector;

import android.util.Log;

import com.ringo.tictactoe.game.Board;
import com.ringo.tictactoe.game.Cell;
import com.ringo.tictactoe.game.Location;
import com.ringo.tictactoe.game.Player;
import com.ringo.tictactoe.game.algorithms.BoardAlgorithm;

public class TicTacToeStrategy implements AIStrategy {
	public static final int DEFAULT_DIFFICULTY = 4;
	
	protected int difficulty;
	protected BoardAlgorithm algorithm;
	
	/**
	 * Constructor.
	 * 
	 * @param algorithm the game algorithm to determine when a winning board is 
	 *        and what is the best score for the current board is.
	 */
	public TicTacToeStrategy(BoardAlgorithm algorithm) {
		this(DEFAULT_DIFFICULTY, algorithm);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param difficulty determines how 'smart' the AI strategy should be.  The 
	 *        lower this value is, the easier the game is to human opponent.  
	 *        The higher this value is, the hard the game is.
	 * @param algorithm the game algorithm to determine when a winning board is 
	 *        and what is the best score for the current board is.
	 */
	public TicTacToeStrategy(int difficulty, BoardAlgorithm algorithm) {
		this.difficulty = difficulty;
		this.algorithm = algorithm;
	}

	@Override
	public Location getNextMove(Board board, Player aiPlayer, Player opponent) {
		BoardScore boardScore = getBestBoardScore(board.clone(), this.difficulty, aiPlayer, opponent, true);
		
		return boardScore.move;
	}
	
	protected BoardScore getBestBoardScore(Board board, int lookAhead, Player aiPlayer, Player opponent, boolean maximizingAIPlayer) {
		Player player = maximizingAIPlayer ? aiPlayer : opponent;
		
		if (this.algorithm.isDrawn(board) || this.algorithm.isWinning(board)) {
			return new BoardScore(board.getLastMove().getLocation(), this.algorithm.getBoardScore(board, player));
		}
		
		Vector<Cell> emptyCells = board.getEmptyCells();
		Board newBoard = board;
		BoardScore newBoardBestScore;
		BoardScore bestScore = new BoardScore();
		
		if (maximizingAIPlayer) {
			// maximizing ai player's winning chance
			bestScore.score = Integer.MIN_VALUE;
			
			for (Cell cell : emptyCells) {
				//Log.d("TicTacToeStrategy", "---> AI moves to {" + cell.getLocation().getX() + ", " + cell.getLocation().getY() + "}");
				newBoard.move(player, cell.getLocation());
				if (lookAhead == 1) {
					newBoardBestScore = new BoardScore(newBoard.getLastMove().getLocation(), this.algorithm.getBoardScore(newBoard, player));
				} else {
					newBoardBestScore = getBestBoardScore(newBoard, lookAhead - 1, aiPlayer, opponent, false);
				}
				
				if (newBoardBestScore.score > bestScore.score) {
					bestScore = newBoardBestScore;
				} else if (newBoardBestScore.score == bestScore.score && (System.nanoTime() % 2 == 0)) {
					// randomize the board selection so that the game won't be identical all the time.
					bestScore = newBoardBestScore;
				}
				
				newBoard.undoMove();
			}
			
			return bestScore;
		} else {
			// minimizing human player's winning chance
			bestScore.score = Integer.MAX_VALUE;
			
			for (Cell cell : emptyCells) {
				//Log.d("TicTacToeStrategy", "===> Opponent moves to {" + cell.getLocation().getX() + ", " + cell.getLocation().getY() + "}");
				newBoard.move(player, cell.getLocation());
				if (lookAhead == 1) {
					newBoardBestScore = new BoardScore(newBoard.getLastMove().getLocation(), this.algorithm.getBoardScore(newBoard, player));
				} else {
					newBoardBestScore = getBestBoardScore(newBoard, lookAhead - 1, aiPlayer, opponent, true);
				}
				
				if (newBoardBestScore.score < bestScore.score) {
					bestScore = newBoardBestScore;
				} else if (newBoardBestScore.score == bestScore.score && (System.nanoTime() % 2 == 0)) {
					// randomize the board selection so that the game won't be identical all the time.
					bestScore = newBoardBestScore;
				}
				
				newBoard.undoMove();
			}
			
			return bestScore;
		}
	}
	
	protected static class BoardScore {
		Location move;
		int score;
		
		BoardScore() {
			this.move = null;
			this.score = 0;
		}
		
		BoardScore(Location mv, int sc) {
			this.move = mv;
			this.score = sc;
		}
	}

}
