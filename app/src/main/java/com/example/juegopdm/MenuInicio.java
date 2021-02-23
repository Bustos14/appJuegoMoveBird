package com.example.juegopdm;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


public class MenuInicio extends Fragment {


    public MenuInicio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView( LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        Log.i("menuFrag", "Created");
        final View rootView = inflater.inflate(R.layout.fragment_menu_inicio, container, false);
        ImageButton playButton = (ImageButton) rootView.findViewById(R.id.play);

        playButton.setOnClickListener(play);

        return rootView;
    }



    //switches to game fragment
    View.OnClickListener play = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Botones btn = new Botones();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main, btn).addToBackStack(null).commit();
        }
    };
}