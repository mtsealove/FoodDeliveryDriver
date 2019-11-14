package com.mtsealove.github.food_delivery_driver.Services;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.mtsealove.github.food_delivery_driver.Entity.Result;
import com.mtsealove.github.food_delivery_driver.LoginActivity;
import com.mtsealove.github.food_delivery_driver.R;
import com.mtsealove.github.food_delivery_driver.Restful.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class UpdateLocationService extends Service {    //백그라운드에서 작동(서비스)
    private Notification notification;
    String tag = getClass().getSimpleName();
    Geocoder geocoder;
    RestAPI restApi;
    ;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "운행이 시작되었습니다", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {  //서비스가 시작될 때
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(UpdateLocationService.this)   //알림 만들기
                .setContentTitle("주문배달 서비스") //제목
                .setContentText("주문배달 서비스가 실행중입니다")  //내용
                .setSmallIcon(R.drawable.logo_no_back) //아이콘 설정
                .setOngoing(true);  //상단바에 띄우기
        if (Build.VERSION.SDK_INT > 26)
            CreateNotificationHigh(notificationBuilder);   //노티 만들기
        else CreateNotificationLow(notificationBuilder);

        restApi = new RestAPI(getBaseContext());
        //위치정보 갱신 활성화
        GetLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {   //종료되었을 때
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "운행이 중지되었습니다", Toast.LENGTH_SHORT).show();
        Log.d(tag, "서비스 종료");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)   //안드로이드 8.0이상에서 작동
    public void CreateNotificationHigh(NotificationCompat.Builder notificationBuilder) {  //알림 만들기
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);    //노티(알림) 매니저
        String channelId = "notify";  //채널 ID
        CharSequence channelName = "알림";  //채널 이름
        String description = "일반 알림입니다";  //채널 설명
        int importance = NotificationManager.IMPORTANCE_HIGH; //중요도 높음
        //안드로이드 8.0 이상부터는 알림 채널을 설정해야 알림 표시가 가능하다
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);    //알림 채널
        notificationChannel.setDescription(description);    //채널 설명 설정
        notificationManager.createNotificationChannel(notificationChannel); //채널 생성

        notificationBuilder.setChannelId("notify"); //채널 ID를 기반으로 위에서 만든 채널을 할당
        notification = notificationBuilder.build();   //실제 노티 빌드
        startForeground(001, notification); //항상 실행
    }

    public void CreateNotificationLow(NotificationCompat.Builder notificationBuilder) {   //안드로이드 8.0 미만에서 작동
        notification = notificationBuilder.build();   //실제 노티 빌드
        startForeground(001, notification); //항상 실행
    }

    LocationManager locationManager;
    Location location;

    //위치 정보 확인하기
    @SuppressLint("MissingPermission")
    private void GetLocation() {
        Log.d(tag, "위치추적 서비스 시작");
        locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //5분에 한 번씩, 10m마다 한번씩 위치 정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, GpsListener);
        geocoder = new Geocoder(this);
    }

    //위치정보 리스너
    private LocationListener GpsListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 10);
                if (addresses != null && addresses.size() != 0) {
                    String addr = addresses.get(0).getAddressLine(0);
                    addr.replace("대한민국 ", "");
                    UpdateLocation(addr);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    //데이터베이스에 위치정보 업데이트
    private void UpdateLocation(final String address) {
        Call<Result> call = restApi.getRetrofitService().UpdateLocation(LoginActivity.login.getID(), address);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Log.d(tag, "updated");
                } else {
                    Log.e(tag, response.toString());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(tag, t.toString());
            }
        });
    }
}