package com.floormastery.dao;

import com.floormastery.exceptions.PersistenceException;
import com.floormastery.model.Product;
import com.floormastery.model.Product;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class ProductDaoFileImpl implements ProductDao {

	private final Map<String, Product> allProducts = new HashMap<>();
	private final String PRODUCT_FILE = "src/main/product.txt";
	private final String DELIMITER = "::";

	@Override
	public void loadFile() throws PersistenceException {
		Scanner scanner;
		try {
			scanner = new Scanner(new BufferedReader(
					new FileReader(PRODUCT_FILE)
			));

		} catch (FileNotFoundException e) {
			throw new PersistenceException("Product file could not be found. ");
		}
		String currentLine;
		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			String[] ProductTokens = currentLine.split(DELIMITER);
			String productType = ProductTokens[0];
			BigDecimal cost = new BigDecimal(ProductTokens[1]);
			BigDecimal laborCost = new BigDecimal(ProductTokens[2]);
			Product currentProduct = new Product(productType, cost, laborCost);
			allProducts.put(currentProduct.getProductType(), currentProduct);
		}
	}

	@Override
	public List<Product> getAllProducts() throws PersistenceException {
		try {
			loadFile();

		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
		return allProducts.values().stream().toList();
	}
}
