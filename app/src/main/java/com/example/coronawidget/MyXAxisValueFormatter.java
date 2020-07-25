package com.example.coronawidget;

import androidx.annotation.NonNull;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter.*;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyXAxisValueFormatter extends ValueFormatter  {

    String oldDate="2020-01-30";




    @Override
    public String getFormattedValue(float value) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyy-MM-dd");
        SimpleDateFormat formater=new SimpleDateFormat("dd MMMM");
        Calendar c=Calendar.getInstance();
        try {
            c.setTime(sdf.parse(oldDate));

        }catch (ParseException f){
            f.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH,(int) value);
        String newDate=formater.format(c.getTime());




        return String.valueOf(newDate);
    }
}

