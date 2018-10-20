package com.example.ayman.cherish.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetCityName {
	
	public static String hereLocation(double lat, double lon,Context con)
	{
		String city_name="";
		
		Geocoder geocoder=new Geocoder(con, Locale.getDefault());
		
		List<Address> addresses;
		try {
			addresses=geocoder.getFromLocation(lat,lon,10);
			
			if(addresses.size()>0)
			{
				for(Address adr:addresses)
				{
					if(adr.getLocality()!=null &&adr.getLocality().length()>0)
					{
						city_name=adr.getLocality();
						break;
					}
				}
			}
			
		}catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return city_name;
	}
	
	public static void onRequestReturn(int requestCode,@NonNull int[] grantResults,Context con)
	{
		switch (requestCode) {
			case 2: {
				if (grantResults.length > 0) {
					for (int i = 0; i < grantResults.length; i++) {
						if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
							Toast.makeText(con, "Permission is granted", Toast.LENGTH_LONG).show();
						}
					}
				} else {
					Toast.makeText(con, "Denied permission"
							, Toast.LENGTH_LONG).show();
				}
				
				break;
			}
			
			case 1000: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					
					LocationManager locationManager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
					if (ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return;
					}
					Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					try {
						String city = GetCityName.hereLocation(location.getLatitude(), location.getLongitude(),con);
						Toast.makeText(con, "" + city, Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						Toast.makeText(con, "Not found!", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(con, "Permission not granted", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			
		}
		
	}
	
}
