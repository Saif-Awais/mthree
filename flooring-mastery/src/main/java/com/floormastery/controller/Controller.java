package com.floormastery.controller;

import com.floormastery.exceptions.NoSuchOrderException;
import com.floormastery.exceptions.PersistenceException;
import com.floormastery.model.Order;
import com.floormastery.model.Product;
import com.floormastery.model.Tax;
import com.floormastery.service.Service;
import com.floormastery.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class Controller {

	@Autowired
	private View view;

	@Autowired
	private Service serviceImpl;

	public void run() {
		try {
			serviceImpl.getAllOrders();
			serviceImpl.getProducts();
			serviceImpl.getTaxes();
		} catch (PersistenceException e) {
			view.displayErrorMessage(e.getMessage());
			return;
		}

		boolean keepGoing = true;
		while (keepGoing) {
			try {

				int selection = getMenuSelection();
				switch (selection) {
					case 1:
						// display orders
						displayOrders();
						break;
					case 2:
						// add an order
						addOrder();
						break;
					case 3:
						editOrder();
						break;
					case 4:
						removeOrder();
						break;
					case 5:
						exportData();
						break;
					case 6:
						view.displayExitMessage();
						keepGoing = false;
						break;
				}
			} catch (NoSuchOrderException | PersistenceException e) {
				view.displayErrorMessage(e.getMessage());
			}
		}
	}

	private int getMenuSelection() {
		return view.displayMainMenuAndGetSelection();
	}

	private void displayOrders() throws NoSuchOrderException {
		// Ask user for date. Display orders for that date.
		while (true) {
			try {
				LocalDate orderDate = view.getDateInput();
				List<Order> orders = serviceImpl.getOrdersForDate(orderDate);
				view.displayOrders(orders);
				break;
			} catch (NoSuchOrderException e) {
				throw new NoSuchOrderException(e.getMessage());
			}
		}
	}

	private void addOrder() throws PersistenceException, NoSuchOrderException {
		// Create order object.
		// Pass to service layer
		List<Product> allProducts = serviceImpl.getProducts();
		List<Tax> allTaxes = serviceImpl.getTaxes();
		view.displayAddOrderBanner();
		Order order = view.getAddOrderInput(allTaxes, allProducts);
		Order newOrder;
		try {
			newOrder = serviceImpl.calculateOrder(order);
		} catch (NoSuchOrderException e) {
			throw new NoSuchOrderException(e.getMessage());
		}
		view.displayOrderInfo(newOrder);
		boolean confirmation = view.getConfirmation();
		if (confirmation) {
			serviceImpl.addOrder(newOrder);
		}

		view.displayAddOrderSuccess();
	}

	private void editOrder() throws PersistenceException, NoSuchOrderException {
		Order order;
		LocalDate ld = view.getDateInput();
		int orderNumber = view.getOrderNumberInput();

		try {
			order = serviceImpl.getOrder(ld, orderNumber);
		} catch (NoSuchOrderException e) {
			throw new NoSuchOrderException(e.getMessage());
		}

		Order edittedOrder = new Order(order);
		try {
			edittedOrder = view.getEditOrderInput(edittedOrder, serviceImpl.getTaxes(), serviceImpl.getProducts());
		} catch (PersistenceException ignored) {
		}
		if (!(edittedOrder.getArea().equals(order.getArea()) && edittedOrder.getState().equals(order.getState()) &&
				edittedOrder.getProductType().equals(order.getProductType()))) {
			try {
				edittedOrder = serviceImpl.calculateOrder(edittedOrder);
			} catch (NoSuchOrderException e) {
				throw new NoSuchOrderException(e.getMessage());
			}
		}
		edittedOrder.setOrderNumber(orderNumber);
		view.displayOrderInfo(edittedOrder);
		boolean confirmation = view.getConfirmation();
		if (confirmation) {
			serviceImpl.editOrder(ld, orderNumber, edittedOrder);
			view.displayEditOrderSuccess();
		}
	}

	private void removeOrder() throws NoSuchOrderException {
		Order order;
		LocalDate ld = view.getDateInput();
		int orderNumber = view.getOrderNumberInput();
		try {
			order = serviceImpl.getOrder(ld, orderNumber);
		} catch (NoSuchOrderException e) {
			throw new NoSuchOrderException(e.getMessage());
		}

		view.displayOrderInfo(order);
		boolean confirmation = view.getConfirmation();
		if (confirmation) {
			serviceImpl.removeOrder(ld, orderNumber);
			view.displayRemoveOrderSuccess();
		}
	}

	private void exportData() throws PersistenceException {

		try {
			serviceImpl.exportData();
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}

		view.displayExportDataSuccess();
	}

}
