package pdm.test.mappe;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class RunnerActivity extends MapActivity {
	MapView mapView;
	MyLocationOverlay myloc;
	RadiusOverlay termini;
	RadiusOverlay piazza;
	RadiusOverlay colosseo;
	RadiusOverlay romolo;
	PendingIntent mPendingTermini;
	ProximityBroadcast prox;
	LocationManager locationmanager;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapView = (MapView)findViewById(R.id.mapview);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);
        myloc = new MyLocationOverlay(this, mapView);
        myloc.runOnFirstFix(new Runnable() {
			
			@Override
			public void run() {
				mapView.getController().animateTo(myloc.getMyLocation());
				
			}
		});
        GeoPoint gptermini = new GeoPoint(41902022, 12500882);
        termini = new RadiusOverlay(gptermini, 400, Color.BLUE);
        mapView.getOverlays().add(termini);
        
        GeoPoint gppiazza = new GeoPoint(41902622, 12495482);
        piazza = new RadiusOverlay(gppiazza, 300, Color.RED);
        mapView.getOverlays().add(piazza);
        
        GeoPoint gpcolosseo = new GeoPoint(41890310, 12492410);
        colosseo = new RadiusOverlay(gpcolosseo, 500, Color.GREEN);
        mapView.getOverlays().add(colosseo);
        
        GeoPoint gpromolo = new GeoPoint(41890492, 12484823);
        romolo = new RadiusOverlay(gpromolo, 450, Color.YELLOW);
        mapView.getOverlays().add(romolo);
        
        Intent intentTermini = new Intent("pdm.test.mappe");
        intentTermini.putExtra("overlay", 1);
        mPendingTermini = PendingIntent.getBroadcast(this, 1, intentTermini, PendingIntent.FLAG_CANCEL_CURRENT);
        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationmanager.addProximityAlert(gptermini.getLatitudeE6() * 0.000001, gptermini.getLongitudeE6() * 0.000001, 400, -1, mPendingTermini);
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	@Override
	protected void onResume() {
		super.onResume();
		myloc.enableMyLocation();
		registerReceiver(prox, new IntentFilter("pdm.test.mappe"));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		myloc.disableMyLocation();
		unregisterReceiver(prox);
		locationmanager.removeProximityAlert(mPendingTermini);
		
	}
	class ProximityBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			Log.d("TAG", "Proximity Alert");
			Toast.makeText(getApplicationContext(), "Alert di prossimità", Toast.LENGTH_LONG).show();
		}

	}

}
