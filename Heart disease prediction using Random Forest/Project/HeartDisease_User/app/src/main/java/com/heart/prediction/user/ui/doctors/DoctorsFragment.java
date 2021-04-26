
package com.heart.prediction.user.ui.doctors;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.heart.prediction.user.R;
import com.heart.prediction.user.models.DoctorModel;
import com.heart.prediction.user.webservices.JSONParse;
import com.heart.prediction.user.webservices.RestAPI;
import com.heart.prediction.user.webservices.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DoctorsFragment extends Fragment {

    private View dView;
    private ArrayList<DoctorModel> mDoctors, mCopyOfDoctors;
    private String[] cate = new String[]{"Search By", "Name", "Specialization"};
    private Spinner docType;
    private Dialog cd;
    private ListView list;
    private String filterText = "";
    private Adapter adapt;
    private androidx.appcompat.widget.SearchView mSearchView;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dView = inflater.inflate(R.layout.doctor, container, false);

        docType = (Spinner) dView.findViewById(R.id.category);
        list = (ListView) dView.findViewById(R.id.list);
        mSearchView = (androidx.appcompat.widget.SearchView) dView.findViewById(R.id.searchView);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, cate);
        cityAdapter.setDropDownViewResource(R.layout.spinner_item);
        docType.setAdapter(cityAdapter);

        new GetDoctors().execute();

        docType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    filterText = "NA";
                    mSearchView.setQuery("", false);
                } else if (i == 1) {
                    filterText = "Name";
                    mSearchView.setQuery("", false);
                } else {
                    filterText = "Special";
                    mSearchView.setQuery("", false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSearchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    if (docType.getSelectedItemPosition() == 0) {
                        Toast.makeText(getActivity(), "Please choose what you want to search.", Toast.LENGTH_SHORT).show();
                        docType.performClick();
                    } else {
                        new SearchDoctors().execute(filterText, query);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return dView;
    }

    private void startAnimation() {
        cd = new Dialog(getActivity(), R.style.AppTheme_NoActionBar);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8D000000")));
        final View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_loader, null);
        LottieAnimationView animationView = view.findViewById(R.id.loader);
        animationView.playAnimation();
        cd.setContentView(view);
        cd.setCancelable(false);
        cd.show();
    }

    private void stopAnimation() {
        if (cd.isShowing())
            cd.dismiss();
    }

    private class GetDoctors extends AsyncTask<String, JSONObject, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnimation();
        }

        @Override
        protected String doInBackground(String... params) {
            String a = "back";
            RestAPI api = new RestAPI();
            try {
                JSONObject json = api.getDoctors();
                JSONParse jp = new JSONParse();
                a = jp.JSONParse(json);
            } catch (Exception e) {
                a = e.getMessage();
            }
            return a;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            stopAnimation();
            mSearchView.clearFocus();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            Log.wtf("reply", "onPostExecute: " + s);
            if (Utility.checkConnection(s)) {
                Pair<String, String> errorMessage = Utility.GetErrorMessage(s);
                Utility.ShowAlertDialog(getActivity(), errorMessage.first, errorMessage.second, false);
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    String ans = json.getString("status");
//                    Log.d("ANS", s);
                    if (ans.compareTo("ok") == 0) {
                        mDoctors = new ArrayList<DoctorModel>();
                        mCopyOfDoctors = new ArrayList<DoctorModel>();

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
                        }

                        setListValues();

                    } else if (ans.compareTo("no") == 0) {
                        Utility.ShowAlertDialog(getActivity(), "No Doctors Found!", "Could not find any doctors.", false);
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setListValues() {

        mCopyOfDoctors.addAll(mDoctors);

        adapt = new Adapter(getActivity(), mCopyOfDoctors);
        list.setAdapter(adapt);
    }

    private class SearchDoctors extends AsyncTask<String, JSONObject, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnimation();
        }

        @Override
        protected String doInBackground(String... params) {
            String a = "back";
            RestAPI api = new RestAPI();
            try {
                JSONObject json = api.searchDoctor(params[0], params[1]);
                JSONParse jp = new JSONParse();
                a = jp.JSONParse(json);
            } catch (Exception e) {
                a = e.getMessage();
            }
            return a;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            stopAnimation();
            Log.wtf("reply", "onPostExecute: " + s);
            if (Utility.checkConnection(s)) {
                Pair<String, String> errorMessage = Utility.GetErrorMessage(s);
                Utility.ShowAlertDialog(getActivity(), errorMessage.first, errorMessage.second, false);
            } else {
                try {
                    JSONObject json = new JSONObject(s);
                    String ans = json.getString("status");
//                    Log.d("ANS", s);
                    if (ans.compareTo("ok") == 0) {
                        mDoctors = new ArrayList<DoctorModel>();
                        mCopyOfDoctors = new ArrayList<DoctorModel>();

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
                        }

                        setSearchValues();

                    } else if (ans.compareTo("no") == 0) {
                        Utility.ShowAlertDialog(getActivity(), "No Doctors Found!", "Could not find any doctors.", false);
                    } else if (ans.compareTo("error") == 0) {
                        String error = json.getString("Data");
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setSearchValues() {

        mSearchView.clearFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mCopyOfDoctors.addAll(mDoctors);

        adapt = new Adapter(getActivity(), mCopyOfDoctors);
        list.setAdapter(adapt);
    }

    private class Adapter extends ArrayAdapter<DoctorModel> {
        Context con;

        public Adapter(@NonNull Context context, ArrayList<DoctorModel> a) {
            super(context, R.layout.doctor_listitem, a);
            con = context;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            DoctorViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new DoctorViewHolder();
                convertView = LayoutInflater.from(con).inflate(R.layout.doctor_listitem, null, true);
                viewHolder.txtname = (TextView) convertView.findViewById(R.id.name);
                viewHolder.txttype = (TextView) convertView.findViewById(R.id.type);
                viewHolder.txtcity = (TextView) convertView.findViewById(R.id.city);
                viewHolder.tableLayout = (TableLayout) convertView.findViewById(R.id.table_layout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (DoctorViewHolder) convertView.getTag();
            }

            viewHolder.txtname.setText(getItem(position).getName());
            viewHolder.txttype.setText("Type: " + getItem(position).getSpecialization());
            viewHolder.txtcity.setText("Address: " + getItem(position).getAddress());

            viewHolder.tableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Bundle bundle = new Bundle();
//                    bundle.putStringArrayList("Contents", contents);

//                Intent docDetails = new Intent(getActivity(), DoctorDetails.class);
//                docDetails.putExtra("Contents", contents);
//                startActivity(docDetails);
                }
            });

            return convertView;
        }

        private class DoctorViewHolder {
            public TextView txtname;
            public TextView txttype;
            public TextView txtcity;
            public TableLayout tableLayout;
        }
    }

}

