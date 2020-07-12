package com.example.coronawidget;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class BarActivity extends AppCompatActivity {

    public Button golineGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bargraph);
        golineGraph=findViewById(R.id.button4);

        Toast.makeText(getApplicationContext(),"Sorry the work for Bar graph in under process",Toast.LENGTH_LONG).show();

        golineGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }


    public void onBackPressed(){
        Intent intent=new Intent(getApplicationContext(),GraphActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();

    }

//    public void createGraph(BarActivity chart, int graphWidth, String description,
//                            int yaxis, ArrayList<Entry> tCaseData, int colour){
//
//
//        XAxis xAxis=chart.get
//
//        xAxis.setDrawGridLines(false);      // remove vertical grid lines
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);      // set bottom x-axis
//        xAxis.setTextColor(Color.GRAY);
//        //    xAxis.setAxisMaximum(170f);
//        xAxis.setAxisLineWidth(2);
//
//
//
//
//        YAxis yAxis3=chart.getAxisLeft();
//        yAxis3.setTextColor(Color.GRAY);
//        yAxis3.setAxisMaximum(yaxis+1);
//        yAxis3.setAxisMinimum(0f);
//        yAxis3.setAxisLineWidth(2);
//
//
//        //      Chart Properties
//        chart.getAxisRight().setEnabled(false);
//        chart.getAxisLeft().setDrawGridLines(false);
//        chart.setScaleEnabled(false);
//        chart.getLegend().setTextColor(Color.WHITE);
//
//
//        LineDataSet set = new LineDataSet(tCaseData, description);
//
//        set.setFillAlpha(30);
//        set.setDrawCircles(false);
//        set.setDrawValues(true);
//        set.setColor(colour);
//        set.setLineWidth(graphWidth);
//
//
//        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
//        dataSets.add(set);
//
//
//        LineData data=new LineData(dataSets);
//
//        chart.setData(data);
//        chart.setTouchEnabled(true);
////
//        chart.getDescription().setEnabled(false);
//
//
//        IMarker marker=new YourMakerView(getApplicationContext(),R.layout.contentview);
//        chart.setMarker(marker);
//        chart.animateX(900);
//
//    }

}
