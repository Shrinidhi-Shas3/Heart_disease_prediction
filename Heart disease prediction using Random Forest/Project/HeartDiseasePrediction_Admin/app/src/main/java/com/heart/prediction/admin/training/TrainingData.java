package com.heart.prediction.admin.training;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.heart.prediction.admin.R;
import com.heart.prediction.admin.models.TrainingModel;
import com.heart.prediction.admin.webservices.JSONParse;
import com.heart.prediction.admin.webservices.RestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrainingData extends AppCompatActivity {

    private ArrayList<TrainingModel> mTrainingData;
    Dialog cd;
    ListView list;
    FloatingActionButton dadd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_acitivity);

        setActionbar("TrainingData");
        list = (ListView) findViewById(R.id.list);
        dadd = (FloatingActionButton) findViewById(R.id.dadd);

        dadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TrainingData.this, AddTrainingActivity.class);
                startActivity(i);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TrainingData.this, TrainingDetails.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("DATA", mTrainingData.get(position));
                i.putExtra("BUNDLE", bundle);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetTrainingData().execute();
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
        cd = new Dialog(TrainingData.this, R.style.AppTheme);
        cd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.setContentView(R.layout.circular_dialog);
        cd.setCancelable(false);
        cd.show();
    }

    private class GetTrainingData extends AsyncTask<String, JSONObject, String> {
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
                JSONObject json = api.getTrainingData();
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
            if (s.contains("Unable to resolve host")) {
                AlertDialog.Builder ad = new AlertDialog.Builder(TrainingData.this);
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
                        mTrainingData = new ArrayList<TrainingModel>();

                        JSONArray jarray = json.getJSONArray("Data");
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jdata = jarray.getJSONObject(i);
                            mTrainingData.add(new TrainingModel(
                                    jdata.getString("data0")
                                    , jdata.getString("data1")
                                    , jdata.getString("data2")
                                    , jdata.getString("data3")
                                    , jdata.getString("data4")
                                    , jdata.getString("data5")
                                    , jdata.getString("data6")
                                    , jdata.getString("data7")
                                    , jdata.getString("data8")
                                    , jdata.getString("data9")
                                    , jdata.getString("data10")
                                    , jdata.getString("data11")
                                    , jdata.getString("data12")
                                    , jdata.getString("data13")
                                    , jdata.getString("data14")));


                        }

                        Adapter adapt = new Adapter(TrainingData.this, mTrainingData);
                        list.setAdapter(adapt);

                    } else if (ans.compareTo("no") == 0) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(TrainingData.this);
                        ad.setTitle("No Training Data Added!");
                        ad.setMessage("To add Training click on the Bottom Right Add Button");
                        ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        ad.show();
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(TrainingData.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(TrainingData.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class Adapter extends ArrayAdapter<TrainingModel> {
        Context con;

        public Adapter(@NonNull Context context, ArrayList<TrainingModel> a) {
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

            tname.setText(mTrainingData.get(position).getDiseaseName());
            ttype.setText(String.format("Age :%s", mTrainingData.get(position).getAge()));
            tcity.setText(String.format("Gender : %s",
                    (mTrainingData.get(position).getGender().equalsIgnoreCase("0")
                            ? "Male" : "Female")));
            return v;
        }
    }
}
