package cartPage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.Connect;
import historyPage.HistoryPage;
import homePage.HomePage;
import isiDB.Cart;
import isiDB.Hoodie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import loginPage.LoginPage;

public class CartPage {
	Connect con = Connect.getInstance();
	Stage stage;
	Scene scn;
	//atribut user untuk dipakai nanti
	String userID;
	String username;
	String userEmail;
	String userPhoneNumber;
	String userAddress;
	//atribut utk cart
	String hoodieID;
	String hoodieName;
	int qty;
	double hoodiePrice;
	double totalPrice;
	double price;
	BorderPane bp;
	//Label - label
	Label userCart;
	Label hoodieDetailTitle;
	Label selectItemLbl;
	Label contactInfoLbl;
	//user info
	Label emailLbl;
	Label phoneLbl;
	Label addressLbl;
	
	Label cartTotalPrice;
	Label confirmPayment;
	
	//Komponen yang nanti invisible
	Label hoodieIDLbl;
	Label hoodieNameLbl;
	Label hoodiePriceLbl;
	Label qtyLbl;
	Label totalPriceLbl;
	
	Button removeBtn;
	Button checkoutBtn;
	
	//button utk popup
	Button paymentBtn;
	Button cancelBtn;
	
	//MenuBar
	MenuBar menuBar;
	
	Menu accountMenu;
	Menu userMenu;
	//Untuk Menu account
	MenuItem logoutMI; 
	//Untuk menu user
	MenuItem homeMI;
	MenuItem cartMI;
	MenuItem historyMI;
	
	Font cartTitleFont;
	Font infoFonts;
	Font totalPriceDetail;
	Font otherFont;
	
	//utk pop up
	Window confirmWindow;
	Screen screen;
	//TableView
	TableView<Cart> cartTV;
	TableColumn<Cart, String> hoodieIDColumn;
	TableColumn<Cart, String> hoodieNameColumn;
	TableColumn<Cart, Integer> qtyColumn;
	TableColumn<Cart, Double> totalPriceColumn;
	ObservableList<Cart> cartInfoList;
	
