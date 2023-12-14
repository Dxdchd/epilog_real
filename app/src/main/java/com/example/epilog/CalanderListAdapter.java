package com.example.epilog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalanderListAdapter extends RecyclerView.Adapter<CalanderListAdapter.MonthView> {
    private final int center = Integer.MAX_VALUE / 2;
    private Calendar calendar = Calendar.getInstance();
    public class MonthView extends RecyclerView.ViewHolder {
        public View layout;
        public TextView textView;

        public MonthView(View layout) {
            super(layout);
            this.layout = layout;
            this.textView = layout.findViewById(R.id.item_month_text);
        }
    }

    @Override
    public MonthView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calanderview, parent, false);
        return new MonthView(view);
    }

    @Override
    public void onBindViewHolder(MonthView holder, int position) {
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, position - center);
        holder.textView.setText(
                calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월"
        );
        int tempMonth = calendar.get(Calendar.MONTH);
        List<Date> dayList = new ArrayList<>(6 * 7);
        for (int i = 0; i < 6; i++) {
            for (int k = 0; k < 7; k++) {
                calendar.add(Calendar.DAY_OF_MONTH, (1 - calendar.get(Calendar.DAY_OF_WEEK)) + k);
                dayList.add(calendar.getTime());
            }
            calendar.add(Calendar.WEEK_OF_MONTH, 1);
        }
        GridLayoutManager dayListManager = new GridLayoutManager(holder.layout.getContext(), 7);
        AdapterDay dayListAdapter = new AdapterDay(tempMonth, dayList,holder.layout);

        RecyclerView dayListView = holder.layout.findViewById(R.id.calendarview);
        dayListView.setLayoutManager(dayListManager);
        dayListView.setAdapter(dayListAdapter);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}
