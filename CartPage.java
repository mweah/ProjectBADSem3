package cartPage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.Connect;
import homePage.HomePage;
import isiDB.Cart;
import isiDB.Hoodie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;

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
	Label labelEmail;
	Label labelPhone;
	Label labelAddress;
	
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
	
	//utk pop up
	Window window;
	Screen screen;
	
	//method untuk diberikan ke tableview cart
	public ObservableList<Cart> giveCartInfo(String userID) {
		ObservableList<Cart> dataCartInfo = FXCollections.observableArrayList();
		List<Hoodie> dataHoodie = new ArrayList<>();
		
		//masukkan data ke List dataHoodie
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
		
		//Ambil data cart & dari dataHoodie dan masukkan nanti ke dataCartInfo
		query = "SELECT * FROM cart";
		con.setResultSet(con.selectData(query));
		
		try {
			while(con.getResultSet().next()) {
				String userIDDB = con.getResultSet().getString("UserID");
				
				if(userIDDB.equals(userID)) {
					String hoodieIDDB = con.getResultSet().getString("HoodieID");
					int qty = con.getResultSet().getInt("Quantity");
					String hoodieName = "";
					double price = 0.0;
					double totalPrice = 0.0;
					
					for(Hoodie hoodie: dataHoodie) {
						if(hoodie.getHoodieID().equals(hoodieIDDB)) {
							hoodieName = hoodie.getHoodieName();
							price = hoodie.getHoodiePrice();
							break;
						}
					}
					totalPrice = price * qty;
					
					Cart cartInfos = new Cart(hoodieName, hoodieIDDB, qty, totalPrice);
					dataCartInfo.add(cartInfos);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataCartInfo;	
	}
	
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
	selectItemLbl = new Label("Select an item from the table...");
	contactInfoLbl = new Label("Contact Information");
	labelEmail = new Label("Email	: " + userEmail);
	labelPhone = new Label("Phone	: " + userPhoneNumber);
	labelAddress = new Label("Address	: " + userAddress);
	
	ObservableList<Cart> cartInfo = giveCartInfo(userID);
    cartTV.setItems(cartInfo);
    
	hoodieIDLbl = new Label();
	hoodieNameLbl = new Label();
	hoodiePriceLbl = new Label();
	qtyLbl = new Label();
	totalPriceLbl = new Label();
	
	removeBtn = new Button("Remove from Cart");
	checkoutBtn = new Button("Checkout");
	
	hoodieIDLbl.setVisible(false);
	hoodieNameLbl.setVisible(false);
	hoodiePriceLbl.setVisible(false);
	qtyLbl.setVisible(false);
	totalPriceLbl.setVisible(false);
	removeBtn.setVisible(false);
	removeBtn.setDisable(true);
	
	paymentBtn = new Button("Make Payment");
	cancelBtn = new Button("Cancel");
	
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
	String totalPriceFormat = String.format("Cart's Total Price: " + price);
	cartTotalPrice = new Label(totalPriceFormat);
	confirmPayment = new Label("Are you sure, you want to complete the payment?");
	
	cartTitleFont = Font.font("Constantia", FontPosture.ITALIC, 56);
	infoFonts = Font.font("Helvetica", FontWeight.BOLD,FontPosture.ITALIC, 36);
	
	userCart.setFont(cartTitleFont);
	
	hoodieDetailTitle.setFont(infoFonts);
	contactInfoLbl.setFont(infoFonts);
	cartTotalPrice.setFont(infoFonts);
	
	hoodieIDColumn.setCellValueFactory(new PropertyValueFactory<Cart, String>("hoodieID"));
	hoodieIDColumn.setMinWidth(100);
	hoodieNameColumn.setCellValueFactory(new PropertyValueFactory<Cart, String>("hoodieName"));
	hoodieNameColumn.setMinWidth(100);
	qtyColumn.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("qty"));
	qtyColumn.setMinWidth(100);
	totalPriceColumn.setCellValueFactory(new PropertyValueFactory<Cart, Double>("totalPrice"));
	
	cartTV.getColumns().add(hoodieIDColumn);
	cartTV.getColumns().add(hoodieNameColumn);
	cartTV.getColumns().add(qtyColumn);
	cartTV.getColumns().add(totalPriceColumn);
	
	initializeTV(userID);
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
	//method utk ambil data cart table dari db melalui userid
	public void initializeTV(String userID) {
		cartTV.setItems(cartInfoList);
	}
	
	public void styling() {
		VBox userTableView = new VBox();
		userTableView.getChildren().addAll(userCart, cartTV);
		
		
		GridPane gpDetail = new GridPane();
		
		gpDetail.add(hoodieDetailTitle, 0, 0);
		gpDetail.add(selectItemLbl, 0, 1);
		gpDetail.add(hoodieIDLbl, 0, 1);
		gpDetail.add(hoodieNameLbl, 0, 2);
		gpDetail.add(hoodiePriceLbl, 0, 3);
		gpDetail.add(totalPriceLbl, 0, 4);
		gpDetail.add(removeBtn, 0, 5);
		gpDetail.setAlignment(Pos.CENTER_LEFT);
		gpDetail.setVgap(3);
		gpDetail.setHgap(10);
		
		GridPane gpContact = new GridPane();
		gpContact.add(contactInfoLbl, 0 ,0);
		gpContact.add(labelEmail, 0, 1);
		gpContact.add(labelPhone, 0, 2);
		gpContact.add(labelAddress, 0, 3);
		gpContact.setAlignment(Pos.CENTER_LEFT);
		gpContact.setVgap(5);
		gpContact.setHgap(10);
		
		GridPane gpCartTP = new GridPane();
		gpCartTP.add(cartTotalPrice, 0, 0);
		gpCartTP.add(checkoutBtn, 0, 1);
		gpCartTP.setAlignment(Pos.CENTER_RIGHT);
		gpCartTP.setVgap(5);
		gpCartTP.setHgap(10);

		
		VBox rightSide = new VBox();
		rightSide.getChildren().addAll(gpDetail, gpContact, gpCartTP);
		HBox combineAll = new HBox();
		combineAll.getChildren().addAll(userTableView, rightSide);
		
		bp.setTop(menuBar);
		bp.setCenter(combineAll);
		scn = new Scene(bp, 1200, 800);
	}
	
	public void menuHandling() {
		homeMI.setOnAction(e ->{
			new HomePage(stage, userID);
		});
	}

	public CartPage(Stage stage, String userID) {
		initialize(userID);
		styling();
		menuHandling();
		this.stage = stage;
		stage.setScene(scn);
		stage.show();
		this.userID = userID;
	}

}
