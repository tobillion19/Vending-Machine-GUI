import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollBar;
/*import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Labeled;
*/
/**
   A menu from the vending machine.
*/
public class VendingMachineGUI extends Application
{    
	private Scanner in;
   /**
      Runs the vending machine system.
      @param machine the vending machine
   */
   
   public void start(Stage primaryStage) throws IOException {
	VendingMachine machine = new VendingMachine();
	Coin[] coins = machine.turnCoinsIntoArray();
   Text text = new Text (40,40, "Vending Machine");
   Pane pane = new Pane(text);
   
   Button btShow = new Button ("Show products");
   Button btInsert = new Button("Insert coins");
   Button btBuy = new Button("Buy product");
   Button btLogin = new Button("Login");
   Button btQuit = new Button("Quit");
   
   HBox runBox = new HBox(btShow, btInsert, btBuy, btLogin, btQuit);
   runBox.setSpacing(10);
   runBox.setAlignment(Pos.CENTER);
   
   BorderPane borderPane = new BorderPane(pane);
   borderPane.setBottom(runBox);
   
   
   btShow.setOnAction(new EventHandler<ActionEvent>() {
	   @Override
	   public void handle(ActionEvent e) {
	   for (Product p : machine.getProductTypes())
               System.out.println(p);
	   }
   });
   
   btLogin.setOnAction(new EventHandler<ActionEvent>()   {
	   @Override 
	   public void handle(ActionEvent e)  {
		   try {
			boolean check = machine.operatorLogIn();
			if(check) {
			   OperatorRun(machine, primaryStage);
			} 
		   }
		   catch(IOException ex) {
		   }
	   }
   });
   
   btInsert.setOnAction(new EventHandler<ActionEvent>() {
	   @Override
	   public void handle(ActionEvent e) {
		   machine.addCoin((Coin) getChoice(coins));
	   }
   });
   
   btBuy.setOnAction(new EventHandler<ActionEvent>() {
	   @Override
	   public void handle(ActionEvent e) {
		   try
            {
               Product p = (Product) getChoice(machine.getProductTypes());
			   try{
               machine.buyProduct(p);
               System.out.println("Purchased: " + p);
			   }
			   catch (IOException ex) {
			   }
            }
            catch (VendingException ex)
            {
               System.out.println(ex.getMessage());
            }
		
	   }
   });
   
	btQuit.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent e) {
			System.exit(1);
		}
	});
	
	Scene MainScene = new Scene(borderPane, 500,100);
   primaryStage.setTitle("Vending Machine");
   primaryStage.setScene(MainScene);
   primaryStage.show();
	}
	
	public void OperatorRun(VendingMachine machine, Stage primaryStage) throws IOException {
		in = new Scanner(System.in);
		Text text = new Text(40,40, "Operator Functions");
		Pane pane = new Pane(text);
		Stage opStage = new Stage();
		
		Button btLogout = new Button("Logout");
		Button btAdd = new Button("Add product");
		Button btRemove = new Button("Remove Coins");
		Button btRestock = new Button("Restock product");
		
		HBox opBox = new HBox(btLogout, btAdd, btRemove, btRestock);
		opBox.setSpacing(10);
		opBox.setAlignment(Pos.CENTER);
		
		BorderPane opPane = new BorderPane(pane);
		opPane.setBottom(opBox);

		Scene operatorScene = new Scene(opPane, 400,100);
		opStage.setTitle("Operator menu");
		opStage.setScene(operatorScene);
		opStage.show();
		
		btAdd.setOnAction(new EventHandler<ActionEvent>() {
	   @Override
	   public void handle(ActionEvent e) {
			addProductStage(machine);
			}	 
   });
		btRestock.setOnAction(new EventHandler<ActionEvent>() { //add a selection box and a number box
		@Override
		public void handle(ActionEvent e) {
			try{
			Product p = (Product) getChoice(machine.getProductTypes()); //NEEDS FIXING
			 System.out.println("How many items of this product are you adding?");
			 int amount = Integer.parseInt(in.nextLine());
			 machine.restockProduct(p, amount);
			}
			catch(IOException ex) {
			}
		}
	});
   
		btRemove.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try{
			System.out.println("Removed: " + machine.removeCoins());
				}
				catch(IOException ex) {
				}
	   }
   });
   
		btLogout.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent e) {
			opStage.close();
		}
   });
	}

   private Object getChoice(Object[] choices)
   {
      while (true)
      {
         char c = 'A';
         for (Object choice : choices)
         {
            System.out.println(c + ") " + choice.toString()); 
            c++;
         }
         String input = in.nextLine();
         int n = input.toUpperCase().charAt(0) - 'A';
         if (0 <= n && n < choices.length)
            return choices[n];
      }      
   }
   
   public void addProductStage(VendingMachine machine) {
			Stage addStage = new Stage();
			
			GridPane grid = new GridPane();
			grid.setPadding(new Insets(10,10,10,10));
			grid.setVgap(5);
			grid.setHgap(5);
			
			final TextField tName = new TextField();
			tName.setPromptText("Enter the name of the product");
			tName.setPrefColumnCount(10);
			tName.getText();
			grid.setConstraints(tName, 0, 1);
			grid.getChildren().add(tName);
			
			final TextField tPrice = new TextField();
			tPrice.setPromptText("Enter the price of the product");
			tPrice.setPrefColumnCount(10);
			tPrice.getText();
			grid.setConstraints(tName, 0, 2);
			grid.getChildren().add(tPrice);
			
			final TextField tQuantity = new TextField();
			tQuantity.setPromptText("Enter the quantity of the product");
			tQuantity.setPrefColumnCount(10);
			tQuantity.getText();
			grid.setConstraints(tQuantity, 0, 3);
			grid.getChildren().add(tQuantity);
			
			Button submit = new Button("Submit");
			grid.setConstraints(submit, 1, 2);
			grid.getChildren().add(submit);	
			
			Button back = new Button("Back");
			grid.setConstraints(back, 1, 3);
			grid.getChildren().add(back);
			
			Scene addScene = new Scene(grid, 400,200);
			addStage.setTitle("Add Product");
			addStage.setScene(addScene);
			addStage.show();
			
			submit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try{
					final String name = tName.getText();
					final Double price = Double.parseDouble(tPrice.getText());
					final int quantity = Integer.parseInt(tQuantity.getText());
					machine.addProduct(new Product(name, price), quantity);
					}
					catch(IOException ex) {
					}
				}
			});
			
			back.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					addStage.close();
				}
			});
					
   }
}

