package editProduct;

import java.sql.SQLException;

import db.Connect;
import isiDB.Hoodie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class EditPage {
	Connect con = Connect.getInstance();
	
	//Attribute yang mau diedit
	String hoodieID;
	String hoodieName;
	double hoodiePrice;
	
	Stage stage;
	Scene scn;
	BorderPane bp;
	
	MenuBar menuBar;
	Menu menuAccount;
	MenuItem logoutMI;
	
	Menu menuAdmin;
	MenuItem editProduct;
	
	Label editTitle;
	Label updateDeleteTitle;
	Label selectItem;
	Label hoodieIDLbl;
	Label hoodieNameLbl;
	Label hoodiePriceLbl;
	Label insertHoodieTitle;
	Label nameLbl;
	Label priceLbl;
	
	Font titleFont;
	Font smallerTitleFont;
	Font btnFont;
	Font otherFont;
	
	Button updatePriceBtn, deleteHoodieBtn, insertBtn;
	TextField updatePriceTF, nameTF, priceTF;
	
	TableView<Hoodie> hoodieTV;
	TableColumn<Hoodie, String> hoodieIDColumn;
	TableColumn<Hoodie, Double> hoodiePriceColumn;
	TableColumn<Hoodie, String> hoodieNameColumn;
	ObservableList<Hoodie> hoodieInfo;
	
	public void initialize() {
		titleFont = Font.font("Times New Roman", FontPosture.ITALIC, 52);
		smallerTitleFont = Font.font("Calibri", FontWeight.BOLD, 32);
		btnFont = Font.font("Times New Roman", FontWeight.BOLD, 14);
		otherFont = Font.font("Times New Roman", 14);
		
		editTitle = new Label("Edit Product");
		editTitle.setFont(titleFont);
		updateDeleteTitle = new Label("Update & Delete Hoodie(s)");
		updateDeleteTitle.setFont(smallerTitleFont);
		selectItem = new Label("Select an item from the list");
		hoodieIDLbl = new Label();
		hoodieIDLbl.setFont(otherFont);
		hoodieNameLbl = new Label();
		hoodieNameLbl.setFont(otherFont);
		hoodiePriceLbl = new Label();
		hoodiePriceLbl.setFont(otherFont);
		insertHoodieTitle = new Label("Insert Hoodie");
		insertHoodieTitle.setFont(smallerTitleFont);
		nameLbl = new Label("Name: ");
		priceLbl = new Label("Price: ");
		
		menuBar = new MenuBar();
		menuAccount = new Menu("Account");
		logoutMI = new MenuItem("Logout");
		menuAdmin = new Menu("Admin");
		editProduct = new MenuItem("Edit Product");
		menuAccount.getItems().add(logoutMI);
		menuAdmin.getItems().add(editProduct);
		menuBar.getMenus().addAll(menuAccount, menuAdmin);
		
		updatePriceBtn = new Button("Edit Price");
		updatePriceBtn.setFont(btnFont);
		deleteHoodieBtn = new Button("Delete Hoodie");
		deleteHoodieBtn.setFont(btnFont);
		insertBtn = new Button("Insert");
		insertBtn.setFont(btnFont);
		
		updatePriceTF = new TextField();
		updatePriceTF.setPromptText("Input price");
		nameTF = new TextField();
		nameTF.setPromptText("Input name");
		priceTF = new TextField();
		priceTF.setPromptText("Input price");
		
		hoodieTV = new TableView<>();
		hoodieIDColumn = new TableColumn<>("Hoodie ID");
		hoodieIDColumn.setPrefWidth(150);
		hoodieNameColumn = new TableColumn<>("Hoodie Name");
		hoodieNameColumn.setPrefWidth(150);
		hoodiePriceColumn = new TableColumn<>("Hoodie Price");
		hoodiePriceColumn.setPrefWidth(150);
		hoodieTV.setPrefHeight(600);
		
		hoodieIDColumn.setCellValueFactory(new PropertyValueFactory<Hoodie, String>("hoodieID"));
		hoodieNameColumn.setCellValueFactory(new PropertyValueFactory<Hoodie, String>("hoodieName"));
		hoodiePriceColumn.setCellValueFactory(new PropertyValueFactory<Hoodie, Double>("hoodiePrice"));
	
		hoodieTV.getColumns().addAll(hoodieIDColumn, hoodieNameColumn, hoodiePriceColumn);
		hoodieInfo = giveHoodieInfos();
		hoodieTV.setItems(hoodieInfo);
		
	}
	
	public void styling() {
		bp = new BorderPane();
		bp.setTop(menuBar);
		
		HBox titleBox = new HBox();
		titleBox.getChildren().add(editTitle);
		VBox tableBox = new VBox();
		tableBox.getChildren().addAll(hoodieTV);
		
		HBox updatePriceBox = new HBox();
		updatePriceBox.getChildren().addAll(hoodiePriceLbl, updatePriceTF);
		updatePriceBox.setSpacing(10);
		HBox updDelBox = new HBox();
		updDelBox.getChildren().addAll(updatePriceBtn, deleteHoodieBtn);
		updDelBox.setSpacing(10);
		
		GridPane gpUpdateNDelete = new GridPane();
		gpUpdateNDelete.add(updateDeleteTitle, 0, 0);
		gpUpdateNDelete.add(selectItem, 0, 1);
		gpUpdateNDelete.add(hoodieIDLbl, 0, 1);
		gpUpdateNDelete.add(hoodieNameLbl, 0, 2);
		gpUpdateNDelete.add(updatePriceBox, 0, 3);
		gpUpdateNDelete.add(updDelBox, 0, 4);
		gpUpdateNDelete.setVgap(10);
		gpUpdateNDelete.setHgap(10);
		//set visible false
		hoodieIDLbl.setVisible(false);
		hoodieNameLbl.setVisible(false);
		hoodiePriceLbl.setVisible(false);
		updatePriceTF.setVisible(false);
		deleteHoodieBtn.setVisible(false);
		updatePriceBtn.setVisible(false);
		
		HBox insertNameBox = new HBox();
		insertNameBox.getChildren().addAll(nameLbl, nameTF);
		insertNameBox.setSpacing(5);
		HBox insertPriceBox = new HBox();
		insertPriceBox.getChildren().addAll(priceLbl, priceTF);
		insertPriceBox.setSpacing(10);
		HBox insertBtnBox = new HBox();
		//komponen biar rata antara textfields serta button
		Label space = new Label("");
		insertBtnBox.getChildren().addAll(space, insertBtn);
		insertBtnBox.setSpacing(42);
		
		GridPane gpInsertHoodie = new GridPane();
		gpInsertHoodie.add(insertHoodieTitle, 0, 0);
		gpInsertHoodie.add(insertNameBox, 0, 1);
		gpInsertHoodie.add(insertPriceBox, 0, 2);
		insertBtn.setPrefSize(150, 20);
		gpInsertHoodie.setVgap(10);
		gpInsertHoodie.setHgap(20);
		
		VBox insertHoodieBox = new VBox();
		insertHoodieBox.getChildren().addAll(gpInsertHoodie, insertBtnBox);
		insertHoodieBox.setSpacing(10);
		VBox combineRight = new VBox();
		combineRight.getChildren().addAll(gpUpdateNDelete, insertHoodieBox);
		combineRight.setSpacing(20);
		
		HBox combineLeftRight = new HBox();
		combineLeftRight.getChildren().addAll(tableBox, combineRight);
		combineLeftRight.setAlignment(Pos.CENTER);
		combineLeftRight.setSpacing(50);
		
		GridPane combineAll = new GridPane();
		combineAll.add(titleBox, 0, 0);
		combineAll.add(combineLeftRight, 0, 1);
		combineAll.setAlignment(Pos.CENTER);
		
		bp.setCenter(combineAll);
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
		editProduct.setOnAction(e ->{
			try {
				new EditPage(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		hoodieTV.setOnMouseClicked(e->{
			int selectedIndex = hoodieTV.getSelectionModel().getSelectedIndex();
			
			if(selectedIndex != -1) {
				Hoodie selectedHoodie = hoodieInfo.get(selectedIndex);
				hoodieID = selectedHoodie.getHoodieID();
				hoodieName = selectedHoodie.getHoodieName();
				
				hoodieIDLbl.setText("Hoodie ID: " + hoodieID);
				hoodieNameLbl.setText("Name: " + hoodieName);
				hoodiePriceLbl.setText("Price: ");
				
				hoodieIDLbl.setVisible(true);
				hoodieNameLbl.setVisible(true);
				hoodiePriceLbl.setVisible(true);
				updatePriceTF.setVisible(true);
				deleteHoodieBtn.setVisible(true);
				updatePriceBtn.setVisible(true);
				selectItem.setVisible(false);
			}
		});
		
		updatePriceBtn.setOnMouseClicked(e->{
			int selectedIndex = hoodieTV.getSelectionModel().getSelectedIndex();
			
			if(selectedIndex != -1) {
				Hoodie selectedHoodie = hoodieInfo.get(selectedIndex);
				hoodieID = selectedHoodie.getHoodieID();
				
				String newPriceText = updatePriceTF.getText();
				if(!updatePriceTF.getText().isEmpty()) {
				if (!newPriceText.matches("\\d*\\.?\\d*")) {
				    alertTemplate("Please enter numbers only");
				    return;
				}
				Double newPrice = Double.parseDouble(newPriceText);
				
				//update query
				String query = "UPDATE hoodie SET HoodiePrice = '"+newPrice+"' WHERE HoodieID = '"+hoodieID+"'";
		        con.executeUpdate(query);

				selectedHoodie.setHoodiePrice(newPrice);
				
				hoodieTV.refresh();
				hoodieTV.getSelectionModel().clearSelection();	
				} else {
					alertTemplate("Must insert textfield");
				}
			}
			
		});
		
		deleteHoodieBtn.setOnMouseClicked(e->{
			int selectedIndex = hoodieTV.getSelectionModel().getSelectedIndex();
			
			if(selectedIndex != -1) {
				Hoodie selectedHoodie = hoodieInfo.get(selectedIndex);
				hoodieID = selectedHoodie.getHoodieID();
				hoodieInfo.remove(selectedIndex);
				
				//delete query
				String query = "DELETE FROM hoodie WHERE HoodieID = '"+hoodieID+"'";
				con.executeUpdate(query);
				hoodieTV.refresh();
			}
		});
		
		insertBtn.setOnMouseClicked(e->{
			String hoodieName = nameTF.getText();
			String priceText = priceTF.getText();
			if(!nameTF.getText().isEmpty() && !priceTF.getText().isEmpty()) {
			if (!priceText.matches("\\d*\\.?\\d*")) {
			    alertTemplate("Must enter numbers only");
			    return;
			}
			Double hoodiePrice = Double.parseDouble(priceText);
			String hoodieID = generateHoodieID();
			
			Hoodie dataHoodie = new Hoodie(hoodieID, hoodieName, hoodiePrice);
			hoodieInfo.add(dataHoodie);
			String query = "INSERT INTO hoodie VALUES('"+hoodieID+"','"+hoodieName+"','"+hoodiePrice+"')";
			con.executeUpdate(query);
			
			//buat tf clear
			nameTF.clear();
			priceTF.clear();
			hoodieTV.refresh();
			} else {
				alertTemplate("Must insert text field");
			}
		});
	}
	
	public String generateHoodieID() {
		String query = "SELECT HoodieID FROM hoodie";
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
		
		String hoodieID = String.format("HO%03d", ++plusOne);
		return hoodieID;
	}
	
	public ObservableList<Hoodie> giveHoodieInfos(){
		ObservableList<Hoodie> hoodieInfo = FXCollections.observableArrayList();
		String query = "SELECT * FROM hoodie";
		con.setResultSet(con.selectData(query));
		
		try {
			while(con.getResultSet().next()) {
				hoodieID = con.getResultSet().getString("HoodieID");
				hoodieName = con.getResultSet().getString("HoodieName");
				hoodiePrice = con.getResultSet().getDouble("HoodiePrice");
				
				Hoodie hoodiedata = new Hoodie(hoodieID, hoodieName, hoodiePrice);
				hoodieInfo.add(hoodiedata);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hoodieInfo;
	}
	
	private void alertTemplate(String message) {
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle("Invalid Input");
	    alert.setHeaderText("Error");
	    alert.setContentText(message);
	    ButtonType okBtn = new ButtonType("OK");
		
	    alert.showAndWait().ifPresent(response -> {
	        if (response == okBtn) {
	            alert.close();
	        }
	    });
	}
	
	
	
	public EditPage(Stage stage) {
		initialize();
		styling();
		menuHandling();
		this.stage = stage;
		stage.show();
		stage.setScene(scn);
		this.stage.setTitle("hO-Ohdie");

	}
}
