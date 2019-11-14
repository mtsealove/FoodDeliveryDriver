package com.mtsealove.github.food_delivery_driver;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.food_delivery_driver.Deisgn.SystemUiTuner;
import com.mtsealove.github.food_delivery_driver.Entity.Order;
import com.mtsealove.github.food_delivery_driver.Entity.OrderDetail;
import com.mtsealove.github.food_delivery_driver.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        int OrderID = getIntent().getIntExtra("orderID", 1);
        GetOrder(OrderID);
    }

    private void GetOrder(int orderID) {
        RestAPI restAPI = new RestAPI(this);
        Call<OrderDetail> call = restAPI.getRetrofitService().GetOrder(orderID);
        call.enqueue(new Callback<OrderDetail>() {
            @Override
            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<OrderDetail> call, Throwable t) {

            }
        });
    }
}
