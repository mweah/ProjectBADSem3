package historyPage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cartPage.CartPage;
import db.Connect;
import homePage.HomePage;
import isiDB.TransactionHeader;
import isiDB.Hoodie;
import isiDB.TransactionDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import loginPage.LoginPage;

public class HistoryPage {
	Connect con = Connect.getInstance();
	
	BorderPane bp;
	
	Stage stage;
	Scene scn;
	
	String userID;
	String transactionID;
	String username;
	
	//Data yang nanti diambil untuk transactiondetail
	String hoodieID;
	String hoodieName;
	double totalPrice;
	double hoodiePrice;
	int qty;
		
	Label thTitle;
	Label tdTitle;
	Label tdTitleAfterClicked;
	
	Label totalPriceLbl;
	
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
	
	//th Table
	TableView<TransactionHeader> thTV;
	TableColumn<TransactionHeader, String> thIDColumn;
	TableColumn<TransactionHeader, String> userIDColumn;
	ObservableList<TransactionHeader> thInfoList;
	
	//td Table
	TableView<TransactionDetail> tdTV;
	TableColumn<TransactionDetail, String> tdIDColumn;
	TableColumn<TransactionDetail, String> hoodieIDColumn;
	TableColumn<TransactionDetail, String> hoodieNameColumn;
	TableColumn<TransactionDetail, Integer> qtyColumn;
	TableColumn<TransactionDetail, Double> totalPriceColumn;
	ObservableList<TransactionDetail> tdInfoList;
	
