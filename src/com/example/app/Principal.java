package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Principal extends Activity implements OnClickListener{
Button entrar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	        setContentView(R.layout.principal);
	        
	        entrar = (Button) findViewById(R.id.entrar);
	        
	        //Agregamos la escucha
	        entrar.setOnClickListener(this);
	        
	}

	@Override
	public void onClick(View boton) {
		// TODO Auto-generated method stub
		Intent cambio=new Intent (".MainActivity");
			startActivity(cambio);
		
		
	}

}
