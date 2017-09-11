import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class Brick extends ImageView{
	private int hit=0;
	private double left,right,top,bottom;
	protected double BallWidth;
	protected double BrickWidth;
	protected double BallHeight;
	protected double BrickHeight;
	
	Brick(Image image, int rowNum, int columnNum,double BallHeight,double BallWidth){
		super(image);
		BrickWidth=image.getWidth();
		BrickHeight=image.getHeight();
		setX(columnNum*(BrickWidth+1));
		setY(rowNum*(BrickHeight+1));
		this.BallHeight=BallHeight;
		this.BallWidth=BallWidth;
	}
	
	double max(double a, double b){
		return a>b?a:b;
	}
	
	private void calculateHitPart(double posX, double posY) {
		left=posX+BallWidth-getX();
			left=(posX<=getX())?left:-1;
		right=(getX()+BrickWidth)-posX;
			right=((posX+BallWidth)>=(getX()+BrickWidth))?right:-1;
		top=posY+BallHeight-getY(); 
			top=(posY<=getY())?top:-1;
		bottom=(getY()+BrickHeight)-posY; 
			bottom=((posY+BallHeight)>=(getY()+BrickHeight))?bottom:-1;
	}
	
	public double ChangeX(double posX, double posY, double speedX, double speedY) {
		calculateHitPart(posX, posY);
		if(max(left,right)>=0L){
	        return (right>=0L)?Math.abs(speedX):(-Math.abs(speedX));
		}else{
			return speedX;
		}
	}
	
	public double ChangeX(double posX, double posY, double speedX, double speedY, Boolean throughWall) {
		calculateHitPart(posX, posY);
		if(max(left,right)>=0L){
	        return (right>=0L)?Math.abs(speedX):(-Math.abs(speedX));
		}else{
			return speedX;
		}
	}

	public double ChangeY(double posX, double posY, double speedX, double speedY) {
		calculateHitPart(posX, posY);
		if(max(top,bottom)>=0L){
			return (bottom>=0L)?Math.abs(speedY):(-Math.abs(speedY));
		}else{
			return speedY;
		}
	}
	
	public double ChangeY(double posX, double posY, double speedX, double speedY, Boolean throughWall) {
		calculateHitPart(posX, posY);
		if(max(top,bottom)>=0L){
			return (bottom>=0L)?Math.abs(speedY):(-Math.abs(speedY));
		}else{
			return speedY;
		}
	}
	
	public String Disappear(){
		return " ";
	}
	
	public void addHit(){
		hit++;
	}
	
}
