package com.example.pang.spacedeliveryforrestaurant.Restaurant;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pang.spacedeliveryforrestaurant.R;
import com.example.pang.spacedeliveryforrestaurant.food;
import com.example.pang.spacedeliveryforrestaurant.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Home extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FoodAdapterRes adapter;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";            //save session
    SharedPreferences sharedpreferences;

    private SwipeRefreshLayout refreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_res_home, null);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        refreshLayout = view.findViewById(R.id.Swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                poultererView();
            }
        });

        poultererView();

        return view;
    }


    private void poultererView(){

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id = sharedpreferences.getString(User_id, "");
        final ArrayList<food> itemArray = new ArrayList<>();
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlFood() ;

        Ion.with(getContext())
                .load(baseUrl+"FoodRecycleViewRes.php")
                .setMultipartParameter("user_id",user_id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject jsonObject;
                        for(int i=0; i < result.size(); i++){
                            food item = new food();
                            jsonObject = (JsonObject)result.get(i);
                            item.setId(jsonObject.get("id").getAsInt());
                            item.setName(jsonObject.get("name").getAsString());
                            item.setDetail(jsonObject.get("detail").getAsString());
                            item.setImage(jsonObject.get("img").getAsString());
                            item.setPrice(jsonObject.get("price").getAsDouble());
                            item.setStamp(jsonObject.get("stamp").getAsDouble());
                            item.setBaseUrl(baseUrl);
                            itemArray.add(item);
                        }
                        adapter = new FoodAdapterRes(itemArray,getActivity(), mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });

        refreshLayout.setRefreshing(false);

    }
}