	public void initialize(String userID) {
	bp = new BorderPane();
	cartTV = new TableView<>();
	hoodieIDColumn = new TableColumn<>("Hoodie ID");
	hoodieNameColumn = new TableColumn<>("Hoodie Name");
	qtyColumn = new TableColumn<>("Quantity");
	totalPriceColumn = new TableColumn<>("Total Price");
	
	getUserInfo(userID);
	userCart = new Label(username + " 's Cart");	
	hoodieDetailTitle = new Label("Hoodie's Detail");
	otherFont = Font.font("Times New Roman", 14);
	selectItemLbl = new Label("Select an item from the table...");
	selectItemLbl.setFont(otherFont);
	contactInfoLbl = new Label("Contact Information");
	emailLbl = new Label("Email	: " + userEmail);
	phoneLbl = new Label("Phone	: " + userPhoneNumber);
	addressLbl = new Label("Address	: " + userAddress);
	
	emailLbl.setFont(otherFont);
	phoneLbl.setFont(otherFont);
	addressLbl.setFont(otherFont);
	
	ObservableList<Cart> cartInfo = giveCartInfo(userID);
    cartTV.setItems(cartInfo);
    
	hoodieIDLbl = new Label();
	hoodieNameLbl = new Label();
	hoodiePriceLbl = new Label();
	qtyLbl = new Label();
	totalPriceLbl = new Label();
	
	hoodieIDLbl.setFont(otherFont);
	hoodieNameLbl.setFont(otherFont);
	hoodiePriceLbl.setFont(otherFont);
	qtyLbl.setFont(otherFont);
	
	removeBtn = new Button("Remove from Cart");
	removeBtn.setFont(otherFont);
	checkoutBtn = new Button("Checkout");
	checkoutBtn.setFont(otherFont);
	
	paymentBtn = new Button("Make Payment");
	cancelBtn = new Button("Cancel");
	
	//window confirmation
	Font confirmFont;
	confirmFont = Font.font("Times New Roman", FontWeight.BOLD, 15);
	confirmWindow = new Window();
	confirmWindow.setTitle("Payment Confirmation");
	confirmWindow.setMaxSize(400, 200);
	
	confirmPayment = new Label("Are you sure, you want to complete the payment?");
	confirmPayment.setFont(confirmFont);
	confirmPayment.setAlignment(Pos.CENTER);
	
	paymentBtn = new Button("Make Payment");
	paymentBtn.setFont(confirmFont);
	paymentBtn.setPrefSize(150, 30);
	cancelBtn = new Button("Cancel");
	cancelBtn.setPrefSize(80, 30);
	cancelBtn.setFont(confirmFont);
	HBox confirmButtonBox = new HBox();
	confirmButtonBox.getChildren().addAll(paymentBtn, cancelBtn);
	confirmButtonBox.setAlignment(Pos.CENTER);
	confirmButtonBox.setSpacing(40);
	VBox confirmationLayout = new VBox();
	confirmationLayout.getChildren().addAll(confirmPayment, confirmButtonBox);
	confirmationLayout.setSpacing(20);
	confirmationLayout.setAlignment(Pos.CENTER);
	confirmWindow.getContentPane().getChildren().add(confirmationLayout);
	
	
	hoodieIDLbl.setVisible(false);
	hoodieNameLbl.setVisible(false);
	hoodiePriceLbl.setVisible(false);
	qtyLbl.setVisible(false);
	totalPriceLbl.setVisible(false);
	removeBtn.setVisible(false);
	removeBtn.setDisable(true);
	
	
	menuBar = new MenuBar();
	accountMenu = new Menu("Account");
	userMenu = new Menu("User");
	logoutMI = new MenuItem("Logout");
	homeMI = new MenuItem("Home");
	cartMI = new MenuItem("Cart");
	historyMI = new MenuItem("History");
	accountMenu.getItems().addAll(logoutMI);
	userMenu.getItems().addAll(homeMI, cartMI, historyMI);
	menuBar.getMenus().addAll(accountMenu, userMenu);
	
	cartInfoList = cartTV.getItems();
	price = hitungTotalPrice(cartInfoList);
	//format biar muncul cuma muncul 2 digit terakhir = %.2f
	String totalPriceFormat = String.format("Cart's Total Price: %.2f", price);
	cartTotalPrice = new Label(totalPriceFormat);
	cartTitleFont = Font.font("Constantia", FontPosture.ITALIC, 56);
	infoFonts = Font.font("Helvetica", FontWeight.BOLD,FontPosture.ITALIC, 36);
	totalPriceDetail = Font.font("Times New Roman", FontWeight.BOLD, 14);
	userCart.setFont(cartTitleFont);
	
	hoodieDetailTitle.setFont(infoFonts);
	contactInfoLbl.setFont(infoFonts);
	cartTotalPrice.setFont(infoFonts);
	totalPriceLbl.setFont(totalPriceDetail);
	
	hoodieIDColumn.setCellValueFactory(new PropertyValueFactory<Cart, String>("hoodieID"));
	hoodieIDColumn.setMinWidth(100);
	hoodieNameColumn.setCellValueFactory(new PropertyValueFactory<Cart, String>("hoodieName"));
	hoodieNameColumn.setMinWidth(100);
	qtyColumn.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("qty"));
	qtyColumn.setMinWidth(100);
	totalPriceColumn.setCellValueFactory(new PropertyValueFactory<Cart, Double>("totalPrice"));
	cartTV.setPrefHeight(600);
	cartTV.getColumns().addAll(hoodieIDColumn, hoodieNameColumn, qtyColumn, totalPriceColumn);

