package com.example.epilog.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.epilog.CalanderListAdapter;
import com.example.epilog.R;
import com.example.epilog.databinding.FragmentCalanderBinding;

import java.util.ArrayList;

public class calander extends Fragment {
    private ArrayList<String> day_of_weekList;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_calander, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        LinearLayoutManager monthListManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        CalanderListAdapter monthListAdapter = new CalanderListAdapter();

        RecyclerView calendarCustom = v.findViewById(R.id.calandermonth);
        calendarCustom.setOnFlingListener(null);
        calendarCustom.setLayoutManager(monthListManager);
        calendarCustom.setAdapter(monthListAdapter);
        calendarCustom.scrollToPosition(Integer.MAX_VALUE / 2);

        PagerSnapHelper snap = new PagerSnapHelper();
        snap.attachToRecyclerView(calendarCustom);
    }
}