package com.example.ayman.cherish.MainMVP;

public class MainMVPInterfaceComponent {
	
	//TODO:CREATE MAIN CLASS THAT CONTENT 3 MAIN INTERFACES FOR MAIN COMP..
	
	//TODO:EACH CLASS WILL INHERENT YOUR OWN INTERFACE
	
	public interface IView {
		//TODO:CREATE ALL METHODS THAT WILL NEED IN VIEW ACTIVITY CLASSES
//		void onDataReceive(String data);
	}
	
	public interface IPresenter {
		//TODO:CREATE ALL METHODS THAT WILL NEED IN PRESENTER CLASS
		void getMoreInfoData(String fname,String lname,String phone);
	}
	
	public interface IModel {
		//TODO:CREATE ALL METHODS THAT WILL NEED IN MODEL CLASS
		void setData();
	}
	
}
