package com.heart.prediction.admin.doctor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.heart.prediction.admin.R;
import com.heart.prediction.admin.models.DoctorModel;
import com.heart.prediction.admin.webservices.JSONParse;
import com.heart.prediction.admin.webservices.RestAPI;
import com.heart.prediction.admin.webservices.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Doctor extends AppCompatActivity {

    //private ArrayList<String> did, name, add, age, category, ll, cont, email, first, rest, currency;
    private ArrayList<DoctorModel> mDoctors;
    private Dialog cd;
    private ListView list;
    private FloatingActionButton dadd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor);

        setActionbar("Doctor");

        list = (ListView) findViewById(R.id.list);
        dadd = (FloatingActionButton) findViewById(R.id.dadd);

        dadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Doctor.this, AddDoctor.class);
                startActivity(i);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Doctor.this, DoctorDetails.class);
                String[] str = new String[]{mDoctors.get(position).getDid()
                        , mDoctors.get(position).getName(), mDoctors.get(position).getAge()
                        , mDoctors.get(position).getGender(), mDoctors.get(position).getEmail()
                        , mDoctors.get(position).getMobile(), mDoctors.get(position).getSpecialization()
                        , mDoctors.get(position).getAddress()};
                i.putExtra("DATA", str);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetDoctorsTask().execute();
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
        cd = new Dialog(Doctor.this, R.style.AppTheme);
        cd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.setContentView(R.layout.circular_dialog);
        cd.setCancelable(false);
        cd.show();
    }

    private class GetDoctorsTask extends AsyncTask<String, JSONObject, String> {
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
                JSONObject json = api.getDoctors();
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
            if (Utility.checkConnection(s)) {
                Pair<String, String> error = Utility.GetErrorMessage(s);
                Utility.ShowAlertDialog(Doctor.this, error.first, error.second, false);
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    String ans = json.getString("status");
                    Log.d("ANS", s);
                    if (ans.compareTo("ok") == 0) {
                        mDoctors = new ArrayList<DoctorModel>();

                        //                        did=new ArrayList<String>();
//                        name=new ArrayList<String>();
//                        add=new ArrayList<String>();
//                        age=new ArrayList<String>();
//                        category=new ArrayList<String>();
//                        ll=new ArrayList<String>();
//                        cont=new ArrayList<String>();
//                        email=new ArrayList<String>();
//                        first=new ArrayList<String>();
//                        rest=new ArrayList<String>();
//                        currency=new ArrayList<String>();

                        JSONArray jarray = json.getJSONArray("Data");
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jdata = jarray.getJSONObject(i);
                            mDoctors.add(new DoctorModel(jdata.getString("data0")
                                    , jdata.getString("data1")
                                    , jdata.getString("data2")
                                    , jdata.getString("data3")
                                    , jdata.getString("data4")
                                    , jdata.getString("data5")
                                    , jdata.getString("data6")
                                    , jdata.getString("data7")));

                            //                            did.add(jdata.getString("data0"));
//                            name.add(jdata.getString("data1"));
//                            add.add(jdata.getString("data2"));
//                            age.add(jdata.getString("data3"));
//                            category.add(jdata.getString("data4"));
//                            ll.add(jdata.getString("data5"));
//                            cont.add(jdata.getString("data6"));
//                            email.add(jdata.getString("data7"));
//                            first.add(jdata.getString("data8"));
//                            rest.add(jdata.getString("data9"));
//                            currency.add(jdata.getString("data10"));

                        }

                        Adapter adapt = new Adapter(Doctor.this, mDoctors);
                        list.setAdapter(adapt);

                    } else if (ans.compareTo("no") == 0) {
                        Utility.ShowAlertDialog(Doctor.this
                                , "No Doctors Added!"
                                , "To add Doctors click on the Bottom Right Button"
                                , false);
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(Doctor.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Doctor.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class Adapter extends ArrayAdapter<DoctorModel> {
        Context con;

        public Adapter(@NonNull Context context, ArrayList<DoctorModel> a) {
            super(context, R.layout.doctor_listitem, a);
            con = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = LayoutInflater.from(con).inflate(R.layout.doctor_listitem, null, true);
            TextView tname = (TextView) v.findViewById(R.id.name);
            TextView ttype = (TextView) v.findViewById(R.id.type);
            TextView tcity = (TextView) v.findViewById(R.id.city);

            tname.setText(mDoctors.get(position).getName());
            ttype.setText("Specialization: " + mDoctors.get(position).getSpecialization());
            tcity.setText("Address: " + mDoctors.get(position).getAddress());
            return v;
        }
    }
}
