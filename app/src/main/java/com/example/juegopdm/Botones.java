package com.example.juegopdm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Botones extends Fragment {
    View rootView;

    private String mParam1;
    private String mParam2;

    public Botones() {

    }


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_botones, container, false);
        FloatingActionButton pauseButton = rootView.findViewById(R.id.resetButton);
        pauseButton.setOnClickListener(pause);

        return rootView;
    }

    public View.OnClickListener pause = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GameView game = (GameView) rootView.findViewById(R.id.gameView);
            Log.i("Paused", "paused");
            game.resetLevel();


        }
    };

}
