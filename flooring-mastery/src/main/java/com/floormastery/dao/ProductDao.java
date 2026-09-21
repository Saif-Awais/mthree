package com.floormastery.dao;

import com.floormastery.exceptions.PersistenceException;
import com.floormastery.model.Product;

import java.util.List;

public interface ProductDao {

	void loadFile() throws PersistenceException;

	List<Product> getAllProducts() throws PersistenceException;

}
