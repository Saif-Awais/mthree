package com.floormastery.dao;

import com.floormastery.exceptions.PersistenceException;
import com.floormastery.model.Tax;

import java.io.FileNotFoundException;
import java.util.List;

public interface TaxDao {
	void loadFile() throws PersistenceException;
	List<Tax> getAllTaxes() throws PersistenceException;

}
