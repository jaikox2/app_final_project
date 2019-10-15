package com.example.pang.spacedeliveryfordeliverman.deliverman;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pang.spacedeliveryfordeliverman.InfoNotification;
import com.example.pang.spacedeliveryfordeliverman.R;
import com.example.pang.spacedeliveryfordeliverman.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Notification extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NotiAdapterRes adapter;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";            //save session
    SharedPreferences sharedpreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_res_notification, null);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        poultererView();


        return view;
    }

    private void poultererView(){

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id = sharedpreferences.getString(User_id, "");
        final ArrayList<InfoNotification> itemArray = new ArrayList<>();
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlOrder() ;

        Ion.with(getContext())
                .load(baseUrl+"NotiRecycleView.php")
                .setMultipartParameter("Res_id","122")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject jsonObject;
                        for(int i=0; i < result.size(); i++){
                            InfoNotification item = new InfoNotification();
                            jsonObject = (JsonObject)result.get(i);
                            item.setOrderId(jsonObject.get("order_id").getAsInt());
                            item.setChooseId(jsonObject.get("cart_id").getAsInt());
                            item.setTime(jsonObject.get("order_time").getAsString());
                            item.setDate(jsonObject.get("order_date").getAsString());
                            itemArray.add(item);
                        }
                        adapter = new NotiAdapterRes(itemArray,getActivity(), mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });


    }
}
