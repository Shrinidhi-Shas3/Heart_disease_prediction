package com.heart.prediction.admin.user;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.heart.prediction.admin.R;
import com.heart.prediction.admin.models.UserModel;

public class UserDetails extends AppCompatActivity {
    private EditText sname, sconact, semail, sage, saddr1;
    private RadioButton maleBtn, femaleBtn;
    private UserModel mUserProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gallery);
        setActionbar("User Details");
        initViews();

        Bundle bundle = getIntent().getBundleExtra("BUNDLE");
        if (bundle != null) {
            mUserProfile = bundle.getParcelable("DATA");
            setValues();
        }
    }

    public void setActionbar(String title) {
        getSupportActionBar().setTitle(Html.fromHtml(title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void initViews() {
        sname = (EditText) findViewById(R.id.sRegName);
        maleBtn = (RadioButton) findViewById(R.id.male);
        femaleBtn = (RadioButton) findViewById(R.id.female);
        sconact = (EditText) findViewById(R.id.sRegContact);
        semail = (EditText) findViewById(R.id.sRegEmail);
        sage = (EditText) findViewById(R.id.sRegDob);
        saddr1 = (EditText) findViewById(R.id.sRegAddr1);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}