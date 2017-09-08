import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class Brick extends ImageView{
	private int hit=0;
	private int kind=0;
	
	Brick(Image image, int rowNum, int columnNum){
		super(image);
		setX(columnNum*(image.getWidth()+1));
		setY(rowNum*(image.getHeight()+1));
	}
	
	double max(double a, double b){
		return a>b?a:b;
	}
	
	public double ChangeX(double posX, double posY, double speedX, double speedY) {
		double left=posX+Starter.BallWidth-getX();
			left=(posX<=getX())?left:-1;
		double right=(getX()+Starter.BrickWidth)-posX;
			right=((posX+Starter.BallWidth)>=(getX()+Starter.BrickWidth))?right:-1;
		double top=posY+Starter.BallHeight-getY(); 
			top=(posY<=getY())?top:-1;
		double bottom=(getY()+Starter.BrickHeight)-posY; 
			bottom=((posY+Starter.BallHeight)>=(getY()+Starter.BrickHeight))?bottom:-1;
		if(max(left,right)>=0L){
	        return (right>=0L)?Math.abs(speedX):(-Math.abs(speedX));
		}else{
			return speedX;
		}
	}

	public double ChangeY(double posX, double posY, double speedX, double speedY) {
		double left=posX+Starter.BallWidth-getX();
			left=(posX<=getX())?left:-1;
		double right=(getX()+Starter.BrickWidth)-posX;
			right=((posX+Starter.BallWidth)>=(getX()+Starter.BrickWidth))?right:-1;
		double top=posY+Starter.BallHeight-getY(); 
			top=(posY<=getY())?top:-1;
		double bottom=(getY()+Starter.BrickHeight)-posY; 
			bottom=((posY+Starter.BallHeight)>=(getY()+Starter.BrickHeight))?bottom:-1;
		if(max(top,bottom)>=0L){
			return (bottom>=0L)?Math.abs(speedY):(-Math.abs(speedY));
		}else{
			return speedY;
		}
	}
	
	public Boolean Disappear(){
		return false;
	}
	
	public void addHit(){
		hit++;
	}
	
}
