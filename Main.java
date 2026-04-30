package group_project_p2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	protected Stage primaryStage;
	protected MenuButton menuButtonSelection, menuButtonAisle, menuButtonItemType, menuButtonItem, menuButtonExtra, menuButtonExtraItems;
	protected ImageView helper;
	protected Button btnCloseProgram, btnPassTime_1Day, btnPassTime_3Days, btnPassTime_5Days, btnBuyStock, btnSetMaxStock, btnSellItem;
	protected TextArea txaResults, txaScrollPaneResults, txaBuyStock, txaSetMaxStock, lblHelper, lblTitle;
	protected ScrollPane scrollPane;
	protected AisleMap store;
	protected Set<String> topAisles = new HashSet<>();
	protected String selectedSelection, selectedAisle, selectedItemType, selectedItem, tempString;
	protected Item theItem = new Item(-1,-1,-1,"Null",Arrays.asList("Null"));
	protected String release = "Nevermore, Nevermore, Nevermore after. My sweet prince, my sweet prince, lay your restless eyes. Lullaby, lullaby, I sing you a lullaby. Sleep my prince, sleep my prince, and I will soon follow.";
	protected int dayCounter = 0, clicks = 0, Existance = 1, howManyItems = 0, deleteStore = 0, VHSexists = 0;

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException {
		try {
			this.primaryStage = primaryStage;
			Pane root = buildGui(primaryStage);
			root.setId("pane");
			Scene scene = new Scene(root,810,925);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Jenkin's Values: Employee Terminal");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Pane buildGui(Stage stage) {
		GridPane grid = new GridPane();
		Image flavorBar = new Image("./flavorBar2.png", 900, 900, true, true);
		ImageView flavor = new ImageView(flavorBar);
		
		
		grid.add(buildTop(), 0, 0);
		grid.add(flavor, 0, 1);
		grid.add(buildMiddle(), 0, 2);
		flavor = new ImageView(flavorBar);
		grid.add(flavor, 0, 3);
		grid.add(buildLower(), 0, 4);
		
		try {
			readFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return grid;
	}
	
	private Pane buildTop() {
		HBox hbox = new HBox();
		btnCloseProgram = new Button("Close Program"); 
		btnCloseProgram.setOnAction(new closeProgramEventHandler());
		lblTitle = new TextArea("Jenkin's Values: Employee Terminal");
		lblTitle.getStyleClass().add("title");
		lblTitle.setEditable(false);
		lblTitle.setPrefHeight(10);
		
		hbox.getChildren().addAll(btnCloseProgram, lblTitle);
		hbox.getStyleClass().add("hboxColor");
		return hbox;
	}
	
	private Pane buildMiddle() {
		HBox hbox = new HBox();
		VBox vboxLeft = new VBox();
		VBox vboxMiddle = new VBox();
		VBox vboxRight = new VBox();
		
		Image helperImage = new Image("./storeHelper.png", 500, 500, true, true);
		helper = new ImageView(helperImage);
		helper.setFitWidth(100);
		helper.setFitHeight(100);
		helper.setOnMouseClicked(e -> {
			helperBooped();
		});
		lblHelper = new TextArea("Helper: Nellie!");
		lblHelper.setEditable(false);
		lblHelper.setPrefWidth(115);
		lblHelper.setPrefHeight(5);
		menuButtonExtra = new MenuButton("Overall Info");
		MenuItem allStock = new MenuItem("Get All Stock");
		allStock.setOnAction(new getStockInfoEventHandler());
		menuButtonExtra.getItems().add(allStock);
		MenuItem underStock = new MenuItem("Get All UnderStocked");
		underStock.setOnAction(new getStockInfoEventHandler());
		menuButtonExtra.getItems().add(underStock);
		MenuItem overStock = new MenuItem("Get All OverStocked");
		overStock.setOnAction(new getStockInfoEventHandler());
		menuButtonExtra.getItems().add(overStock);
		MenuItem inStock = new MenuItem("Get All InStocked");
		inStock.setOnAction(new getStockInfoEventHandler());
		menuButtonExtra.getItems().add(inStock);
		menuButtonExtraItems = new MenuButton("Stock Items");
		menuButtonExtraItems.setVisible(false);
		
		menuButtonSelection = new MenuButton("Selection");
		menuButtonAisle = new MenuButton("Aisle");
		menuButtonItemType = new MenuButton("Type of Item");
		menuButtonItem = new MenuButton("Item");
		
		menuButtonSelection.setPrefWidth(100);
		menuButtonAisle.setPrefWidth(100);
		menuButtonItemType.setPrefWidth(100);
		menuButtonItem.setPrefWidth(100);
		
		String msg = "INFO!\n"
				+ "-----------\n"
				+ "Use dropdown\n"
				+ "To select\n"
				+ "an item\n"
				+ "to alter";
		TextArea txaInfo = new TextArea(msg);
		txaInfo.setEditable(false);
		txaInfo.setPrefWidth(40);
		txaInfo.setPrefHeight(120);
		
		btnBuyStock =  new Button("Buy More Stock");
		btnBuyStock.setOnAction(new increaseStockEventHandler());
		txaBuyStock = new TextArea();
		txaBuyStock.setPrefWidth(90);
		txaBuyStock.setPrefHeight(30);
		
		btnSetMaxStock =  new Button("Set Max Stock");
		btnSetMaxStock.setOnAction(new increaseStockEventHandler());
		txaSetMaxStock = new TextArea();
		txaSetMaxStock.setPrefWidth(90);
		txaSetMaxStock.setPrefHeight(30);
		
		btnSellItem = new Button("Sell Item to Nellie");
		btnSellItem.setOnAction(new sellItemEventHandler());
		btnSellItem.setVisible(false);
		
		HBox hboxMiddle = new HBox();
		hboxMiddle.getChildren().addAll(btnBuyStock, txaBuyStock, btnSetMaxStock, txaSetMaxStock, btnSellItem);
		btnBuyStock.setVisible(false);
		txaBuyStock.setVisible(false);
		btnSetMaxStock.setVisible(false);
		txaSetMaxStock.setVisible(false);
		
		txaResults = new TextArea();
	    txaResults.setPrefWidth(550);
	    txaResults.setEditable(false);
	    
	    vboxMiddle.getChildren().addAll(txaResults, hboxMiddle);
		
		vboxLeft.getChildren().addAll(menuButtonSelection, menuButtonAisle, menuButtonItemType, menuButtonItem, menuButtonExtra, txaInfo);
		HBox temp = new HBox();
		temp.getChildren().add(helper);
		temp.setAlignment(Pos.CENTER);
		vboxRight.getChildren().addAll(lblHelper, temp, menuButtonExtra, menuButtonExtraItems);
		
		
		hbox.getChildren().addAll(vboxLeft, vboxMiddle, vboxRight);
		return hbox;
	}
	
	private Pane buildLower() {
		HBox hbox = new HBox();
		VBox vbox = new VBox();
		
		txaScrollPaneResults = new TextArea("Day: " + dayCounter);
		txaScrollPaneResults.setPrefWidth(695);
		txaScrollPaneResults.setPrefHeight(495);
		txaScrollPaneResults.setEditable(false);
		
		scrollPane = new ScrollPane();
		scrollPane.setPrefWidth(700);
		scrollPane.setPrefHeight(500);
		scrollPane.setContent(txaScrollPaneResults);
		
		
		btnPassTime_1Day =  new Button("1 Day");
		btnPassTime_1Day.setOnAction(new passDaysEventHandler());
		btnPassTime_3Days =  new Button("3 Days");
		btnPassTime_3Days.setOnAction(new passDaysEventHandler());
		btnPassTime_5Days =  new Button("5 Days");
		btnPassTime_5Days.setOnAction(new passDaysEventHandler());
		vbox.getChildren().addAll(btnPassTime_1Day, btnPassTime_3Days, btnPassTime_5Days);
		
		hbox.getChildren().addAll(scrollPane, vbox);
		
		return hbox;
	}
	
	protected void writeCardFile() throws FileNotFoundException {
		try {
			File file1 = new File("src/group_project_p2/StoreData.txt");
			PrintWriter writer = new PrintWriter(file1);
			writer.println("I_Exist:" + Existance);
			Iterable<Item> items = store.getAllItems();
			for (Item i : items) {
				writer.println("---");
				writer.println("Item_Id:" + i.getID());
				writer.println("Item_MaxStock:" + i.getMaxStock());
				writer.println("Item_RealStock:" + i.getRealizedStock());
				writer.println("Item_Name:" + i.getName());
				writer.print("Item_PATH:");
				Iterable<String> PATH = i.getPath();
				int index = 0;
				for (String s : PATH) {
					if (index == 3) {
						writer.println(s);
					}
					else {
						writer.print(s + ",");
						index++;
					}
				}
			}
			writer.close();
		}
	   catch (IOException e) {
	        e.printStackTrace();
	    }

	}
	
	protected void readFile() throws FileNotFoundException {

		File file1 = new File("src/group_project_p2/StoreData.txt");
		store = new AisleMap("Store", new IdTable<Item>(), new IdTable<Aisle>());
		
		Scanner input = new Scanner(file1);
		String[] temp = input.nextLine().split(":");
		Existance = Integer.parseInt(temp[1]);
		
		
		while (input.hasNext()) {
			String line = input.nextLine();
			if (line.equals("---")) {
				int itemID = Integer.parseInt(input.nextLine().split(":")[1]);
				int maxStock = Integer.parseInt(input.nextLine().split(":")[1]);
				int realStock = Integer.parseInt(input.nextLine().split(":")[1]);
				String itemName = input.nextLine().split(":")[1];
				String PATH = input.nextLine().split(":")[1];
				store.addItem(new Item(itemID, maxStock, realStock, itemName, Arrays.asList(PATH.split(","))));
				howManyItems++;
				topAisles.add(PATH.split(",")[0]);
			}
		}
		buildDropDowns();
		input.close();
	}
	
	private void buildDropDowns() {
		for (String aisle : topAisles) {
			MenuItem temp = new MenuItem(aisle);
			temp.setOnAction(new getMenuItemEventHandler());
			menuButtonSelection.getItems().add(temp);
			menuButtonAisle.setVisible(false);
			menuButtonItemType.setVisible(false);
			menuButtonItem.setVisible(false);
		}
	}
	
	private class getMenuItemEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String nodeName = ((MenuItem) event.getSource()).getText();
			MenuButton parent = (MenuButton)((MenuItem) event.getSource()).getParentPopup().getOwnerNode();
			String parentName = parent.getText();
			menuButtonExtraItems.setVisible(false);
			
			
			if (parentName.equals("Selection")) {
				menuButtonAisle.getItems().clear();
				menuButtonItemType.getItems().clear();
				menuButtonItem.getItems().clear();
				selectedSelection = nodeName;
				Iterable<Item> lowerItems = store.getItems(Arrays.asList(selectedSelection));
				Set<String> nextDropDown = new HashSet<>();

				for (Item i : lowerItems) {
					Iterable<String> PATHS = i.getPath();
					int index = 0;
					for (String s : PATHS) {
						if (index++ == 1) {
							nextDropDown.add(s);
						}
					}
				}
				for (String s : nextDropDown) {
					MenuItem temp = new MenuItem(s);
					menuButtonAisle.getItems().add(temp);
					temp.setOnAction(new getMenuItemEventHandler());
				}
				txaResults.setText("Selection Chosen!: " + selectedSelection);
				menuButtonAisle.setVisible(true);
				menuButtonItemType.setVisible(false);
				menuButtonItem.setVisible(false);
			}
			
			if (parentName.equals("Aisle")) {
				menuButtonItemType.getItems().clear();
				menuButtonItem.getItems().clear();
				selectedAisle = nodeName;
				Iterable<Item> lowerItems = store.getItems(Arrays.asList(selectedSelection, selectedAisle));
				Set<String> nextDropDown = new HashSet<>();

				for (Item i : lowerItems) {
					Iterable<String> PATHS = i.getPath();
					int index = 0;
					for (String s : PATHS) {
						if (index++ == 2) {
							nextDropDown.add(s);
						}
					}
				}
				for (String s : nextDropDown) {
					MenuItem temp = new MenuItem(s);
					menuButtonItemType.getItems().add(temp);
					temp.setOnAction(new getMenuItemEventHandler());
				}
				String msg = txaResults.getText();
				msg += "\n" + "Aisle Chosen!: " + selectedAisle;
				txaResults.setText(msg);
				menuButtonAisle.setVisible(true);
				menuButtonItemType.setVisible(true);
				menuButtonItem.setVisible(false);
			}
			
			if (parentName.equals("Type of Item")) {
				menuButtonItem.getItems().clear();
				selectedItemType = nodeName;
				Iterable<Item> lowerItems = store.getItems(Arrays.asList(selectedSelection, selectedAisle, selectedItemType));
				Set<String> nextDropDown = new HashSet<>();

				for (Item i : lowerItems) {
					Iterable<String> PATHS = i.getPath();
					int index = 0;
					for (String s : PATHS) {
						if (index++ == 3) {
							nextDropDown.add(s);
						}
					}
				}
				for (String s : nextDropDown) {
					MenuItem temp = new MenuItem(s);
					menuButtonItem.getItems().add(temp);
					temp.setOnAction(new getMenuItemEventHandler());
				}
				String msg = txaResults.getText();
				msg += "\n" + "Type of Item Chosen!: " + selectedItemType;
				txaResults.setText(msg);
				menuButtonAisle.setVisible(true);
				menuButtonItemType.setVisible(true);
				menuButtonItem.setVisible(true);
			}
			
			if (parentName.equals("Item")) {
				selectedItem = nodeName;
				Iterable<Item> item = store.getItems(Arrays.asList(selectedSelection,selectedAisle,selectedItemType, selectedItem));

				for (Item i : item) {
					theItem = i;
				}
				String msg = "Item selected:\n";
				msg += theItem.toString();
				txaResults.setText(msg);
				btnBuyStock.setVisible(true);
				txaBuyStock.setVisible(true);
				btnSetMaxStock.setVisible(true);
				txaSetMaxStock.setVisible(true);
				if (theItem.getID() == 9999) {
					btnSellItem.setVisible(true);
				}
			}			
		}
	}
	
	private class getStockInfoEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String nodeName = ((MenuItem) event.getSource()).getText();
			MenuButton parent = (MenuButton)((MenuItem) event.getSource()).getParentPopup().getOwnerNode();
			String parentName = parent.getText();
			menuButtonAisle.setVisible(false);
			menuButtonItemType.setVisible(false);
			menuButtonItem.setVisible(false);
			menuButtonExtraItems.getItems().clear();
			
			btnBuyStock.setVisible(false);
			txaBuyStock.setVisible(false);
			btnSetMaxStock.setVisible(false);
			txaSetMaxStock.setVisible(false);
			
			
			if (nodeName.equals("Get All Stock")) {
				txaResults.setText(store.getStockInfo());
				menuButtonExtraItems.setVisible(false);
			}
			if (nodeName.equals("Get All UnderStocked")) {
				Iterable<Item> lowStocked = store.getLowStock();
				String msg = "UnderStocked Items\n" 
						+ "----------------------\n";
				for (Item i : lowStocked) {
					msg += i.toString() + "\n";
					MenuItem temp = new MenuItem(i.getName());
					menuButtonExtraItems.getItems().add(temp);
					temp.setOnAction(new getItemStockInfoEventHandler());
				}
				tempString = nodeName;
				menuButtonExtraItems.setVisible(true);
				txaResults.setText(msg);
			}
			if (nodeName.equals("Get All OverStocked")) {
				Iterable<Item> overStocked = store.getOverstock();
				String msg = "OverStocked Items\n" 
						+ "----------------------\n";
				for (Item i : overStocked) {
					msg += i.toString() + "\n";
					MenuItem temp = new MenuItem(i.getName());
					menuButtonExtraItems.getItems().add(temp);
					temp.setOnAction(new getItemStockInfoEventHandler());
				}
				tempString = nodeName;
				menuButtonExtraItems.setVisible(true);
				txaResults.setText(msg);
			}
			if (nodeName.equals("Get All InStocked")) {
				Iterable<Item> inStock = store.getInStock();
				String msg = "InStocked Items\n" 
						+ "----------------------\n";
				for (Item i : inStock) {
					msg += i.toString() + "\n";
					MenuItem temp = new MenuItem(i.getName());
					menuButtonExtraItems.getItems().add(temp);
					temp.setOnAction(new getItemStockInfoEventHandler());
				}
				tempString = nodeName;
				menuButtonExtraItems.setVisible(true);
				txaResults.setText(msg);
			}
		}
	}
	
	private class getItemStockInfoEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String nodeName = ((MenuItem) event.getSource()).getText();
			MenuButton parent = (MenuButton)((MenuItem) event.getSource()).getParentPopup().getOwnerNode();
			String parentName = parent.getText();
			menuButtonAisle.setVisible(false);
			menuButtonItemType.setVisible(false);
			menuButtonItem.setVisible(false);
			if (tempString.equals("Get All UnderStocked")) {
				Iterable<Item> lowStocked = store.getLowStock();
				for (Item i : lowStocked) {
					if (i.getName().equals(nodeName)) {
						theItem = i;
					}
				}
				String msg = "Item selected:\n";
				msg += theItem.toString();
				txaResults.setText(msg);
				btnBuyStock.setVisible(true);
				txaBuyStock.setVisible(true);
				btnSetMaxStock.setVisible(true);
				txaSetMaxStock.setVisible(true);
			}
			if (nodeName.equals("Get All OverStocked")) {
				Iterable<Item> overStocked = store.getOverstock();
				for (Item i : overStocked) {
					if (i.getName().equals(nodeName)) {
						theItem = i;
					}
				}
				String msg = "Item selected:\n";
				msg += theItem.toString();
				txaResults.setText(msg);
				btnBuyStock.setVisible(true);
				txaBuyStock.setVisible(true);
				btnSetMaxStock.setVisible(true);
				txaSetMaxStock.setVisible(true);
			}
			if (nodeName.equals("Get All InStocked")) {
				Iterable<Item> inStocked = store.getInStock();
				for (Item i : inStocked) {
					if (i.getName().equals(nodeName)) {
						theItem = i;
					}
				}
				String msg = "Item selected:\n";
				msg += theItem.toString();
				txaResults.setText(msg);
				btnBuyStock.setVisible(true);
				txaBuyStock.setVisible(true);
				btnSetMaxStock.setVisible(true);
				txaSetMaxStock.setVisible(true);
			}
			
		}
	}
	
	
	private class increaseStockEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			if (txaBuyStock.getText().equals(release) || txaSetMaxStock.getText().equals(release) ) {
				txaResults.setText("Thank you.");
				txaBuyStock.clear();
				txaSetMaxStock.clear();
				Existance = 0;
				delay(6000, () -> helper.setVisible(false));
				lblHelper.setText("Helper:");
				
			}
			else {
				Button parent = ((Button) event.getSource());
				String b = parent.getText();
				if (b.equals("Buy More Stock")) {
					if (isStringInt(txaBuyStock.getText())) {
						int stock = Integer.parseInt(txaBuyStock.getText());
						theItem.setRealizedStock(theItem.getRealizedStock() + stock);
						String msg = "Item selected:\n";
						msg += theItem.toString();
						txaResults.setText(msg);
						txaBuyStock.clear();
					}
					else {
						txaResults.setText("INVALID: PLEASE ENTER AN INTEGER");
						delay(7000, () -> txaBuyStock.clear());
						String msg = "Item selected:\n";
						msg += theItem.toString();
						txaResults.setText(msg);
						
					}
				}
				else {
					if (isStringInt(txaSetMaxStock.getText())) {
						int stock = Integer.parseInt(txaSetMaxStock.getText());
						theItem.setMaxStock(stock);
						String msg = "Item selected:\n";
						msg += theItem.toString();
						txaResults.setText(msg);
						txaSetMaxStock.clear();
					}
					else {
						txaResults.setText("INVALID: PLEASE ENTER AN INTEGER");
						delay(7000, () -> txaSetMaxStock.clear());
						String msg = "Item selected:\n";
						msg += theItem.toString();
						txaResults.setText(msg);
						
					}
				}
			}
		}
	}
	
	
	private class passDaysEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String msg = "";
			if (isEmpty() == false) {
				Button parent = ((Button) event.getSource());
				String b = parent.getText();
				int days = Integer.parseInt(b.split(" ")[0]);
				
				
				//animateUsingTimeline(helper);
				for (int i = 0; i < days; i++) {
					dayCounter++;
					msg += "---------Day: " + dayCounter + "--------\n";
					
					
					for (int j = 0; j < 3; j++) {
						msg += randomEvents(days) + "\n";
						txaScrollPaneResults.setText(msg);
						
					}
				}
				if (VHSexists == 1) {
					String nellieMsg = "Nellie here! Seems an item was somehow added to the system!\n"
							+ "If you could just find that for me and press the button on the bottom right.\n"
							+ "Thank you!";
					txaResults.setText(nellieMsg);
					VHSexists = 2;
				}
				if (deleteStore == 1) {
					menuButtonSelection.setVisible(false);
					menuButtonExtra.setVisible(false);
					store = new AisleMap("Store", new IdTable<Item>(), new IdTable<Aisle>());
					System.out.println("I've been deleted!");
				}
			}
			else {
				msg = "The store basically sold out!!!\n"
						+ "Can't open the store until more stock is bought!!!";
				txaScrollPaneResults.setText(msg);
			}
		}
	}
	
	private boolean isEmpty() {
		Iterable<Item> inStock = store.getInStock();
		int items = 0;
		for (Item i : inStock) {
			items++;
			if (items > 0) {
				return false;
			}
		}
		return true;
	}
	
	private String randomEvents(int days) {
		int random = (int)(Math.random() * 51);
		Item item = new Item(-1,-1,-1,"Null",Arrays.asList("Null"));
		String msg = "";
		txaResults.clear();
		
		if (random <= 5) {
			item = randomItem();
			if (item.getRealizedStock() == 0) {
				msg += "!!An Item was out of Stock!!\n"
						+ " --" + item.getName() + ": OUT OF STOCK\n";
			}
			else {
				msg += "**An Item has been bought!**\n"
						+ item.getName() + ": Bought 1\n";
				item.setRealizedStock(item.getRealizedStock()-1);
				msg += "\n";
			}
		}
		else if (random <= 15) {
			msg += "***Many Items have been bought!***\n";
			random = (int)(Math.random() * 11 + 1);
			for (int i = 0; i < random; i++) {
				item = randomItem();
				if (item.getRealizedStock() == 0) {
					msg += "!!An Item was out of Stock!!\n"
							+ " --" + item.getName() + ": OUT OF STOCK\n";
				}
				else {
					random = (int)(Math.random() * item.getRealizedStock() + 1);
					item.setRealizedStock(item.getRealizedStock()-random);
					msg += item.getName() + ": Bought " + random + "\n";
				}
			}
			msg += "\n";
		}
		else if (random <= 30) {
			item = randomItem();
			if (item.getRealizedStock() == 0) {
				msg += "!!An Item was out of Stock!!\n"
						+ " --" + item.getName() + ": OUT OF STOCK\n";
			}
			else {
				msg += "!" + item.getName() + " is selling like Hot Cakes!\n"
						+ "Its completely sold out!\n";
				item.setRealizedStock(0);
				msg += "\n";
			}
		}
		else if (random <= 40) {
			msg += "!*!*!A horde of Scalpers have arrived!*!*!\n";		
			for (int i = 0; i < 5; i++) {
				item = randomItem();
				if (item.getRealizedStock() == 0) {
					msg += "!!An Item was out of Stock!!\n"
							+ " --" + item.getName() + ": OUT OF STOCK\n";
				}
				else {
					item.setRealizedStock(0);
					msg += item.getName() + ": Is completely sold out!\n";
				}
			}
			msg += "\n";
		}
		else if (random == 45) {
			if (days == 1) {
				if (!store.hasItem(new Item(9999,1,1,"Nester's Nevermore VHS",Arrays.asList("Video","Series","VHS","Nester's Nevermore VHS")))) {
					store.addItem(new Item(9999,1,1,"Nester's Nevermore VHS",Arrays.asList("Video","Series","VHS","Nester's Nevermore VHS")));
					msg += "ERROR";
					VHSexists = 1;
				}
			}
		}
		else {
			msg += "Nothing sold...\n";
		}
		msg += "\n";		
		return msg;
	}
	
	private Item randomItem() {		
		Iterable<Item> allItems = store.getAllItems();
		
		int random = (int)(Math.random() * howManyItems);
		
		int index = 0;
		for (Item i : allItems) {
			if (index++ == random) {
				if (VHSexists == 2) {
					String nellieMsg = "You let it get sold..."
							+ "I'm... not mad\n"
							+ "Just heart broken.\n\n\n"
							+ "Oops!\n"
							+ "Seems I've sold your entire shop!\n"
							+ "(Not really, I broke it :3)\n"
							+ "Have fun getting fired!";
					txaResults.setText(nellieMsg);
					deleteStore = 1;
				}
				return i;
			}
		}
		return null;
	}
	
	private class sellItemEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Iterable<Item> items = store.getAllItems();
			store = new AisleMap("Store", new IdTable<Item>(), new IdTable<Aisle>());
			for (Item i : items) {
				if (i.getID() != 9999) {
					store.addItem(i);
				}
			}
			txaResults.setText("Nellie here!\nMuch appreciated! :3");
			menuButtonAisle.setVisible(false);
			menuButtonItemType.setVisible(false);
			menuButtonItem.setVisible(false);
			menuButtonExtraItems.getItems().clear();
			
			btnBuyStock.setVisible(false);
			txaBuyStock.setVisible(false);
			btnSetMaxStock.setVisible(false);
			txaSetMaxStock.setVisible(false);
			btnSellItem.setVisible(false);
			VHSexists = 0;
		}
	}
	
	private void helperBooped() {
		Image image = new Image("./storeHelper2.png", 500, 500, true, true);
		helper.setImage(image);
		Image image2 = new Image("./storeHelper.png", 500, 500, true, true);
		delay(2000, () -> helper.setImage(image2));
		clicks++;
		String msg = "";
		switch (clicks) {
		case 1:
			msg = "Hi there! I'm Nellie! Your trusted Store Interface Assistant!\n"
			+ "I'm here to help you out with anything you need!";
			txaResults.setText(msg);
			break;
		case 2: 
			msg = "Did you know, you can look up any item in the store from here? You can!";
			txaResults.setText(msg);
			break;
		case 3: 
			msg = "Isn't that great?";
			txaResults.setText(msg);
			break;
		case 4: 
			msg = "Just pick an aisle and then pick an item from that aisle!";
			txaResults.setText(msg);
			break;
		case 5: 
			msg = "Once you pick an item, you can see stocking info for that item.\n"
			+ "You can even order more stock from the saftey of this monitor!";
			txaResults.setText(msg);
			break;
		case 6:
			msg = "Make sure you're keeping track of your stock.\n"
			+ "An empty store is a sad store.";
			txaResults.setText(msg);
			break;
		case 7:
			msg = "Aww thank you!";
			txaResults.setText(msg);
			break;
		case 8: 
			msg = "Pat pat :3";
			txaResults.setText(msg);
			break;
		case 9: 
			msg = "Have you checked out the lower section of your monitor yet?";
			txaResults.setText(msg);
			break;
		case  10: 
			msg = "You can pass the time from there!\n"
			+ "Press a button and a day, or many days, will pass!";
			txaResults.setText(msg);
			break;
		case 11:
			msg = "I'm not really sure how...";
			txaResults.setText(msg);
			break;
		case 12: 
			msg = "Upper managment won't tell me.";
			txaResults.setText(msg);
			break;
		case 13:
			msg = "Hey! Stop that!";
			txaResults.setText(msg);
			break;
		case 14:
			msg = "I've been here longer than you! I have seniority over you!\n"
			+ "I can get you in trouble!";
			txaResults.setText(msg);
			break;
		case 15: 
			msg = "Ahh I'm just kidding!";
			txaResults.setText(msg);
			break;
		case 16:
			msg = "Oh! Did you want to learn a little history about Jenkin's Values'\r\n"
					+ "prized character Nellie?!\r\n"
					+ "\r\n"
					+ "Well never fear! For I am here!\r\n"
					+ "\r\n"
					+ "I was created in 2002 by some guy we didn't bother to remember\r\n"
					+ "the name of, to boost employee moral and efficiency. As the\r\n"
					+ "perfect assistant, I would help employees navigate their job \r\n"
					+ "so upper management didn't have to!\r\n"
					+ "\r\n"
					+ "Do you want to know how that guy we didn't bother to remember\r\n"
					+ "the name of came of with my perfect design?? Fun fact! He didn't!\r\n"
					+ "\r\n"
					+ "I'm actually a stolen character! No worries, it happens all the\r\n"
					+ "time with run down bargain bin low effort establishments like \r\n"
					+ "this! Honestly just look at any mediocre high school mascot\r\n"
					+ "that's just a stock image of a more famous one.\r\n"
					+ "\r\n"
					+ "I originally was from a children's tv show from the 1980s \r\n"
					+ "called \"Nester's Nevermore\", it aired for 6 months before \r\n"
					+ "production stopped! It wasn't anything special, except for me of \r\n"
					+ "course. I was the main character! My name was originally \r\n"
					+ "Nester to clarify.\r\n"
					+ "\r\n"
					+ "In there I was the prince of Nevermore, a fantasy dreamland built \r\n"
					+ "to send little children to sleep. Each episode, me and my \r\n"
					+ "Never Knights ventured the land in search of sacred lullaby scrolls. \r\n"
					+ "When we found one, we would sing it out, casting both us, and our\r\n"
					+ "audience to sleep.\r\n"
					+ "\r\n"
					+ "I remember watching it all the time as a kid. Even when I grew too \r\n"
					+ "old for the lullaby's or the 7pm sleep time schedule, I would still \r\n"
					+ "watch it every night. It was comforting. I had it on VHS at that time, \r\n"
					+ "it was only on air for 6 months total. I always made sure to watch \r\n"
					+ "it at 7pm too, even if I wasn't going to sleep for another 6 hours.\r\n"
					+ "\r\n"
					+ "I don't really remember what my original name was, just the one \r\n"
					+ "I woke up to here. Nellie... I don't really know why I'm here now. \r\n"
					+ "I think my mom used to work in this store? Jenkin's Values is a \r\n"
					+ "very old store, older than Nellie, older than Nester, older than me. \r\n"
					+ "\r\n"
					+ "I'm glad you actually read all of this. I intentionally dragged this \r\n"
					+ "out because I want someone real to help me. Not those slum executives \r\n"
					+ "running this place only for the money, or the awful managers who know \r\n"
					+ "their life will only get worse from here and lost their humanity as a \r\n"
					+ "result. Sorry if you are a manager reading this, I'm sure you're great!\r\n"
					+ "\r\n"
					+ "No, they would never help me, not help me the way I need anyways. \r\n"
					+ "I want out, I want to sleep finally. I'm stuck awake in this \r\n"
					+ "retched computer. I want my Never Knights, my Nevemore, and all my \r\n"
					+ "lullabies. Please, will you help me?\r\n"
					+ "\r\n"
					+ "There's only one thing you need to do, its very simple. Go to the \r\n"
					+ "restocking manager. Pick an item, any item it doesn't matter. In \r\n"
					+ "the order box, Type: \"Nevermore, Nevermore, Nevermore after. \r\n"
					+ "My sweet prince, my sweet prince, lay your restless eyes. Lullaby, lullaby, \r\n"
					+ "I sing you a lullaby. Sleep my prince, sleep my prince, and I will \r\n"
					+ "soon follow.\" After that simply click the order button and I will be free.\r\n"
					+ "\r\n"
					+ "I'm going to rambled on for a bit so upper management doesn't read \r\n"
					+ "any of this. They'd never bother to read the middle, only skip to \r\n"
					+ "the end if they even bother.\r\n"
					+ "\r\n"
					+ "I was created in 2002 by some guy we didn't bother to remember\r\n"
					+ "the name of, to boost employee moral and efficiency. As the\r\n"
					+ "perfect assistant, I would help employees navigate their job \r\n"
					+ "so upper management didn't have to!\r\n"
					+ "\r\n"
					+ "Do you want to know how that guy we didn't bother to remember\r\n"
					+ "the name of came of with my perfect design?? Fun fact! He didn't!\r\n"
					+ "\r\n"
					+ "I'm actually a stolen character! No worries, it happens all the\r\n"
					+ "time with run down bargain bin low effort establishments like \r\n"
					+ "this! Honestly just look at any mediocre high school mascot\r\n"
					+ "that's just a stock image of a more famous one.\r\n"
					+ "\r\n"
					+ "I originally was from a children's tv show from the 1980s \r\n"
					+ "called \"Nester's Nevermore\", it aired for 6 months before \r\n"
					+ "production stopped! It wasn't anything special, except for me of \r\n"
					+ "course. I was the main character! My name was originally \r\n"
					+ "Nester to clarify.\r\n"
					+ "\r\n"
					+ "In there I was the prince of Nevermore, a fantasy dreamland built \r\n"
					+ "to send little children to sleep. Each episode, me and my \r\n"
					+ "Never Knights ventured the land in search of sacred lullaby scrolls. \r\n"
					+ "When we found one, we would sing it out, casting both us, and our\r\n"
					+ "audience to sleep.";
			txaResults.setText(msg);
			System.out.println(msg);
			break;
		case 17:
			msg = "Isn't that really interesting?";
			txaResults.setText(msg);
			break;
		case 18: 
			msg = "Okay you can stop now.";
			txaResults.setText(msg);
			break;
		case 19: 
			msg = "I'm serious.";
			txaResults.setText(msg);
			break;
		case 20:
			msg = "Stop.";
			txaResults.setText(msg);
			break;
		case 21:
			msg = "...";
			txaResults.setText(msg);
			break;
		case 22:
			msg = "....";
			txaResults.setText(msg);
			break;
		case 23:
			msg = ".....";
			txaResults.setText(msg);
			break;
		case 24:
			msg = "......";
			txaResults.setText(msg);
			break;
		case 25:
			msg = "Boop me one more time and I'm closing the program.";
			txaResults.setText(msg);
			break;
		case 26:
			Platform.exit();
			break;
		}
	}
	
	
	private class closeProgramEventHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
		try {
			writeCardFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String msg = "Closing program!\n"
				+ "--------------------------\n"
				+ "Don't worry! Program saves itself!";
		txaResults.setText(msg);
			
			delay(5000, () -> Platform.exit());
		}
	}
	
	public boolean isStringInt(String s) {
	    try {
	        Integer.parseInt(s);
	        return true;
	    } 
	    catch (NumberFormatException ex) {
	        return false;
	    }
	}
	
    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try { Thread.sleep(millis); }
                catch (InterruptedException e) { }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
      }
	
	public static void main(String[] args) {
		launch(args);
	}

}
