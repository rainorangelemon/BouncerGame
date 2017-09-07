import javafx.scene.image.Image;


public class Brick3 extends Brick {
	
	Brick3(Image image, int xNum, int yNum) {
		super(image, xNum, yNum);
	}
	
	@Override
	public Boolean Disappear(){
		return false;
	}
	
	@Override
	public void addHit(){
	}
	
	@Override
	public double ChangeX(double posX, double posY, double speedX, double speedY) {
		if(LevelRunning.throughWall()){
			return speedX;
		}else{
			return super.ChangeX(posX, posY, speedX, speedY);
		}
	}
	
	@Override
	public double ChangeY(double posX, double posY, double speedX, double speedY) {
		if(LevelRunning.throughWall()){
			return speedY;
		}else{
			return super.ChangeY(posX, posY, speedX, speedY);
		}
	}
}
