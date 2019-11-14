package com.mtsealove.github.food_delivery_driver;

import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mtsealove.github.food_delivery_driver.Deisgn.NewOrderRecyclerAdapter;
import com.mtsealove.github.food_delivery_driver.Deisgn.SystemUiTuner;
import com.mtsealove.github.food_delivery_driver.Entity.Login;
import com.mtsealove.github.food_delivery_driver.Entity.Order;
import com.mtsealove.github.food_delivery_driver.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class NewOrderActivity extends AppCompatActivity {
    Button closeBtn;
    RecyclerView newOrderRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        closeBtn = findViewById(R.id.closeBtn);
        newOrderRv = findViewById(R.id.newOrderRv);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        newOrderRv.setLayoutManager(lm);

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GetNewOrders();
    }

    private void GetNewOrders() {
        RestAPI restAPI = new RestAPI(this);
        Call<List<Order>> call = restAPI.getRetrofitService().GetNewOrders(LoginActivity.login.getManagerID());
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.body().size() == 0) {
                    Toast.makeText(NewOrderActivity.this, "새로운 주문이 없습니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
                NewOrderRecyclerAdapter adapter = new NewOrderRecyclerAdapter(NewOrderActivity.this);
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
                newOrderRv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });
    }
}
