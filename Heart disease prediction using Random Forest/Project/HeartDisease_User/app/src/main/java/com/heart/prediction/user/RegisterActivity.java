package com.heart.prediction.user;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.heart.prediction.user.webservices.JSONParse;
import com.heart.prediction.user.webservices.RestAPI;
import com.heart.prediction.user.webservices.Utility;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends AppCompatActivity {
    //Personal Information View
    private EditText sname, sconact, semail, sage, saddr1, spassword;
    private RadioButton maleBtn,femaleBtn;
    private RadioGroup s1;
    private String gender="";
    private MaterialButton sRegister;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        //General Student Entry View ID's
        sname = (EditText) findViewById(R.id.sRegName);
        maleBtn = (RadioButton) findViewById(R.id.male);
        femaleBtn = (RadioButton) findViewById(R.id.female);
        s1 = (RadioGroup) findViewById(R.id.genderGroup);
        sconact = (EditText) findViewById(R.id.sRegContact);
        semail = (EditText) findViewById(R.id.sRegEmail);
        sage = (EditText) findViewById(R.id.sRegDob);
        saddr1 = (EditText) findViewById(R.id.sRegAddr1);
        spassword = (EditText) findViewById(R.id.sRegPassword);

        sRegister = (MaterialButton) findViewById(R.id.sRegisterButton);

        maleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(maleBtn.isChecked())
                    gender = "Male";
//                Toast.makeText(PatientRegisterActivity.this, gender+"", Toast.LENGTH_SHORT).show();
            }
        });

        femaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(femaleBtn.isChecked())
                    gender = "Female";
//                Toast.makeText(PatientRegisterActivity.this, gender+"", Toast.LENGTH_SHORT).show();
            }
        });


        sRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndAdd();
            }
        });
    }

    private void stopAnimation() {
        if (mDialog.isShowing())
            mDialog.cancel();
    }

    private void startAnimation() {
        mDialog = new Dialog(RegisterActivity.this, R.style.AppTheme_NoActionBar);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8D000000")));
        final View view = RegisterActivity.this.getLayoutInflater().inflate(R.layout.custom_dialog_loader, null);
        LottieAnimationView animationView = view.findViewById(R.id.loader);
        animationView.playAnimation();
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void validateAndAdd() {
        if (sname.getText().toString().length() == 0) {
            Snackbar.make(sRegister, "Enter Name", Snackbar.LENGTH_SHORT).show();
            sname.requestFocus();
        } else if(!maleBtn.isChecked()&& !femaleBtn.isChecked()){
            Snackbar.make(sRegister, "Please Choose Gender", Snackbar.LENGTH_SHORT).show();
            s1.requestFocus();
        }else if (semail.getText().toString().length() == 0) {
            Snackbar.make(sRegister, "Enter Email", Snackbar.LENGTH_SHORT).show();
            semail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(semail.getText().toString()).matches()) {
            Snackbar.make(sRegister, "Enter Valid Email", Snackbar.LENGTH_SHORT).show();
            semail.requestFocus();
        } else if (sconact.getText().toString().length() == 0) {
            Snackbar.make(sRegister, "Enter Contact No", Snackbar.LENGTH_SHORT).show();
            sconact.requestFocus();
        } else if (sage.getText().length() == 0) {
            Snackbar.make(sRegister, "Enter your age", Snackbar.LENGTH_SHORT).show();
            sconact.requestFocus();
        } else if (Integer.parseInt(sage.getText().toString()) == 0) {
            Snackbar.make(sRegister, "Enter your valid Age", Snackbar.LENGTH_SHORT).show();
            sconact.requestFocus();
        } else if (saddr1.getText().length() == 0) {
            Snackbar.make(sRegister, "Enter Address", Snackbar.LENGTH_SHORT).show();
            saddr1.requestFocus();
        } else if (spassword.getText().length() == 0) {
            Snackbar.make(sRegister, "Enter Password", Snackbar.LENGTH_SHORT).show();
            spassword.requestFocus();
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

//            string pic, string dName, string gender, string dob, string address, string city, string state, string cont, string email, string pass
            new RegisterPatient().execute(
                    sname.getText().toString()
                    ,gender
                    , sage.getText().toString()
                    ,saddr1.getText().toString()
                    , sconact.getText().toString()
                    , semail.getText().toString()
                    , spassword.getText().toString());

//            Toast.makeText(PatientRegisterActivity.this,
//                       B64img+"\n"+
//                            sname.getText().toString()+"\n"+
//                            gender+"\n"+
//                            dateFormat.format(SDate.getTimeInMillis())+"\n"+
//                            saddr1.getText().toString()+"\n"+
//                            scity.getText().toString()+"\n"+
//                            sstate.getText().toString()+"\n"+
//                            sconact.getText().toString()+"\n"+
//                            semail.getText().toString()+"\n"+
//                            spassword.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public class RegisterPatient extends AsyncTask<String, JSONObject, String> {
        @Override
        protected void onPreExecute() {
            startAnimation();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String answer = "";
            RestAPI restAPI = new RestAPI();
            try {
                JSONObject jsonObject = restAPI.Register(strings[0], strings[1], strings[2], strings[3],
                        strings[4], strings[5], strings[6]);
                JSONParse jsonparse = new JSONParse();
                answer = jsonparse.JSONParse(jsonObject);
            } catch (Exception e) {
                answer = e.getMessage();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String s) {
            stopAnimation();
            Log.wtf("reply", "onPostExecute: "+s);
            String exp = "";
            if (Utility.checkConnection(s)) {
                Pair<String, String> errorMessage = Utility.GetErrorMessage(s);
                Utility.ShowAlertDialog(RegisterActivity.this, errorMessage.first, errorMessage.second, false);
            }else {
                try {
                    JSONObject object = new JSONObject(s);
                    exp = object.getString("status");
                    if (exp.compareTo("already") == 0) {
                        Snackbar.make(sRegister,"User already exists, Try using different Email", Snackbar.LENGTH_SHORT).show();
                    }  else if (exp.compareTo("true") == 0) {

                        Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                        finish();
                    }else if (exp.compareTo("error") == 0) {
                        String error = object.getString("Data");
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(s);
            }
        }
    }

}
