package com.nearnia.encouragement;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nearnia.encouragement.util.StringRequestActivity;

import com.nearnia.encouragement.InterfacesForcallBack.IAsyncResponse;
import com.nearnia.encouragement.adapters.QoutesYouLovedAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.nearnia.encouragement.beanclasses.QuotesYouLiked;

public class QoutesYouLoved extends Fragment implements OnClickListener, IAsyncResponse {

	private ArrayList<QuotesYouLiked> arraylist;
	private QoutesYouLovedAdapter q;
	private Spinner sp ;
	private EditText serachfragment_serach_edittext;
	private ImageButton searchButton;
	private ListView normalListView;
	private String spinnerArray[] = { "ALL LOVED QUOTES", "ONE THIRD LOVED QUOTES", "TWO THIRD LOVED QUOTES",
			"FULL LOVED QUOTES" };

	public static int imagId[] = { R.drawable.bt_fullheart, R.drawable.onetthird_heart, R.drawable.twothird_heart,
			R.drawable.fullheart };

	
	private View v2;
	public QoutesYouLoved() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		

		v2 = inflater.inflate(R.layout.you_loved_fragment, container, false);
		
//		QuotesYouLiked quoteyouLoved = (QuotesYouLiked) UniversalSingleton.getInstance().getObject();

		sp = (Spinner) v2.findViewById(R.id.spinnder_sort);
		searchButton = (ImageButton) v2.findViewById(R.id.serachfragment_search_clear);

		serachfragment_serach_edittext = (EditText) v2.findViewById(R.id.serachfragment_serach_edittext);

		searchButton.setOnClickListener(this);
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
				spinnerArray);
		sp.setAdapter(dataAdapter);

		String url = VolleySingleton.LIKE_QOUTE_URL
				+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0));

		fetchData(url);

		normalListView = (ListView) v2.findViewById(R.id.listView_enc_you_loved);
		// lv.setClickable(true);

		sp.setOnItemSelectedListener(null);
//		pDialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_DARK);

//		normalListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//
//				// removeListItem(view,position);
//
//			}
//		});

		return v2;
	}

	private void fetchData(String url) {

		StringRequestActivity stringRequestActivity = new StringRequestActivity(this);
		stringRequestActivity.makeStringReqWithInterface(url);
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.serachfragment_search_clear:
			
			Toast.makeText(getActivity(), "Searching...", Toast.LENGTH_SHORT).show();
			
			// Check if no view has focus:
			View view = getActivity().getCurrentFocus();
			if (view != null) {  
			    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
			
			
//			pDialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_DARK);
//			pDialog.setMessage("Searching...");
//			pDialog.setCancelable(false);
			
			fetchData(VolleySingleton.LIKE_QOUTE_URL + "&userid="
					+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0)) + "&search="
					+ serachfragment_serach_edittext.getText().toString());

			break;

		default:
			break;

		}

	}

	@Override
	public void onRestIntractionResponse(String response) {
		// TODO Auto-generated method stub

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONArray jsonArray = jsonObject.optJSONArray("data");

		if (jsonObject.has("data") && jsonArray.length() > 0) {
			arraylist = new ArrayList<QuotesYouLiked>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject dataJsonObj = jsonArray.optJSONObject(i);

				QuotesYouLiked quotesYouLiked = new QuotesYouLiked();

				quotesYouLiked.setQuoteid(dataJsonObj.optString("quoteid"));
				quotesYouLiked.setQuote(dataJsonObj.optString("quote"));
				quotesYouLiked.setPicture(dataJsonObj.optString("picture"));
				quotesYouLiked.setFullname(dataJsonObj.optString("fullname"));
				quotesYouLiked.setCategoryid(dataJsonObj.optString("categoryid"));
				quotesYouLiked.setSubcategoryid(dataJsonObj.optString("subcategoryid"));
				quotesYouLiked.setUserid(dataJsonObj.optString("userid"));
				quotesYouLiked.setCategory(dataJsonObj.optString("category"));
				quotesYouLiked.setSubcategory(dataJsonObj.optString("subcategory"));
				quotesYouLiked.setHeartGrowth(dataJsonObj.optInt("amount"));

				arraylist.add(quotesYouLiked);

			}
//			pDialog.dismiss();

			q = new QoutesYouLovedAdapter(getActivity(), arraylist);
			normalListView.setAdapter(q);
			
			
			sp.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

					if (position == 0) {
						
						String url = VolleySingleton.LIKE_QOUTE_URL
								+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0));

						fetchData(url);	
//						q.getFilter().filter("0");
					}
					if (position == 1) {
						
						String url = VolleySingleton.LIKE_QOUTE_URL
								+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0)+"&filter=1");

						fetchData(url);	
						
//						q.getFilter().filter("1");
					}
					if (position == 2) {
						String url = VolleySingleton.LIKE_QOUTE_URL
								+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0)+"&filter=2");

						fetchData(url);	
//						q.getFilter().filter("2");
					}
					if (position == 3) {
						String url = VolleySingleton.LIKE_QOUTE_URL
								+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0)+"&filter=3");

						fetchData(url);	
						
//						q.getFilter().filter("3");
					}
					// Log.e("item", String.valueOf(position));

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

					// subChannelId = all_Sub_Categories.get(0).getSubChannelId();

				}
			});
		}

		else {
			Toast.makeText(getActivity(), "you dont have loved quotes", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onRestIntractionError(String message) {
//		pDialog.dismiss();

		Toast.makeText(getActivity(), "Iternet Error", Toast.LENGTH_SHORT).show();

	}

}
