package com.csform.businessportfolio.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.content.Intent;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.csform.businessportfolio.BaseActivity;
import com.csform.businessportfolio.R;

public class EventsFragment extends Fragment {

	private String url = "http://techguru.byethost32.com/android/event.php?json={}";
	LinearLayout lm;
	public static EventsFragment newInstance() {
		EventsFragment eventsFragment = new EventsFragment ();
		return eventsFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View eventsView = inflater.inflate(R.layout.events, container, false);

		TextView companyName = (TextView) eventsView.findViewById(R.id.page_title);
		 lm = (LinearLayout) eventsView.findViewById(R.id.linearMain);
		companyName.setTypeface(BaseActivity.sRobotoThin);
		return eventsView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		JSONObject jo = new JSONObject();
		RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
		StringRequest request = new StringRequest(url + jo,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String arg0) {

						jSONifier(arg0);

					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity().getApplicationContext(),
						"Network connection error", Toast.LENGTH_LONG)
						.show();
				Toast.makeText(getActivity().getApplicationContext(),
						"Check your network connection",
						Toast.LENGTH_LONG).show();
				// Log.i(LOG, arg0.getMessage());
			}
		});
		queue.add(request);

	}
	JSONObject jso;

	private void jSONifier(String arg0) {


		try {
			JSONObject obj = new JSONObject(arg0);
			if (obj.getString("success").equals("1")) {

				JSONArray array = obj.getJSONArray("event");
				Gson gson = new Gson();
				jso = new JSONObject();

				for (int j = 0; j < array.length(); ++j) {
					jso = array.getJSONObject(j);


					// Create LinearLayout
					LinearLayout ll = new LinearLayout(getActivity());
					DisplayMetrics lin = ll.getResources().getDisplayMetrics();

					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					layoutParams.setMargins(0,convertDpToPx(10,lin),0, convertDpToPx(10,lin));

					ll.setLayoutParams(layoutParams);

					// ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
					//ll.setOrientation(LinearLayout.HORIZONTAL);


					//Create View
					View view = new View(getActivity());
					DisplayMetrics dm = view.getResources().getDisplayMetrics();
					//view.setLayoutParams(new LayoutParams(convertDpToPx(8,dm), LayoutParams.MATCH_PARENT));
					LinearLayout.LayoutParams myView = new LinearLayout.LayoutParams(
							convertDpToPx(8,dm), LinearLayout.LayoutParams.MATCH_PARENT);
					myView.setMargins(convertDpToPx(10,dm),0,0,0);
					view.setLayoutParams(myView);
					view.setBackgroundResource(R.drawable.left_divider);
					//add view to linear layout
					ll.addView(view);


					//create inner LinearLayout
					LinearLayout inL = new LinearLayout(getActivity());
					inL.setBackgroundResource(R.drawable.rounded_background_white_30);
					LinearLayout.LayoutParams lin2 = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					lin2.setMargins(convertDpToPx(10,lin),0,convertDpToPx(10,lin),0);
					inL.setLayoutParams(lin2);
					// inL.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
					inL.setOrientation(LinearLayout.VERTICAL);


					// Create TextView
					String tittle = jso.getString("title");
					TextView title = new TextView(getActivity());
					title.setText(tittle +", " + jso.get("edate"));
					title.setTextColor(Color.WHITE);
					title.setPadding(convertDpToPx(14,dm), convertDpToPx(14,dm), convertDpToPx(14,dm), 0);
					title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
					title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					inL.addView(title);

					// Create TextView
					String desc = jso.getString("description");
					TextView description = new TextView(getActivity());
					description.setText(desc);
					description.setTextColor(Color.WHITE);
					description.setPadding(convertDpToPx(14,dm), convertDpToPx(26,dm), convertDpToPx(14,dm), convertDpToPx(26,dm));
					description.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
					title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
					inL.addView(description);

					// Create Button
					final Button btn = new Button(getActivity());
					// Give button an ID
					btn.setId(j+1);
					btn.setText("Share Event Via");
					btn.setTextColor(Color.WHITE);
					// set the layoutParams on the button
					btn.setLayoutParams(lin2);

					final int index = j;
					// Set click listener for button
					btn.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {

							Log.i("TAG", "index :" + index);
							shareTextUrl(tittle,desc);

						}
					});

					//Add button to LinearLayout
					inL.addView(btn);
					//adding inner LinearLayout to ll
					ll.addView(inL);
					//Add button to LinearLayout defined in XML
					lm.addView(ll);

				}

			} else {

				// toast(obj.getString("message"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
		float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
		return Math.round(pixels);
	}

	private void shareTextUrl(String title, String body) {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		// Add data to the intent, the receiving app will decide
		// what to do with it.
		share.putExtra(Intent.EXTRA_SUBJECT, title);
		share.putExtra(Intent.EXTRA_TEXT, body);

		startActivity(Intent.createChooser(share, "Share Event!"));
	}

}
