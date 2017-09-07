import javafx.scene.image.Image;


public class PowerThrough extends PowerUp {
	
	@Override
	protected Boolean throughWall(){
		return true;
	}
	
	PowerThrough(double ballX, double ballY, Image image) {
		super(ballX, ballY, image);
	}
}
