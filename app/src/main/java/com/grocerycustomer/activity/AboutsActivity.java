package com.grocerycustomer.activity;

import static com.grocerycustomer.utils.SessionManager.aboutUs;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.grocerycustomer.R;
import com.grocerycustomer.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutsActivity extends BaseActivity {

    @BindView(R.id.txt_about)
    TextView txtAbout;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abouts);
        ButterKnife.bind(this);
        sessionManager=new SessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtAbout.setText(Html.fromHtml(sessionManager.getStringData(aboutUs), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtAbout.setText(Html.fromHtml(sessionManager.getStringData(aboutUs)));
        }
    }
}
