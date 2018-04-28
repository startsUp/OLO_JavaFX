package action;

import java.util.ArrayList;

import gameObjects.Disk;
import javafx.animation.AnimationTimer;
import main.GameApplication;

public class Physics {
	
	private static final double FRIC_CONSTANT=0.01;
	private static String playerTurn="p1"; 
	

	public static void moveDisk(double windowHeight, double windowWidth, ArrayList<Disk> disks){

		AnimationTimer timeA = new AnimationTimer() {

			@Override
			public void handle(long now) {

				for(Disk disk: disks){

					if(disk.getVelocity()==0 && disk.getIniVelocity()==0){
						continue;
					}
					double iniVel=disk.getIniVelocity();
					double count =disk.getTranslation();
					double deacc =disk.getFriction();
					double angle =disk.getAngle(); 
					double decConstant = disk.getMass() * FRIC_CONSTANT;
					double prevVel;
					double vel;


					prevVel = count;
					count=(count+iniVel)-deacc;
					disk.setTranslation(count);
					vel=count-prevVel;
					disk.setVelocity(vel);

					if(iniVel>0)
						deacc+=decConstant;
					else if(iniVel<0)
						deacc-=decConstant;
					disk.setFriction(deacc);

					if((vel<=0 && iniVel>0) || (iniVel<0 && vel>=0) ){
						updateTranslations(disk);
						disk.setVelocity(0);
						disk.setAngle(0);
						disk.setFriction(0);
						disk.setIniVelocity(0);
						

						if(turnOver(disks)&& playerTurn.equals("p1")){
							playerTurn="p2";
							stop();
							updateAll(disks);
							GameApplication.updateScene();
					
						}		
						else if(turnOver(disks)&& playerTurn.equals("p2")){

							playerTurn="p1";
							stop();
							updateAll(disks);
							GameApplication.updateScene();
							
						}

						System.out.println(playerTurn);
					
						continue;
					}

					double posX = disk.getRadius()+disk.getCenterX()+disk.getTranslateX();
					double posY = disk.getRadius()+disk.getCenterY()+disk.getTranslateY();
					double posLX = disk.getCenterX()+disk.getTranslateX()-disk.getRadius();
					double posTY = disk.getCenterY()+disk.getTranslateY()-disk.getRadius();

					for(Disk bDisk: disks){
						if(checkCollision(disk, bDisk)){
							updateTranslations(disk);
							updateTranslations(bDisk);

							ArrayList<Number> list  = getCollisionVectors(disk, bDisk);

							disk.setAngle((double)list.get(0));
							disk.setIniVelocity((double)list.get(1));
							bDisk.setAngle((double)list.get(2));
							bDisk.setIniVelocity((double)list.get(3));

							return;
						}
					}
					if(posX>=windowWidth){
						disk.setTranslateX(disk.getTranslateX()-(posX-windowWidth)-.1);
						updateTranslations(disk);
						ArrayList<Number> vector= getNewVelociy(angle,vel);
						disk.setAngle((double)vector.get(0));
						disk.setIniVelocity((double)vector.get(1));
						continue;

					}
					if (posY>=windowHeight){
						disk.setTranslateY(disk.getTranslateY()-(posY-windowHeight)-.1);
						updateTranslations(disk);
						ArrayList<Number> vector= getNewVelociy(angle,-vel);
						disk.setAngle((double)vector.get(0));
						disk.setIniVelocity((double)vector.get(1));
						continue;

					}
					if(posLX<=0){

						disk.setTranslateX(disk.getTranslateX()-posLX+.1);
						updateTranslations(disk);
						ArrayList<Number> vector= getNewVelociy(angle,vel);
						disk.setAngle((double)vector.get(0));
						disk.setIniVelocity((double)vector.get(1));
						continue;
					}
					if (posTY<=0){
						disk.setTranslateY(disk.getTranslateY()-posTY+.1);
						updateTranslations(disk);
						ArrayList<Number> vector= getNewVelociy(angle,-vel);
						disk.setAngle((double)vector.get(0));
						disk.setIniVelocity((double)vector.get(1));
						continue;

					}
					else{

						disk.setTranslateX(count*Math.cos(angle));
						disk.setTranslateY(count*Math.sin(angle));
					}
				}
			}
		};
		timeA.start();
	}

