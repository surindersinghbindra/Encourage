package com.nearnia.encouragement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nearnia.encouragement.adapters.CustomAdapter;
import com.nearnia.encouragement.beanclasses.SubChannels;
import com.nearnia.encouragement.util.ServiceHandler;
import com.nearnia.encouragement.util.StringRequestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WriteYourOwn extends Activity {
    private SharedPreferences writeyourOwnPref;


    private CheckBox cb_policy;
    private ImageView exit;
    private TextView tv_policy;
    private EditText EmailId, submittedQuote;
    private Spinner spinner;

    private List<SubChannels> all_Sub_Categories;
    private CustomAdapter custom_adapter;
    private String subChannelId;
    private ProgressBar progressBar;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_your_own);
        VolleySingleton.TABHOST = 0;
        spinner = (Spinner) findViewById(R.id.subcategoryspinner);
        progressBar = (ProgressBar) findViewById(R.id.progressbarSmall);
        submittedQuote = (EditText) findViewById(R.id.encouragingQuote);
        exit = (ImageView) findViewById(R.id.logowithEmailIdLogo);
        sendButton = (Button) findViewById(R.id.sendButton);
        tv_policy = (TextView) findViewById(R.id.tv_policy);
        SpannableString styledString = new SpannableString(this.getResources().getString(R.string.policy));

        // url
        styledString.setSpan(new URLSpan("http://www.encouragementforall.com/policies"), 28, styledString.length(), 0);

        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                Toast.makeText(WriteYourOwn.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        };
        styledString.setSpan(clickableSpan, 28, styledString.length(), 0);
        tv_policy.setMovementMethod(LinkMovementMethod.getInstance());
        tv_policy.setText(styledString);

        cb_policy = (CheckBox) findViewById(R.id.cb_policy);

        new RemoteParseData().execute();

        EmailId = (EditText) findViewById(R.id.EmailId);

        final RelativeLayout help_screen_writeYourOwn = (RelativeLayout) findViewById(R.id.help_screen_writeYourOwn);

        writeyourOwnPref = this.getPreferences(Context.MODE_PRIVATE);
        final ImageView encourageothersscreen3 = (ImageView) findViewById(R.id.encourageothersscreen3);
        Boolean writeyourOwnHelpScreenBool = writeyourOwnPref.getBoolean("writeyourOwnHelpScreenBool", false);

        if (!writeyourOwnHelpScreenBool) {

            encourageothersscreen3.setImageResource(R.drawable.encourageothersscreen3);
            help_screen_writeYourOwn.setVisibility(View.VISIBLE);

            help_screen_writeYourOwn.setEnabled(true);
            writeyourOwnPref.edit().putBoolean("writeyourOwnHelpScreenBool", true).commit();
        }

        help_screen_writeYourOwn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                help_screen_writeYourOwn.setVisibility(View.GONE);
                encourageothersscreen3.setImageResource(android.R.color.transparent);

            }
        });

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                subChannelId = all_Sub_Categories.get(position).getSubChannelId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                subChannelId = all_Sub_Categories.get(0).getSubChannelId();

            }
        });
        sendButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!cb_policy.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please Accept privacy policy and end user agreement", Toast.LENGTH_LONG).show();
                    cb_policy.requestFocus();
                } else {
                    String request = null;
                    try {
                        request = VolleySingleton.ADD_QUOTE + "&userid="
                                + String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0))
                                + "&categoryid=" + subChannelId + "&quote="
                                + URLEncoder.encode(submittedQuote.getText().toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();
                    }

                    StringRequestActivity stringRequestActivity = new StringRequestActivity();
                    String result = stringRequestActivity.makeStringReqWithProgressbar(WriteYourOwn.this, request);
                }


            }
        });

        boolean login = VolleySingleton.userLogin.getBoolean(VolleySingleton.LOGIN_STATUS, false);
        if (login) {

            EmailId.setText(VolleySingleton.userLogin.getString(VolleySingleton.USERNAME, "Your username.."));
        }

        exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                VolleySingleton.TABHOST = 4;
                Intent i = new Intent(WriteYourOwn.this, MainActivity2.class);
                // VolleySingleton.userLogin.edit().putBoolean("LION_PRESSES",
                // true).commit();
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.exit, R.anim.enter);

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
    public void onBackPressed() {

        new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        WriteYourOwn.this.finish();
                    }
                }).setNegativeButton("No", null).show();

    }

    // RemoteDataTask AsyncTask
    private class RemoteParseData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

            // mProgressDialog = new ProgressDialog(WriteYourOwn.this,
            // AlertDialog.THEME_HOLO_DARK);
            // mProgressDialog.setMessage("Please wait...");
            // mProgressDialog.setCancelable(false);
            // mProgressDialog.setIndeterminate(true);
            // mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            all_Sub_Categories = new ArrayList<SubChannels>();
            ServiceHandler sH = new ServiceHandler();
            String g = sH.makeServiceCall(VolleySingleton.SUB_CATEGORIES, ServiceHandler.GET);
            JSONObject reader = null;
            try {
                reader = new JSONObject(g);
                int success = reader.getInt("success");
                if (success == 1) {

                    JSONArray jsonArray = reader.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        SubChannels sC = new SubChannels();
                        sC.setSubChannelName(jsonObj.getString("category"));
                        sC.setSubChannelId(jsonObj.getString("categoryid"));
                        all_Sub_Categories.add(sC);
                    }

                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Resources res = getResources();
            custom_adapter = new CustomAdapter(WriteYourOwn.this, R.layout.custom_spinner,
                    (ArrayList<SubChannels>) all_Sub_Categories, res);

            spinner.setAdapter(custom_adapter);
            progressBar.setVisibility(View.GONE);
            // mProgressDialog.dismiss();
        }
    }

}
