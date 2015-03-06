package com.example.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AyudaFragmento extends Fragment {

    private static final String ARG_POSITION = "position";

    TextView textView;

    private int position;

    public static AyudaFragmento newInstance(int position) {
        AyudaFragmento f = new AyudaFragmento();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ayuda,container,false);
        textView = (TextView) rootView.findViewById(R.id.textView);
        ViewCompat.setElevation(rootView, 50);
        textView.setText("CARD "+position);
        return rootView;
    }
}