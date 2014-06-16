package com.ringo.tictactoe.activities.game;


import java.util.List;
import java.util.Vector;

import com.ringo.tictactoe.R;
import com.ringo.tictactoe.game.Board;
import com.ringo.tictactoe.game.Cell;
import com.ringo.tictactoe.game.GameController;
import com.ringo.tictactoe.game.Location;
import com.ringo.tictactoe.game.Player;
import com.ringo.tictactoe.game.TicTacToeGameController;
import com.ringo.tictactoe.game.ai.AIStrategy;
import com.ringo.tictactoe.game.ai.TicTacToeStrategy;
import com.ringo.tictactoe.game.algorithms.BoardAlgorithm;
import com.ringo.tictactoe.game.algorithms.TicTacToeAlgorithm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class TicTacToeActivity extends Activity implements GameController.OnGameAnalyzedListener, GameController.OnGameFinishedListener {
	protected Board board;
	protected Vector<Player> players;
	protected BoardAlgorithm algorithm;
	protected GameController controller;
	protected GridView grid;
	protected GridCellAdapter adapter;
	protected AIStrategy aiStrategy;
	protected AIThinkTask aiTask;

	public TicTacToeActivity() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		List<Player> playersExtra = (List<Player>) getIntent().getExtras().getSerializable("players");
		this.players = new Vector<Player>(playersExtra);
		
		this.setupBoard();
		this.setupButtons();

		this.updateGameStats();
		this.updateGameStateWithCurrentPlayer();
	}
	
	protected void setupBoard() {
		this.board = new Board(this.players, TicTacToeAlgorithm.DEFAULT_CELLS_IN_A_ROW_TO_WIN, TicTacToeAlgorithm.DEFAULT_CELLS_IN_A_ROW_TO_WIN);
		this.algorithm = new TicTacToeAlgorithm();
		this.controller = new TicTacToeGameController(this.players, this.board, this.algorithm);
		this.aiStrategy = new TicTacToeStrategy(5, this.algorithm);
		
		this.controller.setAnalyzedListener(this);
		this.controller.setFinishedListener(this);
		
		initBoardGrid();
	}
	
	protected void initBoardGrid() {
		Log.d("Activity", "initializing board and grid");
		this.grid = (GridView)findViewById(R.id.tic_tac_toe_board); 
		this.grid.setNumColumns(this.board.getColumns());
		this.grid.setBackgroundColor(Color.BLACK);
		this.grid.setVerticalSpacing(5);
		this.grid.setHorizontalSpacing(5);
		this.grid.setPadding(5, 5, 5, 5);
		
		this.adapter = new GridCellAdapter(this, this.controller);
		this.grid.setAdapter(this.adapter);
	}
	
	protected void setupButtons() {
		Button button = (Button)findViewById(R.id.new_game_button);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!algorithm.isDrawn(board) && !algorithm.isWinning(board)) {
					// game has not finished yet.  warning player whether s/he wants to give up the game or not.
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						TicTacToeActivity.this);
		 
					// set title
					alertDialogBuilder.setTitle("Surrender Your Game");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("Are you sure you want to surrender your game and declare a loss?")
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// current player gives up
								controller.surrender();
								updateGameStats();
								resetBoard();
								
								if (TicTacToeActivity.this.controller.getCurrentPlayer().isAI()) {
									moveByAIPlayer();
								}
							}
						  })
						.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
					});
		 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
		 
					// show it
					alertDialog.show();
				} else {
					resetBoard();
					
					if (TicTacToeActivity.this.controller.getCurrentPlayer().isAI()) {
						moveByAIPlayer();
					}	
				}
			}
		});
		
		button = (Button)findViewById(R.id.exit_button);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TicTacToeActivity.this.aiTask.cancel(true);
				TicTacToeActivity.this.onBackPressed();
			}
		});
	}
	
	protected void resetBoard() {
		this.controller.newGame();
		this.grid.invalidateViews();
		this.updateGameStateWithCurrentPlayer();
	}

	protected void updateGameStats() {
		Player player = this.players.get(0);
		updatePlayerStat(player, R.id.player1_wins, R.string.player1_name, R.id.player1_score); 
		
		player = this.players.get(1);
		updatePlayerStat(player, R.id.player2_wins, R.string.player2_name, R.id.player2_score);
		
		updateDrawStat();
	}
	
	protected void updatePlayerStat(Player player, int nameResId, int nameFormatResId, int winResId) {
		String winMsg = getResources().getString(nameFormatResId, player.getName());
		TextView view = (TextView)findViewById(nameResId);
		view.setText(winMsg);
		
		view = (TextView)findViewById(winResId);
		view.setText(String.valueOf(player.getWins()));
	}
	
	protected void updateDrawStat() {
		TextView view = (TextView)findViewById(R.id.draws_total);
		view.setText(String.valueOf(this.players.get(0).getDraws()));
	}
	
	protected void updateGameStateWithCurrentPlayer() {
		Player currentPlayer = this.controller.getCurrentPlayer();
		String stateMsg = getResources().getString(R.string.next_move_hint, currentPlayer.getName());
		updateGameState(stateMsg);
	}
	
	protected void updateGameStateWithWinningPlayer() {
		Player currentPlayer = this.controller.getCurrentPlayer();
		String stateMsg = getResources().getString(R.string.winner_declaration, currentPlayer.getName());
		updateGameState(stateMsg);
	}
	
	protected void updateGameDrawState() {
		String drawMsg = getResources().getString(R.string.game_draw);
		updateGameState(drawMsg);
	}
	
	protected void updateGameState(String msg) {
		TextView gameState = (TextView)findViewById(R.id.game_state);
		gameState.setText(msg);
	}

	@Override
	public void onGameDrawn() {
		this.updateDrawStat();
		this.updateGameDrawState();
	}

	@Override
	public void onGameWon(Player winningPlayer, Vector<Cell> winningCells) {
		this.updateGameStats();
		this.updateGameStateWithWinningPlayer();
	}

	@Override
	public void onGameAnalyzed() {
		updateGameStateWithCurrentPlayer();
		
		if (this.controller.getCurrentPlayer().isAI()) {
			moveByAIPlayer();
		}
	}
	
	protected void moveByAIPlayer() {
		this.aiTask = new AIThinkTask();
		this.aiTask.execute(this.board);
	}
	
	private class AIThinkTask extends AsyncTask<Board, Void, Location> {

		@Override
		protected Location doInBackground(Board... arg0) {
			Player aiPlayer = players.get(0).isAI() ? players.get(0) : players.get(1);
			Player opponent = players.get(0).isAI() ? players.get(1) : players.get(0);
			
			controller.lockBoard(true);
			return aiStrategy.getNextMove(board, aiPlayer, opponent);
		}
		
		@Override
		protected void onPostExecute(Location nextMove) {
			controller.nextMove(nextMove);
			grid.invalidateViews();
			controller.lockBoard(false);
		}
	}
}
