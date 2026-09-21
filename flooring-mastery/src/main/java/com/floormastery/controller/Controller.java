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
			// Load orders, products and taxes from the files.
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
						// Edit order
						editOrder();
						break;
					case 4:
						// Remove order
						removeOrder();
						break;
					case 5:
						// Save orders to text files
						exportData();
						break;
					case 6:
						// Exit the application
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
		try {
			// Ask user for date of orders
			LocalDate orderDate = view.getDateInput();
			List<Order> orders = serviceImpl.getOrdersForDate(orderDate);
			// Display orders to the user.
			view.displayOrders(orders);
		}
		catch (NoSuchOrderException e) {
			throw new NoSuchOrderException(e.getMessage());
		}
	}

	private void addOrder() throws PersistenceException, NoSuchOrderException {
		// Get list of products and states
		List<Product> allProducts = serviceImpl.getProducts();
		List<Tax> allTaxes = serviceImpl.getTaxes();
		view.displayAddOrderBanner();
		// Get input from user to create the order object
		Order order = view.getAddOrderInput(allTaxes, allProducts);
		Order newOrder;

		// Calculate fields of order such as material cost, tax, total
		newOrder = serviceImpl.calculateOrder(order);

		// Display the order with the completed fields
		view.displayOrderInfo(newOrder);

		boolean confirmation = view.getConfirmation();
		// Only save order to memory if user confirms
		if (confirmation) {
			serviceImpl.addOrder(newOrder);
			view.displayAddOrderSuccess();
		}
	}

	private void editOrder() throws PersistenceException, NoSuchOrderException {
		Order order;

		// Ask for order date and number then get corresponsing order
		LocalDate orderDate = view.getDateInput();
		int orderNumber = view.getOrderNumberInput();

		try {
			order = serviceImpl.getOrder(orderDate, orderNumber);
		} catch (NoSuchOrderException e) {
			throw new NoSuchOrderException(e.getMessage());
		}

		// Create copy of order to avoid overwriting the original order
		// Helpful if user does not confirm the edit
		Order editedOrder = new Order(order);

		// Allow user to edit customerName, state, productType, or area
		editedOrder = view.getEditOrderInput(editedOrder, serviceImpl.getTaxes(), serviceImpl.getProducts());

		// If state, productType or area changes, recalculate order fields
		if (!(editedOrder.getArea().equals(order.getArea()) && editedOrder.getState().equals(order.getState()) &&
				editedOrder.getProductType().equals(order.getProductType()))) {

			editedOrder = serviceImpl.calculateOrder(editedOrder);
		}

		// Editted order should have same orderNumber as original order object
		editedOrder.setOrderNumber(orderNumber);
		// Display order info and ask for confirmation to save
		view.displayOrderInfo(editedOrder);
		boolean confirmation = view.getConfirmation();

		// Save to memory if user confirms
		if (confirmation) {
			serviceImpl.editOrder(orderDate, orderNumber, editedOrder);
			view.displayEditOrderSuccess();
		}
	}

	private void removeOrder() throws NoSuchOrderException {

		Order order;
		// Get customer date and number from user
		LocalDate orderDate = view.getDateInput();
		int orderNumber = view.getOrderNumberInput();

		// Retrieve order to remove if order exists
		try {
			order = serviceImpl.getOrder(orderDate, orderNumber);
		} catch (NoSuchOrderException e) {
			throw new NoSuchOrderException(e.getMessage());
		}

		// Display order info to user
		view.displayOrderInfo(order);

		// Remove order only if confirmation is given
		boolean confirmation = view.getConfirmation();
		if (confirmation) {
			serviceImpl.removeOrder(orderDate, orderNumber);
			view.displayRemoveOrderSuccess();
		}
	}

	private void exportData() throws PersistenceException {

		// Save all orders to orders folder
		// Saved to separate files by date
		try {
			serviceImpl.exportData();
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}

		view.displayExportDataSuccess();
	}

}
