package com.floormastery.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Order {


	private int orderNumber;
	private String customerName;
	private String state;
	private LocalDate orderDate;
	private BigDecimal taxRate;
	private String productType;
	private BigDecimal costPerSquareFoot;
	private BigDecimal laborCostPerSquareFoot;
	private BigDecimal materialCost;
	private BigDecimal area;
	private BigDecimal tax;
	private BigDecimal laborCost;
	private BigDecimal total;

	public Order() {}

	public Order(Order order) {
		this.orderNumber = order.getOrderNumber();
		this.customerName = order.getCustomerName();
		this.state = order.getState();
		this.orderDate = order.getOrderDate();
		this.taxRate = order.getTaxRate();
		this.productType = order.getProductType();
		this.costPerSquareFoot = order.getCostPerSquareFoot();
		this.laborCostPerSquareFoot = order.getLaborCostPerSquareFoot();
		this.materialCost = order.getMaterialCost();
		this.area = order.getArea();
		this.tax = order.getTax();
		this.laborCost = order.getLaborCost();
		this.total = order.getTotal();
	}


	public BigDecimal getLaborCost() {
		return laborCost;
	}

	public void setLaborCost(BigDecimal laborCost) {
		this.laborCost = laborCost;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public BigDecimal getCostPerSquareFoot() {
		return costPerSquareFoot;
	}

	public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
		this.costPerSquareFoot = costPerSquareFoot;
	}

	public BigDecimal getLaborCostPerSquareFoot() {
		return laborCostPerSquareFoot;
	}

	public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
		this.laborCostPerSquareFoot = laborCostPerSquareFoot;
	}

	public BigDecimal getMaterialCost() {
		return materialCost;
	}

	public void setMaterialCost(BigDecimal materialCost) {
		this.materialCost = materialCost;
	}

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}


	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return orderNumber == order.orderNumber && Objects.equals(customerName, order.customerName) &&
				Objects.equals(state, order.state) && Objects.equals(orderDate, order.orderDate) &&
				Objects.equals(taxRate, order.taxRate) && Objects.equals(productType, order.productType) &&
				Objects.equals(costPerSquareFoot, order.costPerSquareFoot) &&
				Objects.equals(laborCostPerSquareFoot, order.laborCostPerSquareFoot) &&
				Objects.equals(materialCost, order.materialCost) && Objects.equals(area, order.area) &&
				Objects.equals(tax, order.tax) && Objects.equals(total, order.total);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderNumber, customerName, state, orderDate, taxRate, productType, costPerSquareFoot,
				laborCostPerSquareFoot, materialCost, area, tax, total);
	}

	@Override
	public String toString() {
		return "Order is " +
				"orderNumber=" + orderNumber +
				", customerName='" + customerName + '\'' +
				", state='" + state + '\'' +
				", orderDate=" + orderDate +
				", taxRate=" + taxRate +
				", productType='" + productType + '\'' +
				", costPerSquareFoot=" + costPerSquareFoot +
				", laborCostPerSquareFoot=" + laborCostPerSquareFoot +
				", materialCost=" + materialCost +
				", area=" + area +
				", tax=" + tax +
				", total=" + total;
	}
}