	cartTV.setItems(cartInfoList);
	}
	
	public void getUserInfo(String userID) {
		String query = "SELECT * FROM user WHERE UserID = '"+ userID + "'";
		con.setResultSet(con.selectData(query));
		
		try {
			if(con.getResultSet().next()) {
				username = con.getResultSet().getString("Username");
			    userEmail = con.getResultSet().getString("Email");
			    userPhoneNumber = con.getResultSet().getString("PhoneNumber");
			    userAddress = con.getResultSet().getString("Address");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//ngitung total pricenya
	public double hitungTotalPrice(ObservableList<Cart>cartInfoList) {
		totalPrice = 0.0;
		//Ambil data dari Cart.java 
		for(Cart cart : cartInfoList) {
			totalPrice = totalPrice + cart.getTotalPrice();
		}
		return totalPrice;
	}

	
	public void styling() {
		VBox userTableView = new VBox();
		userTableView.getChildren().addAll(cartTV);
		HBox titleBox = new HBox();
		titleBox.getChildren().add(userCart);
		
		GridPane gpDetail = new GridPane();
		StackPane sp = new StackPane();
		
		gpDetail.add(hoodieDetailTitle, 0, 0);
		gpDetail.add(selectItemLbl, 0, 1);
		gpDetail.add(hoodieIDLbl, 0, 1);
		gpDetail.add(hoodieNameLbl, 0, 2);
		gpDetail.add(hoodiePriceLbl, 0, 3);
		gpDetail.add(qtyLbl, 0, 4);
		gpDetail.add(totalPriceLbl, 0, 5);
		gpDetail.add(removeBtn, 0, 6);
		gpDetail.setAlignment(Pos.CENTER_LEFT);
		gpDetail.setVgap(3);
		gpDetail.setHgap(10);
		
		GridPane gpContact = new GridPane();
		gpContact.add(contactInfoLbl, 0 ,0);
		gpContact.add(emailLbl, 0, 1);
		gpContact.add(phoneLbl, 0, 2);
		gpContact.add(addressLbl, 0, 3);
		gpContact.setAlignment(Pos.CENTER_LEFT);
		gpContact.setVgap(5);
		gpContact.setHgap(10);
		
		VBox cartTotalPriceBox = new VBox();
		cartTotalPriceBox.getChildren().addAll(cartTotalPrice, checkoutBtn);
		cartTotalPriceBox.setAlignment(Pos.CENTER_RIGHT);
		
		VBox rightSide = new VBox();
		rightSide.getChildren().addAll(gpDetail, gpContact, cartTotalPriceBox);
		
		HBox combineLeftRight = new HBox();
		combineLeftRight.getChildren().addAll(userTableView, rightSide);
		combineLeftRight.setSpacing(25);
		combineLeftRight.setAlignment(Pos.CENTER);
		
		GridPane combineAll = new GridPane();
		combineAll.add(titleBox, 0, 0);
		combineAll.add(combineLeftRight, 0, 1);
		combineAll.setAlignment(Pos.CENTER);
		
		bp.setTop(menuBar);
		sp.getChildren().addAll(combineAll, confirmWindow);
		confirmWindow.setVisible(false);
		confirmWindow.setDisable(true);
		sp.setAlignment(Pos.CENTER);
		bp.setCenter(sp);
			
		
		scn = new Scene(bp, 1200, 800);
	}
	
	public void menuHandling() {
		logoutMI.setOnAction(e ->{
			try {
				new LoginPage().start(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		homeMI.setOnAction(e ->{
			try {
				new HomePage(stage, userID);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		cartMI.setOnAction(e ->{
			try {
				new CartPage(stage, userID);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		historyMI.setOnAction(e ->{
			try {
				new HistoryPage(stage, userID);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		cartTV.setOnMouseClicked(e->{
			int selectedIndex = cartTV.getSelectionModel().getSelectedIndex();
			if(selectedIndex != -1){
			Cart selectedCart = cartInfoList.get(selectedIndex);
			
			hoodieID = selectedCart.getHoodieID();
			hoodieName = selectedCart.getHoodieName();
			hoodiePrice = getHoodiePrice(hoodieID);
			qty = selectedCart.getQty();
			totalPrice = selectedCart.getTotalPrice();
			
			//balik jadi visible
			selectItemLbl.setVisible(false);
			hoodieIDLbl.setVisible(true);
			hoodieNameLbl.setVisible(true);
			hoodiePriceLbl.setVisible(true);
			qtyLbl.setVisible(true);
			totalPriceLbl.setVisible(true);
			removeBtn.setVisible(true);
			removeBtn.setDisable(false);
			
			hoodieIDLbl.setText("Hoodie ID: " + hoodieID);
			hoodieNameLbl.setText("Name: " + hoodieName);
			hoodiePriceLbl.setText("Price: " + hoodiePrice);
			qtyLbl.setText("Quantity: " + qty);
			totalPriceLbl.setText("Total Price: " + totalPrice);
			
		}
		});
		
		removeBtn.setOnMouseClicked(e -> {
		    int selectedIndex = cartTV.getSelectionModel().getSelectedIndex();
		    
		    if (selectedIndex != -1) {
		    	Cart selectedCart = cartInfoList.get(selectedIndex);
		    	hoodieID = selectedCart.getHoodieID();
		    	
		        cartInfoList.remove(selectedIndex);

		        removeItemFromDatabase(userID, selectedCart.getHoodieID());

		        price = hitungTotalPrice(cartInfoList);
		        String totalPriceFormat = String.format("Cart's Total Price: %.2f", price);
		        cartTotalPrice.setText(totalPriceFormat);

		        //balikkan ke state awal sebelum remove
		        cartTV.getSelectionModel().clearSelection();
		        clearDetailLabels();
		    }
		});	
		
		checkoutBtn.setOnMouseClicked(e->{
			if(!cartInfoList.isEmpty()) {
				confirmWindow.setVisible(true);
				confirmWindow.setDisable(false);
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error");
				alert.setContentText("Cart is empty");
				
				ButtonType okBtn = new ButtonType("OK");
				
				alert.showAndWait().ifPresent(response -> {
			        if (response == okBtn) {
			            alert.close();
			        }
			    });
			}
		});
		
		paymentBtn.setOnMouseClicked(e->{
			transactionHeaderInsert(userID, cartInfoList);
			clearCartPay(userID);
			
			cartInfoList.clear();
			transactionSuccess();
			confirmWindow.setVisible(false);
			confirmWindow.setDisable(true);
		});
		
		cancelBtn.setOnMouseClicked(e->{
			confirmWindow.setVisible(false);
			confirmWindow.setDisable(true);
		});

	}
	//ambil data dari hoodie untuk display data di Cart Table View
	public List<Hoodie> getHoodieInfo() {
		List<Hoodie> dataHoodie = new ArrayList<>();
		
		String query = "SELECT * FROM hoodie";
		con.setResultSet(con.selectData(query));
		
		try {
			while(con.getResultSet().next()) {
				String hoodieID = con.getResultSet().getString("HoodieID");
				String hoodieName = con.getResultSet().getString("HoodieName");
				double hoodiePrice = con.getResultSet().getDouble("HoodiePrice");
			
				Hoodie hoodie = new Hoodie(hoodieID, hoodieName, hoodiePrice);
				dataHoodie.add(hoodie);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataHoodie;
				
	}
	
	//method untuk diberikan ke tableview cart
	public ObservableList<Cart> giveCartInfo(String userID) {
		ObservableList<Cart> dataCartInfo = FXCollections.observableArrayList();
		List<Hoodie> dataHoodies = getHoodieInfo();
		
		String query = "SELECT * FROM cart";
		con.setResultSet(con.selectData(query));
		
		try {
			while(con.getResultSet().next()) {
				String userIDGet = con.getResultSet().getString("UserID");
				
				if(userIDGet.equals(userID)) {
					String hoodieIDGet = con.getResultSet().getString("HoodieID");
					int qty = con.getResultSet().getInt("Quantity");
					String hoodieName = "";
					double price = 0.0;
					double totalPrice = 0.0;
					//cari hoodie yang equal dengan hoodieIDGet dari getResultSet
					for(Hoodie hoodie: dataHoodies) {
						if(hoodie.getHoodieID().equals(hoodieIDGet)) {
							hoodieName = hoodie.getHoodieName();
							price = hoodie.getHoodiePrice();
							break;
						}
					}
					totalPrice = price * qty;
					Cart cartInfos = new Cart(hoodieIDGet, hoodieName, qty, totalPrice);
					dataCartInfo.add(cartInfos);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataCartInfo;	
	}
	
	
	public void transactionSuccess() {
		getUserInfo(username);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText("Message");
		alert.setContentText(username + " succesfully made a transaction");
		ButtonType okBtn = new ButtonType("OK");
		alert.showAndWait().ifPresent(response -> {
	        if (response == okBtn) {
	            alert.close();
	        }
	    });
	}
	
	public void transactionHeaderInsert(String userID, ObservableList<Cart> cartInfoList) {
		String transactionID = generateTransactionUID();
		String query = "INSERT INTO TransactionHeader VALUES ('"+transactionID+"', '"+userID+"')";
		
		con.executeUpdate(query);
		transactionDetailInsert(transactionID, cartInfoList);
	}
	
	public void transactionDetailInsert(String transactionID, ObservableList<Cart> cartInfoList) {
		String query;
		String hoodieID;
		int qty;
		
		for(Cart cart : cartInfoList) {
			hoodieID = cart.getHoodieID();
			qty = cart.getQty();
			query = "INSERT INTO TransactionDetail VALUES('"+transactionID+"', '"+hoodieID+"', '"+qty+"')";
			
			con.executeUpdate(query);
		}
	}
	
	//buat transactionid baru
	public String generateTransactionUID() {
		String query = "SELECT * FROM TransactionHeader";
		int plusOne = 0;
		
		con.setResultSet(con.selectData(query));
		try {
			while(con.getResultSet().next()) {
				plusOne++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String transactionID = String.format("TR%03d", ++plusOne);
		return transactionID;
		
	}
	//method hilangin item terpilih dari cart table di db
	public void removeItemFromDatabase(String userID, String hoodieID) {
	    String query = "DELETE FROM cart WHERE UserID = '"+userID+"' AND HoodieID = '"+hoodieID+"'";
	    con.executeUpdate(query);
	    //test apakah method terpanggil
	    System.out.println("Item diremove dari user: " + userID);
	    System.out.println("ID item diremove: " + hoodieID);
	}
	
	//method hilangin seluruh item di cart dari user (habis pay)
	public void clearCartPay(String userID) {
		String query = "DELETE FROM cart WHERE UserID LIKE '"+userID+"'";
		con.executeUpdate(query);
		//tes apakah method terpanggil
		System.out.println("Cart is emptied");
	}
	
	//balikkan page ke awal pas remove item dari cart
	public void clearDetailLabels() {
	    selectItemLbl.setVisible(true);
	    hoodieIDLbl.setVisible(false);
	    hoodieNameLbl.setVisible(false);
	    hoodiePriceLbl.setVisible(false);
	    qtyLbl.setVisible(false);
	    totalPriceLbl.setVisible(false);
	    removeBtn.setVisible(false);
	    removeBtn.setDisable(true);

	    hoodieIDLbl.setText("");
	    hoodieNameLbl.setText("");
	    hoodiePriceLbl.setText("");
	    qtyLbl.setText("");
	    totalPriceLbl.setText("");
	}
	
	//method dapatin hoodieprice, soalnya ga ada hoodiePrice di Cart.java
	public double getHoodiePrice(String hoodieID) {
		String query = "SELECT* FROM hoodie";
		con.setResultSet(con.selectData(query));
		
		try {
			while(con.getResultSet().next()) {
				String  hoodieIDGet = con.getResultSet().getString("HoodieID");
				if(hoodieIDGet.equals(hoodieID)) return con.getResultSet().getDouble("HoodiePrice");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hoodiePrice;
	}


	public CartPage(Stage stage, String userID) {
		initialize(userID);
		styling();
		menuHandling();
		this.stage = stage;
		stage.setScene(scn);
		stage.show();
		this.stage.setTitle("hO-Ohdie");
		this.userID = userID;
	}

}
