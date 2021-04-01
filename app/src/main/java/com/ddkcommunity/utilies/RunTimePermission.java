package com.ddkcommunity.utilies;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class RunTimePermission {
    public static final int REQUEST_CODE = 99;
    public static void requestLocation(Activity caller)
    {
        List<String> permissionList = new ArrayList<String>();

        if  (ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if  (ContextCompat.checkSelfPermission(caller,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionList.size()>0)
        {
            String [] permissionArray = new String[permissionList.size()];

            for (int i=0;i<permissionList.size();i++)
            {
                permissionArray[i] = permissionList.get(i);
            }

            ActivityCompat.requestPermissions(caller, permissionArray, REQUEST_CODE);
        }

    }






    public static boolean haveLocationPermission(Activity caller)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED)
        {
            permissionCheck = ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck== PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
        }
        else
        {
            Toast.makeText(caller, "Please enable permission for location", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

}