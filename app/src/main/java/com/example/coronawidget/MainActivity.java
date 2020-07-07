package com.example.coronawidget;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    public TextView totalCaseView;
    public TextView totalRecoveredView;
    public TextView totalDeathView;
    public TextView dailyConfirmedView;
    public TextView dailyRecoveredView;
    public TextView dailyDeathView;
    public TextView dateView;



    int year = Calendar.getInstance().get(Calendar.YEAR);


    String url = "https://api.covid19india.org/data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue= Volley.newRequestQueue(this);
        totalCaseView=findViewById(R.id.textView2);
        totalRecoveredView=findViewById(R.id.textView6);
        totalDeathView=findViewById(R.id.textView7);
        dailyConfirmedView=findViewById(R.id.textView10);
        dailyRecoveredView=findViewById(R.id.textView8);
        dailyDeathView=findViewById(R.id.textView9);
        dateView=findViewById(R.id.textView12);

        fetch();





    }

    public void fetch(){
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("cases_time_series");
                    JSONObject latestData=jsonArray.getJSONObject(jsonArray.length()-1);

                    String totalCase=latestData.getString("totalconfirmed");
                    String totalRecovered=latestData.getString("totalrecovered");
                    String totalDeath=latestData.getString("totaldeceased");
                    String dailyConfirmed=latestData.getString("dailyconfirmed");
                    String dailyRecovered=latestData.getString("dailyrecovered");
                    String dailyDeath=latestData.getString("dailydeceased");
                    String date=latestData.getString("date");


                    String tCase=format(Integer.valueOf(totalCase));
                    String tRec=format(Integer.valueOf(totalRecovered));
                    String tDeath=format(Integer.valueOf(totalDeath));
                    String dCase=format(Integer.valueOf(dailyConfirmed));
                    String dRec=format(Integer.valueOf(dailyRecovered));
                    String dDeath=format(Integer.valueOf(dailyDeath));

                    totalCaseView.setText(tCase);
                    totalRecoveredView.setText(tRec);
                    totalDeathView.setText(tDeath);
                    dailyConfirmedView.setText("+ "+dCase);
                    dailyRecoveredView.setText("+ "+dRec);
                    dailyDeathView.setText("+ "+dDeath);
                    dateView.setText("As on "+date+", "+year);

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
    public static String format(double value) {
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
}
