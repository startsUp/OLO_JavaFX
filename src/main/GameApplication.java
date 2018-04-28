package main;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import gameObjects.Disk;
import gameObjects.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class GameApplication extends Application {

	private static Stage window;
	private static Scene mainScene;
	private static Scene mainBoard;
	
	private static Player player1;
	private static Player player2;
	
	private static ArrayList<Disk> disks;
	private static Disk selectedDiskOne;
	private static Disk selectedDiskTwo;

	public static void main(String[] args) {
		launch(args);
  }

	public void start(Stage primaryStage) {
		window= primaryStage;

		disks = new ArrayList<>();

		mainScene = main();
		player1 = new Player(Color.ANTIQUEWHITE, Color.ANTIQUEWHITE);
		player2 = new Player(Color.YELLOW, Color.YELLOW);
		window.setScene(mainScene);
		window.show();

		selectedDiskOne = new Disk("Large", Color.ALICEBLUE);
		selectedDiskTwo = new Disk("Large", Color.ALICEBLUE);
	}

	public Scene main(){
		BorderPane pane = new BorderPane();
		pane.setId("main");
		pane.setPrefSize(1400,900);

		pane.setLeft(colorPicker("1"));
		pane.setRight(colorPicker("2"));

		//pane.getChildren().addAll(disk1,disk2);
		VBox vbox = new VBox();
		Button play = new Button("Play");
		Button instruct = new Button("Instruction");
		play.setId("play");
		instruct.setId("play");
		play.setOnAction(e->{
			Board m = new Board(player1, player2, disks);
			mainBoard = m.getMainBoard();
			window.setScene(mainBoard);
		});

		instruct.setOnAction(e->window.setScene(instructScene()));
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		vbox.getChildren().addAll(play,instruct);
		pane.setCenter(vbox);
		Scene s=  new Scene(pane);
		s.getStylesheets().add("AppCSS.css");
		return s;
	}

	public Scene instructScene(){
		ScrollPane pane = new ScrollPane();
		BorderPane bPane = new BorderPane();
		
		bPane.setId("scroll");
		VBox v = new VBox();
		v.setAlignment(Pos.CENTER);
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(20,20,20,50));
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(60);
		grid.setHgap(170);
	
		grid.add(getLabelInstruction(0),  0,  0);
		grid.add(getVisualInstruction(0), 0,  1);
		grid.add(getLabelInstruction(1),  0,  2);
		grid.add(getVisualInstruction(1), 0,  3);
		grid.add(getLabelInstruction(2),  0,  4);
		grid.add(getVisualInstruction(2), 0,  5);
		grid.add(getLabelInstruction(3),  0,  6);

		Button back = new Button("Back");
		back.setId("play");
		back.setLayoutX(1300);
		back.setLayoutY(100);
		
		back.setOnAction(e->window.setScene(mainScene));
		grid.add(back, 1, 0);
		
		v.getChildren().add(grid);
		bPane.setPrefSize(1400, 900);
		v.setAlignment(Pos.CENTER);
		pane.setContent(v);
		pane.setPrefSize(1400, 900);
		Scene scene = new Scene(pane);
		scene.getStylesheets().add("AppCSS.css");
		return scene;
	}
	
	public HBox getVisualInstruction(int num){
		HBox hb = new HBox();
		hb.setId("hBOX");
		ImageView imageView = new ImageView(getInsImage(num));
		imageView.setFitHeight(500);
		imageView.setFitWidth(800);
		hb.getChildren().add(imageView);
		hb.setMaxSize(805, 560);
		hb.setAlignment(Pos.CENTER);
		return hb;
	}
	public Image getInsImage(int num){
		Image[] images = new Image[4];
		images[0] = new Image("file:/C:/Shardool/ICS3U1/Game_FinalProject/images/firstIns.png");
		images[1] = new Image("file:/C:/Shardool/ICS3U1/Game_FinalProject/images/secondIns.png");
		images[2] = new Image("file:/C:/Shardool/ICS3U1/Game_FinalProject/images/thirdIns.png");
		return images[num];
	}
	public Label getLabelInstruction(int num){
		
		ArrayList<String> instructions = new ArrayList<>();
		String one = "The game starts with each player having six disks to shoot each turn. When its your turn, "
				+ "\nPerform a flick motion on your disk to shoot it with a velocity.";
		instructions.add(one);
		
		String two = "Your goal is to get as many disks as possible to you opponents (colored) area. You can do that by either shooting your "
				+ "\ndisk directly to opponent's area or by strategically colliding your disk so one or more disk end up in opponent's area."
				+ "\nEach disk in opponent's area will reward you one point.";
		instructions.add(two);
		
		String third = "Be careful when shooting your disk! If your disk ends up in a player's shooting area, the disk is removed from the"
				+ "\nboard and is available for that player for future turn.";
		instructions.add(third);
	
		String fourth = "Good Luck and Have Fun !!";
		instructions.add(fourth);
		
		Label label = new Label(instructions.get(num));
		label.setId("text-Ins");
		
		
	//	label.setAlignment(Pos.CENTER);
		
		return label;
	}

	public BorderPane colorPicker(String num){
		BorderPane pane = new BorderPane();
		HBox hbBox = new HBox();
		pane.setId("scroll");
		Text text = new Text("Select Your Color");
		text.setId("pickColor");
		hbBox.setSpacing(10);
		hbBox.setPadding(new Insets(10));
		hbBox.getChildren().add(text);
		pane.setTop(hbBox);

		VBox vbox = new VBox();
		vbox.setId("allDisks");
		ScrollPane scroll = new ScrollPane();
		scroll.setMaxHeight(300);
		scroll.setId("scroll");
		if(num.equals("1")){
			HBox h =new HBox();
			h.setPrefWidth(20);
			pane.setLeft(h);
		}
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setVbarPolicy(ScrollBarPolicy.NEVER);
		for(Color color: getAllColors(num)){
			if(color.equals(Color.BLACK))
				continue;

			Disk disk = new Disk("Large", color);


			disk.setStroke(Color.BLACK);
			disk.setStrokeWidth(5);
			disk.setOnMouseClicked(e->pickedColor(disk,num));

			vbox.getChildren().add(disk);
		}
		scroll.setContent(vbox);
		pane.setCenter(scroll);


		return pane;
	}

	public void pickedColor(Disk disk, String num){
		if(num.equals("1"))
			selectedDiskOne.setStroke(Color.BLACK);
		else
			selectedDiskTwo.setStroke(Color.BLACK);


		if(disk.getColor().equals(Color.WHITE) || disk.getColor().equals(Color.MINTCREAM)|| disk.getColor().equals(Color.AZURE) || disk.getColor().equals(Color.ALICEBLUE)|| disk.getColor().equals(Color.WHITESMOKE))
			disk.setStroke(Color.BLUEVIOLET);
		else
			disk.setStroke(Color.MINTCREAM);

		if(num.equals("1")){
			selectedDiskOne =disk;
			player1 = new Player(disk.getColor(), disk.getColor());
		}
		else{
			selectedDiskTwo =disk;
			player2 = new Player(disk.getColor(), disk.getColor());
		}

	}
	public VBox colorP(){
		VBox vbox = new VBox();
		Disk disk;
		for(int i=0;i<255;i++){
			disk = new Disk("Large", Color.rgb(i,0,0));
			vbox.getChildren().add(disk);
		}
		return vbox;
	}
	public static void restart(){
		disks= new ArrayList<>();
		Color p1= player1.getColor();
		Color p2= player2.getColor();
		player1 = new Player(p1,p1);
		player2 = new Player(p2,p2);

		Board m = new Board(player1, player2, disks);
		window.setScene(m.getMainBoard());
	}

	public static ArrayList<Color> getAllColors(String num){
		ArrayList<Color> colors = new ArrayList<>();
		Field[] fields = Color.class.getFields();
		if(num.equals("1")){
			for(int i=1; i<fields.length; i++){
				String[] line = fields[i].toString().replace(".", " ").split(" ");
				Color color = Color.web(line[line.length-1]);
				colors.add(color);
			}
		}else{	
			for(int i=fields.length-1; i>0 ; i--){
				String[] line = fields[i].toString().replace(".", " ").split(" ");
				Color color = Color.web(line[line.length-1]);
				colors.add(color);
			}
		}
		return colors;
	}
	public static void updateScene(){
		Board m = new Board(player1, player2, disks);
		window.setScene(m.getMainBoard());
	}
}
