package com.floormastery.dao;

import com.floormastery.exceptions.NoSuchOrderException;
import com.floormastery.exceptions.PersistenceException;
import com.floormastery.model.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Component
public class OrderDaoFileImpl implements OrderDao {
	private final String DELIMITER = "::";
	private final String ORDER_FOLDER = "src/main/orders/";
	private final Map<LocalDate, Map<Integer, Order>> orders = new HashMap<>();

	@Override
	public void writeToFile() throws PersistenceException {
		Path dir;
		// Get each date that there is an order
		Set<LocalDate> orderDates = orders.keySet();
		for (LocalDate orderDate : orderDates) {
			// Format date to pattern MMddyyyy
			String formattedDate = orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));
			// Create new file name using formattedDate
			dir = Paths.get(ORDER_FOLDER, "Orders_" + formattedDate + ".txt");
			PrintWriter out;

			try {
				out = new PrintWriter(new FileWriter(dir.toString()));
			} catch (IOException e) {
				throw new PersistenceException("Could not write to file. ");
			}

			// Create and write a header row
			out.println("OrderNumber::CustomerName::State::TaxRate::ProductType::Area::" +
					"CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total");
			out.flush();

			// For each order for orderDate,
			// Get the text then save to file
			List<Order> ordersAsList = orders.get(orderDate).values().stream().toList();
			for (Order orderToWrite : ordersAsList) {
				String orderAsText = marshallOrder(orderToWrite);
				out.println(orderAsText);
				out.flush();
			}
			// Finally close the file.
			out.close();
		}
	}


	@Override
	public String marshallOrder(Order order) {
		// Format string to have each field of order separated by a delimiter
		String OrderAsText = order.getOrderNumber() + DELIMITER;
		OrderAsText += order.getCustomerName() + DELIMITER;
		OrderAsText += order.getState() + DELIMITER;
		OrderAsText += order.getTaxRate() + DELIMITER;
		OrderAsText += order.getProductType() + DELIMITER;
		OrderAsText += order.getArea() + DELIMITER;
		OrderAsText += order.getCostPerSquareFoot() + DELIMITER;
		OrderAsText += order.getLaborCostPerSquareFoot() + DELIMITER;
		OrderAsText += order.getMaterialCost() + DELIMITER;
		OrderAsText += order.getLaborCost() + DELIMITER;
		OrderAsText += order.getTax() + DELIMITER;
		OrderAsText += order.getTotal();
		return OrderAsText;
	}

	@Override
	public void loadFromFile() throws PersistenceException {
		Path dir = Paths.get(ORDER_FOLDER);

		// For each file in the orders directory
		try (Stream<Path> stream = Files.list(dir)) {

			// Open file
			stream.forEach(path -> {
				try (Scanner scanner = new Scanner(
						new BufferedReader(new FileReader(path.toFile())))) {
					// Skip header row
					String currentLine = scanner.nextLine();
					// Read each line
					while (scanner.hasNextLine()) {
						currentLine = scanner.nextLine();

						if (!currentLine.isBlank()) {
							// Recreate order object from string in file
							Order currentOrder = unmarshallOrder(currentLine);
							// Get orderDate by taking it from file name
							currentOrder.setOrderDate(LocalDate.parse(path.getFileName().toString().substring(7, 15), DateTimeFormatter.ofPattern("MMddyyyy")));


							// Add the order to the map inside orders for its date.
							orders.computeIfAbsent(
											currentOrder.getOrderDate(),
											// Create hashmap if one does not exist
											k -> new HashMap<>())
									.put(currentOrder.getOrderNumber(), currentOrder);
						}
					}

				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			});

		} catch (IOException e) {
			throw new PersistenceException("Could not load from Order files. ");
		}
	}

	private Order unmarshallOrder(String orderAsText) {
		/*
		Split string around the delimiter
		Convert variables to BigDecimal or int if necessary
		Create order, filling in its fields
		 */
		String[] orderTokens = orderAsText.split(DELIMITER);
		Order orderFromFile = new Order();
		int orderNumber = Integer.parseInt(orderTokens[0]);
		String customerName = orderTokens[1];
		String state = orderTokens[2];
		BigDecimal taxRate = new BigDecimal(orderTokens[3]);
		String product = orderTokens[4];
		BigDecimal area = new BigDecimal(orderTokens[5]);
		BigDecimal CostPerSquareFoot = new BigDecimal(orderTokens[6]);
		BigDecimal laborCostPerSquareFoot = new BigDecimal(orderTokens[7]);
		BigDecimal materialCost = new BigDecimal(orderTokens[8]);
		BigDecimal laborCost = new BigDecimal(orderTokens[9]);
		BigDecimal tax = new BigDecimal(orderTokens[10]);
		BigDecimal total = new BigDecimal(orderTokens[11]);

		orderFromFile.setOrderNumber(orderNumber);
		orderFromFile.setCustomerName(customerName);
		orderFromFile.setProductType(product);
		orderFromFile.setArea(area);
		orderFromFile.setCostPerSquareFoot(CostPerSquareFoot);
		orderFromFile.setTax(tax);
		orderFromFile.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
		orderFromFile.setState(state);
		orderFromFile.setMaterialCost(materialCost);
		orderFromFile.setLaborCost(laborCost);
		orderFromFile.setTotal(total);
		orderFromFile.setTaxRate(taxRate);
		return orderFromFile;
	}


	@Override
	public Order addOrder(Order order) {
		// Using number and date, save order to memory
		int orderNumber = order.getOrderNumber();
		LocalDate orderDate = order.getOrderDate();
		orders.computeIfAbsent(orderDate, k -> new HashMap<>()).put(orderNumber, order);
		return order;
	}

	@Override
	public Map<LocalDate, Map<Integer, Order>> getAllOrders() throws PersistenceException {

		// Populate orders map by loading all order objects from file
		try {
			loadFromFile();
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}

		return orders;
	}

	@Override
	public Order getOrder(LocalDate orderDate, int orderNumber) throws NoSuchOrderException {
		// Return order object for date and number
		Order order;

		try {
			order = orders.get(orderDate).get(orderNumber);

			// If orders exist for date, but not order number, throw an exception
			if (order == null) {
				throw new NoSuchOrderException("No such order for this data and order number");
			}

		} catch (NullPointerException e) {
			throw new NoSuchOrderException("No such order for this date. ");
		}

		return order;
	}


	@Override
	public Order editOrder(LocalDate orderDate, int orderNumber, Order newOrder) {
		// Save editted order to memory
		orders.get(orderDate).put(orderNumber, newOrder);
		return orders.get(orderDate).get(orderNumber);
	}

	@Override
	public List<Order> getOrdersForDate(LocalDate orderDate) throws NoSuchOrderException {
		// Get orders for orderDate
		Map<Integer, Order> ordersForDate = orders.get(orderDate);

		// No orders exist for that date so throw exception
		if (ordersForDate == null) {
			throw new NoSuchOrderException("No order for date. ");
		}

		// Return orders as a list
		return ordersForDate.values().stream().toList();
	}

	@Override
	public Order removeOrder(LocalDate orderDate, int orderNumber) {
		// Remove specified order from memory
		return orders.get(orderDate).remove(orderNumber);
	}
}
