package com.example.pang.spacedelivery.Customer;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.food;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class Home extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FoodAdapterRecom adapter;

    private SwipeRefreshLayout refreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home, null);

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

        final ArrayList<food> itemArray = new ArrayList<>();
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlFood() ;

        Ion.with(getContext())
                .load(baseUrl+"FoodRecomRecycleView.php")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject jsonObject;
                        for(int i=0; i < result.size(); i++){
                            food item = new food();
                            jsonObject = (JsonObject)result.get(i);
                            item.setFoodId(jsonObject.get("0").getAsInt());
                            item.setFoodName(jsonObject.get("1").getAsString());
                            item.setFoodPrice(jsonObject.get("2").getAsDouble());
                            item.setFoodImage(jsonObject.get("3").getAsString());
                            item.setResId(jsonObject.get("4").getAsInt());
                            item.setResName(jsonObject.get("5").getAsString());
                            item.setResAddress(jsonObject.get("6").getAsString());
                            item.setResImage(jsonObject.get("7").getAsString());
                            item.setBaseUrl(baseUrl);
                            itemArray.add(item);
                        }
                        adapter = new FoodAdapterRecom(itemArray,getActivity(), mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });

        refreshLayout.setRefreshing(false);

    }
}
