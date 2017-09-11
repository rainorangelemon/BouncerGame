import javafx.scene.image.Image;


public class Brick2 extends Brick {
	private int hit=0;
	private int kind=3;
	
	Brick2(Image image, int xNum, int yNum,double BallHeight,double BallWidth) {
		super(image, xNum, yNum,BallHeight,BallWidth);
	}
	
	@Override
	public String Disappear(){
		if(hit>=kind){
			return "PowerUp";
		}else{
			return " ";
		}
	}
	
	@Override
	public void addHit(){
		hit++;
	}
}
