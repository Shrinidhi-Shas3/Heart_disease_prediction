package com.heart.prediction.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.heart.prediction.user.models.TrainingModel;
import com.heart.prediction.user.utils.LoginSharedPref;
import com.heart.prediction.user.webservices.JSONParse;
import com.heart.prediction.user.webservices.MySingleton;
import com.heart.prediction.user.webservices.RestAPI;
import com.heart.prediction.user.webservices.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import static com.heart.prediction.user.R.id.symptomFType;

public class AnalysisFragment extends Fragment {

    private EditText edtAge, edtChestPain, edtBloodSugar, edtRestingEcg, edtExerciseAngina, edtSlope, edtCa, edtThal, edtRestBloodPressure, edtSerumCho, edtMaxHeartRate, edtOldPeak;
    private MaterialButton mSubmit, mStartAgain;
    private RadioButton maleBtn,femaleBtn;
    private ScrollView mScrollView;
    private LinearLayout mDiseaseView;
    private TextView mPredictedDisease;
    private ProgressDialog cd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_traing_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initViews(view);

        mStartAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearViews(edtChestPain, edtBloodSugar, edtRestingEcg, edtExerciseAngina, edtSlope, edtCa, edtThal, edtRestBloodPressure, edtSerumCho, edtMaxHeartRate, edtOldPeak);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    //String DName,String Age,String gender,String chestpain,String bloodsugar,String restecg
                    // ,String exang,String ca
                    // ,String slope,String thal,String bloodpressure,String cholesterol,String thalach,String oldpea

                    String gender = maleBtn.isChecked() ? "0" : "1";
                    analyseTask(edtAge.getText().toString()
                            , gender
                            , edtChestPain.getText().toString()
                            , edtBloodSugar.getText().toString(), edtRestingEcg.getText().toString()
                            , edtExerciseAngina.getText().toString(), edtSlope.getText().toString()
                            , edtCa.getText().toString(), edtThal.getText().toString()
                            , edtRestBloodPressure.getText().toString(), edtSerumCho.getText().toString()
                            , edtMaxHeartRate.getText().toString(), edtOldPeak.getText().toString());
                }
            }
        });
    }

    private void clearViews(EditText... editTexts) {

        for (EditText editText : editTexts) {
            editText.setText("");
        }

        if (mScrollView.getVisibility() == View.GONE) {
            mScrollView.setVisibility(View.VISIBLE);
        }

        if (mDiseaseView.getVisibility() == View.VISIBLE) {
            mDiseaseView.setVisibility(View.GONE);
        }

        editTexts[0].requestFocus();


    }

    public void prepareDialog() {
        cd = new ProgressDialog(getActivity());
        cd.setMessage("Please wait...");
        cd.show();
    }

    private boolean validate() {
        if (edtAge.getText().length() == 0) {
            Snackbar.make(mSubmit, "Enter Age.", Snackbar.LENGTH_SHORT).show();
            edtAge.requestFocus();
            return false;
        }else if (edtChestPain.getText().length() == 0) {
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

    private void initViews(View view) {
        edtAge = view.findViewById(R.id.check_age);
        maleBtn = (RadioButton) view.findViewById(R.id.male);
        femaleBtn = (RadioButton) view.findViewById(R.id.female);
        edtChestPain = view.findViewById(R.id.check_pain);
        edtBloodSugar = view.findViewById(R.id.blood_sugar);
        edtRestingEcg = view.findViewById(R.id.resting_electrographic);
        edtExerciseAngina = view.findViewById(R.id.excercise_induced_angina);
        edtSlope = view.findViewById(R.id.slope);
        edtCa = view.findViewById(R.id.number_major_vessels);
        edtThal = view.findViewById(R.id.thal);
        edtRestBloodPressure = view.findViewById(R.id.rest_blood_pressure);
        edtSerumCho = view.findViewById(R.id.serum_cholesterol);
        edtMaxHeartRate = view.findViewById(R.id.maximum_heart_rate);
        edtOldPeak = view.findViewById(R.id.exercise_old_peak);

        mScrollView = view.findViewById(R.id.analysis_view);
        mDiseaseView = view.findViewById(R.id.prediction_layout);
        mPredictedDisease = view.findViewById(symptomFType);

        mSubmit = view.findViewById(R.id.submit);
        mStartAgain = view.findViewById(R.id.start_again);


    }

    private void analyseTask(String...strings){
        prepareDialog();
        String urlheartanalyze = getAnalyseURL(strings);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlheartanalyze
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cd.cancel();
                Log.d("RecommendDialog", "onResponse: " + response.toString());
                try {
                    if (response.getString("status").equalsIgnoreCase("no")) {
                        if (mScrollView.getVisibility() == View.GONE) {
                            mScrollView.setVisibility(View.VISIBLE);
                        }

                        if (mDiseaseView.getVisibility() == View.VISIBLE) {
                            mDiseaseView.setVisibility(View.GONE);
                        }
                        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                        ad.setTitle("Analysis Result");
                        ad.setMessage("No Disease found upon analysing.");
                        ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        ad.show();
                    } else {
                        setValues(response.getString("status"));
                    }
                } catch (Exception e) {
                    if (mScrollView.getVisibility() == View.GONE) {
                        mScrollView.setVisibility(View.VISIBLE);
                    }

                    if (mDiseaseView.getVisibility() == View.VISIBLE) {
                        mDiseaseView.setVisibility(View.GONE);
                    }

                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cd.cancel();
                if (mScrollView.getVisibility() == View.GONE) {
                    mScrollView.setVisibility(View.VISIBLE);
                }

                if (mDiseaseView.getVisibility() == View.VISIBLE) {
                    mDiseaseView.setVisibility(View.GONE);
                }

                Toast.makeText(getActivity(), "Something is wrong, please try again.", Toast.LENGTH_SHORT).show();

            }
        });

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private String getAnalyseURL(String... strings) {
        String analyseURL = "http://aheartrandomalgo.hostoise.com/Default.aspx?" +
                "age=%s&gender=%s&chestpain=%s" +
                "&bloodsugar=%s&restecg=%s&exang=%s&slope=%s&ca=%s&thal=%s&bloodpressure=%s" +
                "&cholestrol=%s&thalach=%s&oldpeak=%s";
        //edtAge.getText().toString()
        //                            , gender
        //                            , edtChestPain.getText().toString()
        //                            , edtBloodSugar.getText().toString(), edtRestingEcg.getText().toString()
        //                            , edtExerciseAngina.getText().toString(), edtSlope.getText().toString()
        //                            , edtCa.getText().toString(), edtThal.getText().toString()
        //                            , edtRestBloodPressure.getText().toString(), edtSerumCho.getText().toString()
        //                            , edtMaxHeartRate.getText().toString(), edtOldPeak.getText().toString()
        Log.d("TAG", String.format(analyseURL
                , strings[0], strings[1], strings[2], strings[3], strings[4]
                , strings[5], strings[6], strings[7], strings[8], strings[9], strings[10], strings[11], strings[12])) ;

        return String.format(analyseURL
                , strings[0], strings[1], strings[2], strings[3], strings[4]
                , strings[5], strings[6], strings[7], strings[8], strings[9], strings[10], strings[11], strings[12]);
    }

    private void setValues(String model) {
        if (mScrollView.getVisibility() == View.VISIBLE) {
            mScrollView.setVisibility(View.GONE);
        }

        if (mDiseaseView.getVisibility() == View.GONE) {
            mDiseaseView.setVisibility(View.VISIBLE);
        }

        mPredictedDisease.setText(model);

    }
}
