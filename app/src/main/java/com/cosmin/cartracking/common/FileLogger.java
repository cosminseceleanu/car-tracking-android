package com.cosmin.cartracking.common;


import android.os.Environment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FileLogger {

    public static void log(String tag, String message) {
        PrintWriter pw;
        try {
            pw = new PrintWriter(
                    new FileWriter(Environment.getDataDirectory()+"/rt.log", true));
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            String log = String.format("[%s - %s] - %s", tag, reportDate, message);
            pw.println(log);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
