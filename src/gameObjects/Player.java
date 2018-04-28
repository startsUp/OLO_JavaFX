package gameObjects;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;

public class Player {

	private ArrayList<Disk> disks;
	private int score;
	private Color color;
	private Color playerAreaColor;

	public Player(Color color, Color playerArea) {

		disks = new ArrayList<>();
		setColor(color);
		setPlayerAreaColor(playerArea);
		giveDisks();

		setScore(0);

	}

	public Color getPlayerAreaColor(){
		return playerAreaColor;
	}

	public void setPlayerAreaColor(Color pC){
		playerAreaColor = pC;
	}
	public void giveDisks(){
		int[] sizeCount = new int[3];
		for(int i=0; i<6; i++) {
			int s;
			do{
				Random r = new Random();
				s= r.nextInt(3);
			
			}while(sizeCount[s]>2);
			sizeCount[s]++;

			if(s==0)
				disks.add(new Disk("Large",color));
			if(s==1)
				disks.add(new Disk("Small",color));
			if(s==2)
				disks.add(new Disk("Medium",color));
		}
	}
	

	public Disk getDisk(int index){
		ArrayList<Disk> playerDisks = getDisks();
		Disk disk = new Disk(playerDisks.get(index).getSize(), playerDisks.get(index).getColor());
		return disk;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setScore(int score){
		this.score=score;
	}

	public int getScore(){
		return score;
	}
	public void setDisks(ArrayList<Disk> disks){
		this.disks = disks;
	}

	public ArrayList<Disk> getDisks(){
		return disks;
	}

	public ArrayList<Disk> getBoardDisks(){
		ArrayList<Disk> boardDisks = new ArrayList<>();
		for(Disk disk: disks){
			if(disk.isOnBoard())
				boardDisks.add(disk);
		}
		return boardDisks;
	}
	
	public ArrayList<Disk> getHandDisks(){
		ArrayList<Disk> handDisks = new ArrayList<>();
		for(Disk disk: disks){
			if(!disk.isOnBoard())
				handDisks.add(disk);
		}
		return handDisks;
	}
	public void remove(int index){
		disks.remove(index);
	}
}
