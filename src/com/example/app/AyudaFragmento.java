package com.example.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AyudaFragmento extends Fragment {

	private static final String ARG_POSITION = "position";

	TextView textView;
	TextView textView1;
	TextView textView2;
	TextView textView3;
	TextView textView4;
	ImageView imageView1;
	ImageView imageView2;
	ImageView imageView3;
	ImageView imageView4;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_ayuda, container,
				false);
		textView = (TextView) rootView.findViewById(R.id.textView);
		textView1 = (TextView) rootView.findViewById(R.id.textView1);
		textView2 = (TextView) rootView.findViewById(R.id.textView2);
		textView3 = (TextView) rootView.findViewById(R.id.textView3);
		textView4 = (TextView) rootView.findViewById(R.id.textView4);
		imageView1 = (ImageView) rootView.findViewById(R.id.imageView1);
		imageView2 = (ImageView) rootView.findViewById(R.id.imageView2);
		imageView3 = (ImageView) rootView.findViewById(R.id.imageView3);
		imageView4 = (ImageView) rootView.findViewById(R.id.imageView4);

		ViewCompat.setElevation(rootView, 50);
		if (position == 0) {
			textView.setText(getString(R.string.ayuda11));
			textView1.setText(getString(R.string.ayuda12));
			textView2.setText(getString(R.string.ayuda13));
			textView3.setText(getString(R.string.ayuda14));
			textView4.setText(getString(R.string.ayuda15));
		} else if (position == 1) {
			textView.setText(getString(R.string.ayuda21));
			textView1.setText(getString(R.string.ayuda22));
			textView2.setVisibility(TextView.GONE);
			textView3.setVisibility(TextView.GONE);
			textView4.setVisibility(TextView.GONE);
			imageView1.setImageResource(R.drawable.medicion);
			imageView2.setVisibility(ImageView.GONE);
			imageView3.setVisibility(ImageView.GONE);
			imageView4.setVisibility(ImageView.GONE);
		} else if (position == 2){
			textView.setText(getString(R.string.ayuda31));
			textView1.setText(getString(R.string.ayuda32));
			textView2.setVisibility(TextView.GONE);
			textView3.setVisibility(TextView.GONE);
			textView4.setVisibility(TextView.GONE);
			imageView1.setImageResource(R.drawable.grafica);
			imageView2.setImageResource(R.drawable.grafica2);
			imageView3.setVisibility(ImageView.GONE);
			imageView4.setVisibility(ImageView.GONE);
		} else if(position == 3){
			textView.setText(getString(R.string.ayuda41));
			textView1.setText(getString(R.string.ayuda42));
			textView2.setVisibility(TextView.GONE);
			textView3.setVisibility(TextView.GONE);
			textView4.setVisibility(TextView.GONE);
			imageView1.setImageResource(R.drawable.cargar);
			imageView2.setVisibility(ImageView.GONE);
			imageView3.setVisibility(ImageView.GONE);
			imageView4.setVisibility(ImageView.GONE);
		}else{
			textView.setText(getString(R.string.ayuda51));
			textView1.setVisibility(TextView.GONE);
			textView2.setVisibility(TextView.GONE);
			textView3.setVisibility(TextView.GONE);
			textView4.setVisibility(TextView.GONE);
			imageView1.setImageResource(R.drawable.definiciones);
			imageView2.setVisibility(ImageView.GONE);
			imageView3.setVisibility(ImageView.GONE);
			imageView4.setVisibility(ImageView.GONE);
		}
		return rootView;
	}
}