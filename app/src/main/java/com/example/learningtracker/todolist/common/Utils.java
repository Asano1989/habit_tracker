package com.example.learningtracker.todolist.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class Utils {
  // 9章で追加
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static Date str2date(String s) {
        long ms = 0;
        try {
          ms = sdf.parse(s).getTime();
        } catch (ParseException e) {
          e.printStackTrace();
        }
        return new Date(ms);
    }
}