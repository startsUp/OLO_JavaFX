package main;



import java.util.ArrayList;

import action.Physics;
import action.ShootAction;
import gameObjects.Disk;
import gameObjects.Player;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class Board{

	private Player player1, player2;
	private BorderPane board;
	private Pane boardPane;
	private double count;
	private double count2;
	private String playerTurn;
	private ArrayList<Disk> disks;
	private static Color primaryColor;


	public Board(Player player1, Player player2, ArrayList<Disk> disks) {
		this.player1= player1;
		this.player2= player2;
		this.disks  = disks;
		board = new BorderPane();
		boardPane = new Pane();
		primaryColor = Color.BLACK;
		updateScore(disks);
		//board.setPrefSize(1400, 900);
		board.setTop(null);
		board.setBottom(null);

		if(Physics.getPlayerTurn().equals("p1"))
			playerTurn="p1";
		else
			playerTurn="p2";		


		if(player1.getDisks().size()==0 && playerTurn.equals("p1"))
			boardPane.getChildren().add(gameOverPane());
		else if(player2.getDisks().size()==0 && playerTurn.equals("p2"))
			boardPane.getChildren().add(gameOverPane());
		else{
			board.setLeft(playerBox(player1, primaryColor));
			board.setRight(playerBox(player2, primaryColor));
			board.setCenter(playerArea(player1,player2));


			boardPane = new Pane();
			boardPane.setPrefSize(1400, 900) ;
			boardPane.setMinSize(1400, 900);
			boardPane.getChildren().add(board);
			boardPane.setId("pane");
			fillBoard(boardPane, disks);
			//updateBoard(boardPane, disks);

		}
	}

	public BorderPane gameOverPane(){
		BorderPane pane = new BorderPane();
		pane.setId("main");
		pane.setPrefSize(1400, 900);
		Button restart= new Button("Restart");
		restart.setAlignment(Pos.CENTER);
		restart.setPrefSize(100, 40);
		restart.setLayoutY(700);
		restart.setId("play");
		restart.setOnAction(e->GameApplication.restart());
		GridPane grid= new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setMaxHeight(100);
		VBox hb = new VBox();
		hb.setSpacing(50);
		hb.setAlignment(Pos.CENTER);
		Text winner;
		if(player1.getScore()==player2.getScore())
			winner = new Text("IT'S A TIE.");
		else if(player1.getScore()>player2.getScore())
			winner = new Text("PLAYER 1 WINS!!!");
		else 
			winner = new Text("PLAYER 2 WINS!!!");

		grid.setLayoutY(400);
		grid.setVgap(50);
		//	grid.setLayoutX(650);
		winner.setId("text");
		winner.setTextAlignment(TextAlignment.CENTER);
		hb.getChildren().addAll(winner, restart);
		Image image = new Image("file:/C:/Shardool/ICS3U1/Game_FinalProject/images/Over.png");
		ImageView imageView = new ImageView(image);
		grid.add(imageView, 0,0);
		grid.add(hb, 0, 1);
		imageView.setLayoutX(600);
		imageView.setLayoutY(200);
		pane.setCenter(grid);

		return pane;
	}
	//add Disks here
	public Scene getMainBoard(){
		Scene main = new Scene(boardPane);
		main.getStylesheets().add("AppCSS.css");
		return main;
	}

	public BorderPane playerArea(Player player1, Player player2){
		BorderPane center = new BorderPane();
		center.setLeft(centerArea(player1));
		center.setRight(centerArea(player2));
		HBox h = new HBox();
		h.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, null, null)));
		h.setPrefSize(2, 900);
		center.setMinWidth(880);
		//center.setCenter(h);
		return center;
	}

	public void fillBoard(Pane mBoard, ArrayList<Disk> disks){

		for(Disk disk: disks){
			if(disk.isOnBoard()){
				mBoard.getChildren().add(disk);
			}

		}

	}
	public HBox centerArea(Player player){
		HBox hbox = new HBox();
		hbox.setBackground(new Background(new BackgroundFill(player.getPlayerAreaColor(), null, null)));
		hbox.setMinSize(450, 900);
		return hbox;
	}
	public BorderPane playerBox(Player player, Color primaryColor){
		BorderPane pB = new BorderPane();
		pB.setMinSize(250, 800);
		pB.setPrefSize(250, 900);


		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));
		grid.setHgap(5);
		grid.setAlignment(Pos.CENTER);
		int count=0;

		for(int i=0; i<player.getDisks().size(); i++){
			Disk disk = player.getDisk(i);
			disk.setRadius(disk.getRadius()/3);
			grid.add(disk, ++count, 0);

			//	disksLeft.getChildren().add(disk);
		}

		Button bt= new Button();
		bt.setPrefSize(30, 30);

		//	pB.setCenter(bt);
		pB.setTop(grid);
		pB.setBottom(scoreBox(player));

		pB.setBackground(new Background(new BackgroundFill(primaryColor, null, null)));
		animateDisk(grid, player);
		return pB;

	}

	//call when player turns starts
	public void animateDisk(GridPane p, Player player){
		if((player.equals(player2)&&Physics.getPlayerTurn().equals("p1")) || 
				(player.equals(player1)&&Physics.getPlayerTurn().equals("p2"))){

			return;
		}
		count=0;
		count2=0;
		Disk disk;
		if(Physics.getPlayerTurn().equals("p2"))
			disk = (Disk) p.getChildren().get(p.getChildren().size()-1);
		else
			disk = (Disk) p.getChildren().get(0);
		//	Node disk = p.getChildren().get(0);
		new AnimationTimer() {


			@Override
			public void handle(long now) {
				count+=.6;
				count2+=.04;

				double limit = disk.getTranslateX() + disk.getCenterX();
				double size = p.getChildren().size();

				if(limit<-40*(size-8)  || limit>40*(size-8)){
					p.getChildren().remove(disk);

					stop();
					addNewDisk(boardPane);

					return;

				}
				if(Physics.getPlayerTurn().equals("p1"))
					disk.setTranslateX(-count);
				if(Physics.getPlayerTurn().equals("p2"))
					disk.setTranslateX(count);
				//				disk.setScaleX(count2);
				//				disk.setScaleY(count2);

			}
		}.start();

	}
	public void updateScore(Player player){

	}

	public void addNewDisk(Pane boardPane){

		Disk disk;
		if(playerTurn.equals("p1")){
			disk = player1.getDisk(0);
			player1.getDisks().remove(0);
			disk.setCenterX(20);

		}else {
			disk  = player2.getDisk(player2.getDisks().size()-1);
			player2.getDisks().remove(player2.getDisks().size()-1);
			disk.setCenterX(1300);
		}
		disk.setCenterY(400);

		disk.setTranslateX(0);
		disk.setTranslateY(0);
		disk.setOnBoard(true);
		boardPane.getChildren().add(disk);
		//		disk.setStroke();
		disk.setStyle("-fx-stroke:"+ toRgb(primaryColor)+";"
				+ "-fx-stroke-width: 5;");
		disks.add(disk);
		count=0;
		count2=0;
		new AnimationTimer() {

			@Override
			public void handle(long now) {
				count+=0.03;
				if(playerTurn.equals("p1"))
					count2+=4-count;
				else
					count2-=4+count;

				if(playerTurn.equals("p1")){
					if((disk.getCenterX() + disk.getTranslateX()+50)>210){
						stop();
						disk.setCenterX(disk.getCenterX()+disk.getTranslateX());
						disk.setTranslateX(0);
						ShootAction.makeShootable(disk, disks);
						return;
					}
					disk.setTranslateX(count2);
				}
				else{
					if((disk.getCenterX() + disk.getTranslateX()+ 50)<1300){
						stop();
						disk.setCenterX(disk.getCenterX()+disk.getTranslateX());
						disk.setTranslateX(0);
						ShootAction.makeShootable(disk, disks);
						return;
					}
					disk.setTranslateX(count2);
				}

			}
		}.start();

	}

	private String toRgb(Color color) {

		String rgb= "rgb("
				+ toRgbNum(color.getRed())
				+ "," + toRgbNum(color.getGreen())
				+ "," + toRgbNum(color.getBlue())
				+ ")";


		return rgb;
	}

	private int toRgbNum(double d) {
		return (int) (d * 255);
	}

	public VBox playerGoalBox(){
		VBox pgB = new VBox();
		return pgB;
	}

	public boolean updateScore(ArrayList<Disk> disks){
		int p1=0,p2=0;
		ArrayList<Number> removeIndex = new ArrayList<>();


		System.out.println(disks.size());
		for(Disk disk: disks){
			double posX = disk.getCenterX();
			if(disk.isOnBoard() && posX>=250 && posX<700){ //posLX>= left hBox width
				p2++;
				disk.setColor(player2.getColor());
			}
			if(disk.isOnBoard() && posX<=1150 && posX>700){ //posLX>= left hBox width
				p1++;
				disk.setColor(player1.getColor());
			}
			if(disk.isOnBoard()){
				if(posX<250){
					disk.setOnBoard(false);
					disk.setColor(player1.getColor());
					System.out.println("Removed");
					player1.getDisks().add(disk);
					int num = disks.indexOf(disk);
					removeIndex.add(num);
				}
				else if(posX>1150){
					disk.setOnBoard(false);
					disk.setColor(player2.getColor());
					player2.getDisks().add(0, disk);
					int num = disks.indexOf(disk);
					removeIndex.add(num);
				}


			}
		}
		System.out.println(removeIndex.size());
		for(Number num: removeIndex){
			fadeIn(disks.get((int)num));
			disks.remove(num);

		}


		player1.setScore(p1);
		player2.setScore(p2);

		return false;
	}

	public void fadeIn(Node e){
		FadeTransition ft = new FadeTransition(Duration.millis(1500), e);
		ft.setFromValue(1);
		ft.setToValue(0);
		ft.setCycleCount(1);
		ft.setAutoReverse(true);
		ft.play();
	}
	public HBox scoreBox(Player player){
		HBox hB = new HBox();
		Text score = new Text(""+player.getScore());
		score.setId("scoreBox");
		hB.setAlignment(Pos.CENTER);
		hB.getChildren().add(score);
		return hB;
	}
}
