package com.mtsealove.github.food_delivery_driver;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mtsealove.github.food_delivery_driver.Deisgn.MyOrderRecyclerAdapter;
import com.mtsealove.github.food_delivery_driver.Deisgn.NewOrderRecyclerAdapter;
import com.mtsealove.github.food_delivery_driver.Deisgn.SystemUiTuner;
import com.mtsealove.github.food_delivery_driver.Entity.Order;
import com.mtsealove.github.food_delivery_driver.Restful.RestAPI;
import com.mtsealove.github.food_delivery_driver.Services.UpdateLocationService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView itemRv;
    Button endBtn, checkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemRv = findViewById(R.id.itemRv);
        endBtn = findViewById(R.id.endBtn);
        checkBtn=findViewById(R.id.checkBtn);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        itemRv.setLayoutManager(lm);

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndService();
            }
        });
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowNewOrders();
            }
        });

        ShowNewOrders();
        StartService();
        GetMyOrders();
    }


    private void ShowNewOrders() {
        Intent intent = new Intent(this, NewOrderActivity.class);
        startActivity(intent);
    }

    //내 주문 받아오기
    private void GetMyOrders() {
        RestAPI restAPI = new RestAPI(this);
        Call<List<Order>> call = restAPI.getRetrofitService().GetMyOrders(LoginActivity.login.getID());
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    MyOrderRecyclerAdapter adapter = new MyOrderRecyclerAdapter(MainActivity.this);
                    String lastTime = null;
                    for (Order order : response.body()) {
                        if (lastTime == null) {
                            lastTime = order.getOrderTime();
                            adapter.addItem(order);
                        }
                        //데이터가 다른 경우
                        if (!lastTime.equals(order.getOrderTime())) {
                            lastTime = order.getOrderTime();
                            adapter.addItem(order);
                        }
                    }
                    itemRv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });
    }

    //서비스 실행중인지 확인
    public boolean isServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (UpdateLocationService.class.getName().equals(serviceInfo.service.getClassName())) return true;
        }
        return false;
    }

    //서비스 시작
    private void StartService() {
        if (!isServiceRunning()) {  //서비스가 실행중이 아니라면
            Intent service = new Intent(getApplicationContext(), UpdateLocationService.class);   //인텐트에 서비스를 넣어줌
            startService(service);  //서비스 시작
        }
    }

    private void EndService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("운행 종료")
                .setMessage("운행을 종료하시겠습니까?")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), UpdateLocationService.class);
                stopService(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetMyOrders();
    }
}
