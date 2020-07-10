package com.example.coronawidget;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Calendar;



public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    private RequestQueue queue2;

    public TextView totalCaseView;
    public TextView totalRecoveredView;
    public TextView totalDeathView;
    public TextView dailyConfirmedView;
    public TextView dailyRecoveredView;
    public TextView dailyDeathView;
    public TextView dateView;
    LineChart chart;
    ArrayList<Entry> tCaseData=new ArrayList<>();
    ArrayList<String> date=new ArrayList<String>();

    String[] dta=new String[50];




    int year = Calendar.getInstance().get(Calendar.YEAR);


    String url = "https://api.covid19india.org/data.json";

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue= Volley.newRequestQueue(this);
        queue2=Volley.newRequestQueue(this);
        totalCaseView=findViewById(R.id.textView2);
        totalRecoveredView=findViewById(R.id.textView6);
        totalDeathView=findViewById(R.id.textView7);
        dailyConfirmedView=findViewById(R.id.textView10);
        dailyRecoveredView=findViewById(R.id.textView8);
        dailyDeathView=findViewById(R.id.textView9);
        dateView=findViewById(R.id.textView12);

        chart=findViewById(R.id.linechart1);


        fetch();



//        Toast.makeText(this, vdata, Toast.LENGTH_SHORT).show();

//        setGraphData();
//        createGraph();





    }



    public void fetch(){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tCaseData.clear();
                    String tValue="a";

                    JSONArray jsonArray = response.getJSONArray("cases_time_series");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject tCaseGraphData=jsonArray.getJSONObject(i);
                        tValue=tCaseGraphData.getString("totalconfirmed");
                        date.add(tCaseGraphData.getString("date"));
//
                        tCaseData.add(new Entry(i,Float.parseFloat(tValue)/100000));
//

                    }







                    JSONObject latestData=jsonArray.getJSONObject(jsonArray.length()-1);

                    String totalCase=latestData.getString("totalconfirmed");
                    String totalRecovered=latestData.getString("totalrecovered");
                    String totalDeath=latestData.getString("totaldeceased");
                    String dailyConfirmed=latestData.getString("dailyconfirmed");
                    String dailyRecovered=latestData.getString("dailyrecovered");
                    String dailyDeath=latestData.getString("dailydeceased");
                    String date=latestData.getString("date");


//                    String tCase=format(Integer.valueOf(totalCase));
//                    String tRec=format(Integer.valueOf(totalRecovered));
//                    String tDeath=format(Integer.valueOf(totalDeath));
//                    String dCase=format(Integer.valueOf(dailyConfirmed));
//                    String dRec=format(Integer.valueOf(dailyRecovered));
//                    String dDeath=format(Integer.valueOf(dailyDeath));

//                    vdata=tCase;
                    startCountAnimation(Integer.parseInt(totalCase),totalCaseView);
                    startCountAnimation(Integer.parseInt(totalRecovered),totalRecoveredView);
                    startCountAnimation(Integer.parseInt(totalDeath),totalDeathView);
                    startCountAnimation(Integer.parseInt(totalCase),totalCaseView);
//                    totalCaseView.setText(tCase);
//                    totalRecoveredView.setText(tRec);
//                    totalDeathView.setText(tDeath);
                    startCountAnimationDcase(Integer.parseInt(dailyConfirmed),dailyConfirmedView);
                    startCountAnimationDcase(Integer.parseInt(dailyDeath),dailyDeathView);
                    startCountAnimationDcase(Integer.parseInt(dailyRecovered),dailyRecoveredView);
//                    dailyConfirmedView.setText("+ "+dCase);
//                    dailyRecoveredView.setText("+ "+dRec);
//                    dailyDeathView.setText("+ "+dDeath);
                    dateView.setText("as on "+date+", "+year);

                    createGraph(3,"Total Confirmed Cases",Integer.parseInt(totalCase)/100000);

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        queue.add(jsonObjectRequest);
    }

//    #########################################################################################################
    public static String format(float value) {
        if(value < 1000) {
            return format("###", value);
        } else {
            double hundreds = value % 1000;
            int other = (int) (value / 1000);
            return format(",##", other) + ',' + format("000", hundreds);
        }
    }

    public static String format(Double value) {
        if(value < 1000) {
            return format("###", value);
        } else {
            double hundreds = value % 1000;
            int other = (int) (value / 1000);
            return format(",##", other) + ',' + format("000", hundreds);
        }
    }

    private static String format(String pattern, Object value) {

        return new DecimalFormat(pattern).format(value);
    }




//    ############## Charts functions ###########
//
//    private ArrayList<Entry> setGraphData(){
//        int data;
//        tCaseData.clear();
////        Toast.makeText(this,tdata.get(0),Toast.LENGTH_SHORT).show();
//
//        for(int i=0;i<100;i++){
////            data= tdata.get(i);
//            tCaseData.add(new Entry(i,i+1));
//        }
//
//
//        return tCaseData;
//
//    }




    public void createGraph(int graphWidth,String description,int yaxis){


        XAxis xAxis=chart.getXAxis();

        xAxis.setDrawGridLines(false);      // remove vertical grid lines
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);      // set bottom x-axis
        xAxis.setTextColor(Color.GRAY);
    //    xAxis.setAxisMaximum(170f);
        xAxis.setAxisLineWidth(2);




        YAxis yAxis3=chart.getAxisLeft();
        yAxis3.setTextColor(Color.GRAY);
        yAxis3.setAxisMaximum(yaxis+1);
        yAxis3.setAxisMinimum(0f);
        yAxis3.setAxisLineWidth(2);


        //      Chart Properties
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.setScaleEnabled(false);


        LineDataSet set = new LineDataSet(tCaseData, description);

        set.setFillAlpha(30);
        set.setDrawCircles(false);
    //    set.setCircleRadius((float) 0.3);
        set.setDrawValues(true);
        set.setColor(Color.parseColor("#e84d4a"));
        set.setLineWidth(graphWidth);


        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(set);

        LineData data=new LineData(dataSets);

        chart.setData(data);
        chart.setTouchEnabled(true);
//
        chart.getDescription().setEnabled(false);
        chart.getDescription().setTextSize(9);

        IMarker marker=new YourMakerView(getApplicationContext(),R.layout.contentview);
        chart.setMarker(marker);
        chart.animateX(900);


}

private void startCountAnimation(int leng, final TextView view){
    final ValueAnimator animator=ValueAnimator.ofInt(0,leng);
    animator.setDuration(1000);
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            view.setText(format((Integer) animator.getAnimatedValue()));
        }
    });
    animator.start();
}

    private void startCountAnimationDcase(int leng, final TextView view){
        final ValueAnimator animator=ValueAnimator.ofInt(0,leng);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setText("+ "+format((Integer) animator.getAnimatedValue()));
            }
        });
        animator.start();
    }




}
