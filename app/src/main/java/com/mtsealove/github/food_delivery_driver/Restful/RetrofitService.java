package com.mtsealove.github.food_delivery_driver.Restful;

import com.mtsealove.github.food_delivery_driver.Entity.Login;
import com.mtsealove.github.food_delivery_driver.Entity.Order;
import com.mtsealove.github.food_delivery_driver.Entity.OrderDetail;
import com.mtsealove.github.food_delivery_driver.Entity.Result;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface RetrofitService {

    @FormUrlEncoded
    @POST("DeliveryService/AndroidDriver/Post/Login.php")
    Call<Login> PostLogin(@Field("ID") String ID, @Field("Password") String password, @Field("Token") String Token);

    @GET("DeliveryService/AndroidDriver/Get/NewOrders.php")
    Call<List<Order>> GetNewOrders(@Query("ManagerID") String ManagerID);

    @FormUrlEncoded
    @POST("DeliveryService/AndroidDriver/Post/UpdateStatus.php")
    Call<Result> UpdateStatus(@Field("Status") int Status,
                              @Field("OrderTime") String OrderTime,
                              @Field("ManagerID") String managerID,
                              @Field("DriverID") String DriverID);

    @GET("DeliveryService/AndroidDriver/Get/MyOrders.php")
    Call<List<Order>> GetMyOrders(@Query("DriverID") String DriverID);

    @FormUrlEncoded
    @POST("DeliveryService/AndroidDriver/Post/UpdateLocation.php")
    Call<Result> UpdateLocation(@Field("DriverID") String DriverID,
                                @Field("Location") String location);

    @GET("DeliveryService/AndroidDriver/Get/Order.php")
    Call<OrderDetail> GetOrder(@Query("OrderID") int OrderID);
}
