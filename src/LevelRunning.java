import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;

public class LevelRunning {
	private int RowNum;
	private int ColumnNum;
	private Paint background;
	private Set<Brick> graphs = new HashSet<Brick>();
	private HashMap<Character, Image> pictures;
	private Set<PowerUp> powerUpsFalling = new HashSet<PowerUp>();
	private Set<PowerUp> powerUpsCaught = new HashSet<PowerUp>();
	private Scene myScene;
	private LifeScore lifeScore;
	private Group root = new Group();
	private Paddle paddle;
	private Ball ball;
	private int remainBrick = 0;
	private double BrickHeight;
	private double BrickWidth;
	private double PaddleWidth;
	private double PaddleHeight;
	private double BallHeight;
	private double BallWidth;
	private double PowerUpHeight;
	private double PowerUpWidth;
	private int currentNum;
	

	LevelRunning(Level source, HashMap<Character, Image> pic, int currentNum) {
		pictures=new HashMap<Character, Image>(pic);
		for(Character name:pictures.keySet()){
			if(name=='1'){
				BrickHeight = pictures.get(name).getHeight();
				BrickWidth=pictures.get(name).getWidth();
			}else if(name=='_'){
				PaddleHeight=pictures.get(name).getHeight();
				PaddleWidth=pictures.get(name).getWidth();
			}else if(name=='.'){
				BallHeight=pictures.get(name).getHeight();
				BallWidth=pictures.get(name).getWidth();
			}else if(name=='t'){
				PowerUpHeight=pictures.get(name).getHeight();
				PowerUpWidth=pictures.get(name).getWidth();
			}
		}
		ball = new Ball(0, 0, pictures.get('.'));
		paddle = new Paddle(0, 0, pictures.get('_'),PaddleHeight, PaddleWidth, BallHeight, BallWidth);
		lifeScore=new LifeScore(currentNum);
		this.currentNum=currentNum;
		create(source, currentNum);;
	}

	public void create(Level source,int currentNum) {
		this.RowNum = source.rowNum;
		this.ColumnNum = source.columnNum;
		setBricks(source);
		paddle.reset((ColumnNum / 2) * PaddleWidth, RowNum
				* BrickHeight - PaddleHeight);
		ball.reset(paddle.getX() + PaddleWidth / 2 - BallWidth
				/ 2, paddle.getY() - (BallHeight) - 1);
		lifeScore.reset(currentNum);
	}

	public Scene reset(Level source, int currentNum) {
		this.currentNum=currentNum;
		removeAll2Root();
		graphs.clear();
		powerUpsFalling.clear();
		powerUpsCaught.clear();
		remainBrick = 0;
		create(source,currentNum);
		return getScene(background);
	}

	private void setBricks(Level source) {
		for (int i = 0; i < RowNum; i++) {
			for (int j = 0; j < ColumnNum; j++) {
				char temp = source.getChar(i, j);
				if ((temp >= '1') && (temp <= '3')) {
					switch (temp) {
					case '1': {
						Brick newBrick = new Brick1(pictures.get(temp),
								i, j,BallHeight,BallWidth);
						graphs.add(newBrick);
						remainBrick++;
						break;
					}
					case '2': {
						Brick newBrick = new Brick2(pictures.get(temp),
								i, j,BallHeight,BallWidth);
						graphs.add(newBrick);
						remainBrick++;
						break;
					}
					case '3': {
						Brick newBrick = new Brick3(pictures.get(temp),
								i, j,BallHeight,BallWidth);
						graphs.add(newBrick);
						break;
					}
					}
				}
			}
		}
	}

	public void removeAll2Root() {
		root.getChildren().clear();
	}

	public Scene getScene(Paint background) {
		this.background = background;
		// create one top level collection to organize the things in the scene
		root = new Group();
		// create a place to see the shapes
		myScene = new Scene(root/*
								 * , ColumnNum*(Starter.BrickWidth+1),
								 * RowNum*(Starter.BrickHeight), background
								 */);
		// make some shapes and set their properties
		addAll2Root();
		return myScene;
	}

	private void addAll2Root() {
		for (Brick graph : graphs) {
			root.getChildren().add(graph);
		}
		for (PowerUp powerup : powerUpsFalling) {
			root.getChildren().add(powerup);
		}
		root.getChildren().add(ball);
		root.getChildren().add(paddle);
		root.getChildren().add(lifeScore);
	}

	public String refresh(double elapsedTime) {
		for (Brick brick : graphs) {
			if (bounceWithBrick(brick) == true)
				break;
		}
		bounceWithPaddle();
		if(bounceWithScreen()=="lose"){
			return "lose";
		}
		ballRefresh(elapsedTime);
		powerUpsRefresh(elapsedTime);
		if ((remainBrick == 0) && (lifeScore.getLife() >= 0)) {
			return "win";
		}
		return "";
	}

