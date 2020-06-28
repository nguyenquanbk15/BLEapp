package com.example.bleapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class RawDataFragment extends Fragment implements MyInterface{
    private GraphView RedDataGraph;
    private GraphView IRedDataGraph;
    private LineGraphSeries<DataPoint> mSeriesRed;
    private LineGraphSeries<DataPoint> mSeriesIRed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_raw_data, container, false);
        RedDataGraph = view.findViewById(R.id.gv_red_data);
        mSeriesRed = new LineGraphSeries<>();
        RedDataGraph.addSeries(mSeriesRed);

        IRedDataGraph = view.findViewById(R.id.gv_ired_data);
        mSeriesIRed = new LineGraphSeries<>();
        IRedDataGraph.addSeries(mSeriesIRed);

        return view;
    }

    @Override
    public void setResult(String message) {

    }
}
