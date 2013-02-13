package com.app.locationtracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
import com.app.util.GPSTracker;
import com.app.widget.MessageDialog;
import com.google.android.maps.*;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DragMarkerActivity extends com.google.android.maps.MapActivity
{
    private MapView map = null;
    private MyLocationOverlay me = null;
    private MessageDialog dialog = null;
    private GPSTracker gps = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_drag_marker);

	map = (MapView) findViewById(R.id.mapView);

	Projection projection = map.getProjection();
	int y = map.getHeight() / 2;
	int x = map.getWidth() / 2;

	// GET CURRENT GEO POINT LATER GET BY SERVICE
	// GeoPoint centerGeoPoint = projection.fromPixels(x, y);

	// GeoPoint centerGeoPoint = getPoint(19.07598370, 72.87765589999999);
	gps = new GPSTracker(DragMarkerActivity.this);
	GeoPoint centerGeoPoint = null;

	if (gps.canGetLocation() && gps.getLocation() != null && gps.getLocation().getLatitude() != 0.00)
	{
	    System.out.println("GPS");
	    // IF GPS PROVIDED LOCATION
	    int lat = (int) (gps.getLocation().getLatitude() * 1E6);
	    int lng = (int) (gps.getLocation().getLongitude() * 1E6);
	    centerGeoPoint = new GeoPoint(lat, lng);
	}
	else
	{
	    System.out.println("MAP");
	    // IF GPS NOT PROVIDED LOCATION USE DEFAULT MAP LOCATION
	    centerGeoPoint = projection.fromPixels(x, y);
	}

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
	map.getOverlays().add(new SitesOverlay(marker, centerGeoPoint));
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
    protected boolean isRouteDisplayed()
    {
	return false;
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

    public void btnGoClick(View view)
    {
	try
	{
	    EditText txt = (EditText) findViewById(R.id.editText1);
	    String locationName = txt.getText().toString().replace("\n", " ").replace(" ", "%20");

	    // GeoPoint newCenterPoint =
	    // searchLocationByName(txt.getText().toString());
	    System.out.println("##->" + locationName);
	    GeoPoint newCenterPoint = getGeoPoint(getLocationInfo(locationName));
	    /********************/

	    /********************/

	    if (newCenterPoint == null)
	    {
		throw new BaseException(103);
	    }
	    clearOverlays();
	    addOverlays(newCenterPoint);
	}
	catch (BaseException bEx)
	{
	    dialog = new MessageDialog(bEx, DragMarkerActivity.this);
	}
	catch (Exception ex)
	{
	    dialog = new MessageDialog(new BaseException(ex, 103), DragMarkerActivity.this);
	}
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

    private class SitesOverlay extends ItemizedOverlay<OverlayItem>
    {
	private List<OverlayItem> items = new ArrayList<OverlayItem>();
	private Drawable marker = null;
	private GeoPoint initGeoPoint = null;
	private OverlayItem inDrag = null;
	private ImageView dragImage = null;
	private int xDragImageOffset = 0;
	private int yDragImageOffset = 0;
	private int xDragTouchOffset = 0;
	private int yDragTouchOffset = 0;

	public SitesOverlay(Drawable marker, GeoPoint initGeoPoint)
	{
	    super(marker);
	    this.marker = marker;
	    this.initGeoPoint = initGeoPoint;

	    dragImage = (ImageView) findViewById(R.id.drag);
	    xDragImageOffset = dragImage.getDrawable().getIntrinsicWidth() / 2;
	    yDragImageOffset = dragImage.getDrawable().getIntrinsicHeight();

	    // ADD MARKERS
	    items.add(new OverlayItem(this.initGeoPoint, "Starting Location", "Starting Location"));

	    populate();
	}

	@Override
	protected OverlayItem createItem(int i)
	{
	    return (items.get(i));
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
	    super.draw(canvas, mapView, shadow);

	    boundCenterBottom(marker);
	}

	@Override
	public int size()
	{
	    return (items.size());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView)
	{
	    final int action = event.getAction();
	    final int x = (int) event.getX();
	    final int y = (int) event.getY();
	    boolean result = false;

	    if (action == MotionEvent.ACTION_DOWN)
	    {
		for (OverlayItem item : items)
		{
		    Point p = new Point(0, 0);

		    map.getProjection().toPixels(item.getPoint(), p);

		    if (hitTest(item, marker, x - p.x, y - p.y))
		    {
			result = true;
			inDrag = item;
			items.remove(inDrag);
			populate();

			xDragTouchOffset = 0;
			yDragTouchOffset = 0;

			setDragImagePosition(p.x, p.y);
			dragImage.setVisibility(View.VISIBLE);

			xDragTouchOffset = x - p.x;
			yDragTouchOffset = y - p.y;

			break;
		    }
		}
	    }
	    else if (action == MotionEvent.ACTION_MOVE && inDrag != null)
	    {
		setDragImagePosition(x, y);
		result = true;
	    }
	    else if (action == MotionEvent.ACTION_UP && inDrag != null)
	    {
		dragImage.setVisibility(View.GONE);

		GeoPoint pt = map.getProjection().fromPixels(x - xDragTouchOffset, y - yDragTouchOffset);
		OverlayItem toDrop = new OverlayItem(pt, inDrag.getTitle(), inDrag.getSnippet());

		items.add(toDrop);
		populate();

		inDrag = null;
		result = true;

		Toast.makeText(getBaseContext(), pt.getLatitudeE6() / 1E6 + "," + pt.getLongitudeE6() / 1E6, Toast.LENGTH_SHORT).show();
	    }

	    return (result || super.onTouchEvent(event, mapView));
	}

	private void setDragImagePosition(int x, int y)
	{
	    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dragImage.getLayoutParams();

	    lp.setMargins(x - xDragImageOffset - xDragTouchOffset, y - yDragImageOffset - yDragTouchOffset, 0, 0);
	    dragImage.setLayoutParams(lp);
	}
    }
}