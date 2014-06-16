package com.ringo.tictactoe.activities.game;

import com.ringo.tictactoe.game.Board;
import com.ringo.tictactoe.game.Cell;
import com.ringo.tictactoe.game.GameController;
import com.ringo.tictactoe.game.Location;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

public class GridCellAdapter extends BaseAdapter {
	private Context context;
	private GameController controller;
	private Board board;

	public GridCellAdapter(Context context, GameController controller) {
		super();
		this.context = context;
		this.controller = controller;
		this.board = this.controller.getBoard();
	}

	@Override
	public int getCount() {
		return board.getRows() * board.getColumns();
	}

	@Override
	public Object getItem(int pos) {
		Location loc = convertPositionToLocation(pos);
		return this.board.getCell(loc);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		CellView view = null;
		if (convertView == null) {
			view = new CellView(this.context);
			
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager windowManager = (WindowManager) context
	                .getSystemService(Context.WINDOW_SERVICE);
			windowManager.getDefaultDisplay().getMetrics(metrics);
			
			int rotation = windowManager.getDefaultDisplay().getRotation();
			int width = 0;
			if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
				width = metrics.widthPixels / 3 - 10;
			} else {
				width = metrics.heightPixels / 3 - 10;
			}
			view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			view.getLayoutParams().width = view.getLayoutParams().height = width;
		} else {
			view = (CellView)convertView;
		}
		Location loc = convertPositionToLocation(pos);
		view.setLocation(loc);
		Cell.CellState state = this.board.getState(loc);
		view.setState(state);
		
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CellView cell = (CellView)v;
				Location loc = cell.getLocation();
				Log.d("GridCellAdapter", "Cell {" + loc.getX() + ", " + loc.getY() + "} is cliked!!");
				
				if (controller.isBoardLocked() == false && board.isEmptyCell(loc)) {
					controller.nextMove(loc);
					//Log.d("GridCellAdapter", "New cell state: " + board.getState(loc));
					cell.setState(board.getState(loc));
				}
			}
		});
				
		return view;
	}
	
	private Location convertPositionToLocation(int pos) {
		int total = pos;
		int y = total % this.board.getColumns(), x = 0;
		
		while (total >= this.board.getColumns()) {
			x++;
			total = total - this.board.getColumns();
		}
		
		return new Location(x, y);
	}

}
