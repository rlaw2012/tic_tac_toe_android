package com.ringo.tictactoe.game.algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import android.annotation.SuppressLint;
//import android.util.Log;

import com.ringo.tictactoe.game.Board;
import com.ringo.tictactoe.game.Cell;
import com.ringo.tictactoe.game.Location;
import com.ringo.tictactoe.game.Player;

public class TicTacToeAlgorithm implements BoardAlgorithm {
	public static int DEFAULT_CELLS_IN_A_ROW_TO_WIN = 3;
	public static int[] MOVES_SCORES = {0, 1, 10, 100};   // 0 move per line (horizontal, vertical or diagonal) scores 0; 1 move scores 1; 2 moves score 10; and 3 moves score 100.
	
	protected int cellsInARowToWin;
	
	public TicTacToeAlgorithm() {
		super();
		this.cellsInARowToWin = DEFAULT_CELLS_IN_A_ROW_TO_WIN;
	}
	
	@Override
	public boolean isDrawn(Board board) {
		Vector<Player> players = board.getPlayers();
		
		for (Player player : players) {
			if (isPlayerWon(board, player)) {
				return false;
			}
		}
		
		return !board.hasEmptyCells();
	}
	
	@Override
	public boolean isWinning(Board board) {
		Vector<Player> players = board.getPlayers();
		
		for (Player player : players) {
			if (isPlayerWon(board, player)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean isPlayerWon(Board board, Player player) {
		//Log.d("GameAlgorithm", player.getName());
		Vector<Cell> winningCells = getWinningCells(board, player);
		return winningCells != null;
	}

	@Override
	public Vector<Cell> getWinningCells(Board board, Player player) {
		Vector<Cell> moves = board.getPlayerMoves(player);
		Vector<Cell> winningCells = null;
		
		winningCells = getStraightLineWinningCells(moves, true);  // get 3 winning cells in a row
		if (winningCells == null) {
			winningCells = getStraightLineWinningCells(moves, false);  // get 3 winning cells in a column
		}
		if (winningCells == null) {
			winningCells = getDiagonalWinningCells(moves);
		}
		
		return winningCells;
	}
	
	protected Vector<Cell> getStraightLineWinningCells(Vector<Cell> moves, boolean horizontal) {
		Map<Integer, Vector<Cell>> rows = getStraightLineCells(moves, horizontal);
		
		for (int key : rows.keySet()) {
			Vector<Cell> row = rows.get(key);
			//Log.d("GameAlgorithm", (horizontal ? "row:" : "column:") + key + " has " + row.size() + " " + row.get(0).getState());
			if (row.size() == this.cellsInARowToWin) {
				return row;
			}
		}
		
		return null;
	}
	
	@SuppressLint("UseSparseArrays")
	protected Map<Integer, Vector<Cell>> getStraightLineCells(Vector<Cell> moves, boolean horizontal) {
		Map<Integer, Vector<Cell>> rows = new HashMap<Integer, Vector<Cell>>();
		Vector<Cell> row;
		
		for (Cell move : moves) {
			int ndx = horizontal ? move.getLocation().getX() : move.getLocation().getY();
			if (rows.containsKey(ndx)) {
				row = (Vector<Cell>)rows.get(ndx);
				row.add(move);
			} else {
				row = new Vector<Cell>();
				row.add(move);
				rows.put(ndx, row);
			}
		}
		return rows;
	}
	
	protected Vector<Cell> getDiagonalWinningCells(Vector<Cell> moves) {
		Map<Boolean, Vector<Cell>> cells = getDiagonalCells(moves);
		for (boolean diagonal : cells.keySet()) {
			Vector<Cell> diagonalCells = cells.get(diagonal);
			
			/*
			if (diagonalCells.size() > 0) {
				Log.d("GameAlgorithm", (diagonal ? "diagonal:" : "reverse diagonal:") + " has " + diagonalCells.size() + " " + diagonalCells.get(0).getState());
			}
			*/
			if (diagonalCells.size() == this.cellsInARowToWin) {
				return diagonalCells;
			}
		}
		
		return null;
	}
	
	protected Map<Boolean, Vector<Cell>> getDiagonalCells(Vector<Cell> moves) {
		Map<Boolean, Vector<Cell>> cells = new HashMap<Boolean, Vector<Cell>>();
		
		// TODO: if rows/columns are more than the number of moves to win, we 
		// can easily shift the origin of the top left corner to be {0, 0} and 
		// the following logic will still work.
		// find forward diagonal cells (from top left to bottom right)
		Vector<Cell> results = new Vector<Cell>();
		for (Cell cell : moves) {
			if (cell.getLocation().getX() == cell.getLocation().getY()) {
				results.add(cell);
			} 
		}
		cells.put(true, results);
		
		// TODO: if rows/columns are more than the number of moves to win, we 
		// can easily shift the origin of the top right corner to be {0, 2} and 
		// the following logic will still work.  If the number of moves to win 
		// is greater than 3, we can still generalize the solution by using the 
		// logic: every time we move down a row from cell {x, y}, the new cell 
		// on the reverse diagonal line should be {x+1, y-1}.
		// find reverse diagonal cells (from top right to bottom left)
		results = new Vector<Cell>();
		for (Cell cell : moves) {
			if ((cell.getLocation().getX() == 0 && cell.getLocation().getY() == 2) 
					|| (cell.getLocation().getX() == 1 && cell.getLocation().getY() == 1) 
					|| (cell.getLocation().getX() == 2 && cell.getLocation().getY() == 0)) {
				results.add(cell);
			} 
		}
		cells.put(false, results);
		
		return cells;
	}

	@Override
	public int getBoardScore(Board board, Player player) {
		int score = 0;
		
		for (Player p : board.getPlayers()) {
			score += getBoardScoreForPlayer(board, p, p != player);
		}
		
		return score;
	}
	
	/**
	 * Determines the board score for the current player.
	 * 
	 * @param board board to analyzed.
	 * @param player current player.
	 * @param isOpponent true if current player is an opponent; false otherwise.
	 * @return
	 */
	protected int getBoardScoreForPlayer(Board board, Player player, boolean isOpponent) {
		Vector<Cell> moves = board.getPlayerMoves(player);
		int score = 0;
		Vector<Cell> current;
		Location loc;
		Cell cell;
		boolean hasOpponent;
		Vector<Vector<Cell>> twoInARow = new Vector<Vector<Cell>>();    // use to determine whether there is a fork 
		
		// check score for rows
		Map<Integer, Vector<Cell>> rows = getStraightLineCells(moves, true);
		for (int row : rows.keySet()) {
			current = rows.get(row);
			hasOpponent = false;
			
			if (current.size() == this.cellsInARowToWin) {
				// found a winning row
				return MOVES_SCORES[MOVES_SCORES.length - 1];
			} else {
				for (int column = 0; column < board.getColumns(); column++) {
					loc = new Location(row, column);
					cell = board.getCell(loc);
					
					if (!cell.isEmpty() && !cell.isOccupiedBy(player)) {
						// if current row has opponent's move, player can't make a winning move on this row.
						hasOpponent = true;
						break;
					}
				}
				
				if (!hasOpponent) {
					score += MOVES_SCORES[current.size()];
					if (current.size() == 2) {
						twoInARow.add(current);
					}
				}
			}
		}
		
		// check score for columns
		Map<Integer, Vector<Cell>> columns = getStraightLineCells(moves, false);
		for (int column : columns.keySet()) {
			current = columns.get(column);
			hasOpponent = false;
			
			if (current.size() == this.cellsInARowToWin) {
				// found a winning column
				return MOVES_SCORES[MOVES_SCORES.length - 1];
			} else {
				for (int row = 0; row < board.getRows(); row++) {
					loc = new Location(row, column);
					cell = board.getCell(loc);
					
					if (!cell.isEmpty() && !cell.isOccupiedBy(player)) {
						// if current column has opponent's move, player can't make a winning move on this column.
						hasOpponent = true;
						break;
					}
				}
				
				if (!hasOpponent) {
					score += MOVES_SCORES[current.size()];
					if (current.size() == 2) {
						twoInARow.add(current);
					}
				}
			}
		}
		
		// check score for diagonal lines
		Map<Boolean, Vector<Cell>> diagonals = getDiagonalCells(moves);
		for (boolean diagonal : diagonals.keySet()) {
			current = diagonals.get(diagonal);
			if (current.size() == this.cellsInARowToWin) {
				return MOVES_SCORES[MOVES_SCORES.length - 1];
			}
			
			hasOpponent = false;
			if (diagonal) {
				int size = Math.min(board.getRows(), board.getColumns());
				for (int ndx = 0; ndx < size; ndx++) {
					loc = new Location(ndx, ndx);
					cell = board.getCell(loc);
					
					if (!cell.isEmpty() && !cell.isOccupiedBy(player)) {
						// if current position has opponent's move, player can't make a winning move on this diagonal line.
						hasOpponent = true;
						break;
					}
				}

				if (!hasOpponent) {
					score += MOVES_SCORES[current.size()];
					if (current.size() == 2) {
						twoInARow.add(current);
					}
				}
			} else {
				// reverse diagonal
				int size = Math.min(board.getRows(), board.getColumns());
				for (int ndx = 0; ndx < size; ndx++) {
					loc = new Location(ndx, size - ndx - 1);
					cell = board.getCell(loc);
					
					if (!cell.isEmpty() && !cell.isOccupiedBy(player)) {
						// if current position has opponent's move, player can't make a winning move on this diagonal line.
						hasOpponent = true;
						break;
					}
				}
				
				if (!hasOpponent) {
					score += MOVES_SCORES[current.size()];
					if (current.size() == 2) {
						twoInARow.add(current);
					}
				}
			}
		}
		
		if (hasForkingMoves(twoInARow)) {
			// the same location is shared by two lines, so this is a fork winning condition in two more moves.
			return MOVES_SCORES[MOVES_SCORES.length - 1];
 		}
		
				
		return isOpponent ? -1 * score : score;
	}
	
	/**
	 * Determines whether there is a forking board for the player that will 
	 * lead the player to win in two more moves.
	 * 
	 * @param twoInARow the 'rows' that contain two non-interrupted moves.
	 * @return true if a forking board is found; false otherwise.
	 */
	protected boolean hasForkingMoves(Vector<Vector<Cell>> twoInARow) {
		Cell cell;
		Location loc;
		
		if (twoInARow.size() > 1) {
			Set<Location> cells = new HashSet<Location>();
			Iterator<Vector<Cell>> rowIterator = twoInARow.iterator();
			
			while (rowIterator.hasNext()) {
				Vector<Cell> line = rowIterator.next();
				Iterator<Cell> cellIterator = line.iterator();
				
				while (cellIterator.hasNext()) {
					cell = cellIterator.next();
					loc = cell.getLocation();
					
					if (cells.contains(loc)) {
						return true;
					} else {
						cells.add(loc);
					}
				}
			}
		}
		
		return false;
	}

}
