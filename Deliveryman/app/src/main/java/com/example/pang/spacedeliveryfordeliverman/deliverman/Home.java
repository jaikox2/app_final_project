package com.example.pang.spacedeliveryfordeliverman.deliverman;

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

import com.example.pang.spacedeliveryfordeliverman.R;
import com.example.pang.spacedeliveryfordeliverman.deliveryList;
import com.example.pang.spacedeliveryfordeliverman.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Home extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private OrderAdapterRes adapter;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";            //save session
    SharedPreferences sharedpreferences;

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

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id = sharedpreferences.getString(User_id, "");
        final ArrayList<deliveryList> itemArray = new ArrayList<>();
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlDelivery() ;

        Ion.with(getContext())
                .load(baseUrl+"DeliverRecycleView.php")
                .setMultipartParameter("user_id",user_id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject jsonObject;
                        for(int i=0; i < result.size(); i++){
                            deliveryList item = new deliveryList();
                            jsonObject = (JsonObject)result.get(i);
                            item.setDeli_id(jsonObject.get("deli_id").getAsInt());
                            item.setOrder_id(jsonObject.get("order_id").getAsInt());
                            item.setTime(jsonObject.get("time").getAsString());
                            item.setDate(jsonObject.get("date").getAsString());
                            item.setRes_name(jsonObject.get("res_name").getAsString());
                            item.setDeliman_id(jsonObject.get("deliman_id").getAsInt());
                            item.setRes_location(jsonObject.get("res_location").getAsString());
                            item.setDelivery_location(jsonObject.get("delivery_location").getAsString());
                            itemArray.add(item);
                        }
                        adapter = new OrderAdapterRes(itemArray,getActivity(), mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });

        refreshLayout.setRefreshing(false);

    }
}