	private void powerUpsRefresh(double elapsedTime) {
		// count caught powers counting time and caught clean
		Set<PowerUp> removeSetCaught = new HashSet<PowerUp>();
		for (PowerUp powerup : powerUpsCaught) {
			powerup.addTime();
			if (powerup.disappear()) {
				removeSetCaught.add(powerup);
			}
		}
		powerUpsCaught.removeAll(removeSetCaught);
		// fall clean and caught add and calculate motion
		Set<PowerUp> removeSetFalling = new HashSet<PowerUp>();
		for (PowerUp powerup : powerUpsFalling) {
			if (powerup.getY() + powerup.getFitHeight() > myScene.getY()
					+ myScene.getHeight()) {
				removeSetFalling.add(powerup);
				root.getChildren().remove(powerup);
			} else if (powerup.intersects(paddle.getBoundsInParent())) {
				powerUpsCaught.add(powerup);
				removeSetFalling.add(powerup);
				root.getChildren().remove(powerup);
			} else {
				powerup.setY(powerup.getY() + powerup.getSpeedY() * elapsedTime);
			}
		}
		powerUpsFalling.removeAll(removeSetFalling);
	}

	private Boolean bounceWithBrick(Brick brick) {
		if (ball.intersects(brick.getBoundsInParent())) {
			ball.setSpeedX(brick.ChangeX(ball.getX(), ball.getY(),
					ball.getSpeedX(), ball.getSpeedY(),throughWall()));
			ball.setSpeedY(brick.ChangeY(ball.getX(), ball.getY(),
					ball.getSpeedX(), ball.getSpeedY(),throughWall()));
			brick.addHit();
			String isDisappear=brick.Disappear();
			if (isDisappear != " ") {
				lifeScore.increaseScore(10);
				graphs.remove(brick);
				root.getChildren().remove(brick);
				remainBrick--;
				if(isDisappear=="PowerUp"){
					createPowerUp(brick.getX(),brick.getY());
				}
			}
			return true;
		}
		return false;
	}

	private void ballRefresh(double elapsedTime) {
		ball.setX(ball.getX() + ball.getSpeedX() * elapsedTime);
		ball.setY(ball.getY() + ball.getSpeedY() * elapsedTime);
	}

	private double max(double a, double b) {
		return a > b ? a : b;
	}

	private double min(double a, double b) {
		return a < b ? a : b;
	}

	public void paddleRefresh(KeyCode code) {
		if (code == KeyCode.RIGHT) {
			paddle.setX(min((paddle.getX() + Math.abs(paddle.getSpeedX())),
					myScene.getX() + myScene.getWidth() - PaddleWidth));
		} else if (code == KeyCode.LEFT) {
			paddle.setX(max((paddle.getX() - Math.abs(paddle.getSpeedX())),
					myScene.getX()));
		} else {
			if (verticalPaddle() == true) {
				if (code == KeyCode.UP) {
					paddle.setY(max(
							(paddle.getY() - Math.abs(paddle.getSpeedY())),
							myScene.getY()));
				} else if (code == KeyCode.DOWN) {
					paddle.setY(min(
							(paddle.getY() + Math.abs(paddle.getSpeedY())),
							RowNum * BrickHeight - PaddleHeight));
				}
			} else {
				paddle.setY(RowNum * BrickHeight - PaddleHeight);
			}
		}
	}

	public final Boolean stickBall() {
		for (PowerUp powerup : powerUpsCaught) {
			if (powerup.stickBall() == true) {
				return true;
			}
		}
		return false;
	}

	public final Boolean throughWall() {
		for (PowerUp powerup : powerUpsCaught) {
			if (powerup.throughWall() == true) {
				return true;
			}
		}
		return false;
	}

	public final Boolean verticalPaddle() {
		for (PowerUp powerup : powerUpsCaught) {
			if (powerup.verticalPaddle() == true) {
				return true;
			}
		}
		return false;
	}

	private void bounceWithPaddle() {
		if (ball.intersects(paddle.getBoundsInParent())) {
			ball.setSpeedX(paddle.ChangeX(ball.getX(), ball.getY(),
					ball.getSpeedX(), ball.getSpeedY()));
			ball.setSpeedY(paddle.ChangeY(ball.getX(), ball.getY(),
					ball.getSpeedX(), ball.getSpeedY()));
		}
	}

	private String bounceWithScreen() {
		if ((ball.getX() + BallWidth >= myScene.getX()
				+ myScene.getWidth())
				|| (ball.getX() <= 0)) {
			ball.setSpeedX(-ball.getSpeedX());
		}
		if (ball.getY() <= 0) {
			ball.setSpeedY(-ball.getSpeedY());
		}
		if (ball.getY() > myScene.getY() + myScene.getHeight()) {
			ball.reset();
			return lifeScore.decreaseLife();
		}
		return "";
	}

	public void createPowerUp(double posX, double posY) {
		Random random = new Random();
		int choice = (random.nextInt() % 3);
		PowerUp temp;
		if (choice == 0) {
			temp = new PowerThrough(posX, posY, pictures.get('t'));
			powerUpsFalling.add(new PowerThrough(posX, posY, pictures.get('t')));
			System.out.printf("PowerUp dropping! The power of through unbreakable bricks\n");
		} else if (choice == 1) {
			temp = new PowerVertical(posX, posY, pictures.get('v'));
			System.out.printf("PowerUp dropping! The power of moving paddle vertically\n");
		} else {
			temp = new PowerStick(posX, posY, pictures.get('s'));
			System.out.printf("PowerUp dropping! The power of sticking the balls to the bottom pressing SPACE\n");
		}
		powerUpsFalling.add(temp);
		root.getChildren().add(temp);
	}
	
	public Group getRoot(){
		return root;
	}
	
	public void resetBall(){
		ball.reset();
	}
	
	public Scene getMyScene(){
		return myScene;
	}

}
