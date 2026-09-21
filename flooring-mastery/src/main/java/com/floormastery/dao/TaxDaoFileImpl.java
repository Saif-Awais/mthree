package com.floormastery.dao;


import com.floormastery.exceptions.PersistenceException;
import com.floormastery.model.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class TaxDaoFileImpl implements TaxDao {
	private final Map<String, Tax> allTaxes = new HashMap<>();
	private final String TAX_FILE = "src/main/tax.txt";
	private final String DELIMITER = "::";

	@Override
	public void loadFile() throws PersistenceException {

		// Open tax file
		Scanner scanner;
		try {
			scanner = new Scanner(new BufferedReader(
					new FileReader(TAX_FILE)
			));

		} catch (FileNotFoundException e) {
			throw new PersistenceException("Tax file could not be found. ");
		}

		/*
		For each line in file, split at the delimiter
		Create new tax object
		Save in memory to allTaxes map
		 */
		String currentLine;
		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			String[] taxTokens = currentLine.split(DELIMITER);
			String stateAbr = taxTokens[0];
			String state = taxTokens[1];
			BigDecimal taxRate = new BigDecimal(taxTokens[2]);
			Tax currentTax = new Tax(state, stateAbr, taxRate);
			allTaxes.put(currentTax.getStateAbr(), currentTax);
		}
	}

	@Override
	public List<Tax> getAllTaxes() throws PersistenceException {
		// Load tax from file
		try {
			loadFile();
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
		// Return allTaxes as a list
		return allTaxes.values().stream().toList();
	}
}
