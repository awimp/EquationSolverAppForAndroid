package com.mobileapp.laba1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;

import java.util.ArrayList;
import java.util.List;

import com.anychart.chart.common.dataentry.ValueDataEntry;

public class Graph extends Fragment {

    private AnyChartView anyChartView;

    public Graph() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        anyChartView = view.findViewById(R.id.any_chart_view);

        double[][] equationCoefficients = getCoefficientsFromTwoEquationsFragment();

        if (equationCoefficients != null) {
            createLinearGraph(equationCoefficients);
        }

        return view;
    }

    private double[][] getCoefficientsFromTwoEquationsFragment() {
        TwoEquations twoEquationsFragment = (TwoEquations) getParentFragmentManager().findFragmentByTag("TwoEquationsTag");

        if (twoEquationsFragment != null) {
            return twoEquationsFragment.getCoefficients();
        } else {
            // Log.e("Graph", "Error retrieving coefficients");
            Toast.makeText(getContext(), "Error retrieving coefficients", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void createLinearGraph(double[][] coefficients) {
        Cartesian cartesian = AnyChart.cartesian();

        // Assuming coefficients[0] contains coefficients of the first equation,
        // and coefficients[1] contains coefficients of the second equation
        cartesian.line(seriesData("Equation 1", coefficients[0]));
        cartesian.line(seriesData("Equation 2", coefficients[1]));

        anyChartView.setChart(cartesian);
    }

    private List<DataEntry> seriesData(String seriesName, double[] coefficients) {
        List<DataEntry> data = new ArrayList<>();
        // Assuming coefficients[0] is the coefficient for x,
        // coefficients[1] is the coefficient for y,
        // coefficients[2] is the constant term
        data.add(new ValueDataEntry("x", coefficients[0]));
        data.add(new ValueDataEntry("y", coefficients[1]));
        data.add(new ValueDataEntry("c", coefficients[2]));

        // Log.d("Graph", "Series data for " + seriesName + ": " + data.toString());

        return data;
    }
}
