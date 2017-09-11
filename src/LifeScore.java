import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.*;


public class LifeScore extends Text{
	private int life=3;
	private int score=0;
	private int currentNum;
	LifeScore(int currentNum){
		super();
		setX(0);
		setY(0+36);
		refreshText();
		setFill(Color.YELLOW);
		setFont(Font.font(null, FontWeight.NORMAL, 36));
		this.currentNum=currentNum;
	}
	
	void refreshText(){
		setText("Life: "+String.valueOf(life)+", Score: "+String.valueOf(score)+" ,Level: "+String.valueOf(currentNum));
	}
	
	void reset(int currentNum){
		life=3;
		score=0;
		this.currentNum=currentNum;
		refreshText();
	}
	
	public String decreaseLife(){
		life--;
		if(life<0){
			return "lose";
		}else{
			refreshText();
			return "";
		}
	}
	
	public void increaseLife(){
		life++;
		refreshText();
	}
	
	public void increaseScore(int num){
		score=score+num;
		refreshText();
	}
	
	public int getScore(){
		return score;
	}
	
	public int getLife(){
		return life;
	}
}
