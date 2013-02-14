package com.app.locationtracker;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.app.exception.BaseException;
import com.app.map.DraggableMarker;
import com.app.map.IListenLocation;
import com.app.util.GPSTracker;
import com.app.widget.MessageDialog;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Projection;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class SelectLocationActivity extends com.google.android.maps.MapActivity implements IListenLocation
{
    private MapView map = null;
    private MyLocationOverlay me = null;
    private MessageDialog dialog = null;
    private GPSTracker gps = null;
    private GeoPoint selectedGeoPoint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_select_location);

	initMap();
    }

    public void initMap()
    {
	map = (MapView) findViewById(R.id.mapView);
	Projection projection = map.getProjection();
	int y = map.getHeight() / 2;
	int x = map.getWidth() / 2;

	gps = new GPSTracker(SelectLocationActivity.this);
	GeoPoint centerGeoPoint = null;
	if (gps.canGetLocation() && gps.getLocation() != null && gps.getLocation().getLatitude() != 0.00)
	{
	    // System.out.println("GPS");
	    // IF GPS PROVIDED LOCATION
	    int lat = (int) (gps.getLocation().getLatitude() * 1E6);
	    int lng = (int) (gps.getLocation().getLongitude() * 1E6);
	    centerGeoPoint = new GeoPoint(lat, lng);
	}
	else
	{
	    // System.out.println("MAP");
	    // IF GPS NOT PROVIDED LOCATION USE DEFAULT MAP LOCATION
	    centerGeoPoint = projection.fromPixels(x, y);
	}

	this.selectedGeoPoint = centerGeoPoint;
	map.getController().setCenter(centerGeoPoint);

	map.setBuiltInZoomControls(true);
	map.getController().setZoom(17);
	addOverlays(centerGeoPoint);
    }

    public void clearOverlays()
    {
	map.getOverlays().clear();
	((ImageView) findViewById(R.id.drag)).setVisibility(View.GONE);
    }

    public void addOverlays(GeoPoint centerGeoPoint)
    {
	// MARKER
	Drawable marker = getResources().getDrawable(R.drawable.pointer_red);
	marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
	ImageView dragImage = ((ImageView) findViewById(R.id.drag));
	map.getOverlays().add(new DraggableMarker(marker, centerGeoPoint, dragImage, this.map, this));
	me = new MyLocationOverlay(this, map);
	map.getOverlays().add(me);
	map.getController().setCenter(centerGeoPoint);
    }

    @Override
    protected void onPause()
    {
	super.onPause();

	// me.disableCompass();
    }

    @Override
    protected void onResume()
    {
	super.onResume();

	// me.enableCompass();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
	if (keyCode == KeyEvent.KEYCODE_S)
	{
	    map.setSatellite(!map.isSatellite());
	    return (true);
	}
	else if (keyCode == KeyEvent.KEYCODE_Z)
	{
	    map.displayZoomControls(true);
	    return (true);
	}

	return (super.onKeyDown(keyCode, event));
    }

    public void btnMapGoClick(View view)
    {
	try
	{
	    EditText txt = (EditText) findViewById(R.id.txtReminderLocation);

	    if (txt.getText().toString().trim().equals(""))
	    {
		dialog = new MessageDialog("Please enter location name", "Alert", MessageDialog.MESSAGE_WARN, SelectLocationActivity.this);
	    }
	    else
	    {
		String locationName = txt.getText().toString().replace("\n", " ").replace(" ", "%20");

		GeoPoint newCenterPoint = getGeoPoint(getLocationInfo(locationName));

		if (newCenterPoint == null)
		{
		    throw new BaseException(103);
		}
		clearOverlays();
		addOverlays(newCenterPoint);
		this.selectedGeoPoint = newCenterPoint;
	    }
	}
	catch (BaseException bEx)
	{
	    dialog = new MessageDialog(bEx, SelectLocationActivity.this);
	}
	catch (Exception ex)
	{
	    dialog = new MessageDialog(new BaseException(ex, 103), SelectLocationActivity.this);
	}
    }

    public void btnMapDoneClick(View view)
    {
	Intent resultData = new Intent();
	resultData.putExtra("selected_lat", this.selectedGeoPoint.getLatitudeE6());
	resultData.putExtra("selected_long", this.selectedGeoPoint.getLongitudeE6());
	setResult(Activity.RESULT_OK, resultData);
	finish();
    }

    private GeoPoint getPoint(double lat, double lon)
    {
	return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
    }

    public JSONObject getLocationInfo(String address) throws Exception
    {
	HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" + address + "&ka&sensor=false");
	HttpClient client = new DefaultHttpClient();
	HttpResponse response;
	StringBuilder stringBuilder = new StringBuilder();
	try
	{
	    response = client.execute(httpGet);
	    HttpEntity entity = response.getEntity();
	    InputStream stream = entity.getContent();
	    int b;
	    while ((b = stream.read()) != -1)
	    {
		stringBuilder.append((char) b);
	    }
	}
	catch (ClientProtocolException e)
	{
	    throw e;
	}
	catch (IOException e)
	{
	    throw e;
	}

	JSONObject jsonObject = new JSONObject();
	try
	{
	    jsonObject = new JSONObject(stringBuilder.toString());
	}
	catch (JSONException e)
	{
	    throw e;
	}
	return jsonObject;
    }

    public GeoPoint getGeoPoint(JSONObject jsonObject) throws Exception
    {
	Double lon = new Double(0);
	Double lat = new Double(0);
	try
	{
	    lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
	    lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
	}
	catch (JSONException e)
	{
	    return null;
	}

	return new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
    }

    @Override
    public void setNewGeoPoint(GeoPoint geoPoint)
    {
	this.selectedGeoPoint = geoPoint;
    }

    @Override
    protected boolean isRouteDisplayed()
    {
	return false;
    }

}