	public static String getPlayerTurn() {
		return playerTurn;
	}
	public static void setPlayerTurn(String playerTurn) {
		Physics.playerTurn = playerTurn;
	}
	public static boolean checkCollision(Disk disk, Disk bDisk){

		//check if other disk is on board and not a reference of itself in the list
		if(bDisk.isOnBoard() && !bDisk.equals(disk)){
			double deltaX   = (disk.getCenterX()+disk.getTranslateX())- 
					(bDisk.getCenterX()+bDisk.getTranslateX());
			double deltaY   = (disk.getCenterY()+disk.getTranslateY())- 
					(bDisk.getCenterY()+bDisk.getTranslateY());

			double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
			double angle    = Math.atan2(deltaY, deltaX);

			if(distance <= disk.getRadius()+ bDisk.getRadius()){
				double deltaD = disk.getRadius()+ bDisk.getRadius()  - distance;

				collisionTrans(disk, bDisk, deltaD, angle);
				return true;
			}
		}
		return false;
	}

	public static void updateAll(ArrayList<Disk> disks){
		for(Disk disk: disks){
			if(disk.isOnBoard()){
				disk.setShootable(false);
				disk.setCenterX(disk.getCenterX()+disk.getTranslateX());
				disk.setCenterY(disk.getCenterY()+disk.getTranslateY());
				disk.setTranslateX(0);
				disk.setTranslateY(0);
				disk.setTranslation(0);
				disk.setFriction(0);
				disk.setAngle(0);
				disk.setIniVelocity(0);
			}
		}
	}
	public static void collisionTrans(Disk disk, Disk disk2, double deltaD, double angle){
		double d = (disk.getRadius() + disk2.getRadius())- getDistance(disk, disk2);
		d+=.1;
		disk.setCenterX(disk.getCenterX()+disk.getTranslateX() +(d*Math.cos(angle))); //shift disk back after collision
		disk.setCenterY(disk.getCenterY()+disk.getTranslateY() +(d*Math.sin(angle)));
		disk.setTranslateX(0);
		disk.setTranslateY(0);
	}

	public static ArrayList<Number> getNewVelociy(double angle, double velocity){
		ArrayList<Number> vector= new ArrayList<>();
		vector.add(Math.PI-angle);
		vector.add(velocity);
		return vector;
	}

	public static void updateTranslations(Disk disk){
		disk.setCenterX(disk.getCenterX()+disk.getTranslateX());
		disk.setCenterY(disk.getCenterY()+disk.getTranslateY());
		disk.setTranslateX(0);
		disk.setTranslateY(0);
		disk.setTranslation(0);
		disk.setFriction(0);
	}

