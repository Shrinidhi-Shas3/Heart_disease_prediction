package com.heart.prediction.user.ui.profile;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.heart.prediction.user.R;
import com.heart.prediction.user.models.UserModel;
import com.heart.prediction.user.utils.LoginSharedPref;
import com.heart.prediction.user.webservices.JSONParse;
import com.heart.prediction.user.webservices.RestAPI;
import com.heart.prediction.user.webservices.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    private Dialog dialogLoader;
    private EditText sname, sconact, semail, sage, saddr1;
    private RadioButton maleBtn, femaleBtn;
    private MaterialButton sUpdate;
    private UserModel mUserProfile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        new GetProfile().execute(LoginSharedPref.getUserId(getActivity()));
    }

    private void initViews(View view) {
        sname = (EditText) view.findViewById(R.id.sRegName);
        maleBtn = (RadioButton) view.findViewById(R.id.male);
        femaleBtn = (RadioButton) view.findViewById(R.id.female);
        sconact = (EditText) view.findViewById(R.id.sRegContact);
        semail = (EditText) view.findViewById(R.id.sRegEmail);
        sage = (EditText) view.findViewById(R.id.sRegDob);
        saddr1 = (EditText) view.findViewById(R.id.sRegAddr1);

        sUpdate = (MaterialButton) view.findViewById(R.id.sRegisterButton);

        sUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndUpdate();
            }
        });
    }

    private void validateAndUpdate() {
        if (sname.getText().toString().length() == 0) {
            Snackbar.make(sUpdate, "Enter Name", Snackbar.LENGTH_SHORT).show();
            sname.requestFocus();
        } else if (!maleBtn.isChecked() && !femaleBtn.isChecked()) {
            Snackbar.make(sUpdate, "Please Choose Gender", Snackbar.LENGTH_SHORT).show();
        } else if (semail.getText().toString().length() == 0) {
            Snackbar.make(sUpdate, "Enter Email", Snackbar.LENGTH_SHORT).show();
            semail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(semail.getText().toString()).matches()) {
            Snackbar.make(sUpdate, "Enter Valid Email", Snackbar.LENGTH_SHORT).show();
            semail.requestFocus();
        } else if (sconact.getText().toString().length() == 0) {
            Snackbar.make(sUpdate, "Enter Contact No", Snackbar.LENGTH_SHORT).show();
            sconact.requestFocus();
        } else if (sage.getText().length() == 0) {
            Snackbar.make(sUpdate, "Enter your age", Snackbar.LENGTH_SHORT).show();
            sconact.requestFocus();
        } else if (Integer.parseInt(sage.getText().toString()) == 0) {
            Snackbar.make(sUpdate, "Enter your valid Age", Snackbar.LENGTH_SHORT).show();
            sconact.requestFocus();
        } else if (saddr1.getText().length() == 0) {
            Snackbar.make(sUpdate, "Enter Address", Snackbar.LENGTH_SHORT).show();
            saddr1.requestFocus();
        }  else {
            closeKeyboard();
            String gender = maleBtn.isChecked() ? "Male" : "Female";

//            string pic, string dName, string gender, string dob, string address, string city, string state, string cont, string email, string pass
            //String id,String name,String gender,String age,String address,String city,String state,String cont,String email
            new UpdateProfile().execute(
                    LoginSharedPref.getUserId(getActivity())
                    , sname.getText().toString()
                    , gender
                    , sage.getText().toString()
                    , saddr1.getText().toString()
                    ,""
                    ,""
                    , sconact.getText().toString()
                    , semail.getText().toString());

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

    private void closeKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void startAnimation() {
        dialogLoader = new Dialog(getActivity(), R.style.AppTheme_NoActionBar);
        dialogLoader.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8D000000")));
        final View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_loader, null);
        LottieAnimationView animationView = view.findViewById(R.id.loader);
        animationView.playAnimation();
        dialogLoader.setContentView(view);
        dialogLoader.setCancelable(false);
        dialogLoader.show();
    }

    private void stopAnimation() {
        if (dialogLoader.isShowing())
            dialogLoader.dismiss();
    }

    private class GetProfile extends AsyncTask<String, Void, String> {
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
                JSONObject jsonObject = restAPI.PgetProfile(strings[0]);
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
                Utility.ShowAlertDialog(getActivity(), errorMessage.first, errorMessage.second, false);
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    String ans = json.getString("status");
                    if (ans.compareTo("ok") == 0) {
                        JSONArray array = json.getJSONArray("Data");
                        JSONObject jsonObject = array.getJSONObject(0);
                        mUserProfile = new UserModel(jsonObject.getString("data0")
                                , jsonObject.getString("data1")
                                , jsonObject.getString("data2")
                                , jsonObject.getString("data3")
                                , jsonObject.getString("data4")
                                , jsonObject.getString("data5")
                                , jsonObject.getString("data6"));
                        setValues();
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class UpdateProfile extends AsyncTask<String, JSONObject, String> {
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
                JSONObject jsonObject = restAPI.PUpdateProfile(strings[0], strings[1], strings[2], strings[3],
                        strings[4], strings[5], strings[6], strings[7], strings[8]);
                JSONParse jsonparse = new JSONParse();
                answer = jsonparse.JSONParse(jsonObject);
            } catch (Exception e) {
                answer = e.getMessage();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.wtf("reply", "onPostExecute: "+s);
            stopAnimation();
            String exp = "";
            if (Utility.checkConnection(s)) {
                Pair<String, String> errorMessage = Utility.GetErrorMessage(s);
                Utility.ShowAlertDialog(getActivity(), errorMessage.first, errorMessage.second, false);
            } else {
                try {
                    JSONObject object = new JSONObject(s);
                    exp = object.getString("status");
                    if (exp.compareTo("already") == 0) {
                        Snackbar.make(sUpdate, "User already exists, Try using different Email", Snackbar.LENGTH_SHORT).show();
                    } else if (exp.compareTo("true") == 0) {

                        Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();


                    } else if (exp.compareTo("error") == 0) {
                        String error = object.getString("Data");
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void setValues() {
        sname.setText(mUserProfile.getName());
        semail.setText(mUserProfile.getEmail());
        sconact.setText(mUserProfile.getMobile());
        saddr1.setText(mUserProfile.getAddress());
        sage.setText(mUserProfile.getAge());
        if (mUserProfile.getGender().equalsIgnoreCase("Male")) {
            maleBtn.setChecked(true);
        } else {
            femaleBtn.setChecked(false);
        }
    }


}