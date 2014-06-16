package com.ringo.tictactoe.game;

public class Location {
	private int x, y;
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Location) {
			Location other = (Location)obj;
			return this.x == other.x && this.y == other.y;
		}
		return false;
	}
}
