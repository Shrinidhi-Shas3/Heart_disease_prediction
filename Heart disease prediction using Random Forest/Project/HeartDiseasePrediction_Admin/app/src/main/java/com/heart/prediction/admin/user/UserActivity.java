package com.heart.prediction.admin.user;

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

import com.heart.prediction.admin.R;
import com.heart.prediction.admin.models.UserModel;
import com.heart.prediction.admin.webservices.JSONParse;
import com.heart.prediction.admin.webservices.RestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    ListView list;
    private ArrayList<UserModel> mUserModels;
    Dialog cd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        list = (ListView) findViewById(R.id.list);
        setActionbar("Users");
        new GetUsers().execute();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(UserActivity.this, UserDetails.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("DATA", mUserModels.get(position));
                i.putExtra("BUNDLE", bundle);
                startActivity(i);
            }
        });
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

    public void prepareDialog() {
        cd = new Dialog(UserActivity.this, R.style.AppTheme);
        cd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.setContentView(R.layout.circular_dialog);
        cd.setCancelable(false);
        cd.show();
    }

    private class GetUsers extends AsyncTask<String, JSONObject, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prepareDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String a = "back";
            RestAPI api = new RestAPI();
            try {
                JSONObject json = api.getUsers();
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
                AlertDialog.Builder ad = new AlertDialog.Builder(UserActivity.this);
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
                        mUserModels = new ArrayList<UserModel>();

                        JSONArray jarray = json.getJSONArray("Data");
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jdata = jarray.getJSONObject(i);
                            //Name,Address, Mobile, Email, Age, Gender,
                            mUserModels.add(new UserModel(
                                    jdata.getString("data0")
                                    , jdata.getString("data1")
                                    , jdata.getString("data2")
                                    , jdata.getString("data3")
                                    , jdata.getString("data4")
                                    , jdata.getString("data5")
                                    , jdata.getString("data6")));
                        }

                        Adapter adapt = new Adapter(UserActivity.this, mUserModels);
                        list.setAdapter(adapt);

                    } else if (ans.compareTo("no") == 0) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(UserActivity.this);
                        ad.setTitle("No Users have Registered Yet!");
                        ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        ad.show();
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(UserActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class Adapter extends ArrayAdapter<UserModel> {
        Context con;

        public Adapter(@NonNull Context context, ArrayList<UserModel> a) {
            super(context, R.layout.user_listitem, a);
            con = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = LayoutInflater.from(con).inflate(R.layout.user_listitem, null, true);
            TextView tname = (TextView) v.findViewById(R.id.name);
            TextView tcity = (TextView) v.findViewById(R.id.city);

            tname.setText(mUserModels.get(position).getName());
            tcity.setText("Address: " + mUserModels.get(position).getAddress());

            return v;
        }
    }
}
