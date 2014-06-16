package com.ringo.tictactoe.activities;

import java.util.Vector;

import com.ringo.tictactoe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ringo.tictactoe.game.Player;
import com.ringo.tictactoe.activities.game.TicTacToeActivity;


public class MainActivity extends Activity implements PlayersFragment.OnGameStartSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
        	PlayersFragment players = new PlayersFragment();
        	
            getFragmentManager().beginTransaction()
                    .add(R.id.container, players)
                    .commit();
            
            
        }
    }


	@Override
	public void onGameStarted(Vector<Player> players) {
		Intent intent = new Intent(this, TicTacToeActivity.class);
		intent.putExtra("players", players);
		startActivity(intent);
	}

}
