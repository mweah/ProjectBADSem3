package registerPage;

import java.sql.SQLException;
import db.Connect;
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

public class RegisterPage {
	
	Connect con = Connect.getInstance();
	
	Stage stage;
	Scene scn;
	BorderPane bp;
	GridPane gp;
	VBox vbox;
	HBox hbox;
	HBox hbox1;
	Label registerLabel;
	Label emailLabel;
	Label usernameLabel;
	Label passwordLabel;
	Label confirmPwLabel;
	Label phoneNumberLabel;
	Label genderLabel;
	Label addressLabel;
	Label agreeTnC;
	
	Font font;
	Font otherFont;
	
	Button regisButton;
	
	ToggleGroup genderGroup;
	RadioButton maleButton;
	RadioButton femaleButton;
	
	CheckBox tncCheckBox;
	
	MenuBar logRegMenuBar;
	Menu logRegMenu;
	MenuItem regMenuMI;
	
	TextField emailTF;
	TextField usernameTF;
	PasswordField passwordPF;
	PasswordField confirmPwPF;
	TextField phoneNumberTF;
	TextArea addressTA;
	
	//Attribute utk register
	String Email;
	String Username;
	String Password;
	String ConfirmPW;
	String PhoneNumber;
	String Gender;
	String Address;
	String Role;
	boolean AgreeBox;
	
	public void initialize() {
		bp = new BorderPane();
		gp = new GridPane();
		vbox = new VBox();
		
		otherFont = Font.font("Calibri", FontWeight.BOLD, 14);
		registerLabel = new Label("Register");
		emailLabel = new Label("Email: ");
		usernameLabel = new Label("Username: ");
		passwordLabel = new Label("Password: ");
		confirmPwLabel = new Label("Confirm Password: ");
		phoneNumberLabel = new Label("Phone Number: ");
		genderLabel = new Label("Gender: ");
		addressLabel = new Label("Address: ");
		agreeTnC = new Label("I Agree to Term & Conditions");
		
		registerLabel.setFont(otherFont);
		emailLabel.setFont(otherFont);
		usernameLabel.setFont(otherFont);
		passwordLabel.setFont(otherFont);
		confirmPwLabel.setFont(otherFont);
		phoneNumberLabel.setFont(otherFont);
		genderLabel.setFont(otherFont);
		addressLabel.setFont(otherFont);
		
		regisButton = new Button("Register");
		
		font = Font.font("Helvetica", FontWeight.BOLD,FontPosture.ITALIC, 36);
		registerLabel.setFont(font);
		
		logRegMenuBar = new MenuBar();
		logRegMenu = new Menu("Register");
		regMenuMI = new MenuItem("Login");
		logRegMenu.getItems().addAll(regMenuMI);
		logRegMenuBar.getMenus().add(logRegMenu);
		bp.setTop(logRegMenuBar);
		
		genderGroup = new ToggleGroup();
		maleButton = new RadioButton("Male");
		femaleButton = new RadioButton("Female");
		maleButton.setToggleGroup(genderGroup);
		femaleButton.setToggleGroup(genderGroup);
		
		hbox = new HBox();
		hbox.getChildren().addAll(maleButton, femaleButton);
		hbox.setSpacing(20);
		
		emailTF = new TextField();
		usernameTF = new TextField();
		passwordPF = new PasswordField();
		confirmPwPF = new PasswordField();
		phoneNumberTF = new TextField();
		addressTA = new TextArea();
		addressTA.setWrapText(true);
		
		emailTF.setPromptText("Input hoodie Email");
		usernameTF.setPromptText("Input an Unique Username");
		passwordPF.setPromptText("Input Password");
		confirmPwPF.setPromptText("Confirm Password");
		phoneNumberTF.setPromptText("Input phone Number. Start with +62");
		addressTA.setPromptText("Input Address");
		
		tncCheckBox = new CheckBox();
		hbox1 = new HBox();
		hbox1.getChildren().addAll(tncCheckBox, agreeTnC);
		hbox1.setSpacing(10);
	}
	
	public void styling() {
		vbox.getChildren().add(registerLabel);
		vbox.setAlignment(Pos.CENTER_LEFT);
		
		gp.setHgap(10);
		gp.setVgap(10);

		gp.add(vbox, 0, 0);
		gp.add(emailLabel, 0, 1);
	    gp.add(emailTF, 0, 2, 2, 1);
	    gp.add(usernameLabel, 0, 3);
	    gp.add(usernameTF, 0, 4, 2, 1);
	    gp.add(passwordLabel, 0, 5);
	    gp.add(passwordPF, 0, 6, 2, 1);
	    gp.add(confirmPwLabel, 0, 7);
	    gp.add(confirmPwPF, 0, 8, 2, 1);
	    gp.add(phoneNumberLabel, 0, 9);
	    gp.add(phoneNumberTF, 0, 10, 2, 1);
	    gp.add(genderLabel, 0, 11);
	    gp.add(hbox, 0, 12);
	    hbox.setSpacing(20);
	    gp.add(addressLabel, 0, 13);
	    gp.add(addressTA, 0, 14);
	    gp.add(hbox1, 0, 15);
	    gp.add(regisButton, 0, 16);
	    gp.setAlignment(Pos.CENTER);
	    bp.setCenter(gp);
	    scn = new Scene(bp, 1200, 800);
	}
	
