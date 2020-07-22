package com.example.coronawidget;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.FormatFlagsConversionMismatchException;

public class GraphActivity extends AppCompatActivity {

    public RequestQueue queue;
    int year = Calendar.getInstance().get(Calendar.YEAR);
    String url = "https://api.covid19india.org/data.json";

    public LineChart tCaseChart;
    public LineChart tRecoveredChart;
    public LineChart tActiveChart;
    public LineChart tDeathChart;

    TextView totalCaseView;
    TextView totalActiveView;
    TextView totalRecoveredView;
    TextView totalDeathView;
    TextView dateView;
    Button goBar;

    ArrayList<Entry> tCaseData=new ArrayList<>();
    ArrayList<Entry> tRecoveredData=new ArrayList<>();
    ArrayList<Entry> tDeathData=new ArrayList<>();
    ArrayList<Entry> tActiveData=new ArrayList<>();

    ArrayList<String> list;

    ArrayList<String> date=new ArrayList<String>();
    SharedPre sharedprep;

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        sharedprep=new SharedPre(this);
        if(sharedprep.loadNightModeState()==true){
            setTheme(R.style.AppTheme);
        }else {
            setTheme(R.style.LightMode);
        }
        setContentView(R.layout.graph_activity);

        queue= Volley.newRequestQueue(this);

        totalCaseView=findViewById(R.id.textView26);
        totalActiveView=findViewById(R.id.textView28);
        totalRecoveredView=findViewById(R.id.textView30);
        totalDeathView=findViewById(R.id.textView33);
        dateView=findViewById(R.id.textView34);

        tCaseChart=findViewById(R.id.line1);
        tActiveChart=findViewById(R.id.line2);
        tRecoveredChart=findViewById(R.id.line3);
        tDeathChart=findViewById(R.id.line4);
        goBar=findViewById(R.id.button3);
        back=findViewById(R.id.imageButton7);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        fetch();



        goBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBarActivity();
            }
        });






    }
    public void goBarActivity(){
        Intent intent=new Intent(GraphActivity.this,BarActivity.class);
        startActivity(intent);
        finish();

    }

    public void onBackPressed(){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();

    }

    public void fetch(){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tCaseData.clear();
                    tRecoveredData.clear();
                    tDeathData.clear();

                    String tValue;
                    String tRec;
                    String tDeath;


                    JSONArray jsonArray = response.getJSONArray("cases_time_series");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject tCaseGraphData=jsonArray.getJSONObject(i);
                        tValue=tCaseGraphData.getString("totalconfirmed");
                        tRec=tCaseGraphData.getString("totalrecovered");
                        tDeath=tCaseGraphData.getString("totaldeceased");

                        list.add(tValue);

                        tRecoveredData.add(new Entry(i,Float.parseFloat(tRec)/100000));
                        tDeathData.add(new Entry(i,Float.parseFloat(tDeath)/1000));
                        tActiveData.add(new Entry(i,(Float.parseFloat(tValue)-Float.parseFloat(tDeath)
                                -Float.parseFloat(tRec))/100000));
                        tCaseData.add(new Entry(i,Float.parseFloat(tValue)/100000));
//
                    }

                    JSONObject latestData=jsonArray.getJSONObject(jsonArray.length()-1);

                    String totalCase=latestData.getString("totalconfirmed");
                    String totalRecovered=latestData.getString("totalrecovered");
                    String totalDeath=latestData.getString("totaldeceased");

                    String date=latestData.getString("date");

                    int activValueAxis=(int) (Integer.parseInt(totalCase)-Integer.parseInt(totalRecovered)
                            - Integer.parseInt(totalDeath));
                    String aValue=Integer.toString(activValueAxis);
                    startCountAnimation(Integer.parseInt(totalCase),totalCaseView);
                    startCountAnimation(Integer.parseInt(totalRecovered),totalRecoveredView);
                    startCountAnimation(Integer.parseInt(totalDeath),totalDeathView);
                    startCountAnimation(Integer.parseInt(totalCase),totalCaseView);
                    startCountAnimation(activValueAxis,totalActiveView);
//
//

                    dateView.setText("as on "+date+", "+year);

                    createGraph(tCaseChart,3,"Total Confirmed Cases",
                            Integer.parseInt(totalCase)/100000,tCaseData,Color.parseColor("#1c2cff"));
                    createGraph(tActiveChart,3,"Active Cases",
                            activValueAxis/100000,tActiveData,Color.parseColor("#d640a0"));
                    createGraph(tRecoveredChart,3,"Total Recovered Case",
                            Integer.parseInt(totalRecovered)/100000,tRecoveredData,Color.parseColor("#2fd445"));
                    createGraph(tDeathChart,3,"Total Death Cases",
                            Integer.parseInt(totalDeath)/1000,tDeathData,Color.parseColor("#ed3b4d"));


//                       calling method to create graph
//                    createGraph(3,"Total Confirmed Cases",Integer.parseInt(totalCase)/100000);

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

        list=new ArrayList<>();
        queue.add(jsonObjectRequest);
    }

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


    public void createGraph(LineChart chart,int graphWidth, String description,
                                   int yaxis,ArrayList<Entry> tCaseData,int colour){


        XAxis xAxis=chart.getXAxis();

        xAxis.setDrawGridLines(false);      // remove vertical grid lines
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);      // set bottom x-axis
        xAxis.setTextColor(Color.GRAY);
        //    xAxis.setAxisMaximum(170f);
        xAxis.setAxisLineWidth(2);
        xAxis.setDrawAxisLine(true);
       xAxis.setDrawGridLines(true);





        YAxis yAxis3=chart.getAxisLeft();
        yAxis3.setTextColor(Color.GRAY);
        yAxis3.setDrawGridLines(true);

        yAxis3.setAxisMaximum(yaxis+1);
        yAxis3.setAxisMinimum(0f);
        yAxis3.setAxisLineWidth(2);



        //      Chart Properties
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.setScaleEnabled(false);
        chart.getLegend().setTextColor(Color.WHITE);



        LineDataSet set = new LineDataSet(tCaseData, description);

        set.setFillAlpha(30);
        set.setDrawCircles(false);
        set.setDrawValues(true);
        set.setColor(colour);
        set.setLineWidth(graphWidth);



        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(set);


        LineData data=new LineData(dataSets);


        chart.setData(data);
        chart.setTouchEnabled(true);
//
        chart.getDescription().setEnabled(false);


        IMarker marker=new YourMakerView(getApplicationContext(),R.layout.contentview);
        chart.setMarker(marker);
        chart.animateX(900);


    }

//    Animation Function
    public static void startCountAnimation(int leng, final TextView view){
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





}
