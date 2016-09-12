package com.aimowei.common;

import java.io.StringReader;

import com.google.gson.stream.JsonReader;

public class JsonUtils {
	public String[] parseJson(String JsonData) {
		String payPerson[] = {};
		int i = 0;
		try {
			JsonReader reader = new JsonReader(new StringReader(JsonData));
			reader.beginArray();
			while (reader.hasNext()) {
				reader.beginObject();
				while (reader.hasNext()) {

					String tagName = reader.nextName();
					if (tagName.equals("name")) {
						System.out.print("name:" + reader.nextString());
					} else if (tagName.equals("age"))
						System.out.print("age:" + reader.nextString());
				}
				reader.endObject();
			}
			reader.endArray();
		}// end try
		catch (Exception e) {
			e.printStackTrace();
		}
		return payPerson;

	}// end parsejson

	public String[][] getList(String JsonData) {
		String[][] data = {};
		int i = 0;
		try {
			JsonReader reader = new JsonReader(new StringReader(JsonData));
			reader.beginArray();
			while (reader.hasNext()) {
				reader.beginObject();
				while (reader.hasNext()) {

					String tagName = reader.nextName();
					if (tagName.equals("purpose")) {
						data[i][0] = reader.nextString();
					} else if (tagName.equals("moneyAmount"))
						data[i][0] = reader.nextString();
					else if (tagName.equals("expenceTime"))
						data[i][0] = reader.nextString();
					else if (tagName.equals("payPerson"))
						data[i][0] = reader.nextString();
					else if (tagName.equals("payState"))
						data[i][0] = reader.nextString();
				}
				reader.endObject();
				i++;
			}
			reader.endArray();
		}// end try
		catch (Exception e) {
			e.printStackTrace();
		}
		return data;

	}// end parsejson

	public String[] getAllEmployee(String JsonData) {
		String payPerson[] = {};
		int i = 0;
		try {
			JsonReader reader = new JsonReader(new StringReader(JsonData));
			reader.beginArray();
			while (reader.hasNext()) {
				reader.beginObject();
				while (reader.hasNext()) {

					String tagName = reader.nextName();
					if (tagName.equals("name")) {
						payPerson[i] = reader.nextString();
					}
				}
				reader.endObject();
			}
			reader.endArray();
			i++;
		}// end try
		catch (Exception e) {
			e.printStackTrace();
		}
		return payPerson;

	}// end parsejson
}// end class
