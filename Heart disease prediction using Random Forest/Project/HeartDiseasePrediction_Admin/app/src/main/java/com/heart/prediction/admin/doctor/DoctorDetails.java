package com.heart.prediction.admin.doctor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.heart.prediction.admin.R;
import com.heart.prediction.admin.webservices.JSONParse;
import com.heart.prediction.admin.webservices.RestAPI;

import org.json.JSONObject;

import java.util.ArrayList;

public class DoctorDetails extends AppCompatActivity {

    ArrayList<String> type;
    Spinner cat;
    String did = "";
    Dialog cd;
    ImageView map;
    TextView latlng;
    EditText name, age, email, contact, add;
    private RadioButton male, female;
    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docdetails);
        setActionbar("Doctor Details");
        submit = (Button) findViewById(R.id.submit);
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        email = (EditText) findViewById(R.id.email);
        contact = (EditText) findViewById(R.id.cont);
        cat = (Spinner) findViewById(R.id.cat);
        add = (EditText) findViewById(R.id.add);

        type = new ArrayList<String>();
        type.add("Select Category");
        type.add("Basic");
        type.add("Heart");
        type.add("Brain");
        type.add("Nerve");
        type.add("Physio");
        type.add("Infectious");
        type.add("STD");
        ArrayAdapter<String> ad = new ArrayAdapter<>(DoctorDetails.this, R.layout.spin_item, R.id.txtid, type);
        cat.setAdapter(ad);

        String[] str = getIntent().getStringArrayExtra("DATA");
        did = str[0];
        name.setText(str[1]);
        age.setText(str[2]);
        if (str[3].equalsIgnoreCase("Male")) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }
        email.setText(str[4]);
        contact.setText(str[5]);

        for (int i = 0; i < type.size(); i++) {
            if (str[6].compareTo(type.get(i)) == 0) {
                cat.setSelection(i);
            }
        }

        add.setText(str[7]);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAll(v)) {
                    String gender = male.isChecked() ? "Male" : "Female";
                    //String did,String Name,String Address,String cont,String email,String age,String gender,String cate
                    new UpdateDoctorTask().execute(did, name.getText().toString(), add.getText().toString()
                            , contact.getText().toString(), email.getText().toString(),
                            age.getText().toString(), gender, cat.getSelectedItem().toString());
                }
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean checkAll(View v) {
        if (name.getText().length() == 0) {
            Snackbar.make(v, "Please enter name.", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (age.getText().length() == 0) {
            Snackbar.make(v, "Please enter age.", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(age.getText().toString()) == 0) {
            Snackbar.make(v, "Please enter valid age.", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (email.getText().length() == 0) {
            Snackbar.make(v, "Please enter email.", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!isEmailValid(email.getText().toString())) {
            Snackbar.make(v, "Please enter valid email.", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (contact.getText().length() == 0) {
            Snackbar.make(v, "Please enter contact", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (add.getText().length() == 0) {
            Snackbar.make(v, "Please enter address", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.delete) {
            AlertDialog.Builder ad = new AlertDialog.Builder(DoctorDetails.this);
            ad.setTitle("Delete ? ");
            ad.setMessage("Are you sure you want to Remove the Doctor?");
            ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    new DeleteDoctorTask().execute(did);
                }
            });
            ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            ad.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setActionbar(String title) {
        getSupportActionBar().setTitle(Html.fromHtml(title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void dailog() {
        cd = new Dialog(DoctorDetails.this, R.style.AppTheme);
        cd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.setContentView(R.layout.circular_dialog);
        cd.setCancelable(false);
        cd.show();
    }

    private class UpdateDoctorTask extends AsyncTask<String, JSONObject, String> {
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
                JSONObject json = api.UpdateDoc(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7]);
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
            Log.d("REPLY", s);
            if (s.contains("Unable to resolve host")) {
                AlertDialog.Builder ad = new AlertDialog.Builder(DoctorDetails.this);
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
                    if (ans.compareTo("true") == 0) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(DoctorDetails.this);
                        ad.setTitle("Doctor details Updated");
                        ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });
                        ad.show();
                    } else if (ans.compareTo("already") == 0) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(DoctorDetails.this);
                        ad.setTitle("Doctor already Exits!");
                        ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        ad.show();
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(DoctorDetails.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(DoctorDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class DeleteDoctorTask extends AsyncTask<String, JSONObject, String> {
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
                JSONObject json = api.DelDoc(params[0]);
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
                AlertDialog.Builder ad = new AlertDialog.Builder(DoctorDetails.this);
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
                    if (ans.compareTo("true") == 0) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(DoctorDetails.this);
                        ad.setTitle("Doctor Entry Deleted");
                        ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });
                        ad.show();
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(DoctorDetails.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(DoctorDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
