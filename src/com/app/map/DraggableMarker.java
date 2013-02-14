package com.app.map;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class DraggableMarker extends ItemizedOverlay<OverlayItem>
{
    private MapView map = null;
    private IListenLocation listenLocation = null;
    private List<OverlayItem> items = new ArrayList<OverlayItem>();
    private Drawable marker = null;
    private GeoPoint initGeoPoint = null;
    private OverlayItem inDrag = null;
    private ImageView dragImage = null;
    private int xDragImageOffset = 0;
    private int yDragImageOffset = 0;
    private int xDragTouchOffset = 0;
    private int yDragTouchOffset = 0;

    public DraggableMarker(Drawable marker, GeoPoint initGeoPoint, ImageView dragImage, MapView map, IListenLocation listenLocation)
    {
	super(marker);
	this.marker = marker;
	this.initGeoPoint = initGeoPoint;
	this.listenLocation = listenLocation;
	this.map = map;
	// dragImage = (ImageView) findViewById(R.id.drag);
	this.dragImage = dragImage;
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

	    this.listenLocation.setNewGeoPoint(pt);
	    // Toast.makeText(getBaseContext(), pt.getLatitudeE6() / 1E6 + "," +
	    // pt.getLongitudeE6() / 1E6, Toast.LENGTH_SHORT).show();
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
