package com.ringo.tictactoe.game;

import java.io.Serializable;

public class Player implements Serializable {
  private static final long serialVersionUID = -8702374271590696051L;
  
  private String name;
  private Cell.CellState state;
  private int wins;
  private int losses;
  private int draws;
  private boolean isAI;
  
  public Player(String name, boolean ai) {
	  this(name, 0, 0, 0, ai);
  }
  
  public Player(String name, int wins, int losses, int draws, boolean ai) {
	  this.name = name;
	  this.state = Cell.CellState.NONE;
	  this.wins = wins;
	  this.losses = losses;
	  this.draws = draws;
	  this.isAI = ai;
  }
  
  public String getName() {
	  return this.name;
  }
  
  public void setCellState(Cell.CellState state) {
	  this.state = state;
  }
  
  public Cell.CellState getCellState() {
	  return this.state;
  }
  
  public void incrementWins() {
	  this.wins++;
  }
  
  public int getWins() {
	  return this.wins;
  }
  
  public void incrementLosses() {
	  this.losses++;
  }
   
  public int getLosses() {
	  return this.losses;
  }
  
  public void incrementDraws() {
	  this.draws++;
  }
  
  public int getDraws() {
	  return this.draws;
  }
  
  public boolean isAI() {
	  return this.isAI;
  }
}
