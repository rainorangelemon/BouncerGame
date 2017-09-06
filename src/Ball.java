import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Ball extends ImageView{
	private double SpeedX;
	private double SpeedY;
	private double ballX;
	private double ballY;
	private Random random=new Random();
	
	private double max(double a, double b){
		return a>b?a:b;
	}
	
	private double min(double a, double b){
		return a<b?a:b;
	}
	
	Ball(double ballX, double ballY, Image image){
		super(image);
		this.ballX=ballX;
		this.ballY=ballY;
		reset();
	}
	
	protected void reset(){
		reset(ballX,ballY);
	}
	
	protected void reset(double posX, double posY){
		ballX=posX;
		ballY=posY;
		setX(ballX);
		setY(ballY);
		do{
			this.SpeedX=min(max(((0.3*random.nextDouble()-0.15)),-0.15),0.15);
			this.SpeedY=min(max(((0.3*random.nextDouble()-0.15)),-0.15),0.15);
		}while((Math.abs(this.SpeedX)<=0.07)||(this.SpeedY>=0.0)||(Math.abs(this.SpeedY)<=0.07));
	}
	
	protected double getSpeedX(){
		return this.SpeedX;
	}
	
	protected double getSpeedY(){
		return this.SpeedY;
	}
	
	protected void setSpeedX(double newSpeed){
		this.SpeedX=newSpeed;
	}
	
	protected void setSpeedY(double newSpeed){
		this.SpeedY=newSpeed;
	}
}
