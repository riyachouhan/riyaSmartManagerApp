package com.ddkcommunity.utilies;

import android.app.Application;

import com.ddkcommunity.UserModel;

public class TopExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;
    private Application app = null;

    public TopExceptionHandler(Application app) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.app = app;
    }

    public void uncaughtException(Thread t, Throwable e) {
        if (UserModel.getInstance() == null) {
            UserModel.initInstance(app);
        }
//        StackTraceElement[] arr = e.getStackTrace();
//        String report = e.toString()+"\n\n";
//        report += "--------- Stack trace ---------\n\n";
//        for (int i=0; i<arr.length; i++) {
//            report += "    "+arr[i].toString()+"\n";
//        }
//        report += "-------------------------------\n\n";
//
//        // If the exception was thrown in a background thread inside
//        // AsyncTask, then the actual exception can be found with getCause
//
//        report += "--------- Cause ---------\n\n";
//        Throwable cause = e.getCause();
//        if(cause != null) {
//            report += cause.toString() + "\n\n";
//            arr = cause.getStackTrace();
//            for (int i=0; i<arr.length; i++) {
//                report += "    "+arr[i].toString()+"\n";
//            }
//        }
//        report += "-------------------------------\n\n";

//        try {
//            FileOutputStream trace = app.openFileOutput("stack.trace",
//                    Context.MODE_PRIVATE);
//            trace.write(report.getBytes());
//            trace.close();
//        } catch(IOException ioe) {
//            // ...
//        }

        defaultUEH.uncaughtException(t, e);
    }
}
