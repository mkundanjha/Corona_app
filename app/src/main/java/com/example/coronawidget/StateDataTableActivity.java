package com.example.coronawidget;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.github.mikephil.charting.data.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class StateDataTableActivity extends AppCompatActivity {
    public RequestQueue queue;
    public  TextView dateView;
    String url = "https://api.covid19india.org/data.json";
    SharedPre sharedprep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedprep=new SharedPre(this);
        if(sharedprep.loadNightModeState()==true){
            setTheme(R.style.AppTheme);
        }else {
            setTheme(R.style.LightMode);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_data_table);
        queue= Volley.newRequestQueue(this);
        dateView=findViewById(R.id.textView43);

        fetch();


    }

    public void onBackPressed(){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();


    }

    public void init(JSONArray array,int size) {


        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("States");
        tv0.setTextColor(Color.parseColor("#965150"));
        tv0.setTextSize(22);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("T.");
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(22);
        tv1.setTextColor(Color.parseColor("#965150"));
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("R.");
        tv2.setTextSize(22);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextColor(Color.parseColor("#965150"));
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("D.");
        tv3.setTextSize(22);
        tv3.setGravity(Gravity.CENTER);

        tv3.setTextColor(Color.parseColor("#965150"));
        tbrow0.addView(tv3);
        stk.addView(tbrow0);

        String stateName;
        String stateTCase;
        String stateTRec;
        String stateTDeath;
        String sDCase;
        String sDRec;
        String sDDeath;
        boolean flag=false;




    try{

        for (int i = 1; i < size; i++) {
            JSONObject stateJson=array.getJSONObject(i);
            stateName=stateJson.getString("state");
            stateTCase=stateJson.getString("confirmed");
            stateTRec=stateJson.getString("recovered");
            stateTDeath=stateJson.getString("deaths");
            sDCase=stateJson.getString("deltaconfirmed");
            sDRec=stateJson.getString("deltarecovered");
            sDDeath=stateJson.getString("deltadeaths");



            if(!stateName.equals("State Unassigned")) {
                if(stateName.equals("Dadra and Nagar Haveli and Daman and Diu"))
                    stateName="Dadra and \nNagar Haveli and\nDaman and Diu";
                if(stateName.equals("Andaman and Nicobar Islands"))
                    stateName="Andaman and \nNicobar Islands";

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    runAnimation(t1v,(4+i)*100);
                    t1v.setText(stateName);
                    t1v.setBackground(getDrawable(R.color.lightBlue));
                    t1v.setTextColor(Color.WHITE);
                    t1v.setGravity(Gravity.LEFT);
//                    t1v.setBackground(getDrawable(R.drawable.cell_shape));
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    runAnimation(t2v,(4+i)*100);
                    t2v.setText(format(stateTCase));
                    t2v.setTextColor(Color.WHITE);
                    t2v.setGravity(Gravity.RIGHT);
                    t2v.setBackground((getDrawable(R.drawable.cell_shape)));
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    runAnimation(t3v,(4+i)*100);
                    t3v.setText(format(stateTRec));
                    t3v.setTextColor(Color.WHITE);
                    t3v.setGravity(Gravity.RIGHT);
                    t3v.setBackground((getDrawable(R.drawable.cell_shape)));
                    tbrow.addView(t3v);
                    TextView t4v = new TextView(this);
                    runAnimation(t4v,(4+i)*100);
                    t4v.setText(format(stateTDeath));
                    t4v.setTextColor(Color.WHITE);
                    t4v.setGravity(Gravity.RIGHT);
                    t4v.setBackground((getDrawable(R.drawable.cell_shape)));
                    tbrow.addView(t4v);
                    stk.addView(tbrow);

                    TableRow tableRow2 = new TableRow(this);
                    TextView t1 = new TextView(this);
                    runAnimation(t1,(4+i)*100);
                    t1.setText("");
                    t1.setBackground(getDrawable(R.drawable.state_cell));
                    tableRow2.addView(t1);


                    TextView t2 = new TextView(this);
                    runAnimation(t2,(4+i)*100);
                    if(Integer.parseInt(sDCase)>0) {
                        t2.setText("+ " + format(sDCase));
                    }else
                    {
                        t2.setText("");
                    }
                    t2.setTextColor(Color.parseColor("#ad5798"));
                    t2.setGravity(Gravity.RIGHT);
                    t2.setBackground(getDrawable(R.drawable.state_cell));
                    tableRow2.addView(t2);

                    TextView t3 = new TextView(this);
                    runAnimation(t3,(4+i)*100);
                    if(Integer.parseInt(sDRec)>0) {
                        t3.setText("+ " + format(sDRec));
                    }else
                    {
                        t3.setText("");
                    }
                    t3.setTextColor(Color.parseColor("#ad5798"));
                    t3.setGravity(Gravity.RIGHT);
                    t3.setBackground(getDrawable(R.drawable.state_cell));
                    tableRow2.addView(t3);

                    TextView t4 = new TextView(this);
                    runAnimation(t4,(4+i)*100);
                    if(Integer.parseInt(sDDeath)>0) {
                        t4.setText("+ " + format(sDDeath));
                    }else
                    {
                        t4.setText("");
                    }
                    t4.setTextColor(Color.parseColor("#ad5798"));
                    t4.setGravity(Gravity.RIGHT);
                    t4.setBackground(getDrawable(R.drawable.state_cell));
                    tableRow2.addView(t4);
                    stk.addView(tableRow2);


            }
        }

    }catch (JSONException e){
        e.printStackTrace();
    }

    }

    public void fetch(){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("statewise");

                    init(jsonArray,jsonArray.length());

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

    public static String format(String data) {
        Float value=Float.parseFloat(data);
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

    private void runAnimation(View t, long delay)
    {
        Animation a = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        a.reset();
        a.setStartOffset(delay);
        t.setVisibility(View.VISIBLE);
        t.clearAnimation();

        t.startAnimation(a);
    }
}
