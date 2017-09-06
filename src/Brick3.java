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
}
