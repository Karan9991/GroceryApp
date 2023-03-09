package com.grocerycustomer.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grocerycustomer.R;
import com.grocerycustomer.model.HistryItem;
import com.grocerycustomer.model.User;
import com.grocerycustomer.model.WalletHistry;
import com.grocerycustomer.retrofit.APIClient;
import com.grocerycustomer.retrofit.GetResult;
import com.grocerycustomer.utils.CustPrograssbar;
import com.grocerycustomer.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class WallateHistryActivity extends BaseActivity implements GetResult.MyListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wallate_histry);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(R.string.wallethistory);
        sessionManager = new SessionManager(WallateHistryActivity.this);
        user = sessionManager.getUserDetails();
        custPrograssbar = new CustPrograssbar();

        recyclerView.setLayoutManager(new GridLayoutManager(WallateHistryActivity.this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getHistry();

    }

    private void getHistry() {
        try {
            custPrograssbar.prograssCreate(WallateHistryActivity.this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", user.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getHistry((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                WalletHistry walletHistry = gson.fromJson(result.toString(), WalletHistry.class);
                if (walletHistry.getResult().equalsIgnoreCase("true")) {
                    HistryAdp histryAdp = new HistryAdp(walletHistry.getData());
                    recyclerView.setAdapter(histryAdp);
                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }
    }


    public class HistryAdp extends RecyclerView.Adapter<HistryAdp.MyViewHolder> {
        private List<HistryItem> list;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txtNo;
            public TextView txtMessage;
            public TextView txtStatus;
            public TextView txtAmount;

            public MyViewHolder(View view) {
                super(view);
                txtNo = (TextView) view.findViewById(R.id.txt_no);
                txtMessage = (TextView) view.findViewById(R.id.txt_message);
                txtStatus = (TextView) view.findViewById(R.id.txt_status);
                txtAmount = (TextView) view.findViewById(R.id.txt_amount);
            }
        }

        public HistryAdp(List<HistryItem> list) {
            this.list = list;

        }

        @Override
        public HistryAdp.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_histry, parent, false);
            return new HistryAdp.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HistryAdp.MyViewHolder holder, int position) {

            HistryItem category = list.get(position);
            holder.txtNo.setText(category.getId());
            holder.txtMessage.setText(category.getMessage());
            holder.txtStatus.setText(category.getStatus());
            holder.txtAmount.setText(sessionManager.getStringData(SessionManager.currncy) + category.getAmt());


        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}