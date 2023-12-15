package isiDB;

import javafx.beans.property.*;

public class Hoodie {
	
	SimpleStringProperty hoodieID;
	SimpleStringProperty hoodieName;
	SimpleDoubleProperty hoodiePrice;
	
	public Hoodie(String hoodieID, String hoodieName, double hoodiePrice) {
		this.hoodieID = new SimpleStringProperty(hoodieID);
		this.hoodieName = new SimpleStringProperty(hoodieName);
		this.hoodiePrice = new SimpleDoubleProperty(hoodiePrice);
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
		this.hoodieName.set(hoodieName);;
	}

	public double getHoodiePrice() {
		return hoodiePrice.get();
	}

	public void setHoodiePrice(double hoodiePrice) {
		this.hoodiePrice.set(hoodiePrice);
	}
	
}
