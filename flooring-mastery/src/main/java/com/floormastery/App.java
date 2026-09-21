package com.floormastery;

import com.floormastery.controller.Controller;
import com.floormastery.exceptions.NoSuchOrderException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;

public class App {
	public static void main(String[] args) throws FileNotFoundException, NoSuchOrderException {
		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
		appContext.scan("com.floormastery");
		appContext.refresh();

		Controller controller = appContext.getBean("controller", Controller.class);
		controller.run();
	}
}
