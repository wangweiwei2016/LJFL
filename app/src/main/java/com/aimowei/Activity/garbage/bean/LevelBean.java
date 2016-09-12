package com.aimowei.Activity.garbage.bean;

public class LevelBean {
private double level_score;
private String Name;
public double getLevel_score() {
	return level_score;
}
public void setLevel_score(double level_score) {
	this.level_score = level_score;
}
public String getName() {
	return Name;
}
public void setName(String name) {
	Name = name;
}
@Override
public String toString() {
	return "LevelBean [level_score=" + level_score + ", Name=" + Name + "]";
}
public LevelBean(double level_score, String name) {
	super();
	this.level_score = level_score;
	Name = name;
}
public LevelBean() {
	super();
	// TODO Auto-generated constructor stub
}

}
