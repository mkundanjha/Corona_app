package com.example.coronawidget;

import android.animation.ValueAnimator;
import android.content.*;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
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

import java.security.PublicKey;
import java.text.DecimalFormat;

import java.util.*;


public class MainActivity<SharedPref> extends AppCompatActivity {

    private RequestQueue queue;
    private RequestQueue queue2;

    public TextView totalCaseView;
    public TextView totalRecoveredView;
    public TextView totalDeathView;
    public TextView dailyConfirmedView;
    public TextView dailyRecoveredView;
    public TextView dailyDeathView;
    public TextView dateView;
    public TextView wTCaseView;
    public TextView wTRecView;
    public TextView wTDeathView;
    public TextView wDCaseView;
    public TextView wDRecView;
    public TextView wDDeathView;
    public TextView tCountry1;
    public TextView tCountry1Data;
    public TextView tCountry2;
    public TextView tCountry2Data;
    public TextView tCountry3;
    public TextView tCountry3Data;
    public TextView topCountries;

    public ImageView redDot;
    public ImageView yellowDot;
    public ImageView greenDot;

//    LineChart chart;
//    ArrayList<Entry> tCaseData=new ArrayList<>();
//    ArrayList<Entry> tRecoveredData=new ArrayList<>();
//    ArrayList<Entry> tDeathData=new ArrayList<>();

    ArrayList<String> date=new ArrayList<String>();
    ArrayList<Integer> activeWorld=new ArrayList<Integer>();
    HashMap<Integer,String> activeWCase=new HashMap<>();


//    public Button showGraph;
    public ImageButton showGraph;
    public ImageButton showTable;
    public ImageButton showSearch;
    public ImageButton showCreator;

    AlertDialog.Builder builder;

    private Switch myswitch;
    SharedPre sharedprep;







//    String url = "https://api.covid19india.org/data.json";
    String url = "https://api.covid19api.com/summary";

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        sharedprep=new SharedPre(this);
        if(sharedprep.loadNightModeState()==true){
            setTheme(R.style.AppTheme);
        }else {
        setTheme(R.style.LightMode);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myswitch=findViewById(R.id.switch1);
        if (sharedprep.loadNightModeState()==true) {
            myswitch.setChecked(true);
        }
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedprep.setNightModeState(true);
                    restartApp();
                }
                else {
                    sharedprep.setNightModeState(false);
                    restartApp();
                }
            }
        });





        queue= Volley.newRequestQueue(this);

        totalCaseView=findViewById(R.id.textView2);
        totalRecoveredView=findViewById(R.id.textView6);
        totalDeathView=findViewById(R.id.textView7);
        dailyConfirmedView=findViewById(R.id.textView10);
        dailyRecoveredView=findViewById(R.id.textView8);
        dailyDeathView=findViewById(R.id.textView9);
        dateView=findViewById(R.id.textView12);
        wTCaseView=findViewById(R.id.textView19);
        wTRecView=findViewById(R.id.textView20);
        wTDeathView=findViewById(R.id.textView21);
        wDCaseView=findViewById(R.id.textView22);
        wDRecView=findViewById(R.id.textView23);
        wDDeathView=findViewById(R.id.textView24);

        tCountry1=findViewById(R.id.textView37);
        tCountry1Data=findViewById(R.id.textView40);
        tCountry2=findViewById(R.id.textView38);;
        tCountry2Data=findViewById(R.id.textView41);
        tCountry3=findViewById(R.id.textView39);
        tCountry3Data=findViewById(R.id.textView42);
        topCountries=findViewById(R.id.textView43);

        redDot=findViewById(R.id.imageView2);
        yellowDot=findViewById(R.id.imageView3);
        greenDot=findViewById(R.id.imageView4);

