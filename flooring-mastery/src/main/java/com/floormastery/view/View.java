package com.floormastery.view;

import com.floormastery.io.UserIO;
import com.floormastery.model.Order;
import com.floormastery.model.Product;
import com.floormastery.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class View {

	@Autowired
	private UserIO io;

	public int displayMainMenuAndGetSelection() {
		// Display options and get their choice
		io.print("Main Menu");
		io.print("1. Display Orders");
		io.print("2. Add an Order");
		io.print("3. Edit an Order");
		io.print("4. Remove an Order");
		io.print("5. Export All Data");
		io.print("6. Quit");
		return io.readInt("Enter the corresponding number. ", 1, 6);
	}

	public void displayOrders(List<Order> orderList) {
		// Display all orders in the list to the user
		for (Order order : orderList) {
			io.print(order.toString());
		}
	}

	public Order getAddOrderInput(List<Tax> taxes, List<Product> products) {
		Order order = new Order();
		String customerName = "";

		// Customer name can only contain letters, numbers, commas and periods
		while (customerName.isBlank() || !customerName.matches("[a-zA-Z0-9., ]+")) {
			customerName = io.readString("Customer Name: ");
		}

		LocalDate orderDate;

		// Get date input, must be in the future
		while (true) {
			orderDate = getDateInput();

			if (orderDate.isAfter(LocalDate.now())) {
				break;

			}
			displayErrorMessage("Date must be in future. ");
		}

		// Get product type input
		displayProducts(products);
		int s = io.readInt("Input Product type: (Enter corresponding number)", 1, products.size());
		String productType = products.get(s - 1).getProductType();


		// Get state input
		displayTax(taxes);

		int stateInput = io.readInt("Input state: (Enter corresponding number)", 1, taxes.size());

		String state = taxes.get(stateInput - 1).getState();

		int integer = io.readInt("Input area: (Must be greater than 100)", 100, 10000);
		BigDecimal area = new BigDecimal(integer);

		order.setCustomerName(customerName);
		order.setState(state);
		order.setOrderDate(orderDate);
		order.setArea(area);
		order.setProductType(productType);
		return order;
	}

	public void displayAddOrderBanner() {
		io.print(" == Add Order == ");
	}

	public void displayAddOrderSuccess() {
		io.print(" == Successfully added order == ");
	}

	public void displayRemoveOrderSuccess() {
		io.print(" == Successfully removed order == ");
	}

	public void displayEditOrderSuccess() {
		io.print(" == Successfully edited order == ");
	}

	public void displayExportDataSuccess() {
		io.print(" == Successfully exported orders == ");
	}


	public void displayOrderInfo(Order order) {
		io.print(order.toString());
	}

	public LocalDate getDateInput() {
		LocalDate localDate;
		// Loop until user provides a valid date
		while (true) {

			try {
				String date = io.readString("Enter Date in format yyyy-mm-dd");
				localDate = LocalDate.parse(date);
				break;
			} catch (DateTimeParseException e) {
				displayErrorMessage("No date provided or invalid format");
			}

		}
		return localDate;
	}

	public boolean getConfirmation() {
		// Loop until user inputs Y or N.
		while (true) {

			// Takes input and converts to uppercase
			String confirmation = io.readString("Save change (Y/N)? ").toUpperCase();

			if (confirmation.equals("Y")) {
				return true; // User has confirmed
			} else if (confirmation.equals("N")) {
				return false; // User does not confirm
			}

		}
	}

	public void displayErrorMessage(String str) {
		io.print(str);
	}


	public int getOrderNumberInput() {
		return io.readInt("Input Order Number");
	}

	public void displayExitMessage() {
		io.print("Bye!");
	}

	public void displayProducts(List<Product> products) {
		// Display products to user

		for (int i = 0; i < products.size(); i++) {
			String msg = i + 1 + ". " + products.get(i).getProductType() + " - " + products.get(i).getCostPerSquareFoot();
			io.print(msg);
		}
	}

	public void displayTax(List<Tax> taxes) {
		// Display tax to user

		for (int i = 0; i < taxes.size(); i++) {
			io.print(i + 1 + ". " + taxes.get(i).getState());
		}
	}

	public Order getEditOrderInput(Order order, List<Tax> taxes, List<Product> products) {

		// Loop until input is blank or a valid customer name.
		while (true) {
			// Allow user to change name, product type, state, and area.
			String newCustomerName = io.readString("Enter customer name (" + order.getCustomerName() + ")");

			// If new name provided, and is valid customer name, save it to order
			if (!newCustomerName.isBlank() && newCustomerName.matches("[a-zA-Z0-9., ]+")) {
				order.setCustomerName(newCustomerName);
				break;
			}
			// If blank input then break out of loop - do not save a name
			else if (newCustomerName.isBlank()) {
				break;
			}
		}

		displayProducts(products);

		while (true) {
			// Show user current product and allows user to input number to edit.
			String editProduct = io.readString("Product type: (Enter corresponsing number) (" + order.getProductType() + ") ");

			try {

				// If input provided, must be int and between 1 and products.size()
				if (!editProduct.isBlank()) {
					int productIndex = Integer.parseInt(editProduct);

					if (productIndex < 1 || productIndex > products.size()) {
						throw new IndexOutOfBoundsException("Must be between 1 and " + products.size());
					}
					String productType = products.get(productIndex - 1).getProductType();
					order.setProductType(productType);
				}

				// Correct input provided so break out of loop
				break;

			} catch (NumberFormatException e) {
				displayErrorMessage("Must be blank or a integer. ");
			} catch (IndexOutOfBoundsException e) {
				displayErrorMessage(e.getMessage());
			}
		}

		displayTax(taxes);

		// Loop until blank input or int is provided between 1 and taxes.size()
		while (true) {
			String editState = io.readString("State type: (Enter corresponding number) (" + order.getState() + ") ");

			try {

				if (!editState.isBlank()) {
					int stateIndex = Integer.parseInt(editState);
					if (stateIndex < 1 || stateIndex > taxes.size()) {
						throw new IndexOutOfBoundsException("Must be between 1 and " + taxes.size());
					}
					String state = taxes.get(stateIndex - 1).getState();
					order.setState(state);
				}

				break;

			} catch (NumberFormatException e) {
				displayErrorMessage("Must be blank or a integer. ");
			} catch (IndexOutOfBoundsException e) {
				displayErrorMessage(e.getMessage());
			}
		}

		// Loop until blank input or number provided which is greater or equal to 100
		while (true) {
			String editArea = io.readString("Area: (" + order.getArea() + ") ");

			try {

				if (!editArea.isBlank()) {
					int number = Integer.parseInt(editArea);

					if (number < 100) {
						throw new NumberFormatException("Must be >= 100");
					}

					BigDecimal area = new BigDecimal(number);
					order.setArea(area);

				}

				break;

			} catch (NumberFormatException e) {
				displayErrorMessage("Must be blank or an integer greater than 100. ");
			}
		}
		return order;
	}
}
