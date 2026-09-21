package com.floormastery.dao;

import com.floormastery.exceptions.NoSuchOrderException;
import com.floormastery.exceptions.PersistenceException;
import com.floormastery.model.Order;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {
	void writeToFile() throws PersistenceException;

	String marshallOrder(Order order);

	void loadFromFile() throws PersistenceException;
	void getNextOrderNumber();
	Order addOrder(Order order);
	Map<LocalDate, Map<Integer, Order>> getAllOrders() throws PersistenceException;
	Order getOrder(LocalDate ld, int ii) throws NoSuchOrderException;
	Order editOrder(LocalDate ld, int ii, Order order);
	List<Order> getOrdersForDate(LocalDate ld) throws NoSuchOrderException;

	Order removeOrder(LocalDate ld, int orderNumber);
}
