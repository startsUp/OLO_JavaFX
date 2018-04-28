package main;

import java.util.ArrayList;
import java.util.List;

import action.Physics;
import gameObjects.Disk;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class GameApp extends Application {

	private Stage window;
//	private Board board;
//	private double count;
//	private boolean start;
	private long startTime;


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		window = stage;
		window.show();


		Pane pane = new Pane();
		VBox player1Start = new VBox(); 
		player1Start.setPrefSize(100, 700);
		player1Start.setStyle("-fx-background-color: Red;");


		Disk circle = new Disk("Large", Color.AZURE); 
		circle.setFill(Color.AQUA);
		circle.setCenterX(400);
		circle.setCenterY(200);
		//System.out.println(circgetBoundsInParent().toString());
		Disk circle2 = new Disk("Large", Color.WHITE); 
		circle2.setCenterX(200);
		circle2.setCenterY(200);

		Disk circle3 = new Disk("Medium", Color.YELLOW);
		circle3.setCenterX(500);
		circle3.setCenterY(500);


		//	circle.setOnMouseEntered(e->System.out.println(true));
		circle.setOnMousePressed(e->{

			startTime= System.currentTimeMillis();
		});

		ArrayList<Disk> disks = new ArrayList<>();
		disks.add(circle2);
		disks.add(circle);
		disks.add(circle3);
		circle2.setOnBoard(true);

		circle.setOnMouseReleased(
				new EventHandler<MouseEvent>(){

					@Override
					public void handle(MouseEvent event) {
						//System.out.println(event.getX() + ", " + event.getY());


						long r = System.currentTimeMillis() - startTime;
						//	System.out.println("Start Time: " + startTime+ " Current Time: " + System.currentTimeMillis() +  " Result " + r);
						Circle cEnd = new Circle(10);
						cEnd.setFill(Color.YELLOW);
						cEnd.setCenterX(event.getX());
						cEnd.setCenterY(event.getY());
						List<Number> vector = velVector(circle, event.getX(), event.getY(), r);

						circle.setOnBoard(true);
						circle.setIniVelocity((double)vector.get(1));
						circle.setAngle((double)vector.get(0));
						Physics.moveDisk(900,1400,disks);

					}});

		Button b = new Button("Toggle");
		b.setLayoutX(500);
		b.setLayoutY(500);

		//	b.setOnAction(e->Physics.moveDisk(circle2, 0,4,700,1000,disks));
		Button b2 = new Button("Close");
		b2.setLayoutX(600);
		b2.setLayoutY(500);

		b2.setOnAction(e-> Platform.exit());

		circle3.setOnBoard(true);
		
		Disk d4 = new Disk("small" , Color.BLUE);
		Disk d5 = new Disk("Large" , Color.CHARTREUSE);
		d4.setCenterX(500);
		d4.setCenterY(800);
		d5.setCenterX(700);
		d5.setCenterY(200);
		d5.setOnBoard(true);
		d4.setOnBoard(true);
		
		disks.add(d4);
		disks.add(d5);
		for(Disk disk: disks)
			pane.getChildren().add(disk);

	




		pane.setPrefSize(1400, 900);
		pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null,null)));
		Scene mainScene = new Scene(pane);
		mainScene.getStylesheets().add("AppCSS.css");
		window.setScene(mainScene);

		//System.out.println(circle.translateXProperty());



	}


	public List<Number> velVector(Disk disk, double x, double y, long deltaT){
		ArrayList<Number> vector= new ArrayList<>();
		double deltaX   = x - (disk.getCenterX()+disk.getTranslateX());
		double deltaY   = y - (disk.getCenterY()+disk.getTranslateY());
		double dist     = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		double angle    = Math.atan2(deltaY, deltaX);			//in Radians
		double velocity = 5+ (dist/deltaT);    

		vector.add(angle);
		vector.add(velocity);		
		return vector;
	}



}

