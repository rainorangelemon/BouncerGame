import javafx.scene.image.Image;


public class PowerStick extends PowerUp {
	@Override
	protected Boolean stickBall(){
		return true;
	}
	
	PowerStick(double ballX, double ballY, Image image) {
		super(ballX, ballY, image);
	}
}