//        showGraph=(ImageButton) findViewById(R.id.imageView3);
        showGraph=findViewById(R.id.imageButton3);
        showTable=findViewById(R.id.imageButton4);
        showSearch=findViewById(R.id.imageButton5);
        showCreator=findViewById(R.id.imageButton2);
        builder=new AlertDialog.Builder(this);



        redDot.setVisibility(View.INVISIBLE);
        yellowDot.setVisibility(View.INVISIBLE);
        greenDot.setVisibility(View.INVISIBLE);


        if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
        {
            fetch();
            showGraph.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goGraph();
                }
            });
            showTable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goTable();
                }
            });

            showCreator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.setMessage("Thank you for using our app!\nThis app is designed to show the live data of Coronavirus spread around the world.\nStay home and stay safe.\nLet's fight this pandemic together.\n\nDeveloper: Kundan Jha\nMail: kundanjha38@gmail.com\nPlease contact for any query!!\n\nSource:\ncovid19api.com\ncovid19india.org ")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert=builder.create();
                    alert.setTitle("App Info");
                    alert.show();
                }
            });
            showSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goWorldActivity();
                }
            });

        }
        else
        {
            Toast.makeText(MainActivity.this,"No Internet Connection",1000).show();
        }





    }
    public void restartApp () {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
    public void goWorldActivity(){
        Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(intent);
        finish();
    }


    public void goGraph(){
        Intent intent=new Intent(getApplicationContext(),GraphActivity.class);
        startActivity(intent);
        finish();

    }




    public void fetch(){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

//
                    String date="";
                    String year="";
                    String time="";

                    JSONObject jsonObject=response.getJSONObject("Global");



//                    World Data :
                    String wCase=jsonObject.getString("TotalConfirmed");
                    String wRec=jsonObject.getString("TotalRecovered");
                    String wDeath=jsonObject.getString("TotalDeaths");
                    String wDCase=jsonObject.getString("NewConfirmed");
                    String wDRec=jsonObject.getString("NewRecovered");
                    String wDDeath=jsonObject.getString("NewDeaths");

                    startCountAnimation(Integer.parseInt(wCase),wTCaseView);
                    startCountAnimation(Integer.parseInt(wRec),wTRecView);
                    startCountAnimation(Integer.parseInt(wDeath),wTDeathView);
                    startCountAnimationDcase(Integer.parseInt(wDCase),wDCaseView);
                    startCountAnimationDcase(Integer.parseInt(wDRec),wDRecView);
                    startCountAnimationDcase(Integer.parseInt(wDDeath),wDDeathView);

                    JSONArray jsonArray=response.getJSONArray("Countries");


                    int activePerCountry;

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);

                        activePerCountry=Integer.parseInt(jsonObject1.getString("TotalConfirmed"))
                                -Integer.parseInt(jsonObject1.getString("TotalRecovered"))
                                -Integer.parseInt(jsonObject1.getString("TotalDeaths"));

                        activeWCase.put(activePerCountry,jsonObject1.getString("Country"));
                        activeWorld.add(activePerCountry);






                        if(jsonObject1.getString("CountryCode").equals("IN")){
                            startCountAnimation(Integer.parseInt(jsonObject1.getString("TotalConfirmed")),
                                    totalCaseView);
                            startCountAnimation(Integer.parseInt(jsonObject1.getString("TotalRecovered")),
                                    totalRecoveredView);
                            startCountAnimation(Integer.parseInt(jsonObject1.getString("TotalDeaths")),
                                    totalDeathView);
                            startCountAnimationDcase(Integer.parseInt(jsonObject1.getString("NewConfirmed")),
                                    dailyConfirmedView);
                            startCountAnimationDcase(Integer.parseInt(jsonObject1.getString("NewRecovered")),
                                    dailyRecoveredView);
                            startCountAnimationDcase(Integer.parseInt(jsonObject1.getString("NewDeaths")),
                                    dailyDeathView);
                        }
                    }


                    Collections.sort(activeWorld);
                    runAnimation(redDot,1000);
                    runAnimation(yellowDot,1200);
                    runAnimation(greenDot,1400);
//                    runAnimation(showGraph,200);
//                    runAnimation(showTable,400);
//                    runAnimation(showSearch,600);
//                    runAnimation(showCreator,800);

                    runAnimation(topCountries,500);
                    topCountries.setText("Top 3 Countries With Most Active Cases");
                    runAnimation(tCountry1,1000);
                    tCountry1.setText(activeWCase.get(activeWorld.get(activeWorld.size()-1)));
                    runAnimation(tCountry2,1200);
                    tCountry2.setText(activeWCase.get(activeWorld.get(activeWorld.size()-2)));
                    runAnimation(tCountry3,1400);
                    tCountry3.setText(activeWCase.get(activeWorld.get(activeWorld.size()-3)));
                    runAnimation(tCountry1Data,1000);
                    startCountAnimation(activeWorld.get(activeWorld.size()-1),tCountry1Data);
                    runAnimation(tCountry2Data,1200);
                    startCountAnimation(activeWorld.get(activeWorld.size()-2),tCountry2Data);
                    runAnimation(tCountry3Data,1400);
                    startCountAnimation(activeWorld.get(activeWorld.size()-3),tCountry3Data);

//                    Setting the date
                    date=response.getString("Date");
                    int count=0;
                    for(int i=0; i<date.length();i++){
                        if(Character.compare(date.charAt(i), 'T')==0) {
                            break;}
                            year=year+date.charAt(i);
                            count++;
                    }
                    for(int i=count+1;i<date.length()-4;i++){
                        time=time+date.charAt(i);
                    }
                    runAnimation(dateView,1600);
                    dateView.setText("last updated on "+year+"  T: "+time);

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

//////////////////

    private void runAnimation(View t,long delay)
    {
        Animation a = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        a.reset();
        a.setStartOffset(delay);
        t.setVisibility(View.VISIBLE);
        t.clearAnimation();

        t.startAnimation(a);
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

    public void goTable(){
        Intent intent=new Intent(this,StateDataTableActivity.class);
        startActivity(intent);
        finish();
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




//    public static void createGraph(int graphWidth, String description, int yaxis){
//
//
//        XAxis xAxis=chart.getXAxis();
//
//        xAxis.setDrawGridLines(false);      // remove vertical grid lines
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);      // set bottom x-axis
//        xAxis.setTextColor(Color.GRAY);
//    //    xAxis.setAxisMaximum(170f);
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
//        set.setColor(Color.parseColor("#573285"));
//        set.setLineWidth(graphWidth);
//
//        LineDataSet set2=new LineDataSet(tRecoveredData,"Total Recovered");
//        set2.setFillAlpha(30);
//        set2.setDrawCircles(false);
//        set2.setDrawValues(true);
//        set2.setColor(Color.parseColor("#339e6a"));
//        set2.setLineWidth(graphWidth);
//
//        LineDataSet set3=new LineDataSet(tDeathData,"Total Death");
//        set3.setFillAlpha(30);
//        set3.setDrawCircles(false);
//        set3.setDrawValues(true);
//        set3.setColor(Color.parseColor("#a13a52"));
//        set3.setLineWidth(graphWidth);
//
//
//
//        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
//        dataSets.add(set);
//        dataSets.add(set2);
//        dataSets.add(set3);
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
//
//}

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

    public static void startCountAnimationDcase(int leng, final TextView view){
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
