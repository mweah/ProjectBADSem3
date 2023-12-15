package isiDB;

import javafx.beans.property.*;

public class Cart {
	
	SimpleStringProperty hoodieID;
	SimpleStringProperty hoodieName;
	SimpleIntegerProperty qty;
	SimpleDoubleProperty totalPrice;
	
	public Cart(String hoodieID, String hoodieName, int qty, double totalPrice) {
		this.hoodieID = new SimpleStringProperty(hoodieID);
		this.hoodieName = new SimpleStringProperty(hoodieName);
		this.qty = new SimpleIntegerProperty(qty);
		this.totalPrice = new SimpleDoubleProperty(totalPrice);
	}

	public String getHoodieID() {
		return hoodieID.get();
	}

	public void setHoodieID(String hoodieID) {
		this.hoodieID.set(hoodieID);
	}

	public String getHoodieName() {
		return hoodieName.get();
	}

	public void setHoodieName(String hoodieName) {
		this.hoodieName.set(hoodieName);
	}

	public int getQty() {
		return qty.get();
	}

	public void setQty(Integer qty) {
		this.qty.set(qty);;
	}

	public double getTotalPrice() {
		return totalPrice.get();
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice.set(totalPrice);
	}
	

}
