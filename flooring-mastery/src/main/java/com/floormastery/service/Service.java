package com.floormastery.service;

import com.floormastery.exceptions.NoSuchOrderException;
import com.floormastery.exceptions.PersistenceException;
import com.floormastery.model.Order;
import com.floormastery.model.Product;
import com.floormastery.model.Tax;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface Service {

	Order addOrder(Order order) throws PersistenceException;

	Order calculateOrder(Order order) throws PersistenceException;

	Order getOrder(LocalDate orderDate, int orderNumber) throws NoSuchOrderException;

	Order editOrder(LocalDate orderDate, int orderNumber, Order order);

	Map<LocalDate, Map<Integer, Order>> getAllOrders() throws PersistenceException;

	List<Order> getOrdersForDate(LocalDate orderDate) throws NoSuchOrderException;

	List<Tax> getTaxes() throws PersistenceException;

	List<Product> getProducts() throws PersistenceException;

	void exportData() throws PersistenceException;


	void removeOrder(LocalDate orderDate, int orderNumber);
}
