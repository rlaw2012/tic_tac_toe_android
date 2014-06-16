package com.ringo.tictactoe.game;

import java.util.Vector;

import android.util.Log;

import com.ringo.tictactoe.game.algorithms.BoardAlgorithm;

public class GameController {
	protected Board board;
	protected BoardAlgorithm algorithm;
	protected Vector<Player> players;
	protected int totalMoves;
	protected int totalGames;
	protected int firstMovePlayerNdx;
	protected boolean boardLocked;
	protected OnGameAnalyzedListener analyzedListener;
	protected OnGameFinishedListener finishedListener;
	
	public GameController(Vector<Player> players, Board board, BoardAlgorithm algorithm) {
		this.players = players;
		this.board = board;
		this.algorithm = algorithm;
		this.totalMoves = 0;
		this.totalGames = 0;
		this.newGame();
	}

	public void setAnalyzedListener(OnGameAnalyzedListener analyzedListener) {
		this.analyzedListener = analyzedListener;
	}

	public void setFinishedListener(OnGameFinishedListener finishedListener) {
		this.finishedListener = finishedListener;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public boolean isBoardLocked() {
		return this.boardLocked;
	}
	
	public void lockBoard(boolean lock) {
		this.boardLocked = lock;
	}
	
	public void newGame() {
		this.board.reset();
		this.totalMoves = 0;
		this.firstMovePlayerNdx = this.totalGames % this.players.size();
		this.totalGames++;
		this.boardLocked = false;
	}
	
	public Player getCurrentPlayer() {
		return this.players.get(this.getCurrentPlayerIndex());
	}
	
	public void nextMove(Location loc) {
		Player player = this.players.get(this.getCurrentPlayerIndex());
		this.board.move(player, loc);
		
		if (this.algorithm.isPlayerWon(board, player)) {
			Log.d("GameController", player.getName() + " won the game!!!!");
			this.boardLocked = true;
			player.incrementWins();
			for (Player p : this.players) {
				if (p != player) {
					p.incrementLosses();
				}
			}
			
			if (this.finishedListener != null) {
				Vector<Cell> winningCells = this.algorithm.getWinningCells(board, player);
				this.finishedListener.onGameWon(player, winningCells);
			}
		} else if (this.algorithm.isDrawn(board)) {
			Log.d("GameController", "Game is draw!!!!");
			this.boardLocked = true;
			for (Player p : this.players) {
				p.incrementDraws();
			}
			
			if (this.finishedListener != null) {
				this.finishedListener.onGameDrawn();
			}
		} else {
			this.totalMoves++;
			
			if (this.analyzedListener != null) {
				this.analyzedListener.onGameAnalyzed();
			}
		}
	}
	
	public void surrender() {
		this.totalMoves++;
		Player player = this.players.get(this.getCurrentPlayerIndex());
		player.incrementWins();
		this.boardLocked = true;
	}
	
	protected int getCurrentPlayerIndex() {
		return (((this.totalMoves % this.players.size()) + this.firstMovePlayerNdx) % this.players.size());
	}
	
	public interface OnGameAnalyzedListener {
		public void onGameAnalyzed();
	}
	
	public interface OnGameFinishedListener {
		public void onGameDrawn();
		
		public void onGameWon(Player winningPlayer, Vector<Cell> winningCells);
	}
}