	public String generateUID() {
		String query = "SELECT UserID FROM user";
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
		
		String plusUID = String.format("US%03d", ++plusOne);
		return plusUID;
		
	}
	
	public void insertDataToDB(String Email, String Username, String Password, String PhoneNumber, String Address, String Gender, String Role) {
	    String query = String.format("INSERT INTO user "
				+ "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
	            generateUID(), Email, Username, Password, PhoneNumber, Address, Gender, Role);

	    con.executeUpdate(query);
	}
	
	public void alertTemplate(String errorContent) {
		Alert alert = new Alert(AlertType.ERROR);
		
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText(errorContent);
		
		ButtonType okBtn = new ButtonType("OK");
		
		alert.showAndWait().ifPresent(response -> {
	        if (response == okBtn) {
	            alert.close();
	        }
	    });
	}

	
	public void menuHandling() {
		regMenuMI.setOnAction(e -> {
			try {
				new LoginPage().start(stage);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		});
		
		regisButton.setOnMouseClicked(e ->{
			String Email = emailTF.getText();
			String Username = usernameTF.getText();
			String Password = passwordPF.getText();
			String ConfirmPW = confirmPwPF.getText();
			String PhoneNumber = phoneNumberTF.getText();
		    String Gender = getGender(genderGroup.getSelectedToggle());
			String Address = addressTA.getText();
			Role = "User";
			
		    AgreeBox = tncCheckBox.isSelected();
		    
		  
		    
		    //validasi pastikan ga ada kosong
		    if(Email.isEmpty()&&Username.isEmpty()&&Password.isEmpty()&& ConfirmPW.isEmpty()&&PhoneNumber.isEmpty()&&Gender.isEmpty()
		    		&&Address.isEmpty()) {
		    	alertTemplate("Form must be filled");
		    	return;
		    } else if(Email.isEmpty()) {
		    	alertTemplate("Email must be filled");
		    	return;
		    } else if(Username.isEmpty()) {
		    	alertTemplate("Username must be filled");
		    	return;
		    } else if(Password.isEmpty()) {
		    	alertTemplate("Password must be filled");
		    	return;
		    } else if(ConfirmPW.isEmpty()) {
		    	alertTemplate("Confirm password must be filled");
		    	return;
		    } else if(PhoneNumber.isEmpty()) {
		    	alertTemplate("Phone number must be filled");
		    	return;
		    } else if(Gender.isEmpty()) {
		    	alertTemplate("You must choose gender");
		    	return;
		    } else if(Address.isEmpty()) {
		    	alertTemplate("Address must be filled");
		    	return;
		    } else if(!AgreeBox) {
		    	alertTemplate("You must check the box as agreement");
		    	return;
		    }
		    
		    //validasi yang lain	
		    if(!isUsernameUnique(Username)) {
		    	alertTemplate("Username must be unique");
		    	return;
		    }
		    
		    if(!Email.endsWith("@hoohdie.com")) {
		    	alertTemplate("Email must ends with '@hoohdie.com!'");
		    	return;
		    }
		    
		    if(Password.length() < 5) {
		    	alertTemplate("Password must contain minimal 5 characters");
		    	return;
		    }
		    
		    if(!ConfirmPW.equals(Password)) {
		    	alertTemplate("Confirm password must be the same as Password");
		    	return;
		    }
		    
		    if(!PhoneNumber.startsWith("+62")) {
		    	alertTemplate("Phone number must stars with +62");
		    	return;
		    }
		    
		    if(PhoneNumber.length()!=14) {
		    	alertTemplate("Phone number length must be 14 characters");
		    	return;
		    }
		    
			insertDataToDB(Email, Username, Password, PhoneNumber, Address, Gender, Role);
			try {
				new LoginPage().start(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}
	
	public String getGender(Toggle toggle) {
	    RadioButton selectedRadioButton = (RadioButton) toggle;
	    if (selectedRadioButton != null) {
	        return selectedRadioButton.getText();
	    }
	    return "";
	}

	public boolean isUsernameUnique(String Username) {
		String query = "SELECT * FROM user WHERE Username = '"+Username+"'";
		con.setResultSet(con.selectData(query));
		
		try {		//kalau result setnya dapat kosong, berarti username unique
			return !con.getResultSet().next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public RegisterPage(Stage stage) {
		initialize();
		styling();
		menuHandling();
		this.stage = stage;
		this.stage.setScene(scn);
		this.stage.setTitle("hO-Ohdie");
		this.stage.show();
	}

}
