package isiDB;

import javafx.beans.property.*;

public class TransactionDetail {

	SimpleStringProperty transactionID;
	SimpleStringProperty hoodieID;
	SimpleStringProperty hoodieName;
	SimpleIntegerProperty qty;
	SimpleDoubleProperty totalPrice;
	public TransactionDetail(String transactionID, String hoodieID,
			String hoodieName, int qty, double totalPrice) {
		this.transactionID = new SimpleStringProperty(transactionID);
		this.hoodieID = new SimpleStringProperty(hoodieID);
		this.hoodieName = new SimpleStringProperty(hoodieName);
		this.qty = new SimpleIntegerProperty(qty);
		this.totalPrice = new SimpleDoubleProperty(totalPrice);
		
	
	}
	public String getTransactionID() {
		return transactionID.get();
	}
	public void setTransactionID(String transactionID) {
		this.transactionID.set(transactionID);;
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
	public Integer getQty() {
		return qty.get();
	}
	public void setQty(Integer qty) {
		this.qty.set(qty);;
	}
	public Double getTotalPrice() {
		return totalPrice.get();
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice.set(totalPrice);
	}
	
	
	
}
