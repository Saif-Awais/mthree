package com.floormastery.io;


public interface UserIO {
	void print(String msg);

	int readInt(String prompt);

	int readInt(String prompt, int min, int max);

	int readIntForEdit(String prompt);

	String readString(String prompt);
}