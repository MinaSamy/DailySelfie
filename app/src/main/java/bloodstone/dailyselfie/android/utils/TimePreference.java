package bloodstone.dailyselfie.android.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.DialogPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import bloodstone.dailyselfie.android.MainActivity;
import bloodstone.dailyselfie.android.R;
import bloodstone.dailyselfie.android.Receiver.AlarmReceiver;

/**
 * Created by minsamy on 11/18/2015.
 */
public class TimePreference extends DialogPreference {
    private TimePicker mTimePicker;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
        setPersistent(true);
        String savedValue=getPersistedString("");
        if (!TextUtils.isEmpty(savedValue)) {
            long time=Long.parseLong(savedValue);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            setSummary(sdf.format(calendar.getTime()));
        }


    }

    @Override
    protected View onCreateDialogView() {
        mTimePicker = new TimePicker(getContext());
        mTimePicker.setIs24HourView(false);
        return mTimePicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        Calendar c = Calendar.getInstance();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mTimePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));
        } else {
            mTimePicker.setHour(c.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setMinute(c.get(Calendar.MINUTE));
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            int hour = 0;
            int minute = 0;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                hour = mTimePicker.getCurrentHour();
                minute = mTimePicker.getCurrentMinute();
            } else {
                hour = mTimePicker.getHour();
                minute = mTimePicker.getMinute();
            }

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            persistString(String.valueOf(c.getTimeInMillis()));
            callChangeListener(String.valueOf(c.getTimeInMillis()));



        }
    }
}
