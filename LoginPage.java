package loginPage;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import registerPage.RegisterPage;

import java.sql.SQLException;

import db.Connect;
import homePage.HomePage;
import isiDB.User;

public class LoginPage extends Application{
	
	Connect con = Connect.getInstance();
	
	Stage stage;
	Scene scn;
	BorderPane bp;
	GridPane gp;
	
	VBox vbox;
	HBox hbox;
	
	Label loginLabel;
	Label usernameLabel;
	Label passwordLabel;
	
	TextField usernameField;
	PasswordField pwField;
	
	Button loginButton;
	
	MenuBar logRegMenuBar;
	Menu logRegMenu;
	MenuItem logRegMI;
	
	Font font;

	public LoginPage() {
		// TODO Auto-generated constructor stub
	}
	
	public void initialize() {
		loginLabel = new Label("Login");
		usernameLabel = new Label("Username: ");
		passwordLabel = new Label("Password: ");
		
		font = new Font(56);
		loginLabel.setFont(font);
		
		usernameField = new TextField();
		pwField = new PasswordField();
		
		loginButton = new Button("Login");
		
		gp = new GridPane();
		bp = new BorderPane();
		vbox = new VBox();
		hbox = new HBox();
		
		logRegMenuBar = new MenuBar();
		logRegMenu = new Menu("Login");
		logRegMI = new Menu("Register");
		
		logRegMenuBar.getMenus().add(logRegMenu);
		logRegMenu.getItems().addAll(logRegMI);
		
		bp.setTop(logRegMenuBar);
	}
	
	public void styling() {
		vbox.getChildren().add(loginLabel);
		vbox.setAlignment(Pos.CENTER);
		
		gp.add(vbox, 0, 0);
		gp.add(usernameLabel, 0, 2);
		gp.add(passwordLabel, 0, 3);
		
		gp.add(usernameField, 1, 2);
		gp.add(pwField, 1, 3);
		usernameField.setPromptText("Input Username");
		pwField.setPromptText("Input Password");
		
		gp.add(loginButton, 1, 4);
		loginButton.setPrefSize(150, 20);
		
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);
		
		bp.setCenter(gp);
		scn = new Scene(bp, 700, 700);
	}
	
	public boolean accountValidation(String usernameCheck, String passwordCheck) throws SQLException {
		boolean accountValid = false;
		String query = "SELECT user.Username, user.Password FROM user";
		
		con.setResultSet(con.selectData(query));
		
		try {
			while(con.getResultSet().next()) {
				String Username = con.getResultSet().getString("Username");
				String Password = con.getResultSet().getString("Password");
				
				accountValid = Username.equals(usernameCheck) && Password.equals(passwordCheck);
				if(accountValid) break;
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accountValid;
		
	}
	
	public String checkRole(String Username) {
		String query = "SELECT user.Username, user.Role FROM user";
		String Role = "";
		
		con.setResultSet(con.selectData(query));
		
		try {
			while(con.getResultSet().next()) {
				String cekUsername = con.getResultSet().getString("Username");
				String cekRole = con.getResultSet().getString("Role");
				if(cekUsername.equals(Username)) {
					Role = cekRole;
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Role;
	}
	
	public void errorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText("Credential doesn't exist or Wrong");
		
		ButtonType okBtn = new ButtonType("OK");
		
		alert.showAndWait().ifPresent(response -> {
	        if (response == okBtn) {
	            alert.close();
	        }
	    });
	}

	public void menuHandling() {
		logRegMI.setOnAction(e->{
			try {
			new RegisterPage(stage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		loginButton.setOnMouseClicked(e -> {
			String usernameCheck = usernameField.getText();
			String passwordCheck = pwField.getText();
			try {
				if(accountValidation(usernameCheck, passwordCheck)) {
					if(checkRole(usernameCheck).equals("User")) {
					new HomePage(stage);
					System.out.println("Mantap anda user");
					}
					else if(checkRole(usernameCheck).equals("Admin")) {
						System.out.println("Anda admin omg");
					}
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}
	

	@Override
	public void start(Stage s) throws Exception {
		// TODO Auto-generated method stub
		initialize();
		styling();
		menuHandling();
		this.stage = s;
		this.stage.setScene(scn);
		this.stage.show();
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
