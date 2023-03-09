package com.grocerycustomer.activity;

import static com.grocerycustomer.utils.SessionManager.area;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.grocerycustomer.R;
import com.grocerycustomer.model.Area;
import com.grocerycustomer.model.AreaD;
import com.grocerycustomer.retrofit.APIClient;
import com.grocerycustomer.retrofit.GetResult;
import com.grocerycustomer.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class SelectAreaActivity extends BaseActivity implements GetResult.MyListener {
    List<AreaD> areaDS = new ArrayList<>();
    @BindView(R.id.spinner)
    Spinner spinner;
    String areaSelect;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(SelectAreaActivity.this);
        getArea();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaSelect = areaDS.get(position).getName();
                sessionManager.setStringData(area, areaSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getArea() {
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        Call<JsonObject> call = APIClient.getInterface().getArea((JsonObject) jsonParser.parse(jsonObject.toString()));
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Area area = gson.fromJson(result.toString(), Area.class);
                areaDS = area.getData();
                List<String> arrayList = new ArrayList<>();
                for (int i = 0; i < areaDS.size(); i++) {
                    if (areaDS.get(i).getStatus().equalsIgnoreCase("1")) {
                        arrayList.add(areaDS.get(i).getName());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        } catch (Exception e) {
e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_next)
    public void onClick() {
        startActivity(new Intent(SelectAreaActivity.this, LoginActivity.class));
    }
}
