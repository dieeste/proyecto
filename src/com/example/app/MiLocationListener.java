package com.example.app;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MiLocationListener implements LocationListener 
{
	private String TAG = "MiLocationListener";
	private Activity actividad;
	private LocationManager locManager = null;
	private Location locGPS = null; // Es la localización del GPS

	public MiLocationListener(Activity actividad)
	{
		this.actividad = actividad;
		inicializaGPS();
	}

	@Override
	public void onLocationChanged(Location location) 
	{
		locGPS = location;
	}
	@Override
	public void onProviderDisabled(String provider) 
	{
	}
	@Override
	public void onProviderEnabled(String provider) 
	{
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
	}

	/**
	 * Des registra el location listener 
	 */
    public void unregisterLocationListener() {
    	locManager.removeUpdates( this );
    }

    /**
     * Registra el location listener
     */
    public void registerLocationListener() {
    	locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1000, 20, this );
    }

	/**
	 * Inicializa el GPS para que tengamos las coordenadas GPS actualizadas
	 * Se puede utilizar cualquiera de las siguientes opciones
	 * 
	 * NETWORK_PROVIDER: Determina la posición del usuario basándose en la celda telefónica en la que se 
	 * encuentre y, si dispone de conexión wifi, de la combinación del punto de acceso al que se encuentre 
	 * conectado.
	 * 
	 * GPS_PROVIDER: Determina la posición el usuario usando satélites GPS. Dependiendo de las condiciones 
	 * en las que se encuentre el usuario puede tardar más tiempo en devolver una posición buena, por 
	 * ejemplo, si se encuentra en espacios interiores.
	 * 
	 * PASSIVE_PROVIDER: Determina la posición del usuario basándose en la recogida de posiciones de otras 
	 * aplicaciones. Por ejemplo, si Google Latitude necesita recoger la posición GPS para enviarla a un 
	 * servicio web, este proveedor se aprovecha de esa acción para obtener la coordenada. Si necesitamos 
	 * saber la posición de un usuario en un determinado momento, este es el proveedor menos adecuado. 
	 * Sin embargo, este es el proveedor que menos batería gasta.
	 */
	private void inicializaGPS() {
		try {
			//Obtenemos una referencia al LocationManager
			locManager = (LocationManager)actividad.getSystemService(Context.LOCATION_SERVICE);
			registerLocationListener();
			//Obtenemos la última posición conocida
			getLastKnownLocation(locManager);
        }catch(Exception e){
    		Log.e(TAG,"inizializaGPS Error: " + e.getMessage(),e);
        }
	}

	/**
	 * Cogemos la última posición conocida
	 * @param manager
	 */
    private void getLastKnownLocation( final LocationManager manager ) 
    {
		locGPS = manager.getLastKnownLocation( LocationManager.GPS_PROVIDER );

        if ( locGPS == null ) {
        	locGPS = manager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
        }

        if ( locGPS == null ) {
        	locGPS = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        
    }

}