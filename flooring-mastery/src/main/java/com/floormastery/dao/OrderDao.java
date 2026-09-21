package com.floormastery.dao;

import com.floormastery.exceptions.NoSuchOrderException;
import com.floormastery.exceptions.PersistenceException;
import com.floormastery.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderDao {
	void writeToFile() throws PersistenceException;

	String marshallOrder(Order order);

	void loadFromFile() throws PersistenceException;

	Order addOrder(Order order);

	Map<LocalDate, Map<Integer, Order>> getAllOrders() throws PersistenceException;

	Order getOrder(LocalDate orderDate, int orderNumber) throws NoSuchOrderException;

	Order editOrder(LocalDate orderDate, int orderNumber, Order order);

	List<Order> getOrdersForDate(LocalDate orderDate) throws NoSuchOrderException;

	Order removeOrder(LocalDate orderDate, int orderNumber);
}
