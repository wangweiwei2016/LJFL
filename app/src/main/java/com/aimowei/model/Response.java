package com.aimowei.model;

public class Response {
	private int Status;
	private String Info;
	private String Data;
	public boolean getStatus() {
		if(Status==1)
		{
			return true;
		}else
		{
			return false;
		}
	}
	public void setStatus(int status) {
		Status = status;
	}
	public String getInfo() {
		return Info;
	}
	public void setInfo(String info) {
		Info = info;
	}
	public String getData() {
		return Data;
	}
	public void setData(String data) {
		Data = data;
	}
	
	
}
