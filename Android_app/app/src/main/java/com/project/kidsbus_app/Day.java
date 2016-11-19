package com.project.kidsbus_app;

import android.content.Context;
import android.widget.Button;
import android.widget.ToggleButton;

import java.util.Calendar;

public class Day extends Button {
    Calendar c;

    public Day(Context c)
    {
        super(c);
    }

    public void setC(int year, int month, int day) {
        c.set(year, month, day);
    }

    public Calendar getC() {
        return c;
    }

}