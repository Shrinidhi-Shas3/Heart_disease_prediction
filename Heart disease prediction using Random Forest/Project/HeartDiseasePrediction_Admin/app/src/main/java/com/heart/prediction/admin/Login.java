package com.heart.prediction.admin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.heart.prediction.admin.webservices.JSONParse;
import com.heart.prediction.admin.webservices.RestAPI;
import com.heart.prediction.admin.webservices.Utility;

import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private Dialog cd;
    private EditText Email, Pass;
    private Button Loginbtn;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String uid = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("oda", Context.MODE_PRIVATE);
        uid = sp.getString("uid", "");

        if (uid.length() > 0) {
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            setContentView(R.layout.login);
            getSupportActionBar().setTitle("Login");
//            getSupportActionBar().hide();
            Email = (EditText) findViewById(R.id.email);
            Pass = (EditText) findViewById(R.id.pass);
            Loginbtn = (Button) findViewById(R.id.login);

            Loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Email.length() > 0) {
                        if (Pass.length() > 0) {
                            new LoginTask().execute(Email.getText().toString(), Pass.getText().toString());
                        } else {
                            Snackbar.make(view, "Enter Password", Snackbar.LENGTH_SHORT).show();
                            Pass.requestFocus();
                        }
                    } else {
                        Snackbar.make(view, "Enter User Name", Snackbar.LENGTH_SHORT).show();
                        Email.requestFocus();
                    }
                }
            });

        }
    }

    public void prepareDialog() {
        cd = new Dialog(Login.this, R.style.AppTheme);
        cd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.setContentView(R.layout.circular_dialog);
        cd.setCancelable(false);
        cd.show();
    }

    private class LoginTask extends AsyncTask<String, JSONObject, String> {

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
                JSONObject json = api.ALogin(params[0], params[1]);
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
                Utility.ShowAlertDialog(Login.this, error.first, error.second, false);
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    String ans = json.getString("status");
                    if (ans.compareTo("true") == 0) {
                        editor = sp.edit();
                        editor.putString("uid", "admin");
                        editor.commit();
                        editor.apply();

                        Intent i = new Intent(Login.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else if (ans.compareTo("false") == 0) {
                        Email.setText("");
                        Pass.setText("");
                        AlertDialog.Builder ad = new AlertDialog.Builder(Login.this);
                        ad.setTitle("Invalid User!");
                        ad.setMessage("Wrong Credentials, check the entered username and Password!");
                        ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        ad.show();
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(Login.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
