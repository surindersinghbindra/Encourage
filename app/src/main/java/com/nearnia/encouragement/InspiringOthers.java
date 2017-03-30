package com.nearnia.encouragement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nearnia.encouragement.util.RoundprofImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InspiringOthers extends AppCompatActivity implements OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {


    private SharedPreferences inspireOthers;
    private ListView listView;
    private ContactBaseAdapter contactBaseAdapter;

    private LinearLayout originialLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspire_others);

        VolleySingleton.TABHOST = 0;

        final ImageView lionRoar = (ImageView) findViewById(R.id.LionRoar);
        listView = (ListView) findViewById(R.id.contactsListView);
        final EditText search = (EditText) findViewById(R.id.search);

        originialLayout = (LinearLayout) findViewById(R.id.originialLayout);
        final RelativeLayout top_layout_SignUp = (RelativeLayout) findViewById(R.id.top_layout_inspireOthers);

        final ImageView imageButton1 = (ImageView) findViewById(R.id.imageButton1);
        final ImageView imageButton2 = (ImageView) findViewById(R.id.imageButton2);

        inspireOthers = this.getPreferences(Context.MODE_PRIVATE);
        Boolean ranBeforeInspireOthers = inspireOthers.getBoolean("ranBeforeInspireOthers", false);

        if (!ranBeforeInspireOthers) {

            top_layout_SignUp.setVisibility(View.VISIBLE);
            imageButton2.setImageResource(R.drawable.encourageothersscreen2);
            originialLayout.setEnabled(false);
            listView.setEnabled(false);
            search.setEnabled(false);
            lionRoar.setEnabled(false);
            imageButton1.setEnabled(false);
            top_layout_SignUp.setEnabled(true);
            inspireOthers.edit().putBoolean("ranBeforeInspireOthers", true).commit();
        }

        imageButton2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                imageButton2.setVisibility(View.GONE);
                imageButton2.setImageResource(android.R.color.transparent);
                imageButton1.setEnabled(true);
                imageButton1.setImageResource(R.drawable.encourageothersscreen1);
                imageButton1.setVisibility(View.VISIBLE);

            }
        });
        imageButton1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                imageButton1.setVisibility(View.GONE);
                imageButton1.setImageResource(android.R.color.transparent);

                top_layout_SignUp.setVisibility(View.GONE);

                listView.setEnabled(true);
                search.setEnabled(true);
                lionRoar.setEnabled(true);
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ContactBean clickedObj = (ContactBean) parent.getItemAtPosition(position);

                Intent intentsms = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + clickedObj.getPhoneNo()));
                intentsms.putExtra("sms_body", "Hey! I am using Inspirerr....");
                try {
                    startActivity(intentsms);
                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(getApplicationContext(), "There are no Messaging clients installed.",
                            Toast.LENGTH_SHORT).show();
                }

            }

        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactBaseAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                contactBaseAdapter.getFilter().filter(s);

            }
        });

        lionRoar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                VolleySingleton.TABHOST = 4;

                Intent i = new Intent(InspiringOthers.this, MainActivity2.class);

                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                // VolleySingleton.userLogin.edit().putBoolean("LION_PRESSES",
                // true).commit();
                startActivity(i);
                overridePendingTransition(R.anim.exit, R.anim.enter);
            }
        });

        ContactBaseAdapter contactBaseAdapter = new ContactBaseAdapter();
        listView.setAdapter(contactBaseAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.signin:
                // it was the second button
                break;
            case R.id.profileImage:
                break;

        }

    }

    private List<ContactBean> getDataForListView() {

        List<ContactBean> list = new ArrayList<ContactBean>();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                Phone.DISPLAY_NAME + " ASC");

        while (phones.moveToNext()) {

            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            String image_uri = phones
                    .getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            ContactBean objContact = new ContactBean();
            objContact.setName(name);
            objContact.setImage_uri(image_uri);
            objContact.setPhoneNo(phoneNumber);
            list.add(objContact);
        }
        return list;


    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        InspiringOthers.this.finish();
                    }
                }).setNegativeButton("No", null).show();

    }

    private class ContactBean {
        private String name;
        private String phoneNo;
        private String image_uri;

        ContactBean() {

        }

        ContactBean(String name, String phoneNo, String image_uri) {
            this.name = name;
            this.phoneNo = phoneNo;
            this.image_uri = image_uri;

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getImage_uri() {
            return image_uri;
        }

        public void setImage_uri(String image_uri) {
            this.image_uri = image_uri;
        }

    }

    public class ContactBaseAdapter extends BaseAdapter implements Filterable {
        ValueFilter valueFilter;

        List<ContactBean> list = getDataForListView();

        List<ContactBean> list2 = list;

        ContactBaseAdapter() {

        }

        ContactBaseAdapter(ArrayList<ContactBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Object getItem(int position) {

            return list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.alluser_row, parent, false);
            }

            TextView ContactName = (TextView) convertView.findViewById(R.id.tvPhone);
            TextView contactPhone = (TextView) convertView.findViewById(R.id.tvName);
            ImageView conactImageView = (ImageView) convertView.findViewById(R.id.ContactImage);
            // TODO Auto-generated method stub

            ContactBean obj = list.get(position);
            ContactName.setText(obj.getName());
            contactPhone.setText(obj.getPhoneNo());

            Bitmap bp = BitmapFactory.decodeResource(getResources(), R.drawable.user1);

            if (obj.getImage_uri() != null) {
                try {
                    bp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(obj.getImage_uri()));

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            RoundprofImage roundedImage = new RoundprofImage(bp);

            conactImageView.setImageDrawable(roundedImage);
            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {
                    List<ContactBean> filterList = new ArrayList<ContactBean>();
                    for (int i = 0; i < list2.size(); i++) {
                        if ((list2.get(i).getName().toUpperCase()).startsWith(constraint.toString().toUpperCase())) {

                            ContactBean filtredObj = new ContactBean(list2.get(i).getName(), list2.get(i).getPhoneNo(),
                                    list2.get(i).getImage_uri());

                            filterList.add(filtredObj);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = list2.size();
                    results.values = list2;
                }
                return results;

            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<ContactBean>) results.values;
                notifyDataSetChanged();
            }

        }

    }
}