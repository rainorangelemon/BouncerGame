import javafx.scene.image.Image;


public class Brick1 extends Brick{
	private int hit=0;
	private int kind=1;
	
	Brick1(Image image, int xNum, int yNum,double BallHeight,double BallWidth) {
		super(image, xNum, yNum,BallHeight,BallWidth);
	}
	
	@Override
	public String Disappear(){
		if(hit>=kind){
			return "disappear";
		}else{
			return " ";
		}
	}
	
	@Override
	public void addHit(){
		hit++;
	}
}
