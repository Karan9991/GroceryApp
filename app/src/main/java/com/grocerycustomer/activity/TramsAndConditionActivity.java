package com.grocerycustomer.activity;

import static com.grocerycustomer.utils.SessionManager.tremcodition;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.grocerycustomer.R;
import com.grocerycustomer.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TramsAndConditionActivity extends BaseActivity {
    @BindView(R.id.txt_privacy)
    TextView txtPrivacy;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trams_and_condition);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtPrivacy.setText(Html.fromHtml(sessionManager.getStringData(tremcodition), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtPrivacy.setText(Html.fromHtml(sessionManager.getStringData(tremcodition)));
        }
    }
}
