package com.ringo.tictactoe.activities;

import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ringo.tictactoe.R;
import com.ringo.tictactoe.game.Player;
import com.ringo.tictactoe.game.ai.AIPlayer;

public class PlayersFragment extends Fragment {
	private OnGameStartSelectedListener listener;
	private int numPlayers;

	public PlayersFragment() {
		this.numPlayers = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_players, container, false);
        setupButton(rootView);
        setupPlayerSelections(rootView);
        
        return rootView;
    }
    
    private void setupButton(View rootView) {
    	Button playButton = (Button)rootView.findViewById(R.id.play_button);
    	playButton.setOnClickListener(new OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View arg0) {
				Activity parent = PlayersFragment.this.getActivity();
				Vector<Player> players = new Vector<Player>();
				EditText input = (EditText)parent.findViewById(R.id.player1_name_input);
				Editable name = input.getText();
				
				// check player 1 name
				if (name == null || name.length() == 0) {
					Toast.makeText(parent, "Please enter name for player 1", Toast.LENGTH_SHORT).show();
					return;
				}
				
				// add player 1
				Player player = new Player(name.toString(), false);
				players.add(player);
				
				if (PlayersFragment.this.numPlayers == 1) {
					player = new AIPlayer();
					players.add(player);
				} else {
					input = (EditText)parent.findViewById(R.id.player2_name_input);
					name = input.getText();
					
					// check player 2 name
					if (name == null || name.length() == 0) {
						Toast.makeText(parent, "Please enter name for player 2", Toast.LENGTH_SHORT).show();
						return;
					}
					
					// add player 2
					player = new Player(name.toString(), false);
					players.add(player);
				}
				
				listener.onGameStarted(players);
			}
    		
    	});
    }
    
    private void setupPlayerSelections(final View rootView) {
    	togglePlayer2Views(rootView, false);
    	
		RadioButton onePlayerRadio = (RadioButton)rootView.findViewById(R.id.radio_one_player);
		onePlayerRadio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PlayersFragment.this.numPlayers = 1;
				PlayersFragment.this.togglePlayer2Views(rootView, false);
			}
		});
		
		RadioButton twoPlayerRadio = (RadioButton)rootView.findViewById(R.id.radio_two_players);
		twoPlayerRadio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PlayersFragment.this.numPlayers = 2;
				PlayersFragment.this.togglePlayer2Views(rootView, true);
			}
		});
    }
    
    private void togglePlayer2Views(View rootView, boolean show) {
    	TextView player2Label = (TextView)rootView.findViewById(R.id.name_label2);
    	player2Label.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    	
    	final EditText player2Input = (EditText)rootView.findViewById(R.id.player2_name_input);
    	player2Input.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        if (activity instanceof OnGameStartSelectedListener) {
          this.listener = (OnGameStartSelectedListener) activity;
        } else {
          throw new ClassCastException(activity.toString()
              + " must implemenet PlayersFragment.OnGameStartSelectedListener");
        }
      }
    
    public interface OnGameStartSelectedListener {
    	public void onGameStarted(Vector<Player> players);
    }
}
