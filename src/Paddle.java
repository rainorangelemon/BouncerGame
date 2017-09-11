import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;


public class Paddle extends Ball{
	private final double SpeedX=20;
	private final double SpeedY=20;
	private double PaddleHeight;
	private double PaddleWidth;
	private double BallHeight;
	private double BallWidth;
	private KeyCode code;
	
	Paddle(double paddleX, double paddleY, Image image){
		super(paddleX, paddleY, image);
	}
	
	Paddle(double paddleX, double paddleY, Image image, double PaddleHeight, double PaddleWidth, double BallHeight, double BallWidth){
		super(paddleX, paddleY, image);
		this.PaddleHeight=PaddleHeight;
		this.PaddleWidth=PaddleWidth;
		this.BallHeight=BallHeight;
		this.BallWidth=BallWidth;
	}
	
	@Override
	protected void reset(){
		super.reset();
		setSpeedX(20);
		setSpeedY(20);
	}
	
	@Override
	protected double getSpeedX(){
		return SpeedX;
	}
	
	@Override
	protected double getSpeedY(){
		return SpeedY;
	}
	
	private Boolean hitTop(double posX, double posY){
		return ((posY+BallHeight/8>=getY())||(posY<=getY()));
	}
	
	private Boolean hitLeft(double posX, double posY){
		return (posX+BallWidth/2<=getX()+PaddleWidth/3);
	}
	
	private Boolean hitRight(double posX, double posY){
		return (posX+BallWidth/2>getX()+2*PaddleWidth/3);
	}
	
	public double ChangeX(double posX, double posY, double speedX, double speedY) {
		if( (hitTop(posX,posY)) &&  (!((  (hitLeft(posX,posY))&&(speedX>0)) || ((hitRight(posX,posY))&&(speedX<0)))) ){
			return speedX;
		}else{
			return -speedX;
		}
	}

	public double ChangeY(double posX, double posY, double speedX, double speedY) {
		if(hitTop(posX,posY)){
			return -Math.abs(speedY);
		}else{
			return Math.abs(speedY);
		}
	}
	
}
