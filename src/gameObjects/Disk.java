package gameObjects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Disk extends Circle {

	private double angle;
	private double iniVelocity;
	private double velocity;
	private double translation;

	@SuppressWarnings("unused")
	private static final double FRIC_CONSTANT=0.005;
	private double friction;
	private double mass;
	private double radius;
	private boolean onBoard;
	private boolean shootable;

	private String size;
	private Color color;

	private final static double LARGE=50;
	private final static double LARGE_MASS=5;
	private final static double SMALL=15;
	private final static double SMALL_MASS=3;
	private final static double MEDIUM=30;
	private final static double MEDIUM_MASS=3.5;


	public Disk(String size, Color color) {
		super();
		setSize(size);
		setMass(size);
		setRadius(size);
		setColor(color);
		setOnBoard(false);
		setShootable(false);
		setVelocity(0);
		setRadius(radius);
		setFill(color); 
		setAngle(0);
		
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	public double getIniVelocity() {
		return iniVelocity;
	}

	public void setIniVelocity(double iniVelocity) {
		this.iniVelocity = iniVelocity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public double getMass() {
		return mass;
	}

	public void setOnBoard(boolean value){
		onBoard = value;
	}

	public boolean isOnBoard(){
		return onBoard;
	}
	public boolean isShootable() {
		return shootable;
	}

	public void setShootable(boolean shootable) {
		this.shootable = shootable;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public void setMass(String size) {
		if(size.equalsIgnoreCase("Large"))
			mass = LARGE_MASS; 
		else if(size.equalsIgnoreCase("Small"))
			mass = SMALL_MASS;
		else if(size.equalsIgnoreCase("Medium"))
			mass = MEDIUM_MASS;
	}
	public double getTranslation() {
		return translation;
	}

	public void setTranslation(double translation) {
		this.translation = translation;
	}

	public double getFriction() {
		return friction;
	}

	public void setFriction(double friction) {
		this.friction = friction;
	}

	public void setRadius(String size) {
		if(size.equalsIgnoreCase("Large"))
			radius = LARGE; 
		if(size.equalsIgnoreCase("Small"))
			radius = SMALL;
		if(size.equalsIgnoreCase("Medium"))
			radius = MEDIUM;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		setFill(color);
	}


}
