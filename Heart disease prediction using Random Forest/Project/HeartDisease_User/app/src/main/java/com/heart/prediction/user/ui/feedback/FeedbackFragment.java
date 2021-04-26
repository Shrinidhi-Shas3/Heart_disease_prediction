package com.heart.prediction.user.ui.feedback;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.heart.prediction.user.R;
import com.heart.prediction.user.utils.LoginSharedPref;
import com.heart.prediction.user.webservices.JSONParse;
import com.heart.prediction.user.webservices.RestAPI;
import com.heart.prediction.user.webservices.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class FeedbackFragment extends Fragment {
    private View fView;
    private SharedPreferences sharedPreferences;
    private String pid = "";
    private ListView listView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<String> name, src, feed, date, time;
    private Dialog mDialog;
    //name,src,feed,date,time

    public FeedbackFragment() {
        // Required empty public constructor
    }


    private void startAnimation() {
        mDialog = new Dialog(getActivity(), R.style.AppTheme_NoActionBar);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8D000000")));
        final View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_loader, null);
        LottieAnimationView animationView = view.findViewById(R.id.loader);
        animationView.playAnimation();
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void stopAnimation() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fView = inflater.inflate(R.layout.fragment_send, container, false);

        listView = (ListView) fView.findViewById(R.id.feedbackList);
        floatingActionButton = (FloatingActionButton) fView.findViewById(R.id.addFeedback);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Feedback feedback = new Feedback(getActivity());
                feedback.show();
            }
        });

        return fView;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetFeedback().execute(LoginSharedPref.getUserId(getActivity()));
    }

    private class GetFeedback extends AsyncTask<String, JSONObject, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnimation();
        }

        @Override
        protected String doInBackground(String... params) {
            String a = "back";
            RestAPI api = new RestAPI();
            try {
                JSONObject json = api.PgetFeedback(params[0]);
                JSONParse jp = new JSONParse();
                a = jp.JSONParse(json);
            } catch (Exception e) {
                a = e.getMessage();
            }
            return a;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.wtf("reply", "onPostExecute: "+s);
            stopAnimation();
//            Toast.makeText(getActivity(), s+"", Toast.LENGTH_SHORT).show();
            if (Utility.checkConnection(s)) {
                Pair<String, String> errorMessage = Utility.GetErrorMessage(s);
                Utility.ShowAlertDialog(getActivity(), errorMessage.first, errorMessage.second, false);
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    String ans = json.getString("status");
//                    Log.d("ANS", s);
                    if (ans.compareTo("ok") == 0) {
                        name = new ArrayList<String>();
                        feed = new ArrayList<String>();
                        date = new ArrayList<String>();
                        time = new ArrayList<String>();

                        JSONArray jarray = json.getJSONArray("Data");
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jdata = jarray.getJSONObject(i);
                            name.add(jdata.getString("data0"));
                            feed.add(jdata.getString("data1"));
                            date.add(jdata.getString("data2"));
                            time.add(jdata.getString("data3"));
                        }

                        Adapter adapter = new Adapter(getActivity(), feed);
                        listView.setAdapter(adapter);

                    } else if (ans.compareTo("no") == 0) {
                        Utility.ShowAlertDialog(getActivity(), "No Feedback!", "You have not added any feedback.", false);
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class Adapter extends ArrayAdapter<String> {
        Context con;

        public Adapter(@NonNull Context context, ArrayList<String> a) {
            super(context, R.layout.feed_list_item, a);
            con = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = LayoutInflater.from(con).inflate(R.layout.feed_list_item, null, true);
            TextView fname = (TextView) v.findViewById(R.id.FeedName);
            TextView fdate = (TextView) v.findViewById(R.id.FeedDate);
            TextView ftime = (TextView) v.findViewById(R.id.FeedTime);

            fname.setText(feed.get(position));
            fdate.setText("Date : " + date.get(position));
            ftime.setText("Time : " + time.get(position));
            return v;
        }
    }

    public class Feedback extends Dialog {
        protected View mView;
        private Context context;

        protected Calendar calendar;
        protected SimpleDateFormat sdfd = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        protected SimpleDateFormat sdft = new SimpleDateFormat("HH:mm", Locale.US);

        protected EditText edtFeedbackText;
        protected Button btnSendFeed;
        private Dialog mDialog;

        public Feedback(@NonNull Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_feedback);

            edtFeedbackText = (EditText) findViewById(R.id.feedbackText);
            btnSendFeed = (Button) findViewById(R.id.btnSend);

            btnSendFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    calendar = Calendar.getInstance();

                    if (edtFeedbackText.getText().toString().length() == 0) {
                        Snackbar.make(v, "Enter Some FeedBack.", Snackbar.LENGTH_SHORT).show();
                        edtFeedbackText.requestFocus();
                    } else {
//                    string uid, string did, string src, string feed, string date, string time
                        new SENDFEED().execute(LoginSharedPref.getUserId(getActivity()), edtFeedbackText.getText().toString(), sdfd.format(calendar.getTime()),
                                sdft.format(calendar.getTime()));
                    }
                }
            });
        }

        public class SENDFEED extends AsyncTask<String, JSONObject, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                startAnimation();
            }

            @Override
            protected String doInBackground(String... strings) {
                String a = "back";
                RestAPI api = new RestAPI();
                try {
//                string uid, string did, string src, string feed, string date, string time
                    JSONObject json = api.AddFeedBack(strings[0], strings[1], strings[2], strings[3]);
                    JSONParse jp = new JSONParse();
                    a = jp.JSONParse(json);
                } catch (Exception e) {
                    a = e.getMessage();
                }
                return a;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                stopAnimation();
                if (Utility.checkConnection(s)) {
                    Pair<String, String> errorMessage = Utility.GetErrorMessage(s);
                    Utility.ShowAlertDialog(getActivity(), errorMessage.first, errorMessage.second, false);
                } else {

                    try {
                        JSONObject json = new JSONObject(s);
                        String StatusValue = json.getString("status");

                        if (StatusValue.compareTo("true") == 0) {

                            Snackbar.make(btnSendFeed, "Feedback Added Successfully", Snackbar.LENGTH_SHORT).show();

                            edtFeedbackText.setText("");

                            if (isShowing()) {
                                dismiss();
                            }

                            new GetFeedback().execute(LoginSharedPref.getUserId(getActivity()));

                        } else if (StatusValue.compareTo("error") == 0) {
                            String error = json.getString("Data");
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            if (isShowing()) {
                                dismiss();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        if (isShowing()) {
                            dismiss();
                        }
                    }

                }
            }
        }
    }

}


