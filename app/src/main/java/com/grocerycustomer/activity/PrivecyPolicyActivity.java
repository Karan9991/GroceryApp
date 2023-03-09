package com.grocerycustomer.activity;

import static com.grocerycustomer.utils.SessionManager.privacy;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.grocerycustomer.R;
import com.grocerycustomer.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivecyPolicyActivity extends BaseActivity {

    @BindView(R.id.txt_privacy)
    TextView txtPrivacy;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privecypolicy);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtPrivacy.setText(Html.fromHtml(sessionManager.getStringData(privacy), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtPrivacy.setText(Html.fromHtml(sessionManager.getStringData(privacy)));
        }
    }
}
