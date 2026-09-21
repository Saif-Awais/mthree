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
		Set<LocalDate> keySet = orders.keySet();
		for (LocalDate key : keySet) {
			String formattedKey = key.format(DateTimeFormatter.ofPattern("MMddyyyy"));
			dir = Paths.get(ORDER_FOLDER, "Orders_" + formattedKey + ".txt");
			PrintWriter out;

			try {
				out = new PrintWriter(new FileWriter(dir.toString()));
			} catch (IOException e) {
				throw new PersistenceException("Could not write to file. ");
			}

			out.println("OrderNumber::CustomerName::State::TaxRate::ProductType::Area::" +
					"CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total");
			out.flush();
			List<Order> orderList = orders.get(key).values().stream().toList();
			for (Order currentOrder : orderList) {
				String orderAsText = marshallOrder(currentOrder);
				out.println(orderAsText);
				out.flush();
			}
			out.close();
		}
	}


	@Override
	public String marshallOrder(Order order) {
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

		try (Stream<Path> stream = Files.list(dir)) {

			stream.forEach(path -> {
				try (Scanner scanner = new Scanner(
						new BufferedReader(new FileReader(path.toFile())))) {
					String currentLine = scanner.nextLine(); // Reads header row first.
					while (scanner.hasNextLine()) {
						currentLine = scanner.nextLine();

						if (!currentLine.isBlank()) {
							Order currentOrder = unmarshallOrder(currentLine);
							currentOrder.setOrderDate(LocalDate.parse(path.getFileName().toString().substring(7, 15), DateTimeFormatter.ofPattern("MMddyyyy")));

							orders
									.computeIfAbsent(
											currentOrder.getOrderDate(),
											k -> new HashMap<>()
									)
									.put(
											currentOrder.getOrderNumber(),
											currentOrder
									);
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
	public void getNextOrderNumber() {
		throw new UnsupportedOperationException("");

	}

	@Override
	public Order addOrder(Order order) {
		int orderNumber = order.getOrderNumber();
		LocalDate date = order.getOrderDate();
		orders.computeIfAbsent(date, k -> new HashMap<Integer, Order>()).put(orderNumber, order);
		return order;
	}

	@Override
	public Map<LocalDate, Map<Integer, Order>> getAllOrders() throws PersistenceException {

		try {
			loadFromFile();
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
		return orders;
	}

	@Override
	public Order getOrder(LocalDate ld, int orderNumber) throws NoSuchOrderException {
		Order order = orders.get(ld).get(orderNumber);

		if (order == null) {
			throw new NoSuchOrderException("No such order for this date and order number. ");
		}

		return order;
	}


	@Override
	public Order editOrder(LocalDate orderDate, int orderNumber, Order newOrder) {
		orders.get(orderDate).put(orderNumber, newOrder);
		return orders.get(orderDate).get(orderNumber);
	}

	@Override
	public List<Order> getOrdersForDate(LocalDate orderDate) throws NoSuchOrderException {
		Map<Integer, Order> ordersForDate = orders.get(orderDate);

		if (ordersForDate == null) {
			throw new NoSuchOrderException("No order for date. ");
		}

		return ordersForDate.values().stream().toList();
	}

	@Override
	public Order removeOrder(LocalDate orderDate, int orderNumber) {
		return orders.get(orderDate).remove(orderNumber);
	}
}
