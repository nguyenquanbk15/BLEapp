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

public class SpO2Fragment extends Fragment implements MyInterface{
    private GraphView SpO2Graph;
    private TextView tvSpO2Idx;
    private LineGraphSeries<DataPoint> mSeries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_spo2, container, false);
        tvSpO2Idx = view.findViewById(R.id.tv_spo2_index);
        SpO2Graph = view.findViewById(R.id.gv_spo2_graph);
        mSeries = new LineGraphSeries<>();
        SpO2Graph.addSeries(mSeries);


        return view;
    }

    @Override
    public void setResult(String message) {

    }
}
