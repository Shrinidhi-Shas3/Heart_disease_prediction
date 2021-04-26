package com.heart.prediction.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.heart.prediction.user.utils.EmailValidation;
import com.heart.prediction.user.utils.LoginSharedPref;
import com.heart.prediction.user.webservices.JSONParse;
import com.heart.prediction.user.webservices.RestAPI;
import com.heart.prediction.user.webservices.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailIdET, passET;
    private LinearLayout llLogin;
    private Button loginBtn, regBtn;
    private ArrayList<EditText> editTextAL;
    private EmailValidation validate;
    private String uName, UID,shareId;
    private Dialog dialogLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LoginSharedPref.getUserId(LoginActivity.this).equals("")){
            Intent regIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(regIntent);
            finish();
        }
        setContentView(R.layout.activity_login);
        initUI();
    }

    private void registerMe() {
        Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
//        //use for profile intent/
//        //regIntent.putExtra(UtilConstants.REG_OR_PROFILE_KEY, UtilConstants.PROFILE_VALUE);
//        regIntent.putExtra(UtilConstants.REG_OR_PROFILE_KEY, UtilConstants.REG_VALUE);
        startActivity(regIntent);
        //finish();
    }

    private void initUI() {
        emailIdET = findViewById(R.id.et_email_log);
        passET = findViewById(R.id.et_pass_log);
        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
        regBtn = findViewById(R.id.btn_inlogin_reg);
        regBtn.setOnClickListener(this);
        llLogin = findViewById(R.id.ll_login_ui);
        editTextAL = new ArrayList<>();
        editTextAL.add(emailIdET);
        editTextAL.add(passET);
        validate = new EmailValidation();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            if (emailIdET.getText().toString().isEmpty()) {
                Snackbar.make(llLogin, "Email field cannot be empty", Snackbar.LENGTH_SHORT).show();
            } else if (!validate.validateEmail(emailIdET.getText().toString())) {
                Snackbar.make(llLogin, "Email Id is invalid", Snackbar.LENGTH_SHORT).show();
            } else if (passET.getText().toString().isEmpty()) {
                Snackbar.make(llLogin, "Password field cannot be empty", Snackbar.LENGTH_SHORT).show();
            } else {
                loginSuccess();
            }
        } else if (id == R.id.btn_inlogin_reg) {
            registerMe();
        }
        hideKeyboard(v);
    }

    private void stopAnimation() {
        if (dialogLoader.isShowing())
            dialogLoader.cancel();
    }

    private void startAnimation() {
        dialogLoader = new Dialog(LoginActivity.this, R.style.AppTheme_NoActionBar);
        dialogLoader.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8D000000")));
        final View view = LoginActivity.this.getLayoutInflater().inflate(R.layout.custom_dialog_loader, null);
        LottieAnimationView animationView = view.findViewById(R.id.loader);
        animationView.playAnimation();
        dialogLoader.setContentView(view);
        dialogLoader.setCancelable(false);
        dialogLoader.show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void loginSuccess() {
        //LoginAPi succes->then
        LoginTask task = new LoginTask();
        task.execute(emailIdET.getText().toString(), passET.getText().toString());
    }

    //Params, prog, result
    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnimation();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            RestAPI restAPI = new RestAPI();
            try {
                JSONObject jsonObject = restAPI.PLogin(strings[0], strings[1]);
                JSONParse jsonParser = new JSONParse();
                result = jsonParser.JSONParse(jsonObject);
            } catch (Exception e) {
                result = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            stopAnimation();
            Log.wtf("reply", s);
            if (Utility.checkConnection(s)) {
                Pair<String, String> errorMessage = Utility.GetErrorMessage(s);
                Utility.ShowAlertDialog(LoginActivity.this, errorMessage.first, errorMessage.second, false);
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    String ans = json.getString("status");
                    if (ans.compareTo("ok") == 0) {
                        JSONArray array = json.getJSONArray("Data");
                        JSONObject jsonObject = array.getJSONObject(0);
                        //uid
                        UID = jsonObject.getString("data0");
                        LoginSharedPref.setPid(LoginActivity.this, UID);
                        if (!UID.equals("")) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } else if (ans.compareTo("false") == 0) {
                        Snackbar.make(llLogin, "Login credentials incorrect", Snackbar.LENGTH_SHORT).show();
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
