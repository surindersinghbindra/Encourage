package com.nearnia.encouragement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nearnia.encouragement.util.CompressImage;
import com.nearnia.encouragement.util.DCircularImageView;
import com.nearnia.encouragement.util.ImageLoadingUtils;
import com.nearnia.encouragement.util.ServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    String rP;
    int i = 0;
    byte[] bitMapData;
    private DCircularImageView profileImage;
    private ImageView addProfileImageButton;
    private RadioGroup gender;
    private Uri uri;
    private SharedPreferences signup;
    private String TAG = SignUpActivity.class.getSimpleName();
    private Calendar myCalendar = Calendar.getInstance();
    private Bitmap bp;
    private TextView tv_policy;
    private ImageLoadingUtils utils;

    private Button signUp;
    private String usernametxt, passwordtxt, emailId1, PhoneNumberForParse, userSignUpData, genderForParse = "male", dateofBirth1;
    private TextView signIn;
    private ProgressDialog mProgressDialog;
    private EditText fullName, password, emailId, phoneNumber;
    private int PICK_IMAGE_REQUEST = 1;
    private CheckBox cb_policy;
    private RelativeLayout signup_Main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up_activity);

        utils = new ImageLoadingUtils(this);

        signup = this.getPreferences(Context.MODE_PRIVATE);
        Boolean ranBeforeSignup = signup.getBoolean("ranBeforeSignup", false);

        if (!ranBeforeSignup) {
            startActivity(new Intent(getApplicationContext(), TutorialSignup.class));
            overridePendingTransition(R.anim.enter, R.anim.exit);
            signup.edit().putBoolean("ranBeforeSignup", true).commit();
        }

        fullName = (EditText) findViewById(R.id.fullName);
        password = (EditText) findViewById(R.id.editTextPassword);
        emailId = (EditText) findViewById(R.id.EmailId);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);

        cb_policy = (CheckBox) findViewById(R.id.cb_policy);

        tv_policy = (TextView) findViewById(R.id.tv_policy);

        SpannableString styledString = new SpannableString(this.getResources().getString(R.string.policy));

        styledString.setSpan(new URLSpan("http://www.encouragementforall.com/policies"), 28, styledString.length(), 0);

        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                Toast.makeText(SignUpActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        };
        styledString.setSpan(clickableSpan, 28, styledString.length(), 0);
        tv_policy.setMovementMethod(LinkMovementMethod.getInstance());
        tv_policy.setText(styledString);

        mProgressDialog = new ProgressDialog(SignUpActivity.this, AlertDialog.THEME_HOLO_DARK);
        mProgressDialog.setTitle("Signing Up");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);

        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

        signIn = (TextView) findViewById(R.id.signIn);
        signUp = (Button) findViewById(R.id.createAnAccount);
        profileImage = (DCircularImageView) findViewById(R.id.profileImage);
        addProfileImageButton = (ImageView) findViewById(R.id.addProfileImageButton);

        gender = (RadioGroup) this.findViewById(R.id.gender);

        final EditText datePicker = (EditText) findViewById(R.id.DATEOFBIRTHPICKER);
        datePicker.setFocusableInTouchMode(false);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateofBirth1 = sdf.format(myCalendar.getTime());
                datePicker.setText(dateofBirth1);
            }

        };
        datePicker.setOnClickListener(null);
        datePicker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpActivity.this, date, 1995, 01, 01).show();

            }
        });

        SpannableString content = new SpannableString("ALREADY HAVE AN ACCOUNT ? SIGN IN");
        content.setSpan(new UnderlineSpan(), 26, content.length(), 0);
        signIn.setText(content);

        gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.FEMALE) {
                    genderForParse = "female";
                } else if (checkedId == R.id.MALE) {
                    genderForParse = "male";

                }

            }
        });

        signIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

        addProfileImageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                bp = null;
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        signUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // addingSubChannels();
                // signUp.setEnabled(false);

                // Retrieve the text entered from the EditText
                usernametxt = fullName.getText().toString();
                passwordtxt = password.getText().toString();
                emailId1 = emailId.getText().toString();
                PhoneNumberForParse = phoneNumber.getText().toString();

                if (TextUtils.isEmpty(usernametxt) || TextUtils.isEmpty(passwordtxt) || bp == null || TextUtils.isEmpty(emailId1)
                        || !isEmailValid(emailId1) || TextUtils.isEmpty(PhoneNumberForParse) || !cb_policy.isChecked()) {

                    if (bp == null) {
                        Toast.makeText(getApplicationContext(), "Please Add Your Profile Image", Toast.LENGTH_LONG)
                                .show();
                    } else if (TextUtils.isEmpty(usernametxt)) {
                        Toast.makeText(getApplicationContext(), "Please fill Full-Name", Toast.LENGTH_LONG).show();
                        fullName.requestFocus();
                    } else if (TextUtils.isEmpty(PhoneNumberForParse)) {
                        Toast.makeText(getApplicationContext(), "Please fill Phone Number", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(emailId1)) {
                        Toast.makeText(getApplicationContext(), "Please fill Email-ID", Toast.LENGTH_LONG).show();
                    } else if (!isEmailValid(emailId1)) {
                        Toast.makeText(getApplicationContext(), "Please fill Valid EmailiD", Toast.LENGTH_LONG).show();
                    } else if (TextUtils.isEmpty(passwordtxt)) {
                        Toast.makeText(getApplicationContext(), "Please fill Password", Toast.LENGTH_LONG).show();
                        password.requestFocus();
                    } else if (!cb_policy.isChecked()) {
                        Toast.makeText(getApplicationContext(), "Please Accept privacy policy and end user agreement", Toast.LENGTH_LONG).show();
                        cb_policy.requestFocus();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please fill Form", Toast.LENGTH_LONG).show();
                    }

                    signUp.setEnabled(true);

                } else {

                    new RemoteDataTask().execute();

                }
            }

        });



    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            CompressImage cI = new CompressImage();
            rP = cI.compressImage(this, uri);

            bp = utils.decodeBitmapFromPath(rP);

            profileImage.setImageBitmap(bp);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bitMapData = stream.toByteArray();

