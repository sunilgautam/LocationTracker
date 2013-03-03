package com.app.locationtracker;

import group.pals.android.lib.ui.filechooser.FileChooserActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.app.pojo.Setting;
import com.app.util.Utility;
import com.app.widget.MessageDialog;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemSelectedListener;

public class SettingsActivity extends Activity implements ViewSwitcher.ViewFactory
{
    private static final int REQUEST_SELECT_FILE = 1001;
    private static final int MIN_SNOOZE_TIMEOUT = 1;
    private static final int MAX_SNOOZE_TIMEOUT = 30;
    private Spinner spnTone = null;
    private CheckBox cVibrate = null;
    private Context context = null;
    private Setting setting = null;
    private List<String> arrTones = null;
    private TextSwitcher switchTimeout;

    boolean isCreated = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_settings);

	isCreated = true;

	spnTone = (Spinner) findViewById(R.id.spnAlarmTone);
	cVibrate = (CheckBox) findViewById(R.id.chkVibrate);
	switchTimeout = (TextSwitcher) findViewById(R.id.switcherTimeout);
	switchTimeout.setFactory(this);

	Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
	Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
	switchTimeout.setInAnimation(in);
	switchTimeout.setOutAnimation(out);

	context = getBaseContext();
	setting = Utility.getSettings(context);

	arrTones = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.set_alarm_tones)));

	spnTone.setSelection(arrTones.indexOf(setting.getAlarmTone()));
	spnTone.setOnItemSelectedListener(selectListener);

	cVibrate.setChecked(setting.isVibrate());

	updateCounter();
    }

    public void btnChangePasswordClick(View view)
    {
	LayoutInflater factory = LayoutInflater.from(this);
	final View passEntryView = factory.inflate(R.layout.change_password_entry, null);
	AlertDialog passDialog = new AlertDialog.Builder(SettingsActivity.this).setIcon(R.drawable.info_icon).setTitle(R.string.set_change_pass).setView(passEntryView).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
	{
	    public void onClick(DialogInterface dialog, int whichButton)
	    {
		EditText txtOlderPassword = (EditText) passEntryView.findViewById(R.id.txtOlderPassword);
		EditText txtNewPassword = (EditText) passEntryView.findViewById(R.id.txtNewPassword);
		EditText txtConfirmPassword = (EditText) passEntryView.findViewById(R.id.txtConfirmPassword);

		if (txtOlderPassword.getText().toString().trim().equals("") || txtNewPassword.getText().toString().trim().equals("") || txtConfirmPassword.getText().toString().trim().equals(""))
		{
		    Toast.makeText(getBaseContext(), R.string.set_change_pass_fail, Toast.LENGTH_SHORT).show();
		}
		else
		{
		    if (!txtNewPassword.getText().toString().equals(txtConfirmPassword.getText().toString()))
		    {
			Toast.makeText(getBaseContext(), R.string.set_change_pass_conf_fail, Toast.LENGTH_SHORT).show();
		    }
		    else
		    {
			if (!Utility.validateUser(getBaseContext(), txtOlderPassword.getText().toString()))
			{
			    Toast.makeText(getBaseContext(), R.string.set_change_pass_fail_1, Toast.LENGTH_SHORT).show();
			}
			else
			{
			    Utility.setPassword(getBaseContext(), txtNewPassword.getText().toString());
			    Toast.makeText(getBaseContext(), R.string.set_change_pass_succ, Toast.LENGTH_SHORT).show();
			}
		    }
		}
	    }
	}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
	{
	    public void onClick(DialogInterface dialog, int whichButton)
	    {

	    }
	}).create();

	passDialog.show();
    }

    public void btnSettingSaveClick(View view)
    {
	setting.setAlarmTone(String.valueOf(spnTone.getSelectedItem()));
	setting.setVibrate(cVibrate.isChecked());

	Utility.setSettings(context, setting);
	Toast.makeText(getBaseContext(), R.string.msg_set_success, Toast.LENGTH_LONG).show();
	finish();
    }

    public void imgBtnUpClick(View view)
    {
	if (setting.getSnoozeTimeout() > MIN_SNOOZE_TIMEOUT)
	{
	    setting.setSnoozeTimeout(setting.getSnoozeTimeout() - 1);
	    updateCounter();
	}
    }

    public void imgBtnDownClick(View view)
    {
	if (setting.getSnoozeTimeout() < MAX_SNOOZE_TIMEOUT)
	{
	    setting.setSnoozeTimeout(setting.getSnoozeTimeout() + 1);
	    updateCounter();
	}
    }

    private void updateCounter()
    {
	switchTimeout.setText(String.valueOf(setting.getSnoozeTimeout()) + " minutes");
    }

    public View makeView()
    {
	TextView t = new TextView(this);
	t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
	t.setTextSize(36);
	t.setTextColor(R.color.light_black);
	return t;
    }

    private final OnItemSelectedListener selectListener = new OnItemSelectedListener()
    {
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
	    if (isCreated)
	    {
		isCreated = false;
		return;
	    }

	    if (Utility.isToneUserDefined(String.valueOf(spnTone.getSelectedItem())))
	    {
		// User defined selected => Show file chooser
		Intent intent = new Intent(getBaseContext(), FileChooserActivity.class);
		intent.putExtra(FileChooserActivity.MultiSelection, false);
		intent.putExtra(FileChooserActivity.RegexFilenameFilter, "(?si).*\\.(mp3|wav)$");
		intent.putExtra(FileChooserActivity.SelectionMode, FileChooserActivity.FilesOnly);

		startActivityForResult(intent, REQUEST_SELECT_FILE);
	    }
	    else
	    {
		setting.setAlarmTone(String.valueOf(spnTone.getSelectedItem()));
	    }
	}

	public void onNothingSelected(AdapterView<?> parent)
	{

	}
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	super.onActivityResult(requestCode, resultCode, data);

	if (requestCode == REQUEST_SELECT_FILE)
	{
	    if (resultCode == RESULT_OK)
	    {
		List<File> list = (List<File>) data.getSerializableExtra(FileChooserActivity.Results);
		if (list != null && list.size() > 0)
		{
		    setting.setAlarmUri(list.get(0).toURI().toString());
		    setting.setAlarmPath(list.get(0).getAbsolutePath());
		}
		else
		{
		    spnTone.setSelection(arrTones.indexOf(setting.getAlarmTone()));
		}
	    }
	    else
	    {
		spnTone.setSelection(arrTones.indexOf(setting.getAlarmTone()));
	    }
	}
    }

}
