package com.mtsealove.github.food_delivery_driver;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.food_delivery_driver.Deisgn.SystemUiTuner;
import com.mtsealove.github.food_delivery_driver.Entity.Order;
import com.mtsealove.github.food_delivery_driver.Entity.OrderDetail;
import com.mtsealove.github.food_delivery_driver.Restful.RestAPI;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    String tag = getClass().getSimpleName();
    LinearLayout mapLayout;
    TextView locationTv, currentLocationTv, timeTv, statusTv;
    Button naviBtn;
    double latitude, longitude;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mapLayout = findViewById(R.id.mapLayout);
        locationTv = findViewById(R.id.locationTv);
        currentLocationTv = findViewById(R.id.currentLocationTv);
        timeTv = findViewById(R.id.timeTv);
        statusTv = findViewById(R.id.statusTv);
        naviBtn = findViewById(R.id.naviBtn);

        SystemUiTuner tuner = new SystemUiTuner(this);
        tuner.setStatusBarWhite();

        int OrderID = getIntent().getIntExtra("orderID", 1);
        GetOrder(OrderID);
    }

    private void GetOrder(int orderID) {
        dialog=new ProgressDialog(this);
        dialog.setMessage("잠시만 기다려주세요");
        dialog.setCancelable(false);
        dialog.show();
        RestAPI restAPI = new RestAPI(this);
        Call<OrderDetail> call = restAPI.getRetrofitService().GetOrder(orderID);
        call.enqueue(new Callback<OrderDetail>() {
            @Override
            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                if (response.isSuccessful()) {
                    Log.d(tag, response.body().toString());
                    OrderDetail detail = response.body();
                    locationTv.setText("도착 주소: " + detail.getLocation());
                    currentLocationTv.setText("현재 위치: " + detail.getCurrentLocation());
                    timeTv.setText("주문 시간: " + detail.getOrderTime());
                    statusTv.setText("상태: " + detail.getStatusName());
                    Geocode(detail.getLocation());
                }
                Log.e(tag, response.toString());
            }

            @Override
            public void onFailure(Call<OrderDetail> call, Throwable t) {
                Log.e(tag, t.toString());
            }
        });
    }

    private void Geocode(String address) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 10);
            if (addresses != null && addresses.size() != 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
                setMap(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            naviBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetNaviBtn();
                }
            });
            dialog.dismiss();
        }
    }

    //위치 설정
    private void setMap(double latitude, double longitude) {
        //카카오맵 설정
        MapView mapView = new MapView(this);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 3, true);

        //마커 설정
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("현재 위치");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        //마커 표시
        mapView.addPOIItem(marker);
        //화면에 추가
        mapLayout.addView(mapView);
    }

    @SuppressLint("MissingPermission")
    private void SetNaviBtn() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        String url="daummaps://route?" +
                "sp="+location.getLatitude()+","+location.getLongitude() +
                "&ep="+latitude+","+longitude+"&by=CAR";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