//            pictureEncoded = Base64.encodeToString(bitMapData, Base64.DEFAULT);
            System.gc();
        }
    }


    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
        super.onBackPressed();
    }

    public boolean isEmailValid(String email) {

        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private class RemoteDataTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ServiceHandler sH = new ServiceHandler();
            String response1 = null;

            String g = sH.toUploadImage(VolleySingleton.IMAGEUPLOAD_URL, rP);

            try {
                JSONObject reader = new JSONObject(g);
                int success = reader.getInt("success");
                if (success == 1) {

                    String imageName = reader.getString("imageName");

                    String s = VolleySingleton.SERVER_URL_WITH_SLASH + "index.php?method=signup&fullname="
                            + URLEncoder.encode(usernametxt, "UTF-8") + "&email=" + emailId1 + "&password="
                            + passwordtxt + "&picture=" + URLEncoder.encode(imageName, "UTF-8") + "&phone="
                            + PhoneNumberForParse + "&dateofbirth=" + dateofBirth1 + "&notification=" + "6-9"
                            + "&gender=" + genderForParse + "&timezone="
                            + VolleySingleton.userLogin.getString("TIME_ZONE", "America/Los_Angeles") + "&devicetoken="
                            + VolleySingleton.userLogin.getString("REGISTRAION_ID", "defValue") + "&device=" + "2"
                            + "&categoryid=" + FirstIntent.result;
                    VolleySingleton.userLogin.edit().putString(VolleySingleton.PROFILE_IMAGE_URL, imageName).commit();

                    response1 = sH.makeServiceCall(s, ServiceHandler.GET);

                }

            } catch (JSONException e) {

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

            return response1;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject reader = new JSONObject(result);

                int success = reader.getInt("success");

                if (success == 1) {
                    signUp.setEnabled(true);
                    // Toast.makeText(SignUpActivity.this, result,
                    // Toast.LENGTH_LONG).show();
                    VolleySingleton.userLogin.edit().putInt("LOGIN_ID", reader.getInt("data")).commit();
                    VolleySingleton.userLogin.edit().putString(VolleySingleton.USERNAME, usernametxt).commit();
                    VolleySingleton.userLogin.edit().putString(VolleySingleton.PASSWORD, passwordtxt).commit();
                    VolleySingleton.userLogin.edit().putString(VolleySingleton.EMAIL_ID, emailId1).commit();
                    VolleySingleton.userLogin.edit().putString(VolleySingleton.DATEOFBIRTH, dateofBirth1).commit();
                    VolleySingleton.userLogin.edit().putString(VolleySingleton.GENDER, genderForParse).commit();
                    VolleySingleton.userLogin.edit().putString(VolleySingleton.PHONENUMBER, PhoneNumberForParse)
                            .commit();
                    VolleySingleton.userLogin.edit().putString(VolleySingleton.PROFILE_IMAGE_BYTE_DATA,
                            Base64.encodeToString(bitMapData, Base64.DEFAULT)).commit();

                    VolleySingleton.userLogin.edit().putBoolean(VolleySingleton.LOGIN_STATUS, true).commit();
                    VolleySingleton.userLogin.edit().putString(VolleySingleton.NOTIFICATION_TIME, "0-3").commit();

                    mProgressDialog.dismiss();
                    Intent i = new Intent(SignUpActivity.this, MainActivity2.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.exit);

                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, reader.getString("message"), Toast.LENGTH_LONG).show();
                    VolleySingleton.userLogin.edit().putBoolean(VolleySingleton.LOGIN_STATUS, false).commit();

                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

            mProgressDialog.dismiss();
            return;
        }
    }

}
