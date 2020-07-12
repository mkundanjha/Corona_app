package com.example.coronawidget;

import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class YourMakerView extends MarkerView {
    String oldDate="2020-01-30";
    private TextView tvContent;
    private TextView tvContent2;
    GraphActivity dateValue=new GraphActivity();
    ArrayList<String> listDate=new ArrayList<String>(dateValue.date);

    public YourMakerView(Context context, int layoutResource) {
        super(context, layoutResource);

        // find your layout components
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvContent2=(TextView) findViewById(R.id.tvContent2);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
// content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        SimpleDateFormat sdf=new SimpleDateFormat("yyy-MM-dd");
        SimpleDateFormat formater=new SimpleDateFormat("dd MMMM");
        Calendar c=Calendar.getInstance();
        try {
            c.setTime(sdf.parse(oldDate));

        }catch (ParseException f){
            f.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH,(int) e.getX());
        String newDate=formater.format(c.getTime());





        tvContent2.setText("" + MainActivity.format((double)e.getY()*100000)+"       ");
        tvContent.setText("    "+newDate+"      ");

        // this will perform necessary layouting
        super.refreshContent(e,highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}
