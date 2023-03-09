package com.grocerycustomer.activity;

import static com.grocerycustomer.utils.Utiles.isvarification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.grocerycustomer.R;
import com.grocerycustomer.model.CCode;
import com.grocerycustomer.model.Contry;
import com.grocerycustomer.model.RestResponse;
import com.grocerycustomer.model.User;
import com.grocerycustomer.retrofit.APIClient;
import com.grocerycustomer.retrofit.GetResult;
import com.grocerycustomer.utils.CustPrograssbar;
import com.grocerycustomer.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class SingActivity extends BaseActivity implements GetResult.MyListener {
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_alternatmob)
    EditText edAlternatmob;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.ed_refercode)
    EditText edRefercode;
    @BindView(R.id.spinner)
    Spinner spinner;
    List<CCode> cCodes = new ArrayList<>();
    String codeSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(SingActivity.this);
        custPrograssbar = new CustPrograssbar();
        getCode();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codeSelect = cCodes.get(position).getCcode();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCode() {
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        Call<JsonObject> call = APIClient.getInterface().getCode((JsonObject) jsonParser.parse(jsonObject.toString()));
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    private void isRegister() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", edAlternatmob.getText().toString());
            jsonObject.put("ccode", codeSelect);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getForgot((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.prograssCreate(SingActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("1")) {
                custPrograssbar.closePrograssBar();
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equals("true")) {
                    Toast.makeText(SingActivity.this, getString(R.string.mobile_numberalreadyregistered), Toast.LENGTH_LONG).show();
                } else {
                    User user = new User();
                    user.setEmail(edEmail.getText().toString());
                    user.setMobile(edAlternatmob.getText().toString());
                    user.setName(edUsername.getText().toString());
                    user.setPassword(edPassword.getText().toString());
                    user.setCcode(codeSelect);
                    user.setRcode(edRefercode.getText().toString());
                    sessionManager.setUserDetails(user);
                    isvarification =1;
                    startActivity(new Intent(SingActivity.this, VerifyPhoneActivity.class).putExtra("code", codeSelect).putExtra("phone", edAlternatmob.getText().toString()));

                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Contry contry = gson.fromJson(result.toString(), Contry.class);
                cCodes = contry.getData();
                List<String> arealist = new ArrayList<>();
                for (int i = 0; i < cCodes.size(); i++) {
                    if (cCodes.get(i).getStatus().equalsIgnoreCase("1")) {
                        arealist.add(cCodes.get(i).getCcode());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinnercode_layout, arealist);
                dataAdapter.setDropDownViewResource(R.layout.spinnercode_layout);
                spinner.setAdapter(dataAdapter);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClick({R.id.btn_sign, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign:
                if (validation()) {
                    isRegister();
                }
                break;
            case R.id.btn_login:
                startActivity(new Intent(SingActivity.this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
    public boolean validation() {
        if (edUsername.getText().toString().isEmpty()) {
            edUsername.setError(getString(R.string.ename));
            return false;
        }
        if (edEmail.getText().toString().isEmpty()) {
            edEmail.setError(getString(R.string.evalisemail));
            return false;
        }
        if (edAlternatmob.getText().toString().isEmpty()) {
            edAlternatmob.setError(getString(R.string.evalidmobile));
            return false;
        }
        if (edPassword.getText().toString().isEmpty()) {
            edPassword.setError(getString(R.string.epassword));
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SingActivity.this, LoginActivity.class));
        finish();
    }
}
