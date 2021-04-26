package com.heart.prediction.admin.doctor;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.heart.prediction.admin.R;
import com.heart.prediction.admin.webservices.JSONParse;
import com.heart.prediction.admin.webservices.RestAPI;
import com.heart.prediction.admin.webservices.Utility;

import org.json.JSONObject;

import java.util.ArrayList;

public class AddDoctor extends AppCompatActivity {

    ArrayList<String> type;
    Spinner cat;
    Dialog cd;
    TextView latlng;
    EditText name, age, email, contact, add;
    private RadioButton male, female;
    EditText[] textboxes;
    String[] mesg = new String[]{"Name", "Age", "Email", "Contact", "Address"};
    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docdetails);
        setActionbar("Add Doctor");

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        email = (EditText) findViewById(R.id.email);
        contact = (EditText) findViewById(R.id.cont);
        cat = (Spinner) findViewById(R.id.cat);
        add = (EditText) findViewById(R.id.add);

        submit = (Button) findViewById(R.id.submit);

        textboxes = new EditText[]{name, age, email, email, contact, add};

        type = new ArrayList<String>();
        type.add("Select Category");
        type.add("Basic");
        type.add("Heart");
        type.add("Brain");
        type.add("Nerve");
        type.add("Physio");
        type.add("Infectious");
        type.add("STD");

        final ArrayAdapter<String> ad = new ArrayAdapter<String>(AddDoctor.this, R.layout.spin_item, R.id.txtid, type);
        cat.setAdapter(ad);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAll(v)) {
                    if (cat.getSelectedItemPosition() > 0) {
                        String gender = male.isChecked() ? "Male" : "Female";
                        //String Name,String Address,String cont,String email,String age,String gender,String cate
                        new AddDoctorTask().execute(name.getText().toString(), add.getText().toString(), contact.getText().toString(), email.getText().toString(),
                                age.getText().toString(), gender, cat.getSelectedItem().toString());
                    } else {
                        Snackbar.make(v, "Select a Category", Snackbar.LENGTH_SHORT).show();
                    }
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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
        cd = new Dialog(AddDoctor.this, R.style.AppTheme);
        cd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.setContentView(R.layout.circular_dialog);
        cd.setCancelable(false);
        cd.show();
    }

    private class AddDoctorTask extends AsyncTask<String, JSONObject, String> {
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
                JSONObject json = api.AddDoc(params[0], params[1], params[2], params[3], params[4], params[5], params[6]);
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
                Utility.ShowAlertDialog(AddDoctor.this, error.first, error.second, false);
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    String ans = json.getString("status");
                    if (ans.compareTo("true") == 0) {
                        Utility.ShowAlertDialog(AddDoctor.this
                                , "Doctor Added", "", true);
                    } else if (ans.compareTo("already") == 0) {
                        Utility.ShowAlertDialog(AddDoctor.this
                                , "Email Address already Exits!", "", false);
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(AddDoctor.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AddDoctor.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
