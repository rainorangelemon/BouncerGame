import javafx.scene.image.Image;


public class PowerVertical extends PowerUp {
	@Override
	protected Boolean verticalPaddle(){
		return true;
	}
	
	PowerVertical(double ballX, double ballY, Image image) {
		super(ballX, ballY, image);
	}
}
