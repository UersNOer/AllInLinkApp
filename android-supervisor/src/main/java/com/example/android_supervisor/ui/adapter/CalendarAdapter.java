package com.example.android_supervisor.ui.adapter;

import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;

import java.util.Calendar;
import java.util.Date;

/**
 * @author wujie
 */
public class CalendarAdapter extends ObjectAdapter<Date> {

    public CalendarAdapter() {
        super(R.layout.item_calendar_day);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, Date date, int position) {
        TextView tvDay = holder.getView(android.R.id.text1, TextView.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
    }
}
