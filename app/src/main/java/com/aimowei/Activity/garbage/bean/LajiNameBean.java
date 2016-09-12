package com.aimowei.Activity.garbage.bean;

public class LajiNameBean {

private String Name;


public String getName() {
	return Name;
}
public void setName(String name) {
	Name = name;
}
@Override
public String toString() {
	return "LevelBean [Name=" + Name + "]";
}
public LajiNameBean( String name) {
	super();
	
	Name = name;
}
public LajiNameBean() {
	super();
	// TODO Auto-generated constructor stub
}

}