	public static ArrayList<Number> getCollisionVectors(Disk disk, Disk disk2){
		ArrayList<Number> vectors = new ArrayList<>();

		double m1= disk.getMass();
		double m2 = disk2.getMass();
		double a1 = disk.getAngle();
		double a2 = disk2.getAngle();
		double v1 = disk.getVelocity();
		double v2 = disk2.getVelocity();

		double deltaX   = (disk.getCenterX()+disk.getTranslateX())- 
				(disk2.getCenterX()+disk2.getTranslateX());
		double deltaY   = (disk.getCenterY()+disk.getTranslateY())- 
				(disk2.getCenterY()+disk2.getTranslateY());


		double phi; 

		if(deltaX==0)
			phi = Math.PI/2;
		else 
			phi = Math.atan2(deltaY, deltaX);

//		double ang1 = findNewAngle(v1*Math.cos(a1), v1*Math.sin(a1));
//		double ang2 = findNewAngle(v2*Math.cos(a2), v2*Math.sin(a2));
////
//		double v1xr = v1*Math.cos(ang1-phi);
//		double v1yr = v1*Math.sin(ang1-phi);
//		double v2xr = v2*Math.cos(ang2-phi);
//		double v2yr = v2*Math.sin(ang2-phi);

//		double v1fxr = ((m1-m2)*v1xr+(m2+m2)*v2xr)/(m1+m2);
//		double v2fxr = ((m1+m1)*v1xr+(m2-m1)*v2xr)/(m1+m2);
//		double v1fyr = v1yr;
//		double v2fyr = v2yr;

//		double v1fx = Math.cos(phi)*v1fxr+Math.cos(phi+Math.PI/2)*v1fyr;
//		double v1fy = Math.sin(phi)*v1fxr+Math.sin(phi+Math.PI/2)*v1fyr;
//		double v2fx = Math.cos(phi)*v2fxr+Math.cos(phi+Math.PI/2)*v2fyr;
//		double v2fy = Math.sin(phi)*v2fxr+Math.sin(phi+Math.PI/2)*v2fyr;

//		double v1f = Math.sqrt(Math.pow(v1fx, 2) + Math.pow(v1fy, 2));
//		double v2f = Math.sqrt(Math.pow(v2fx, 2) + Math.pow(v2fy, 2));

		//Elastic collision calculations
		double vx    = (v1*cos(a1-phi)*(m1-m2))+(2*m2*v2*cos(a2-phi));
		double v01fx = (vx/m1+m2)*cos(phi) + (v1*sin(a1-phi)*cos(phi+(Math.PI/2)));
		double v01fy = (vx/m1+m2)*sin(phi) + (v1*sin(a1-phi)*sin(phi+(Math.PI/2)));
		double nA    = Math.atan2(v01fy, v01fx);
		double v01f  = Math.sqrt(Math.pow(v01fx, 2) + Math.pow(v01fy, 2));

		double vx2   = (v2*cos(a2-phi)*(m2-m1))+(2*m1*v1*cos(a1-phi));
		double v02fx = (vx2/m2+m1)*cos(phi) + (v2*sin(a2-phi)*cos(phi+(Math.PI/2)));
		double v02fy = (vx2/m2+m1)*sin(phi) + (v2*sin(a2-phi)*sin(phi+(Math.PI/2)));
		double nA2   = Math.atan2(v02fy, v02fx);
		double v02f  = Math.sqrt(Math.pow(v02fx, 2) + Math.pow(v02fy, 2));

		if(nA<0)
			nA += 2*Math.PI;
		if(nA2<0)
			nA2 += 2*Math.PI;

		if(v01f>10 ){
			vectors.add(nA);
			vectors.add(v01f/9);
			vectors.add(nA2);
			vectors.add(-v02f/9);

		}
		else{
			vectors.add(nA);
			vectors.add(v01f);
			vectors.add(nA2);
			vectors.add(v02f);
		}

		return vectors;
	}
	public static double findNewAngle(double vX, double vY){
		if(vX<0)
			return Math.PI + Math.atan2(vY, vX);
		else if(vX>0 && vY>=0)
			return Math.atan2(vY, vX);
		else if(vX>0 && vY<0)
			return (2*Math.PI) + Math.atan2(vY, vX);
		else if(vX==0 && vY==0)
			return 0;
		else if(vX==0 && vY>=0)
			return Math.PI/2;
		else
			return Math.PI*3/2;
	}
	public static double cos(double a){
		return Math.cos(a);
	}
	public static double sin(double a){
		return Math.sin(a);
	}
	public static double tan(double a){
		return Math.sin(a);
	}

	public static boolean turnOver(ArrayList<Disk> disks){
		int count=0;
		for(Disk disk: disks){
			if(disk.getIniVelocity()==0 && disk.getVelocity()==0)
				count++;
		}
		if(count==disks.size())
			return true;
		else 
			return false;
	}
	public static double getDistance(Disk disk, Disk bDisk){
		double deltaX   = (disk.getCenterX()+disk.getTranslateX())- 
				(bDisk.getCenterX()+bDisk.getTranslateX());
		double deltaY   = (disk.getCenterY()+disk.getTranslateY())- 
				(bDisk.getCenterY()+bDisk.getTranslateY());

		double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		return distance;
	}
}



