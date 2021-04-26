package com.heart.prediction.user.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.heart.prediction.user.R;
import com.heart.prediction.user.utils.LoginSharedPref;
import com.heart.prediction.user.webservices.JSONParse;
import com.heart.prediction.user.webservices.RestAPI;
import com.heart.prediction.user.webservices.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePassword extends Dialog {
    private Context appContext;
    private EditText oldPassword, newPassword;
    private TextView updatePassword;

    private String pid = "";
    private Dialog mDialog;

    public UpdatePassword(@NonNull Context context) {
        super(context);
        appContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_password_layout);
        pid = LoginSharedPref.getUserId(appContext);

        oldPassword = (EditText) findViewById(R.id.pOldPassword);
        newPassword = (EditText) findViewById(R.id.pNewPassword);

        updatePassword = (TextView) findViewById(R.id.txtUpdatePassword);

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldPassword.getText().toString().length() == 0){
                    Snackbar.make(updatePassword, "Enter Old Password", Snackbar.LENGTH_SHORT).show();
                    oldPassword.requestFocus();
                } else if (newPassword.getText().toString().length() == 0){
                    Snackbar.make(updatePassword, "Enter Old Password", Snackbar.LENGTH_SHORT).show();
                    newPassword.requestFocus();
                } else {
                    new UpdatePass().execute(pid, oldPassword.getText().toString(), newPassword.getText().toString());
                }
            }
        });
    }

    private void startAnimation() {
        mDialog = new Dialog(appContext, R.style.AppTheme_NoActionBar);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8D000000")));
        final View view = getLayoutInflater().inflate(R.layout.custom_dialog_loader, null);
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

    public class UpdatePass extends AsyncTask<String, JSONObject, String> {
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
                JSONObject jsonObject = restAPI.PChangePass(strings[0],strings[1],strings[2]);
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
            String exp = "";
            if (Utility.checkConnection(s)) {
                Pair<String, String> errorMessage = Utility.GetErrorMessage(s);
                Utility.ShowAlertDialog(appContext, errorMessage.first, errorMessage.second, false);
            }else {
                try {
                    JSONObject object = new JSONObject(s);
                    exp = object.getString("status");
                    if (exp.compareTo("true") == 0) {
                        Snackbar.make(updatePassword, "Password Updated Successfully", Snackbar.LENGTH_SHORT).show();

                        //LoginSharedPref.setPid(appContext, "");

                        if(isShowing())
                            dismiss();

                    } else if (exp.compareTo("false") == 0) {
                        Snackbar.make(updatePassword, "You have entered an in-correct old password.", Snackbar.LENGTH_SHORT).show();
                    } else {
                        String error = object.getString("Data");
                        Toast.makeText(appContext, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(appContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(s);
            }
        }
    }
}


