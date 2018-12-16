package com.example.ag.bedsoreapp;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

public class MPAndroidChart {
    public static LineChart displayLineChart(List<Entry> lineentries, LineChart lineChart) {
        LineDataSet dataSet = new LineDataSet(lineentries, "Pressure");
        LineData data = new LineData(dataSet);
        lineChart.setData(data);
        lineChart.invalidate();


        // To format the Y-axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setLabelCount(6);
        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(20);
        lineChart.getAxisRight().setEnabled(false);


        // To format the X-axis with HH-mm time format
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new com.example.ag.bedsoreapp.HourAxisValueFormatter());
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setLabelCount(5);
        xAxis.setAvoidFirstLastClipping(true);
        lineChart.setVisibleXRangeMaximum(10000f); //IMPORTANT 100000f
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Disable the legend
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        // For customizing the aesthetics of the graph
        dataSet.setColor(Color.rgb(56, 142, 60));
        dataSet.setCircleColor(Color.rgb(255, 183, 77));
        dataSet.setDrawCircleHole(false);
        dataSet.setCircleRadius(4f);
        dataSet.setLineWidth(4f);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.BLUE);
        lineChart.getDescription().setEnabled(false);
        lineChart.setBackgroundColor(Color.rgb(178, 235, 242));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(true);
        lineChart.setMaxVisibleValueCount(5);   //show text values only if zoomed-in to 5 data points
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.rgb(165, 214, 167));
        Log.i("LineEntries", lineentries.toString());

        return lineChart;
    }
}
