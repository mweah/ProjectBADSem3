package isiDB;

import javafx.beans.property.SimpleStringProperty;

public class TransactionHeader {
	
	SimpleStringProperty transactionID;
	SimpleStringProperty userID;
	public TransactionHeader(String transactionID, String userID) {
		this.transactionID = new SimpleStringProperty(transactionID);
		this.userID = new SimpleStringProperty(userID);
	}
	public String getTransactionID() {
		return transactionID.get();
	}
	public void setTransactionID(String transactionID) {
		this.transactionID.set(transactionID);;
	}
	public String getUserID() {
		return userID.get();
	}
	public void setUserID(String userID) {
		this.userID.set(userID);;
	}

	
	
}
