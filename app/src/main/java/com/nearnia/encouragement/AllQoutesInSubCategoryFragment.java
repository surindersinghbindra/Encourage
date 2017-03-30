package com.nearnia.encouragement;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nearnia.encouragement.util.StringRequestActivity;
import com.squareup.picasso.Picasso;

import com.nearnia.encouragement.InterfacesForcallBack.IAsyncResponse;
import com.nearnia.encouragement.adapters.AllQoutesInSubCategoryAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.nearnia.encouragement.beanclasses.AllQoutesBean;

public class AllQoutesInSubCategoryFragment extends Fragment implements IAsyncResponse {
	private ArrayList<AllQoutesBean> arraylist;
	private ListView lv;

	private String subCategoryId;
	private String subCategoryName;
	private TextView categoryNameAllCat;
	private ImageView profile_cover_image123;
	private View v2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			subCategoryId = getArguments().getString("SUB_CATEGORY_ID");
			getArguments().getString("CATEGORY_NAME");
			subCategoryName = getArguments().getString("SUB_CATEGORY_NAME");
			// mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

		v2 = inflater.inflate(R.layout.debt_free_layout, container, false);

		categoryNameAllCat = (TextView) v2.findViewById(R.id.subcategoryNameInAllCat);

//		subcategoryNameInAllCat = (TextView) v2.findViewById(R.id.subcategoryNameInAllCat123);

		profile_cover_image123 = (ImageView) v2.findViewById(R.id.profile_cover_image123);

//		QuotesYouLiked obj = (QuotesYouLiked) UniversalSingleton.getInstance().getObject();

		categoryNameAllCat.setText(subCategoryName);
//		subcategoryNameInAllCat.setText(categoryName);

		StringRequestActivity stringRequestActivity = new StringRequestActivity(this);

		// stringRequestActivity.makeStringReqWithInterface(
		// "http://techwinlabs.com/inspiration/index.php?method=subcategoryQuotes&userid="
		// +
		// String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID,
		// 0)
		// + "&subcategoryid=" + obj.getSubcategoryid()));

		stringRequestActivity.makeStringReqWithInterface(
				"http://techwinlabs.com/inspiration/index.php?method=subcategoryQuotes&userid="
						+ String.valueOf(VolleySingleton.userLogin.getInt(VolleySingleton.LOGIN_ID, 0)
								+ "&subcategoryid=" + subCategoryId));

		arraylist = new ArrayList<AllQoutesBean>();

		lv = (ListView) v2.findViewById(R.id.selected_categories_settings1);

		return v2;
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
		if (jsonObject.optInt("success") == 1) {
			JSONArray topDataArray = jsonObject.optJSONArray("topdata");

			JSONObject topDataJsonObj = topDataArray.optJSONObject(0);
			Picasso.with(getActivity()).load(topDataJsonObj.optString("coverpicture").replace(" ", "%20"))
					.placeholder(R.drawable.logo).into(profile_cover_image123);

			if (jsonObject.has("data")) {

				JSONArray jsonArray = jsonObject.optJSONArray("data");

				arraylist = new ArrayList<AllQoutesBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataJsonObj = jsonArray.optJSONObject(i);

					AllQoutesBean allQoutesBean = new AllQoutesBean();

					allQoutesBean.setQuoteid(dataJsonObj.optString("quoteid"));
					allQoutesBean.setQuote(dataJsonObj.optString("quote"));
					allQoutesBean.setPicture(dataJsonObj.optString("picture"));
					allQoutesBean.setFullname(dataJsonObj.optString("fullname"));
					allQoutesBean.setCategoryid(dataJsonObj.optString("categoryid"));
					allQoutesBean.setSubcategoryid(dataJsonObj.optString("subcategoryid"));
					allQoutesBean.setHeartGrowth((dataJsonObj.optInt("amount")));
					allQoutesBean.setUserid((dataJsonObj.optString("userid")));

					arraylist.add(allQoutesBean);

				}

				AllQoutesInSubCategoryAdapter q = new AllQoutesInSubCategoryAdapter(getActivity(), arraylist);
				lv.setAdapter(q);
			} else {
				Toast.makeText(getActivity(), "Dont Have Quotes in this category", Toast.LENGTH_SHORT).show();
			}

		}
//		Toast.makeText(getActivity(), "Not a valid category", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onRestIntractionError(String message) {
		// TODO Auto-generated method stub

		Toast.makeText(getActivity(), "Iternet Error", Toast.LENGTH_SHORT).show();

	}

}
