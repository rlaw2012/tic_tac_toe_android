package com.ringo.tictactoe.game;


public class Cell {
	public enum CellState { NONE, CROSS, NOUGHT }
	
	private Location loc;
	private Player player;
	
	public Cell(Location loc) {
		this.loc = loc;
		this.player = null;
	}

	public Location getLocation() {
		return this.loc;
	}
	
	public Player getOccupant() {
		return this.player;
	}
	
	public void setOccupant(Player occupant) {
		this.player = occupant;
	}
	
	public boolean isOccupiedBy(Player player) {
		return this.player == player;
	}
	
	public CellState getState() {
		if (this.player == null) {
			return Cell.CellState.NONE;
		} else {
			return this.player.getCellState();
		}
	}
	
	public void clearOccupant() {
		this.player = null;
	}
	
	public boolean isEmpty() {
		return this.player == null;
	}
}
