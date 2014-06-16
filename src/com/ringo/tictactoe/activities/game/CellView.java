package com.ringo.tictactoe.activities.game;

import com.ringo.tictactoe.R;
import com.ringo.tictactoe.game.Cell;
import com.ringo.tictactoe.game.Location;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class CellView extends View {
	private Location loc;
	private Cell.CellState state;

	public CellView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
	public void setState(Cell.CellState state) {
		if (this.state == state) {
			return;
		}
		
		this.state = state;
		if (state == Cell.CellState.CROSS) {
			setBackgroundResource(R.drawable.tic_tac_toe_x);
		} else if (state == Cell.CellState.NOUGHT) {
			setBackgroundResource(R.drawable.tic_tac_toe_o);
		} else {
			setBackgroundResource(R.drawable.tic_tac_toe_blank);
		}
	}

	public Cell.CellState getState() {
		return this.state;
	}
}
