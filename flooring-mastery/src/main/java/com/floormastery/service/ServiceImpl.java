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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
	public Order addOrder(Order order) {
		// Give order to orderDao so it saves it to memory
		return orderDao.addOrder(order);
	}

	@Override
	public Order calculateOrder(Order order) throws PersistenceException {

		// Find the biggest orderNumber for the date
		List<Order> ordersForDate;
		int biggestOrderNumber = 0;

		try {
			ordersForDate = getOrdersForDate(order.getOrderDate());
			// There could exist no orders for that date
			if (!ordersForDate.isEmpty()) {
				biggestOrderNumber = ordersForDate.stream().mapToInt(Order::getOrderNumber).max().getAsInt();
			}

		} catch (NoSuchOrderException ignored) {} // Ignore exception and carry on

		// increment order number
		order.setOrderNumber(++biggestOrderNumber);

		// Get the tax rate depending on the state inside order.
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

		// Get the CostPerSquareFoot and LaborCostPerSquareFoot from product
		for (Product product : products) {

			if (product.getProductType().equals(order.getProductType())) {
				order.setCostPerSquareFoot(product.getCostPerSquareFoot());
				order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
				break;
			}

		}

		// Calculate material cost, labor cost, tax, and total.
		// Fill in order fields with the results.
		// Round to 2 d.p.

		// MaterialCost = (Area * CostPerSquareFoot)
		BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot())
				.setScale(2, RoundingMode.HALF_UP);
		order.setMaterialCost(materialCost);

		// LaborCost = (Area * LaborCostPerSquareFoot)
		BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot())
				.setScale(2, RoundingMode.HALF_UP);
		order.setLaborCost(laborCost);

		// Tax = (MaterialCost + LaborCost) * (TaxRate/100)
		BigDecimal tax = order.getMaterialCost().add(order.getLaborCost()).multiply(order.getTaxRate().movePointLeft(2))
				.setScale(2, RoundingMode.HALF_UP);
		order.setTax(tax);

		// Total = (MaterialCost + LaborCost + Tax)
		BigDecimal total = order.getMaterialCost().add(order.getLaborCost()).add(order.getTax())
				.setScale(2, RoundingMode.HALF_UP);
		order.setTotal(total);
		return order;
	}

	@Override
	public Order getOrder(LocalDate orderDate, int orderNumber) throws NoSuchOrderException {

		// Get order for date and number
		try {
			return orderDao.getOrder(orderDate, orderNumber);
		}
		catch (NoSuchOrderException e) {
			throw new NoSuchOrderException(e.getMessage());
		}
	}

	public List<Order> getOrdersForDate(LocalDate orderDate) throws NoSuchOrderException {

		// Get orders for date and return
		try {
			return orderDao.getOrdersForDate(orderDate);
		} catch (NoSuchOrderException e) {
			throw new NoSuchOrderException(e.getMessage());
		}

	}

	@Override
	public List<Tax> getTaxes() throws PersistenceException {
		// Get list of taxes
		try {
			return taxDao.getAllTaxes();

		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	@Override
	public List<Product> getProducts() throws PersistenceException {
		// Get list of products
		try {
			return productDao.getAllProducts();

		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	@Override
	public void exportData() throws PersistenceException {
		// Write order data to orders folder
		try {
			orderDao.writeToFile();
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	@Override
	public void removeOrder(LocalDate orderDate, int orderNumber) {
		orderDao.removeOrder(orderDate, orderNumber);
	}

	@Override
	public Order editOrder(LocalDate orderDate, int orderNumber, Order order) {
		return orderDao.editOrder(orderDate, orderNumber, order);
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
