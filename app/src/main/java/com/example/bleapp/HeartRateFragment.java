package com.example.bleapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class HeartRateFragment extends Fragment implements MyInterface{

    private GraphView HeartRateGraph;
    private TextView tvHeartRateIdx;
    private LineGraphSeries<DataPoint> mSeries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_heart_rate, container, false);
        tvHeartRateIdx = view.findViewById(R.id.tv_heart_rate_index);
        HeartRateGraph = view.findViewById(R.id.gv_heart_rate_graph);
        mSeries = new LineGraphSeries<>();
        HeartRateGraph.addSeries(mSeries);

        return view;
    }

    @Override
    public void setResult(String message) {

    }
}
