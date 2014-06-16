package com.ringo.tictactoe.game;

import java.util.Vector;

import android.util.Log;

import com.ringo.tictactoe.game.algorithms.BoardAlgorithm;

public class TicTacToeGameController extends GameController {

	public TicTacToeGameController(Vector<Player> players, Board board,
			BoardAlgorithm algorithm) {
		super(players, board, algorithm);
	}

	public void newGame() {
		Log.d("TicTacToe", "Creating a new game");
		super.newGame();
		
		// set player's state
		Player firstPlayer = this.players.get(this.firstMovePlayerNdx);
		firstPlayer.setCellState(Cell.CellState.CROSS);
		Player secondPlayer = this.players.get((this.firstMovePlayerNdx + 1) % this.players.size());
		secondPlayer.setCellState(Cell.CellState.NOUGHT);
	}
}
