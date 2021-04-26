package com.heart.prediction.admin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.heart.prediction.admin.webservices.JSONParse;
import com.heart.prediction.admin.webservices.RestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Feedback extends AppCompatActivity {

    ListView list;
    ArrayList<String> name, feed, date, time;
    Dialog cd;
    boolean a = false;
    Adapter adapt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        list = (ListView) findViewById(R.id.list);
        setActionbar("Feedback");


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fdialog(name.get(position), feed.get(position), date.get(position) + " " + time.get(position));
            }
        });

        new GetFeedback().execute();
    }

    public void setActionbar(String title) {

        getSupportActionBar().setTitle(Html.fromHtml(title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void dailog() {
        cd = new Dialog(Feedback.this, R.style.AppTheme);
        cd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.setContentView(R.layout.circular_dialog);
        cd.setCancelable(false);
        cd.show();
    }

    private class GetFeedback extends AsyncTask<String, JSONObject, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dailog();
        }

        @Override
        protected String doInBackground(String... params) {
            String a = "back";
            RestAPI api = new RestAPI();
            try {
                JSONObject json = api.getFeedback();
                JSONParse jp = new JSONParse();
                a = jp.parse(json);
            } catch (Exception e) {
                a = e.getMessage();
            }
            return a;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cd.cancel();
            a = true;
            Log.d("FEED", s);
            if (s.contains("Unable to resolve host")) {
                AlertDialog.Builder ad = new AlertDialog.Builder(Feedback.this);
                ad.setTitle("Unable to Connect!");
                ad.setMessage("Check your Internet Connection,Unable to connect the Server");
                ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                ad.show();
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    String ans = json.getString("status");
                    Log.d("ANS", s);
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

                        adapt = new Adapter(Feedback.this, name);
                        list.setAdapter(adapt);
                    } else if (ans.compareTo("no") == 0) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(Feedback.this);
                        ad.setTitle("There are No Feedbacks!");
                        ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        ad.show();
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(Feedback.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Feedback.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class Adapter extends ArrayAdapter<String> {
        Context con;

        public Adapter(@NonNull Context context, ArrayList<String> a) {
            super(context, R.layout.training_listitem, a);
            con = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = LayoutInflater.from(con).inflate(R.layout.training_listitem, null, true);
            TextView tname = (TextView) v.findViewById(R.id.name);
            TextView ttype = (TextView) v.findViewById(R.id.type);
            TextView tcity = (TextView) v.findViewById(R.id.city);

            tname.setText(name.get(position));
            ttype.setText(date.get(position) + " " + time.get(position));
            tcity.setText("Feedback : " + feed.get(position));
            return v;
        }
    }

    public void Fdialog(String n, String f, String da) {
        final Dialog d = new Dialog(Feedback.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.feedback_dialog);
        d.show();

        TextView name = (TextView) d.findViewById(R.id.name);
        TextView feed = (TextView) d.findViewById(R.id.feed);
        TextView dt = (TextView) d.findViewById(R.id.dt);
        TextView cancel = (TextView) d.findViewById(R.id.cancel);

        name.setText(n);
        feed.setText(f);
        dt.setText(da);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });

    }
}
