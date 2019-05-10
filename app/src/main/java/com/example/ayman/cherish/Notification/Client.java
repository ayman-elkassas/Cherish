package com.example.ayman.cherish.Notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

	private static Retrofit retrofit=null;
	
	public static Retrofit getClient(String url)
	{
		//Singleton design pattern
		if(retrofit==null)
		{
			retrofit=new Retrofit.Builder()
					.baseUrl(url)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}
		return retrofit;
	}

}
