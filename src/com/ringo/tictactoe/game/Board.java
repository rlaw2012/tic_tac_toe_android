package com.ringo.tictactoe.game;

import java.util.Vector;

public class Board {
	private int rows, columns;
	private Cell[][] cells;
	private Vector<Player> players;
	private Vector<Cell> moves;
	
	/**
	 * Constructor. 
	 * 
	 * @param rows
	 * @param columns
	 */
	public Board(Vector<Player> players, int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		initCells();
		this.players = players;
		this.moves = new Vector<Cell>();
	}
	
	private void initCells() {
		this.cells = new Cell[this.rows][this.columns];
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.cells[i][j] = new Cell(new Location(i, j));
			}
		}
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public int getColumns() {
		return this.columns;
	}
	
	public Vector<Player> getPlayers() {
		return new Vector<Player>(this.players);
	}
	
	/**
	 * Resets the state of the board so that no players has make any move on the board.
	 */
	public void reset() {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.cells[i][j].clearOccupant();
			}
		}
		this.moves = new Vector<Cell>();
	}
	
	/**
	 * Creates a new board that is identical to the current one.
	 * 
	 * @return a clone of the current board.
	 */
	public Board clone() {
		Board instance = new Board(this.players, this.rows, this.columns);
		
		for (Cell move : this.moves) {
			instance.move(move.getOccupant(), move.getLocation());
		}
		
		return instance;
	}
	
	/**
	 * Plays a move by a given player at at given location.
	 * 
	 * @param player the player who makes the move.
	 * @param loc the location where the player makes the move.
	 */
	public void move(Player player, Location loc) {
		Cell cell = this.cells[loc.getX()][loc.getY()];
		cell.setOccupant(player);
		this.moves.add(cell);
	}
	
	/**
	 * Rolls back the previous move.
	 */
	public void undoMove() {
		if (!this.moves.isEmpty()) {
			Cell cell = this.moves.remove(this.moves.size() - 1);
			cell.clearOccupant();
		}
	}
	
	/**
	 * Gets all the moves in this game.
	 * 
	 * @return a list of moves that have been played so far.
	 */
	public Vector<Cell> getMoves() {
		return new Vector<Cell>(this.moves);
	}
	
	/**
	 * Gets the list of moves done by the given player.
	 * 
	 * @param player the player who made the moves.
	 * @return a vector of cells that is occupied by the player.
	 */
	public Vector<Cell> getPlayerMoves(Player player) {
		Vector<Cell> playerMoves = new Vector<Cell>();
		
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				if (this.cells[i][j].getOccupant() == player) {
					playerMoves.add(this.cells[i][j]);
				}
			}
		}
		return playerMoves;
	}
	
	public Cell getLastMove() {
		return moves.lastElement();
	}
	
	public boolean isEmptyCell(Location loc) {
		Cell cell = this.cells[loc.getX()][loc.getY()];
		return cell.isEmpty();
	}
	
	/**
	 * Gets the remaining empty cells on the board.
	 * 
	 * @return a vector of empty cells.
	 */
	public Vector<Cell> getEmptyCells() {
		Vector<Cell> emptyCells = new Vector<Cell>();
		
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				if (this.cells[i][j].isEmpty()) {
					emptyCells.add(this.cells[i][j]);
				}
			}
		}
		return emptyCells;
	}
	
	public boolean hasEmptyCells() {
		return getEmptyCells().size() > 0;
	}
	
	public Cell.CellState getState(Location loc) {
		if (loc.getX() < this.rows && loc.getY() < this.columns) {
			return cells[loc.getX()][loc.getY()].getState();
		}
		
		throw new RuntimeException("Invalid location: {" + loc.getX() + ", " + loc.getY() + "}");
	}
	
	public Cell getCell(Location loc) {
		if (loc.getX() < this.rows && loc.getY() < this.columns) {
			return cells[loc.getX()][loc.getY()];
		}
		
		throw new RuntimeException("Invalid location: {" + loc.getX() + ", " + loc.getY() + "}");
	}
}
