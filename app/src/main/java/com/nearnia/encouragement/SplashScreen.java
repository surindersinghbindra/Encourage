package com.nearnia.encouragement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nearnia.encouragement.util.StringRequestActivity;

public class SplashScreen extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 0;
    //    private static final int REQUEST_CONTACTS = 1;
    private static String[] PERMISSIONS_CONTACT = {android.Manifest.permission.READ_CONTACTS};
    private RelativeLayout splashMain;
    private String TAG = SplashScreen.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashMain = (RelativeLayout) findViewById(R.id.splashMain);

        new StringRequestActivity().makeStringReq(VolleySingleton.ANALYTIC_APP_OPEN_COUNT_URL
                + String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0)));

        // Verify that all required contact permissions have been granted.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Contacts permissions have not been granted.
            Log.i(TAG, "Contact permissions has NOT been granted. Requesting permissions.");
            requestExternalStoragePermission();

        } else {

            // Contact permissions have been granted. Show the contacts fragment.
            Log.i(TAG,
                    "Contact permissions have already been granted. Displaying contact details.");

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    if (isOnline()) {
                        VolleySingleton.userLogin.edit().putBoolean("NOTIFICATION_RECIEVED", false).commit();
                        Intent i = new Intent(SplashScreen.this, MainActivity2.class);
                        startActivity(i);

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your network connection!!", Toast.LENGTH_LONG)
                                .show();

                        finish();
                    }

                }
            }, 500);


        }

//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Camera permission has not been granted.
//
//            requestExternalStoragePermission();
//
//        } else {
//
//            // Camera permissions is already available, show the camera preview.
//            Log.i(TAG,
//                    "External Storage permission has already been granted. Displaying camera preview.");
//
//        }

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void requestExternalStoragePermission() {

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(splashMain, "Please allow all the permissions to proceed further in App",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(SplashScreen.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                    REQUEST_CAMERA);
        }
    }

    /**
     * Requests the Contacts permissions.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
//    private void requestContactsPermissions() {
//        // BEGIN_INCLUDE(contacts_permission_request)
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.READ_CONTACTS)) {
//
//            // Provide an additional rationale to the user if the permission was not granted
//            // and the user would benefit from additional context for the use of the permission.
//            // For example, if the request has been denied previously.
//            Log.i(TAG,
//                    "Displaying contacts permission rationale to provide additional context.");
//
//            // Display a SnackBar with an explanation and a button to trigger the request.
//            Snackbar.make(splashMain, R.string.permission_contacts_rationale,
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat
//                                    .requestPermissions(SplashScreen.this, PERMISSIONS_CONTACT,
//                                            REQUEST_CONTACTS);
//                        }
//                    })
//                    .show();
//        } else {
//            // Contact permissions have not been granted yet. Request them directly.
//            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
//        }
//        // END_INCLUDE(contacts_permission_request)
//    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i(TAG, "Received response for Camera permission request.");
            Log.e("JAMM", permissions[0] + permissions[1] + permissions[2]);
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager
                    .PERMISSION_GRANTED) {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        if (isOnline()) {
                            VolleySingleton.userLogin.edit().putBoolean("NOTIFICATION_RECIEVED", false).commit();
                            Intent i = new Intent(SplashScreen.this, MainActivity2.class);
                            startActivity(i);

                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your network connection!!", Toast.LENGTH_LONG)
                                    .show();

                            finish();
                        }

                    }
                }, 500);
            } else {
                requestExternalStoragePermission();
                Log.e("JAMM", "Else");
//                if (permissions[0] == android.Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//
//
//                }
//                if (permissions[2] == Manifest.permission.READ_CONTACTS && grantResults[2] == PackageManager.PERMISSION_DENIED) {
//                    requestExternalStoragePermission();
//                }
            }

            // Check if the only required permission has been granted
//                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Camera permission has been granted, preview can be displayed
//                    Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
//                    Snackbar.make(splashMain, R.string.permision_available_camera,
//                            Snackbar.LENGTH_SHORT).show();
//                } else {
//                    Log.i(TAG, "CAMERA permission was NOT granted.");
//                    Snackbar.make(splashMain, R.string.permissions_not_granted,
//                            Snackbar.LENGTH_SHORT).show();
//
//                }
            // END_INCLUDE(permission_result)

        }
//        else if (requestCode == REQUEST_CONTACTS) {
//            Log.i(TAG, "Received response for contact permissions request.");
//
//            // We have requested multiple permissions for contacts, so all of them need to be
//            // checked.
//            if (PermissionUtil.verifyPermissions(grantResults)) {
//                // All required permissions have been granted, display contacts fragment.
//                Snackbar.make(splashMain, R.string.permision_available_contacts,
//                        Snackbar.LENGTH_SHORT)
//                        .show();
//            } else {
//                Log.i(TAG, "Contacts permissions were NOT granted.");
//                Snackbar.make(splashMain, R.string.permissions_not_granted,
//                        Snackbar.LENGTH_SHORT)
//                        .show();
//            }
//
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
    }
}

