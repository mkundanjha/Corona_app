package com.example.coronawidget;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.app.INotificationSideChannel;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity {
    private RequestQueue queue;
    public TextView title;

    ArrayList<Integer> activeWorld=new ArrayList<Integer>();
    HashMap<Integer,Integer> tCase=new HashMap<>();
    HashMap<Integer,Integer> tDeath=new HashMap<>();
    HashMap<Integer,Integer> tRec=new HashMap<>();
    HashMap<Integer,Integer> dCase=new HashMap<>();
    HashMap<Integer,Integer> dRec=new HashMap<>();
    HashMap<Integer,Integer> dDeath=new HashMap<>();

    HashMap<Integer,String> activeWCase=new HashMap<>();

    String url = "https://api.covid19api.com/summary";
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



        setContentView(R.layout.activity_world_data);

        title=findViewById(R.id.textView55);

        title.setVisibility(View.VISIBLE);
        back=findViewById(R.id.imageButton6);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        queue= Volley.newRequestQueue(this);
        fetch();


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

                    JSONArray jsonArray=response.getJSONArray("Countries");


                    int activePerCountry;

                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        activePerCountry = Integer.parseInt(jsonObject1.getString("TotalConfirmed"))
                                - Integer.parseInt(jsonObject1.getString("TotalRecovered"))
                                - Integer.parseInt(jsonObject1.getString("TotalDeaths"));

                        activeWCase.put(activePerCountry, jsonObject1.getString("Country"));
                        activeWorld.add(activePerCountry);
                        tCase.put(activePerCountry,Integer.parseInt(jsonObject1.getString("TotalConfirmed")));
                        tRec.put(activePerCountry,Integer.parseInt(jsonObject1.getString("TotalRecovered")));
                        tDeath.put(activePerCountry,Integer.parseInt(jsonObject1.getString("TotalDeaths")));
                        dCase.put(activePerCountry,Integer.parseInt(jsonObject1.getString("NewConfirmed")));
                        dRec.put(activePerCountry,Integer.parseInt(jsonObject1.getString("NewRecovered")));
                        dDeath.put(activePerCountry,Integer.parseInt(jsonObject1.getString("NewDeaths")));




                    }

                    Collections.sort(activeWorld);


                        init(jsonArray.length(),activeWCase,activeWorld,tCase,tRec,tDeath,dCase,dRec,dDeath);

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



        public void init(int size,HashMap<Integer,String> map,ArrayList<Integer> alist,
                         HashMap<Integer,Integer> t,HashMap<Integer,Integer> r,HashMap<Integer,Integer> d,
                         HashMap<Integer,Integer> dC,HashMap<Integer,Integer> dR,HashMap<Integer,Integer> dD) {


            TableLayout stk = (TableLayout) findViewById(R.id.table_main2);
            TableRow tbrow0 = new TableRow(this);
            TextView tv0 = new TextView(this);
            tv0.setText("Countries");
            tv0.setTextColor(Color.parseColor("#b52f2f"));
            tv0.setTextSize(22);
            tv0.setPadding(30,0,0,0);
            tbrow0.addView(tv0);
            TextView tv1 = new TextView(this);
            tv1.setText("T.");
            tv1.setGravity(Gravity.CENTER);
            tv1.setTextSize(22);
            tv1.setTextColor(Color.parseColor("#b52f2f"));
            tbrow0.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setText("R.");
            tv2.setTextSize(22);
            tv2.setGravity(Gravity.CENTER);
            tv2.setTextColor(Color.parseColor("#b52f2f"));
            tbrow0.addView(tv2);
            TextView tv3 = new TextView(this);
            tv3.setText("D.");
            tv3.setTextSize(22);
            tv3.setGravity(Gravity.CENTER);

            tv3.setTextColor(Color.parseColor("#b52f2f"));
            tbrow0.addView(tv3);
            stk.addView(tbrow0);

            String cName;
            String TCase;
            String TRec;
            String TDeath;
            String DCase;
            String DRec;
            String DDeath;
            boolean flag = false;


            try {

                int count = 0;
                for (int i = 0; i < 30; i++) {

                    cName = map.get(alist.get(size - 1 - i));
                    TCase = String.valueOf(t.get(alist.get(size - i - 1)));
                    TRec = String.valueOf(r.get(alist.get(size - i - 1)));
                    TDeath = String.valueOf(d.get(alist.get(size - i - 1)));
                    DCase=String.valueOf(dC.get(alist.get(size - i - 1)));
                    DRec=String.valueOf(dR.get(alist.get(size - i - 1)));
                    DDeath=String.valueOf(dD.get(alist.get(size - i - 1)));
                    String temp="",temp2="";



                    if(cName.equals("United States of America")){
                        cName="USA";
                    }
                    if(cName.equals("Russian Federation")){
                        cName="Russia";
                    }
                    if(cName.equals("Dominican Republic")){
                        cName="Dominican";
                    }


                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    runAnimation(t1v, (4 + i) * 100);
                    t1v.setText(cName);

                    t1v.setBackground(getDrawable(R.drawable.top_tablecell_state_name));
                    t1v.setTextColor(Color.WHITE);
                    t1v.setGravity(Gravity.LEFT);
//                    t1v.setBackground(getDrawable(R.drawable.cell_shape));
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(this);
                    runAnimation(t2v, (4 + i) * 100);
                    t2v.setText(format(TCase));
                    t2v.setTextColor(Color.WHITE);
                    t2v.setGravity(Gravity.RIGHT);
                    t2v.setBackground((getDrawable(R.drawable.cell_shape)));
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(this);
                    runAnimation(t3v, (4 + i) * 100);
                    t3v.setText(format(TRec));
                    t3v.setTextColor(Color.WHITE);
                    t3v.setGravity(Gravity.RIGHT);
                    t3v.setBackground((getDrawable(R.drawable.cell_shape)));
                    tbrow.addView(t3v);
                    TextView t4v = new TextView(this);
                    runAnimation(t4v, (4 + i) * 100);
                    t4v.setText(format(TDeath));
                    t4v.setTextColor(Color.WHITE);
                    t4v.setGravity(Gravity.RIGHT);
                    t4v.setBackground((getDrawable(R.drawable.lst_top_cell)));
                    tbrow.addView(t4v);
                    stk.addView(tbrow);

                    TableRow tableRow2 = new TableRow(this);
                    TextView t1 = new TextView(this);
                    runAnimation(t1, (4 + i) * 100);
                    t1.setText("");
                    tableRow2.addView(t1);
                    t1.setBackground(getDrawable(R.drawable.bottom_tablecell_state_name));
                    TextView t2 = new TextView(this);
                    runAnimation(t2, (4 + i) * 100);
                    t2.setText("+ "+format(DCase));
                    t2.setTextColor(Color.BLACK);
                    t2.setGravity(Gravity.RIGHT);
                    t2.setBackground(getDrawable(R.drawable.cell_shape_bottom));
                    tableRow2.addView(t2);
                    TextView t3 = new TextView(this);
                    runAnimation(t3, (4 + i) * 100);
                    t3.setText("+ "+format(DRec));
                    t3.setTextColor(Color.BLACK);
                    t3.setGravity(Gravity.RIGHT);
                    t3.setBackground(getDrawable(R.drawable.cell_shape_bottom));
                    tableRow2.addView(t3);
                    TextView t4 = new TextView(this);
                    runAnimation(t4, (4 + i) * 100);
                    t4.setText("+ "+format(DDeath));
                    t4.setTextColor(Color.BLACK);
                    t4.setGravity(Gravity.RIGHT);
                    t4.setBackground(getDrawable(R.drawable.lst_bottom_cell));
                    tableRow2.addView(t4);
                    stk.addView(tableRow2);

//                    Third Row
                    TableRow tableRow3=new TableRow(this);
                    TextView tt=new TextView(this);
                    tt.setText("");

                    tableRow3.addView(tt);
                    stk.addView(tableRow3);


                }


            } catch (Exception e) {
                e.printStackTrace();
            }


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


    }

