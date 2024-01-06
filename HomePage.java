package homePage;

import java.sql.SQLException;
import cartPage.CartPage;
import db.Connect;
import historyPage.HistoryPage;
import isiDB.Hoodie;
import isiDB.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import loginPage.LoginPage;

public class HomePage {
	
	Connect con = Connect.getInstance();
	
	String hoodieID;
	String hoodieName;
	double hoodiePrice;
	double totalPrice;
	int qty;

	Stage stage;
	Scene scn;
	
	BorderPane bp;
	GridPane gpInfo;
	
	Button addToCartBtn;
	//Labelnya
	Label hoohdieTitle;
	Label hoodieDetailTitle;
	Label hoodieSelect;
	Label hoodieIDLbl;
	Label hoodieNameLbl;
	Label hoodiePriceLbl;
	Label hoodieQuantityLbl;
	Label totalPriceLbl;
	
	MenuBar menuBar;
	
	Menu accountMenu;
	Menu userMenu;
	//Untuk Menu account
	MenuItem logoutMI; 
	//Untuk menu user
	MenuItem homeMI;
	MenuItem cartMI;
	MenuItem historyMI;
	
	Spinner<Integer> qtySpinner;
	SpinnerValueFactory<Integer> qtyValue;
	
	ListView<String> hoodieLV;
	ObservableList<Hoodie> dataHoodie;
	
