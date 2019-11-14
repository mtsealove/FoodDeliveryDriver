package com.mtsealove.github.food_delivery_driver.Deisgn;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.mtsealove.github.food_delivery_driver.Entity.Order;
import com.mtsealove.github.food_delivery_driver.Entity.OrderDetail;
import com.mtsealove.github.food_delivery_driver.Entity.Result;
import com.mtsealove.github.food_delivery_driver.LoginActivity;
import com.mtsealove.github.food_delivery_driver.MainActivity;
import com.mtsealove.github.food_delivery_driver.OrderActivity;
import com.mtsealove.github.food_delivery_driver.R;
import com.mtsealove.github.food_delivery_driver.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MyOrderRecyclerAdapter extends RecyclerView.Adapter<MyOrderRecyclerAdapter.ItemViewHolder> {
    Context context;

    public MyOrderRecyclerAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Order> listData = new ArrayList<>();


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_order, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(Order data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView timeTv, locationTv;
        Button confirmBtn;
        CardView cardView;

        ItemViewHolder(View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.timeTv);
            locationTv = itemView.findViewById(R.id.locationTv);
            confirmBtn = itemView.findViewById(R.id.confirmBtn);
            cardView=itemView.findViewById(R.id.cardView);
        }

        void onBind(final Order data) {
            timeTv.setText("주문시간: " + data.getOrderTime().substring(0, 16));
            locationTv.setText("도착주소: " + data.getLocation());
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Complete(data.getOrderTime());
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, OrderActivity.class);
                    intent.putExtra("orderID", data.getOrderID());
                    context.startActivity(intent);
                }
            });
        }


        private void Complete(final String orderTime) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("확인")
                    .setMessage("배송을 완료하시겠습니까?")
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RestAPI restAPI = new RestAPI(context);
                    Call<Result> call = restAPI.getRetrofitService().UpdateStatus(4, orderTime, LoginActivity.login.getManagerID(), LoginActivity.login.getID());
                    call.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getResult().equals("Ok")) {
                                    Toast.makeText(context, "베송이 종료되었습니다", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {

                        }
                    });
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}