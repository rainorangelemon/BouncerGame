import javafx.scene.image.Image;


public class Brick3 extends Brick {
	
	Brick3(Image image, int xNum, int yNum,double BallHeight,double BallWidth) {
		super(image, xNum, yNum,BallHeight,BallWidth);
	}
	
	@Override
	public String Disappear(){
		return " ";
	}
	
	@Override
	public double ChangeX(double posX, double posY, double speedX, double speedY, Boolean throughWall) {
		if(throughWall){
			return speedX;
		}else{
			return ChangeX(posX,posY,speedX,speedY);
		}
	}

	@Override
	public double ChangeY(double posX, double posY, double speedX, double speedY, Boolean throughWall) {
		if(throughWall){
			return speedY;
		}else{
			return ChangeY(posX,posY,speedX,speedY);
		}
	}
	
	@Override
	public void addHit(){
	}
}