	Font titleFont = Font.font("Constantia", FontPosture.ITALIC, 56);
	Font detailFont = Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 36);
	Font btnFont = Font.font("Times New Roman", FontWeight.BOLD, 14);
	Font otherFont = Font.font("Times New Roman", 14);
	User user;

	private String userID;
	
	public void initialize() {
		bp = new BorderPane();
		gpInfo = new GridPane();
		addToCartBtn = new Button("Add to Cart");
		addToCartBtn.setFont(btnFont);
		// Udah muncul dari awal
		hoohdieTitle = new Label("hO-Ohdie");
		hoohdieTitle.setFont(titleFont);
		hoodieDetailTitle = new Label("Hoodie's Detail");
		hoodieDetailTitle.setFont(detailFont);
		hoodieSelect = new Label("Select an item from the list");
		hoodieSelect.setFont(otherFont);
		//Gak muncul sebelum item ada di tekan
		hoodieIDLbl = new Label("Hoodie ID: ");
		hoodieNameLbl = new Label("Name: ");
		hoodiePriceLbl = new Label("Price: ");
		hoodieQuantityLbl = new Label("Quantity: ");
		totalPriceLbl = new Label("Total Price");
		
		hoodieIDLbl.setFont(otherFont);
		hoodieNameLbl.setFont(otherFont);
		hoodiePriceLbl.setFont(otherFont);
		hoodieQuantityLbl.setFont(otherFont);
		totalPriceLbl.setFont(otherFont);
		
		//buat jadi invisible
		hoodieIDLbl.setVisible(false);
		hoodieNameLbl.setVisible(false);
		hoodiePriceLbl.setVisible(false);
		hoodieQuantityLbl.setVisible(false);
		totalPriceLbl.setVisible(false);
		addToCartBtn.setVisible(false);
		
		//matikan fungsi
		hoodieNameLbl.setDisable(true);
		hoodiePriceLbl.setDisable(true);
		hoodieQuantityLbl.setDisable(true);
		totalPriceLbl.setDisable(true);
		addToCartBtn.setDisable(true);
		
		//Menu
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
		
		qtyValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
		qtySpinner = new Spinner<>(qtyValue);
		
		hoodieLV = new ListView<>();
		dataHoodie = FXCollections.observableArrayList();
		
		hoodieLV.setPrefWidth(400);
		hoodieLV.setPrefHeight(600);		
		
	}
	
	public void hoodieList() {
		String query = "SELECT * FROM hoodie";
		con.setResultSet(con.selectData(query));
		
		try {
			while(con.getResultSet().next()) {
				String hoodieID = con.getResultSet().getString("HoodieID");
				String hoodieName = con.getResultSet().getString("HoodieName");
				Double hoodiePrice = con.getResultSet().getDouble("HoodiePrice");
				Hoodie hoodie = new Hoodie(hoodieID, hoodieName, hoodiePrice);
				dataHoodie.add(hoodie);
				//Pakai string format karna list view cuma bisa list 1 data
				//%s\t%s
				String infos = String.format("%s\t%s", hoodieID, hoodieName);
				hoodieLV.getItems().add(infos);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	private void styling() {
		//Title Page sama tabelnya
		VBox listViewBox = new VBox();
		listViewBox.getChildren().addAll(hoodieLV);
		listViewBox.setAlignment(Pos.CENTER_LEFT);
		HBox titleBox = new HBox();
		titleBox.getChildren().add(hoohdieTitle);
		hoodieList();
		//Quantity + Total Price
		//hboxhp ngegabung tabel sama info, dibuat center pakai bp

		gpInfo.add(hoodieDetailTitle, 0, 0);
		gpInfo.add(hoodieSelect, 0, 1);
		gpInfo.add(hoodieIDLbl, 0, 1);
		gpInfo.add(hoodieNameLbl, 0, 2);
		gpInfo.add(hoodiePriceLbl, 0, 3);
		gpInfo.add(hoodieQuantityLbl, 0, 4);
		gpInfo.add(addToCartBtn, 0, 6);
		gpInfo.setAlignment(Pos.CENTER);
		
		HBox HBoxHP;
		HBoxHP = new HBox();
		HBoxHP.getChildren().addAll(listViewBox, gpInfo);
		HBoxHP.setSpacing(25);
		HBoxHP.setAlignment(Pos.CENTER);
			
		GridPane combineAll = new GridPane();
		combineAll.add(titleBox, 0, 0);
		combineAll.add(HBoxHP, 0, 1);
		combineAll.setAlignment(Pos.CENTER);
		
		bp.setTop(menuBar);
		bp.setCenter(combineAll);
		
		//kasih jarak
		gpInfo.setHgap(10);
		gpInfo.setVgap(10);
		HBoxHP.setSpacing(50);
		listViewBox.setSpacing(5);
		
		scn = new Scene(bp, 1200, 800);
	}
	
	public void cartHandling(String userID) {
		addToCartBtn.setOnAction(e ->{
			cartToDB(userID, hoodieID, qty);
			successAlert();
			// tes apakah datanya berhasil di simpan
			System.out.println("ID anda: " + userID);
			System.out.println("Hoodie anda: " + hoodieID);
			System.out.println("Jumlah: " + qty);
		});
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
			
		//Bagian listview
		
		hoodieLV.setOnMouseClicked(e -> {
			//munculin spinnner
			HBox qtyBox = new HBox(qtySpinner, totalPriceLbl);
			gpInfo.add(qtyBox, 0, 5);
			qtyBox.setSpacing(5);
			
			int selectedIndex = hoodieLV.getSelectionModel().getSelectedIndex();

            if (selectedIndex != -1) {
                Hoodie selectedHoodie = dataHoodie.get(selectedIndex);
                hoodieID = selectedHoodie.getHoodieID();
                hoodieName = selectedHoodie.getHoodieName();

                hoodieIDLbl.setText("Hoodie ID: " + selectedHoodie.getHoodieID());
                hoodieNameLbl.setText("Name: " + selectedHoodie.getHoodieName());
                hoodiePriceLbl.setText("Price: $" + selectedHoodie.getHoodiePrice());

                updateTotalPrice(selectedHoodie);
                
                //set visible false
                hoodieSelect.setVisible(false);
                //set visible true
            	hoodieIDLbl.setVisible(true);
        		hoodieNameLbl.setVisible(true);
        		hoodiePriceLbl.setVisible(true);
        		hoodieQuantityLbl.setVisible(true);
                totalPriceLbl.setVisible(true);
                addToCartBtn.setVisible(true);
                //set disablenya false
                hoodieNameLbl.setDisable(false);
        		hoodiePriceLbl.setDisable(false);
        		hoodieQuantityLbl.setDisable(false);
        		totalPriceLbl.setDisable(false);
        		addToCartBtn.setDisable(false);
            }
		});
		
		qtySpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
	    updateTotalPrice(dataHoodie.get(hoodieLV.getSelectionModel().getSelectedIndex()));
	    });
	}	
	
	public void successAlert() {
		qty = qtySpinner.getValue();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText("Message");
		alert.setContentText(String.format("%s - %s - %dx added", hoodieID, hoodieName, qty));
		ButtonType okBtn = new ButtonType("OK");
		alert.showAndWait().ifPresent(response -> {
	        if (response == okBtn) {
	            alert.close();
	        }
	    });
	}
	
	public void cartToDB(String userID, String hoodieID, int qty) {
		qty = qtySpinner.getValue();
		String query = "SELECT * FROM cart";
		con.setResultSet(con.selectData(query));
		try {
			while(con.getResultSet().next()) {
				String userIDGet = con.getResultSet().getString("UserID");
				String hoodieIDGet = con.getResultSet().getString("HoodieID");
				if(userIDGet.equals(userID)&&hoodieIDGet.equals(hoodieID)) {
					updateCart(userID, hoodieID, qty);
					return;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		query = "INSERT INTO cart VALUES ('"+userID+"', '"+hoodieID+"', '"+qty+"')";
		con.executeUpdate(query);
	}
	
	//Update cart (Dipakai ketika quantity item cart bertambah)
	public void updateCart(String userID, String hoodieID, int qty) {
		String query = String.format
				("UPDATE cart SET quantity = quantity + '"+qty+"' WHERE UserID = '"+userID+"' AND HoodieID = '"+hoodieID+"'");
	con.executeUpdate(query);
	}
	
	private void updateTotalPrice(Hoodie selectedHoodie) {
	    int selectedQty = qtySpinner.getValue(); 
	    double total = selectedHoodie.getHoodiePrice() * selectedQty;
	    String formattedTotal = String.format("%.2f", total);
	    totalPriceLbl.setText("Total Price: " + formattedTotal);
	}
	
	public HomePage(Stage stage, String userID) {
		// TODO Auto-generated constructor stub
		initialize();
		styling();
		menuHandling();
		this.stage = stage;
		stage.setScene(scn);
		stage.show();
		this.stage.setTitle("hO-Ohdie");
		this.userID = userID;
		cartHandling(userID);		
	}
	

}
