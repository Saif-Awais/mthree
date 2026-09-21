package com.floormastery.service;

import com.floormastery.dao.OrderDao;
import com.floormastery.dao.ProductDao;
import com.floormastery.dao.TaxDao;
import com.floormastery.exceptions.NoSuchOrderException;
import com.floormastery.exceptions.PersistenceException;
import com.floormastery.model.Order;
import com.floormastery.model.Product;
import com.floormastery.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class ServiceImpl implements Service {

	@Autowired
	OrderDao orderDao;

	@Autowired
	TaxDao taxDao;

	@Autowired
	ProductDao productDao;

	@Override
	public int getNextOrderNumber() {
		return 0;
	}

	@Override
	public Order addOrder(Order order) {
		return orderDao.addOrder(order);
	}

	@Override
	public Order calculateOrder(Order order) throws NoSuchOrderException, PersistenceException {
		// Get max orderNumber for orderDate and add one
		List<Order> ordersForDate;
		try {
			ordersForDate = getOrdersForDate(order.getOrderDate());
		} catch (NoSuchOrderException e) {
			throw new NoSuchOrderException(e.getMessage());
		}
		int i = 0;
		if (!ordersForDate.isEmpty()) {
			i = ordersForDate.stream().mapToInt(Order::getOrderNumber).max().getAsInt();
		}
		order.setOrderNumber(++i);

		List<Tax> taxes = getTaxes();
		for (Tax tax : taxes) {
			if (tax.getState().equals(order.getState())) {
				order.setTaxRate(tax.getTaxRate());
				break;
			}
		}
		List<Product> products;
		try {
			products = getProducts();
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}

		for (Product product : products) {
			if (product.getProductType().equals(order.getProductType())) {
				order.setCostPerSquareFoot(product.getCostPerSquareFoot());
				order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
				break;
			}
		}

		BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot());
		order.setMaterialCost(materialCost);

		BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot());
		order.setLaborCost(laborCost);

		order.setTax(order.getMaterialCost().add(order.getLaborCost()).multiply(order.getTaxRate().movePointLeft(2)));

		order.setTotal(order.getMaterialCost().add(order.getLaborCost()).add(order.getTax()));
		return order;
	}

	@Override
	public Order getOrder(LocalDate ld, int orderNumber) throws NoSuchOrderException {
		try {


			return orderDao.getOrder(ld, orderNumber);

		} catch (NoSuchOrderException e) {
			throw new NoSuchOrderException(e.getMessage());
		}
	}

	public List<Order> getOrdersForDate(LocalDate orderDate) throws NoSuchOrderException {

		try {
			return orderDao.getOrdersForDate(orderDate);
		} catch (NoSuchOrderException e) {
			throw new NoSuchOrderException(e.getMessage());
		}

	}

	@Override
	public List<Tax> getTaxes() throws PersistenceException {
		try {
			return taxDao.getAllTaxes();

		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	@Override
	public List<Product> getProducts() throws PersistenceException {
		try {
			return productDao.getAllProducts();

		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	@Override
	public void exportData() throws PersistenceException {
		try {
			orderDao.writeToFile();
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	@Override
	public void removeOrder(LocalDate ld, int orderNumber) {
		Order order = orderDao.removeOrder(ld, orderNumber);
	}

	@Override
	public Order editOrder(LocalDate ld, int i, Order order) {
		return orderDao.editOrder(ld, i, order);
	}

	@Override
	public Map<LocalDate, Map<Integer, Order>> getAllOrders() throws PersistenceException{
		try {
			return orderDao.getAllOrders();
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
	}
}