	Font titlesFont = Font.font("Cambria",FontWeight.BOLD, FontPosture.ITALIC, 40);
	Font tpFont = Font.font("Times New Roman", FontWeight.BOLD, 28);
	@SuppressWarnings("unchecked")
	public void initialize(String userID, String transactionID) {
		getUserInfo(userID);
		
		thTitle = new Label(username + "'s Transaction(s)");
		thTitle.setFont(titlesFont);
		tdTitle = new Label("Select a Transaction");
		tdTitle.setFont(titlesFont);
		tdTitleAfterClicked = new Label(transactionID + "'s Transaction Detail(s)");
		tdTitleAfterClicked.setVisible(false);
		tdTitleAfterClicked.setFont(titlesFont);
		totalPriceLbl = new Label();
		totalPriceLbl.setFont(tpFont);
		
		thTV = new TableView<>();
		thIDColumn = new TableColumn<>("Transaction ID");
		userIDColumn = new TableColumn<>("User ID");
		
		tdTV = new TableView<>();
		tdIDColumn = new TableColumn<>("Transaction ID");
		hoodieIDColumn = new TableColumn<>("Hoodie ID");
		hoodieNameColumn = new TableColumn<>("Hoodie Name");
		qtyColumn = new TableColumn<>("Quantity");
		totalPriceColumn = new TableColumn<>("Total Price");
		
		thIDColumn.setCellValueFactory(new PropertyValueFactory<TransactionHeader, String>("transactionID"));		
		userIDColumn.setCellValueFactory(new PropertyValueFactory<TransactionHeader, String>("userID"));	
		thInfoList = thGetInfo(userID);
		thTV.setPrefHeight(600);
		thTV.setMaxWidth(500);
		thIDColumn.setPrefWidth(250);
		userIDColumn.setPrefWidth(250);
		thTV.getColumns().addAll(thIDColumn, userIDColumn);
		thTV.setItems(thInfoList);

		tdIDColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("transactionID"));
		hoodieIDColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("hoodieID"));
		hoodieNameColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("hoodieName"));
		qtyColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, Integer>("qty"));
		totalPriceColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, Double>("totalPrice"));
		tdInfoList = tdGetInfo(transactionID);
		
		tdIDColumn.setPrefWidth(110);
		hoodieIDColumn.setPrefWidth(110);
		hoodieNameColumn.setPrefWidth(110);
		qtyColumn.setPrefWidth(110);
		totalPriceColumn.setPrefWidth(110);
		tdTV.setMaxWidth(550);
		tdTV.setPrefHeight(600);
		tdTV.getColumns().addAll(tdIDColumn, hoodieIDColumn, hoodieNameColumn, qtyColumn, totalPriceColumn);
		
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
	}
	
	
	public void styling() {
		bp = new BorderPane();
		bp.setTop(menuBar);

		VBox userTHBox = new VBox();
		userTHBox.getChildren().addAll(thTitle, thTV);
		userTHBox.setAlignment(Pos.CENTER);
		
		GridPane gpTD = new GridPane();
		gpTD.add(tdTitle, 0, 0);
		gpTD.add(tdTitleAfterClicked, 0, 0);
		gpTD.add(tdTV, 0, 1);
		gpTD.setAlignment(Pos.CENTER);
		
		HBox totalPriceBottom = new HBox();
		totalPriceBottom.getChildren().add(totalPriceLbl);
		totalPriceBottom.setAlignment(Pos.CENTER_RIGHT);
		
		HBox combineTables = new HBox();
		combineTables.getChildren().addAll(userTHBox, gpTD);
		combineTables.setAlignment(Pos.CENTER);
		combineTables.setSpacing(50);
		
		GridPane combineAll = new GridPane();
		combineAll.add(combineTables, 0, 0);
		combineAll.add(totalPriceBottom, 0, 1);
		combineAll.setAlignment(Pos.CENTER);
		
		bp.setCenter(combineAll);
		
		
		scn = new Scene(bp, 1200, 800);
	};
	
	
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
		
		thTV.setOnMouseClicked(e -> {
		    int selectedIndex = thTV.getSelectionModel().getSelectedIndex();
		    if (selectedIndex != -1) {
		    	transactionID = thTV.getItems().get(selectedIndex).getTransactionID();

		        tdInfoList = tdGetInfo(transactionID);
		        tdTV.setItems(tdInfoList);
		        double totalPrice = calculateTotalPrice(tdInfoList);
		        String totalPriceFormat = String.format("Total Price: %.2f", totalPrice);
		        totalPriceLbl.setText(totalPriceFormat);
		        

		        tdTitleAfterClicked.setText(transactionID + "'s Transaction Detail(s)");
		        tdTitle.setVisible(false);
		        tdTitleAfterClicked.setVisible(true);
		    }
		});


	}

	//dapatin info transactionheader
	public ObservableList<TransactionHeader> thGetInfo(String userID) {
	    ObservableList<TransactionHeader> thData = FXCollections.observableArrayList();
		String query = "SELECT * FROM TransactionHeader WHERE userID = '"+userID+"'";
		con.setResultSet(con.selectData(query));
		
		try {
			while(con.getResultSet().next()) {
				String transactionID = con.getResultSet().getString("TransactionID");
				String userIDGet = con.getResultSet().getString("UserID");
				TransactionHeader th = new TransactionHeader(transactionID, userIDGet);
				thData.add(th);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thData;
	}
	
	//masalah di detail table gak muncul
	
	public ObservableList<TransactionDetail> tdGetInfo(String transactionID) {
	    ObservableList<TransactionDetail> tdData = FXCollections.observableArrayList();
	    List<Hoodie> dataHoodies = getHoodieInfo();
	    
	    String query = "SELECT * FROM TransactionDetail WHERE transactionID = '"+transactionID+"'";
	    con.setResultSet(con.selectData(query));
	    try {
	        while (con.getResultSet().next()) {
	        	transactionID = con.getResultSet().getString("TransactionID");
	        	//ini dari tabel transactiondetail dari database
	            hoodieID = con.getResultSet().getString("HoodieID");
	            qty = con.getResultSet().getInt("Quantity");
	            
	            //sekarang ambil data dari tabel hoodie dari database
	            for (Hoodie hoodie : dataHoodies) {
	                if (hoodie.getHoodieID().equals(hoodieID)) {
	                    hoodieName = hoodie.getHoodieName();
	                    hoodiePrice = hoodie.getHoodiePrice();
	                    break; 
	                }
	            }
	            totalPrice = hoodiePrice * qty;
	            
	            TransactionDetail td = new TransactionDetail(transactionID, hoodieID, hoodieName, qty, totalPrice);
	            tdData.add(td);
	            
	            //cek apakah method terpanggil
	            System.out.println("Transaction ID: " + transactionID);
	            System.out.println("Hoodie ID: " + hoodieID);
	            System.out.println("Hoodie Name: " + hoodieName);
	            System.out.println("Quantity: " + qty);
	            System.out.println("Total Price: " + totalPrice);
	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return tdData;
	}
	
	//ambil data dari hoodie utk transactiondetail
	public List<Hoodie> getHoodieInfo(){
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

	//hitung total price dri seluruh history transaksi
	public double calculateTotalPrice(ObservableList<TransactionDetail> transactionDetails) {
	    double totalPrice = 0.0;
	    for (TransactionDetail transactionDetail : transactionDetails) {
	        totalPrice = totalPrice + transactionDetail.getTotalPrice();
	    }
	    return totalPrice;
	}


	//dapatin info user	utk Transaction Header
	public void getUserInfo(String userID) {
		String query = "SELECT * FROM user WHERE UserID = '"+ userID + "'";
		con.setResultSet(con.selectData(query));
		
		try {
			if(con.getResultSet().next()) {
				username = con.getResultSet().getString("Username");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public HistoryPage(Stage stage, String userID) {
		initialize(userID, transactionID);
		styling();
		menuHandling();
		this.stage = stage;
		stage.setScene(scn);
		stage.show();
		this.userID = userID;
		this.stage.setTitle("hO-Ohdie");

	}

}
