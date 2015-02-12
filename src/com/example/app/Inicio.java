package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class Inicio extends Activity{
	 ProgressBar mProgressBar;
	    int progreso=0;
	    int paso = 500;
	    final int WELCOME = 25;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inicio);
		 mProgressBar=(ProgressBar) findViewById(R.id.progressbar);
	}
	
	private void cuentaAtras(long milisegundos){
	    
	    CountDownTimer mCountDownTimer;
	    mProgressBar.setMax((int)milisegundos);
	    mProgressBar.setProgress(paso);

	    mCountDownTimer=new CountDownTimer(milisegundos, paso) {
	        
	        @Override
	        public void onTick(long millisUntilFinished) {
	            
	            progreso+=paso;
	            mProgressBar.setProgress(progreso);
	        }

	        @Override
	        public void onFinish() {
	    
	            progreso+= paso;
	            mProgressBar.setProgress(progreso);
	            Intent i = new Intent (".MainActivity");
	            startActivityForResult(i, WELCOME);}
	           
	        };
	    mCountDownTimer.start();
	    }
	 @Override
	    protected void onResume() {
	        super.onResume();

	     
	        cuentaAtras(3000);   //3 sec.
	    }
	   @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		   
		   if (requestCode==WELCOME)
				// volvemos a la pantalla de bienvenida desde la pantalla principal,
				// la cerramos (no tiene sentido permanecer aquí):
				finish();   
			else
				super.onActivityResult(requestCode, resultCode, data);
	        }
}
