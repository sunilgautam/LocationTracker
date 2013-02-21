package com.app.locationtracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.app.pojo.Setting;
import com.app.util.Utility;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity
{
    Spinner spnSnooze = null;
    Spinner spnTone = null;
    CheckBox cVibrate = null;
    Context context = null;
    Setting setting = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_settings);

	spnSnooze = (Spinner) findViewById(R.id.spnSnoozeTimeout);
	spnTone = (Spinner) findViewById(R.id.spnAlarmTone);
	cVibrate = (CheckBox) findViewById(R.id.chkVibrate);

	context = getBaseContext();
	setting = Utility.getSettings(context);

	List<String> arrTimes = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.set_snooze_times)));
	List<String> arrTones = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.set_alarm_tones)));

	spnSnooze.setSelection(arrTimes.indexOf(Utility.getSnoozeTimeoutText(setting.getSnoozeTimeout())));
	spnTone.setSelection(arrTones.indexOf(setting.getAlarmTone()));
	cVibrate.setChecked(setting.isVibrate());
    }

    public void btnSettingSaveClick(View view)
    {
	setting.setSnoozeTimeout(Utility.getSnoozeTimeoutValue(String.valueOf(spnSnooze.getSelectedItem())));
	setting.setAlarmTone(String.valueOf(spnTone.getSelectedItem()));
	setting.setVibrate(cVibrate.isChecked());

	Utility.setSettings(context, setting);
	Toast.makeText(getBaseContext(), R.string.msg_set_success, Toast.LENGTH_LONG).show();
	finish();
    }

}
