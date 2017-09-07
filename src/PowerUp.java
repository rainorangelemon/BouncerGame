import javafx.scene.image.Image;


public class PowerUp extends Ball{

	private double time=0;
	private final double SpeedX=0;
	private final double SpeedY=0.01;
	
	PowerUp(double ballX, double ballY, Image image) {
		super(ballX, ballY, image);
	}	
	
	@Override
	protected void reset(){
		super.reset();
		setSpeedX(0);
		setSpeedY(1);
	}
		
	@Override
	protected double getSpeedX(){
		return SpeedX;
	}
		
	@Override
	protected double getSpeedY(){
		return SpeedY;
	}
	
	protected Boolean throughWall(){
		return false;
	}
	
	protected Boolean verticalPaddle(){
		return false;
	}
	
	protected Boolean stickBall(){
		return false;
	}
	
	protected Boolean disappear(){
		if(time>100000){
			return true;
		}else{
			return false;
		}
	}
	
	protected void addTime(){
		time++;
	}
	
	
}
