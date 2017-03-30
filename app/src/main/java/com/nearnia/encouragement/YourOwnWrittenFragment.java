package com.nearnia.encouragement;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nearnia.encouragement.util.StringRequestActivity;

import com.nearnia.encouragement.InterfacesForcallBack.IAsyncResponse;
import com.nearnia.encouragement.adapters.OwnWrittenAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.nearnia.encouragement.beanclasses.OwnWrittenQuotes;

public class YourOwnWrittenFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, IAsyncResponse {
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

	private ArrayList<OwnWrittenQuotes> arraylist;
	private ListView lsv;

	private SwipeRefreshLayout swipeRefreshLayout;
//	private OwnWrittenAdapter owa;

	public static final YourOwnWrittenFragment newInstance(String message) {

		YourOwnWrittenFragment f = new YourOwnWrittenFragment();
		Bundle bdl = new Bundle();
		bdl.putString(EXTRA_MESSAGE, message);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v2 = new View(getActivity());

		v2 = inflater.inflate(R.layout.your_own_written_fragment, container, false);

		swipeRefreshLayout = (SwipeRefreshLayout) v2.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);

		// swipeRefreshLayout.post(new Runnable() {
		// @Override
		// public void run() {
		// swipeRefreshLayout.setRefreshing(true);
		//
		// // fetchMovies();
		// }
		// });

		arraylist = new ArrayList<OwnWrittenQuotes>();
		fetchData();
		// DatabaseHandler db = new DatabaseHandler(getActivity());

		// List<OwnWrittenQuotes> contacts = db.getAllContacts();

		// for (OwnWrittenQuotes quote : contacts)
		//
		// {
		//
		// OwnWrittenQuotes owq = new OwnWrittenQuotes(quote.getSubCategory(),
		// quote.getQoute());
		// Log.e("QQQQQQQQQQ: ", quote.getQoute());
		// arraylist.add(owq);
		//
		// }

		lsv = (ListView) v2.findViewById(R.id.your_own_written_list);
		return v2;

	}

	@Override
	public void onRefresh() {
		swipeRefreshLayout.setRefreshing(true);
		Log.e("REFRESHING", "REFRESHING");
		fetchData();
		// fetchMovies();

	}

	private void fetchData() {

		StringRequestActivity stringRequestActivity = new StringRequestActivity(this);
		String url = VolleySingleton.QUOTES_YOU_WRITTEN_URL
				+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0));
		stringRequestActivity.makeStringReqWithInterface(url);

		// TODO Auto-generated method stub

	}

	// private void fetchMovies() {
	//
	// DatabaseHandler db = new DatabaseHandler(getActivity());
	// arraylist.clear();
	//
	// List<OwnWrittenQuotes> contacts = db.getAllContacts();
	//
	// for (OwnWrittenQuotes quote : contacts)
	//
	// {
	//
	// OwnWrittenQuotes owq = new OwnWrittenQuotes(quote.getSubCategory(),
	// quote.getQoute());
	// Log.e("QQQQQQQQQQ: ", quote.getQoute());
	// arraylist.add(owq);
	//
	// }
	//
	// owa.notifyDataSetChanged();
	//
	// // lsv.indexOfChild(child)
	//
	// swipeRefreshLayout.setRefreshing(false);
	//
	// }

	@Override
	public void onRestIntractionResponse(String response) {
		// TODO Auto-generated method stub

		swipeRefreshLayout.setRefreshing(false);

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (jsonObject.has("data")) {
			JSONArray jsonArray = jsonObject.optJSONArray("data");

			arraylist = new ArrayList<OwnWrittenQuotes>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject dataJsonObj = jsonArray.optJSONObject(i);

				OwnWrittenQuotes owq = new OwnWrittenQuotes();

				owq.setQuoteid(dataJsonObj.optString("quoteid"));
				owq.setQuote(dataJsonObj.optString("quote"));
				owq.setPicture(dataJsonObj.optString("picture"));
				owq.setFullname(dataJsonObj.optString("fullname"));
				owq.setCategoryid(dataJsonObj.optString("categoryid"));
				owq.setSubcategoryid(dataJsonObj.optString("subcategoryid"));

				owq.setCategory(dataJsonObj.optString("category"));
				owq.setSubcategory(dataJsonObj.optString("subcategory"));
				owq.setHeartGrowth(dataJsonObj.optInt("amount"));

				arraylist.add(owq);

			}

			OwnWrittenAdapter q = new OwnWrittenAdapter(getActivity(), arraylist);

//			owa = new OwnWrittenAdapter(getActivity(), arraylist);
			lsv.setAdapter(q);

			swipeRefreshLayout.setRefreshing(false);
		}

	}

	@Override
	public void onRestIntractionError(String message) {
		// TODO Auto-generated method stub
		swipeRefreshLayout.setRefreshing(false);
		Toast.makeText(getActivity(), "Iternet Error", Toast.LENGTH_SHORT).show();

	}

}
