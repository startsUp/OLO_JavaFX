package action;

import java.util.ArrayList;
import java.util.List;

import gameObjects.Disk;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


public class ShootAction {
	private static long startTime;

	public ShootAction() {
		// TODO Auto-generated constructor stub
	}

	public static void makeShootable(Disk disk, ArrayList<Disk> disks){
		disk.setShootable(true);
		disk.setOnMousePressed(e->{
			System.out.println(true);
			startTime = System.currentTimeMillis();
		});

		disk.setOnMouseReleased(
				new EventHandler<MouseEvent>(){

					@Override
					public void handle(MouseEvent event) {
						//System.out.println(event.getX() + ", " + event.getY());
 

						long r = System.currentTimeMillis() - startTime;
						//	System.out.println("Start Time: " + startTime+ " Current Time: " + System.currentTimeMillis() +  " Result " + r);

						List<Number> vector = velVector(disk, event.getX(), event.getY(), r);
						if(disk.isShootable()){
							disk.setOnBoard(true);
							disk.setIniVelocity((double)vector.get(1));
							disk.setAngle((double)vector.get(0));
							System.out.println(false);
							Physics.moveDisk(900,1400,disks);
						}
					}});

	}
	public static double calculateAngle(double mouseInX, double mouseReX, double mouseInY, double mouseReY){
		double angle = 0;

		return angle;
	}

	public static List<Number> velVector(Disk disk, double x, double y, long deltaT){
		ArrayList<Number> vector= new ArrayList<>();
		double deltaX   = x - (disk.getCenterX()+disk.getTranslateX());
		double deltaY   = y - (disk.getCenterY()+disk.getTranslateY());
		double dist     = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		double angle    = Math.atan2(deltaY, deltaX);			//in Radians
		double velocity = 5+ (dist/deltaT);    

		vector.add(angle);
		if(velocity>50)
			velocity=0;
		vector.add(velocity);		
	
		return vector;
	}
	public static void calculateVelocity(){

	}

	public void move(){

	}

}
