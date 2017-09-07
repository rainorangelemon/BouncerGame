import javafx.scene.image.Image;


public class Brick2 extends Brick {
	private int hit=0;
	private int kind=3;
	
	Brick2(Image image, int xNum, int yNum) {
		super(image, xNum, yNum);
	}
	
	@Override
	public Boolean Disappear(){
		if(hit>=kind){
			Starter.currentLevel.createPowerUp(getX(),getY());
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void addHit(){
		hit++;
	}
}
