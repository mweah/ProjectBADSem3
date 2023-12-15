package registerPage;

import java.sql.SQLException;
import db.Connect;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
	
	public void initialize() {
		bp = new BorderPane();
		gp = new GridPane();
		vbox = new VBox();
		
		registerLabel = new Label("Register");
		emailLabel = new Label("Email: ");
		usernameLabel = new Label("Username: ");
		passwordLabel = new Label("Password: ");
		confirmPwLabel = new Label("Confirm Password: ");
		phoneNumberLabel = new Label("Phone Number: ");
		genderLabel = new Label("Gender: ");
		addressLabel = new Label("Address: ");
		agreeTnC = new Label("I Agree to Term & Conditions");
		
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
			String Gender = null;
			RadioButton selectedRadioButton = (RadioButton) genderGroup.getSelectedToggle();
		      if (selectedRadioButton != null) {
		        Gender = selectedRadioButton.getText();
		    }
			String Address = addressTA.getText();
			String Role = "User";
			
		    boolean isBoxChecked = tncCheckBox.isSelected();
		    
			insertDataToDB(Email, Username, Password, PhoneNumber, Address, Gender, Role);
			try {
				new LoginPage().start(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}
	
	public RegisterPage(Stage stage) {
		initialize();
		styling();
		menuHandling();
		this.stage = stage;
		this.stage.setScene(scn);
		this.stage.show();
	}

}
