package com.example.barcodeattendancetaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AttendanceAdapter.OnAttendanceListener{

   static ArrayList<AttendanceModel> attendanceModels;
    RecyclerView recyclerView;
   static AttendanceAdapter adapter;

    public void nextActivity(View view){
        Intent intent = new Intent(getApplicationContext(),AttendanceActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (allPermissionsGranted()) {
            Log.i("permission", "granted");
        } else {
            getRuntimePermissions();
        }
        recyclerView = findViewById(R.id.my_recycler_view);
        attendanceModels = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        temp.add("1");
        temp.add("2");
        temp.add("3");
        attendanceModels.add(new AttendanceModel(temp,"2020-08-25"));
        adapter = new AttendanceAdapter(attendanceModels,this);
        recyclerView.setAdapter(adapter);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.divider));
//        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onAttendanceClick(int position) {
        Log.i("position",position+"");
//        Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
//        intent.putExtra("position",position);
//        startActivity(intent);
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i("Status", "Permission granted!");
        if (allPermissionsGranted()) {
            Log.i("Status","Granted!");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("Status", "Permission granted: " + permission);
            return true;
        }
        Log.i("Status", "Permission NOT granted: " + permission);
        return false;
    }
}