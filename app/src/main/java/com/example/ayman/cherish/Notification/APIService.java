package com.example.ayman.cherish.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
	
	@Headers(
			{
					"content-Type:application/json",
					"Authorization:key=AAAAl3DqB4I:APA91bGAmB2DHBh5R5nOeEJsXKBcQA3vcpYW7BJqFxj5gZEw9uaPVlw8_sdMIcyD3-hRzar3xpUgYxwQRfb2HX636KWYHcSi3YKZvBmrskmz9HzCGa9h3Rlhxh7JPitGy2LL7M1w3xPQ"
			}
	)
	
	@POST("fcm/send")
	Call<MyResponse> sendNotification(@Body Sender body);
	
}
