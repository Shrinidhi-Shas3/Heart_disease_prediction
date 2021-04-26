package com.heart.prediction.admin.training;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.heart.prediction.admin.R;
import com.heart.prediction.admin.models.TrainingModel;
import com.heart.prediction.admin.webservices.JSONParse;
import com.heart.prediction.admin.webservices.RestAPI;
import com.heart.prediction.admin.webservices.Utility;

import org.json.JSONObject;

public class AddTrainingActivity extends AppCompatActivity {

    private EditText edtDiseaseName, edtAge, edtChestPain, edtBloodSugar, edtRestingEcg, edtExerciseAngina, edtSlope, edtCa, edtThal, edtRestBloodPressure, edtSerumCho, edtMaxHeartRate, edtOldPeak;
    private RadioButton mMale, mFemale;
    private MaterialButton mSubmit;
    private Dialog cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traing_details);

        getSupportActionBar().setTitle(Html.fromHtml("Add Training Details"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initViews();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String gender = mMale.isChecked() ? "0" : "1";

                    //String DName,String Age,String gender,String chestpain,String bloodsugar,String restecg
                    // ,String exang,String ca
                    // ,String slope,String thal,String bloodpressure,String cholesterol,String thalach,String oldpea

                    new AddTrainingData().execute(
                            edtDiseaseName.getText().toString(), edtAge.getText().toString()
                            , gender, edtChestPain.getText().toString()
                            , edtBloodSugar.getText().toString(), edtRestingEcg.getText().toString()
                            , edtExerciseAngina.getText().toString(), edtCa.getText().toString()
                            , edtSlope.getText().toString(), edtThal.getText().toString()
                            , edtRestBloodPressure.getText().toString(), edtSerumCho.getText().toString()
                            , edtMaxHeartRate.getText().toString(), edtOldPeak.getText().toString());
                }
            }
        });
    }

    public void prepareDialog() {
        cd = new Dialog(AddTrainingActivity.this, R.style.AppTheme);
        cd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cd.setContentView(R.layout.circular_dialog);
        cd.setCancelable(false);
        cd.show();
    }

    private boolean validate() {
        if (edtDiseaseName.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter Disease Name", Snackbar.LENGTH_SHORT).show();
            edtDiseaseName.requestFocus();
            return false;
        } else if (edtAge.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter Age.", Snackbar.LENGTH_SHORT).show();
            edtAge.requestFocus();
            return false;
        } else if (Integer.parseInt(edtAge.getText().toString()) == 0) {
            Snackbar.make(mSubmit, "Enter Valid Age.", Snackbar.LENGTH_SHORT).show();
            edtAge.requestFocus();
            return false;
        } else if (edtChestPain.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter Chest Pain.", Snackbar.LENGTH_SHORT).show();
            edtChestPain.requestFocus();
            return false;
        } else if (edtBloodSugar.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter Blood Sugar.", Snackbar.LENGTH_SHORT).show();
            edtBloodSugar.requestFocus();
            return false;
        } else if (edtRestingEcg.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter Resting Electrographic.", Snackbar.LENGTH_SHORT).show();
            edtRestingEcg.requestFocus();
            return false;
        } else if (edtExerciseAngina.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter Exercise Induced Angina", Snackbar.LENGTH_SHORT).show();
            edtExerciseAngina.requestFocus();
            return false;
        } else if (edtSlope.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter Slope.", Snackbar.LENGTH_SHORT).show();
            edtSlope.requestFocus();
            return false;
        } else if (edtCa.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter "
                    + getResources().getString(R.string.number_major_vessels), Snackbar.LENGTH_SHORT).show();
            edtCa.requestFocus();
            return false;
        } else if (edtThal.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter "
                    + getResources().getString(R.string.thal), Snackbar.LENGTH_SHORT).show();
            edtThal.requestFocus();
            return false;
        } else if (edtRestBloodPressure.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter "
                    + getResources().getString(R.string.rest_blood_pressure), Snackbar.LENGTH_SHORT).show();
            edtRestBloodPressure.requestFocus();
            return false;
        } else if (edtSerumCho.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter "
                    + getResources().getString(R.string.serum_cholesterol), Snackbar.LENGTH_SHORT).show();
            edtSerumCho.requestFocus();
            return false;
        } else if (edtSerumCho.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter "
                    + getResources().getString(R.string.serum_cholesterol), Snackbar.LENGTH_SHORT).show();
            edtSerumCho.requestFocus();
            return false;
        } else if (edtMaxHeartRate.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter "
                    + getResources().getString(R.string.maximum_heart_rate), Snackbar.LENGTH_SHORT).show();
            edtMaxHeartRate.requestFocus();
            return false;
        } else if (edtOldPeak.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter "
                    + getResources().getString(R.string.exercise_old_peak), Snackbar.LENGTH_SHORT).show();
            edtOldPeak.requestFocus();
            return false;
        }


        return true;
    }

    private void initViews() {
        edtDiseaseName = findViewById(R.id.disease_name);
        edtAge = findViewById(R.id.age);
        edtChestPain = findViewById(R.id.check_pain);
        edtBloodSugar = findViewById(R.id.blood_sugar);
        edtRestingEcg = findViewById(R.id.resting_electrographic);
        edtExerciseAngina = findViewById(R.id.excercise_induced_angina);
        edtSlope = findViewById(R.id.slope);
        edtCa = findViewById(R.id.number_major_vessels);
        edtThal = findViewById(R.id.thal);
        edtRestBloodPressure = findViewById(R.id.rest_blood_pressure);
        edtSerumCho = findViewById(R.id.serum_cholesterol);
        edtMaxHeartRate = findViewById(R.id.maximum_heart_rate);
        edtOldPeak = findViewById(R.id.exercise_old_peak);

        mMale = findViewById(R.id.male);
        mFemale = findViewById(R.id.female);

        mSubmit = findViewById(R.id.submit);

    }

    private class AddTrainingData extends AsyncTask<String, JSONObject, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prepareDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            String a = "back";
            RestAPI api = new RestAPI();
            try {
                JSONObject json = api.AddTrainingData(strings[0], strings[1], strings[2], strings[3], strings[4]
                        , strings[5], strings[6], strings[7], strings[8], strings[9], strings[10], strings[11]
                        , strings[12], strings[13]);
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
                Utility.ShowAlertDialog(AddTrainingActivity.this, error.first, error.second, false);
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    String ans = json.getString("status");
                    if (ans.compareTo("true") == 0) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(AddTrainingActivity.this);
                        ad.setTitle("Training Details Updated");
                        ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });
                        ad.show();
                    } else if (ans.compareTo("true") == 0) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(AddTrainingActivity.this);
                        ad.setTitle("No Training found.");
                        ad.setMessage("No training data found with such details. Please try again");
                        ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });
                        ad.show();
                    } else {
                        String error = json.getString("Data");
                        Toast.makeText(AddTrainingActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AddTrainingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
