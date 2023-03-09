package com.grocerycustomer.activity;

import static com.grocerycustomer.fragment.OrderSumrryFragment.paymentsucsses;
import static com.grocerycustomer.utils.SessionManager.currncy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.grocerycustomer.R;
import com.grocerycustomer.model.Payment;
import com.grocerycustomer.model.PaymentItem;
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

public class WalletActivity extends BaseActivity implements GetResult.MyListener {
    @BindView(R.id.txt_wallet)
    TextView txtWallet;
    @BindView(R.id.ed_amount)
    EditText edAmount;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.txt_pay)
    TextView txtPay;
    String payment = "";
    int postionss;
    CustPrograssbar custPrograssbar;
    List<PaymentItem> paymentList;
    User user;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(WalletActivity.this);
        user = sessionManager.getUserDetails();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        custPrograssbar = new CustPrograssbar();
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(R.string.wallet);
        txtWallet.setText(getIntent().getStringExtra("wallat"));


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payment = spinner.getItemAtPosition(position).toString();
                postionss = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getPayment();
    }

    private void getPayment() {
        custPrograssbar.prograssCreate(WalletActivity.this);
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        Call<JsonObject> call = APIClient.getInterface().getpaymentgateway((JsonObject) jsonParser.parse(jsonObject.toString()));
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");
    }

    private void sendPyment() {
        try {
            custPrograssbar.prograssCreate(WalletActivity.this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", user.getId());
            jsonObject.put("wallet", edAmount.getText().toString());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().walletUpdate((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.txt_pay})
    public void onClick(View view) {
        if (view.getId() == R.id.txt_pay) {

            if (!TextUtils.isEmpty(edAmount.getText().toString())) {
                if (payment.equalsIgnoreCase("Razorpay")) {
                    int temtoal = Integer.parseInt(edAmount.getText().toString());
                    startActivity(new Intent(WalletActivity.this, RazerpayActivity.class).putExtra("amount", temtoal).putExtra("detail", paymentList.get(postionss)));
                } else if (payment.equalsIgnoreCase("Paypal")) {
                    Double totle = Double.parseDouble(edAmount.getText().toString());
                    startActivity(new Intent(WalletActivity.this, PaypalActivity.class).putExtra("amount", totle).putExtra("detail", paymentList.get(postionss)));
                }
            } else {
                edAmount.setError("Enter Amount");
            }
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {

                Gson gson = new Gson();
                Payment payment = gson.fromJson(result.toString(), Payment.class);
                List<String> arealist = new ArrayList<>();
                for (int i = 0; i < payment.getData().size(); i++) {
                    if (payment.getData().get(i).getwShow().equalsIgnoreCase("1")) {
                        arealist.add(payment.getData().get(i).getTitle());
                    }
                }
                paymentList = payment.getData();
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinnercode_layout, arealist);
                dataAdapter.setDropDownViewResource(R.layout.spinnercode_layout);
                spinner.setAdapter(dataAdapter);

            }
            if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(WalletActivity.this, restResponse.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    HomeActivity.getInstance().txtWallet.setText(sessionManager.getStringData(currncy) + restResponse.getWallet());
                    txtWallet.setText(sessionManager.getStringData(currncy) + restResponse.getWallet());
                    edAmount.setText("");
                }
            }
        } catch (Exception e) {
            Log.e("Errror", "" + e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paymentsucsses == 1) {
            paymentsucsses = 0;
            sendPyment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item_more) {
            startActivity(new Intent(this, WallateHistryActivity.class));
        } else if (itemId == android.R.id.home) {
            finish();
        }
        return true;
    }
}