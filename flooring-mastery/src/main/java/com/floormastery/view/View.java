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
		for (Order order : orderList) {
			io.print(order.toString());
		}
	}

	public Order getAddOrderInput(List<Tax> taxes, List<Product> products) {
		Order order = new Order();
		String customerName = "";
		while (customerName.isBlank() && !customerName.matches("[a-zA-Z0-9., ]+")) {
			customerName = io.readString("Customer Name: ");
		}
		LocalDate ld;
		do {
			io.print("Date must be in future. ");
			ld = getDateInput();
		}
		while (!ld.isAfter(LocalDate.now()));

		displayProducts(products);
		int s = io.readInt("Product type: ", 1, products.size());
		String productType = products.get(s - 1).getProductType();

		for (int i = 0; i < taxes.size(); i++) {
			io.print(i + 1 + ". " + taxes.get(i).getState());
		}
		s = io.readInt("State: ", 1, taxes.size());

		String state = taxes.get(s - 1).getState();

		int integer = io.readInt("Area: (Must be greater than 100)", 100, 10000);
		BigDecimal area = new BigDecimal(integer);

		order.setCustomerName(customerName);
		order.setState(state);
		order.setOrderDate(ld);
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
		io.print(" == Successfully editted order == ");
	}

	public void displayExportDataSuccess() {
		io.print(" == Successfully exported orders == ");
	}


	public void displayOrderInfo(Order order) {
		io.print(order.toString());
	}

	public LocalDate getDateInput() throws DateTimeParseException {
		LocalDate ld;
		while (true) {
			try {
				String date = io.readString("Enter Date in format yyyy-mm-dd");
				ld = LocalDate.parse(date);
				break;
			} catch (DateTimeParseException e) {
				displayErrorMessage("Could not parse date");
			}
		}
		return ld;
	}

	public boolean getConfirmation() {
		while (true) {
			String confirmation = io.readString("Save change (Y/N)? ");
			if (confirmation.equals("Y")) {
				return true;
			} else if (confirmation.equals("N")) {
				return false;
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
		for (int i = 0; i < products.size(); i++) {
			String msg = i + 1 + ". " + products.get(i).getProductType() + " - " + products.get(i).getCostPerSquareFoot();
			io.print(msg);
		}
	}

	public void displayTax(List<Tax> taxes) {
		for (int i = 0; i < taxes.size(); i++) {
			io.print(i + 1 + ". " + taxes.get(i).getState());
		}
	}

	public Order getEditOrderInput(Order order, List<Tax> taxes, List<Product> products) {


		String newCustomerName = io.readString("Enter customer name (" + order.getCustomerName() + ")");
		if (!newCustomerName.isBlank()) {
			order.setCustomerName(newCustomerName);
		}

		displayProducts(products);

		while (true) {
			String editProduct = io.readString("Product type: (" + order.getProductType() + ") ");
			try {
				if (!editProduct.isBlank()) {
					int number = Integer.parseInt(editProduct);
					String productType = products.get(number - 1).getProductType();
					order.setProductType(productType);
				}
				break;
			} catch (NumberFormatException e) {
				displayErrorMessage("Must be blank or a integer. ");
			} catch (IndexOutOfBoundsException e) {
				displayErrorMessage("Must be between 1 and " + products.size());
			}
		}

		displayTax(taxes);
		while (true) {
			String editState = io.readString("State type: (" + order.getState() + ") ");

			try {
				if (!editState.isBlank()) {
					int number = Integer.parseInt(editState);
					String state = taxes.get(number - 1).getState();
					order.setState(state);
				}
				break;
			} catch (NumberFormatException e) {
				displayErrorMessage("Must be blank or a integer. ");
			} catch (IndexOutOfBoundsException e) {
				displayErrorMessage("Must be between 1 and " + taxes.size());
			}
		}

		while (true) {
			String editArea = io.readString("Area: (" + order.getArea() + ") ");

			try {
				if (!editArea.isBlank()) {
					int number = Integer.parseInt(editArea);
					if (number < 100) {
						throw new NumberFormatException("Must be > 100");
					}
					BigDecimal areaa = new BigDecimal(number);
					order.setArea(areaa);
				}
				break;
			} catch (NumberFormatException e) {
				displayErrorMessage("Must be blank or an integer greater than 100. ");
			}
		}
		return order;
	}
}
