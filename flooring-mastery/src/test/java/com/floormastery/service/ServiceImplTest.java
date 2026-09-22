package com.floormastery.service;

import com.floormastery.dao.OrderDao;
import com.floormastery.dao.ProductDao;
import com.floormastery.dao.TaxDao;
import com.floormastery.exceptions.NoSuchOrderException;
import com.floormastery.exceptions.PersistenceException;
import com.floormastery.model.Order;
import com.floormastery.model.Product;
import com.floormastery.model.Tax;
import com.floormastery.service.ServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceImplTest {

	@Mock
	private OrderDao orderDao;

	@Mock
	private TaxDao taxDao;

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ServiceImpl service;

	private LocalDate date;
	private Order order;
	private List<Order> orders;

	@BeforeEach
	void setUp() {
		date = LocalDate.of(2026, 9, 22);

		order = new Order();
		order.setOrderDate(date);
		order.setOrderNumber(1);
		order.setCustomerName("John Smith");
		order.setState("Texas");
		order.setProductType("Wood");
		order.setArea(new BigDecimal("100"));
		orders = new ArrayList<>();

		orders.add(order);
	}

	@Test
	void addOrder_ShouldReturnOrderFromDao() {

		// Stubbing orderDao call
		when(orderDao.addOrder(order)).thenReturn(order);

		// Execute method
		Order result = service.addOrder(order);

		// Check that order is returned
		assertEquals(order, result);
		// Check that method was called
		verify(orderDao).addOrder(order);
	}

	@Test
	void getOrder_valid() throws NoSuchOrderException {
		int orderNumber = 1;

		when(orderDao.getOrder(date, orderNumber)).thenReturn(order);

		Order result = service.getOrder(date, orderNumber);
		assertEquals(result, order);
		verify(orderDao).getOrder(date, orderNumber);


	}

	@Test
	void getOrder_doesNotExist() throws NoSuchOrderException {
		int orderNumber = 2;

		when(orderDao.getOrder(date, orderNumber)).thenThrow(new NoSuchOrderException("No order for date. "));

		NoSuchOrderException exception = assertThrows(NoSuchOrderException.class,
				() -> service.getOrder(date, orderNumber));
		assertEquals("No order for date. ", exception.getMessage());
		verify(orderDao).getOrder(date, orderNumber);

	}

	@Test
	void getOrdersForDate_HasOrders() throws NoSuchOrderException {

		when(orderDao.getOrdersForDate(date)).thenReturn(orders);

		List<Order> result = service.getOrdersForDate(date);

		assertEquals(result, orders);
		verify(orderDao).getOrdersForDate(date);
	}

	@Test
	void getOrdersForDate_doesNotExist() throws NoSuchOrderException {

		LocalDate orderDate = LocalDate.of(2026, 9, 25);
		when(orderDao.getOrdersForDate(orderDate)).thenThrow(new NoSuchOrderException("No order for date. "));

		NoSuchOrderException exception = assertThrows(NoSuchOrderException.class,
				() -> service.getOrdersForDate(orderDate));
		assertEquals("No order for date. ", exception.getMessage());
		verify(orderDao).getOrdersForDate(orderDate);

	}

	@Test
	void calculateOrder() throws PersistenceException {

	}

//	public Order calculateOrder(Order order) throws PersistenceException {
//
//		// Find the biggest orderNumber for the date
//		List<Order> ordersForDate;
//		int biggestOrderNumber = 0;
//
//		try {
//			ordersForDate = getOrdersForDate(order.getOrderDate());
//			// There could exist no orders for that date
//			if (!ordersForDate.isEmpty()) {
//				biggestOrderNumber = ordersForDate.stream().mapToInt(Order::getOrderNumber).max().getAsInt();
//			}
//
//		} catch (NoSuchOrderException ignored) {} // Ignore exception and carry on
//
//		// increment order number
//		order.setOrderNumber(++biggestOrderNumber);
//
//		// Get the tax rate depending on the state inside order.
//		List<Tax> taxes = getTaxes();
//
//		for (Tax tax : taxes) {
//
//			if (tax.getState().equals(order.getState())) {
//				order.setTaxRate(tax.getTaxRate());
//				break;
//			}
//
//		}
//
//		List<Product> products;
//
//		try {
//			products = getProducts();
//		} catch (PersistenceException e) {
//			throw new PersistenceException(e.getMessage());
//		}
//
//		// Get the CostPerSquareFoot and LaborCostPerSquareFoot from product
//		for (Product product : products) {
//
//			if (product.getProductType().equals(order.getProductType())) {
//				order.setCostPerSquareFoot(product.getCostPerSquareFoot());
//				order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
//				break;
//			}
//
//		}
//
//		// Calculate material cost, labor cost, tax, and total.
//		// Fill in order fields with the results.
//		// Round to 2 d.p.
//
//		// MaterialCost = (Area * CostPerSquareFoot)
//		BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot())
//				.setScale(2, RoundingMode.HALF_UP);
//		order.setMaterialCost(materialCost);
//
//		// LaborCost = (Area * LaborCostPerSquareFoot)
//		BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot())
//				.setScale(2, RoundingMode.HALF_UP);
//		order.setLaborCost(laborCost);
//
//		// Tax = (MaterialCost + LaborCost) * (TaxRate/100)
//		BigDecimal tax = order.getMaterialCost().add(order.getLaborCost()).multiply(order.getTaxRate().movePointLeft(2))
//				.setScale(2, RoundingMode.HALF_UP);
//		order.setTax(tax);
//
//		// Total = (MaterialCost + LaborCost + Tax)
//		BigDecimal total = order.getMaterialCost().add(order.getLaborCost()).add(order.getTax())
//				.setScale(2, RoundingMode.HALF_UP);
//		order.setTotal(total);
//		return order;
//	}




}
