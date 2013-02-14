package com.app.locationtracker;

import com.app.pojo.Reminder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddLocationActivity extends Activity
{
    private Reminder reminder = null;
    private int REQUEST_SELECT_MAP = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_add_location);

	this.reminder = new Reminder();
    }

    public void btnSelectMapClick(View view)
    {
	Intent intent = new Intent(AddLocationActivity.this, SelectLocationActivity.class);
	startActivityForResult(intent, REQUEST_SELECT_MAP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	super.onActivityResult(requestCode, resultCode, data);

	if (requestCode == REQUEST_SELECT_MAP)
	{
	    if (resultCode == RESULT_OK)
	    {
		int lat = data.getIntExtra("selected_lat", 0);
		int lon = data.getIntExtra("selected_long", 0);
		if (lat != 0 && lon != 0)
		{
		    this.reminder.setLatitude((lat / 1E6));
		    this.reminder.setLongitude((lon / 1E6));

		    TextView txtView = (TextView) findViewById(R.id.tvLocationStatus);
		    Drawable img = getResources().getDrawable(R.drawable.small_success_icon);
		    // img.setBounds( 0, 0, 60, 60 );
		    txtView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
		    txtView.setText("Location : " + String.valueOf(this.reminder.getLatitude()) + ", " + String.valueOf(this.reminder.getLongitude()));
		}
		Toast.makeText(getBaseContext(), lat / 1E6 + "," + lon / 1E6, Toast.LENGTH_SHORT).show();
	    }
	}
    }
}
