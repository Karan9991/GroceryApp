package com.grocerycustomer.fragment;

import static com.grocerycustomer.activity.HomeActivity.homeActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grocerycustomer.R;
import com.grocerycustomer.activity.HomeActivity;
import com.grocerycustomer.adepter.CategoryAdp;
import com.grocerycustomer.model.CatItem;
import com.grocerycustomer.model.Category;
import com.grocerycustomer.model.User;
import com.grocerycustomer.retrofit.APIClient;
import com.grocerycustomer.retrofit.GetResult;
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
import butterknife.Unbinder;
import retrofit2.Call;


public class CategoryFragment extends Fragment implements CategoryAdp.RecyclerTouchListener, GetResult.MyListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    CategoryAdp adapter;
    List<CatItem> categoryList;
    Unbinder unbinder;
    SessionManager sessionManager;
    User user;
    public CategoryFragment() {
        // Required empty public constructor
    }


    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        categoryList = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        HomeActivity.getInstance().setFrameMargin(60);
        getCategory();
        return view;
    }

    @Override
    public void onClickItem(String titel, int position) {
        homeActivity.showMenu();
        Bundle args = new Bundle();
        args.putInt("id", position);
        args.putString("titel", titel);
        Fragment fragment = new SubCategoryFragment();
        fragment.setArguments(args);
        HomeActivity.getInstance().callFragment(fragment);
    }

    @Override
    public void onLongClickItem(View v, int position) {
    }

    private void getCategory() {
        HomeActivity.custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getCat((JsonObject) jsonParser.parse(jsonObject.toString()));
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
            HomeActivity.custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1") && result.toString() != null) {
                Gson gson = new Gson();
                Category category = gson.fromJson(result.toString(), Category.class);
                categoryList = category.getData();
                adapter = new CategoryAdp(getActivity(), categoryList, this);
                recyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.getInstance().serchviewShow();
        HomeActivity.getInstance().setFrameMargin(60);
    }
}
