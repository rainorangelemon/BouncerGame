import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;

public class LevelRunning {
	public static int RowNum;
	public static int ColumnNum;
	Paint background;
	private Set<Brick> graphs = new HashSet<Brick>();
	private Set<PowerUp> powerUpsFalling = new HashSet<PowerUp>();
	private static Set<PowerUp> powerUpsCaught = new HashSet<PowerUp>();
	public static Scene myScene;
	private static LifeScore lifeScore = new LifeScore();
	public Group root = new Group();
	Paddle paddle = new Paddle(0, 0, Starter.pictures.get('_'));
	Ball ball = new Ball(0, 0, Starter.pictures.get('.'));
	private int remainBrick = 0;

	LevelRunning(Level source) {
		create(source);
	}

	public void create(Level source) {
		this.RowNum = source.rowNum;
		this.ColumnNum = source.columnNum;
		setBricks(source);
		paddle.reset((ColumnNum / 2) * Starter.PaddleWidth, RowNum
				* Starter.BrickHeight - Starter.PaddleHeight);
		ball.reset(paddle.getX() + Starter.PaddleWidth / 2 - Starter.BallWidth
				/ 2, paddle.getY() - (Starter.BallHeight) - 1);
		lifeScore.reset();
	}

	public Scene reset(Level source) {
		removeAll2Root();
		graphs.clear();
		powerUpsFalling.clear();
		powerUpsCaught.clear();
		remainBrick = 0;
		create(source);
		return getScene(background);
	}

	private void setBricks(Level source) {
		for (int i = 0; i < RowNum; i++) {
			for (int j = 0; j < ColumnNum; j++) {
				char temp = source.getChar(i, j);
				if ((temp >= '1') && (temp <= '3')) {
					switch (temp) {
					case '1': {
						Brick newBrick = new Brick1(Starter.pictures.get(temp),
								i, j);
						graphs.add(newBrick);
						remainBrick++;
						break;
					}
					case '2': {
						Brick newBrick = new Brick2(Starter.pictures.get(temp),
								i, j);
						graphs.add(newBrick);
						remainBrick++;
						break;
					}
					case '3': {
						Brick newBrick = new Brick3(Starter.pictures.get(temp),
								i, j);
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

	public void refresh(double elapsedTime) {
		for (Brick brick : graphs) {
			if (bounceWithBrick(brick) == true)
				break;
		}
		bounceWithPaddle();
		bounceWithScreen();
		ballRefresh(elapsedTime);
		powerUpsRefresh(elapsedTime);
		if ((remainBrick == 0) && (lifeScore.getLife() >= 0)) {
			Starter.win();
		}
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
					ball.getSpeedX(), ball.getSpeedY()));
			ball.setSpeedY(brick.ChangeY(ball.getX(), ball.getY(),
					ball.getSpeedX(), ball.getSpeedY()));
			brick.addHit();
			if (brick.Disappear() == true) {
				lifeScore.increaseScore(10);
				graphs.remove(brick);
				root.getChildren().remove(brick);
				remainBrick--;
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
					myScene.getX() + myScene.getWidth() - Starter.PaddleWidth));
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
							RowNum * Starter.BrickHeight - Starter.PaddleHeight));
				}
			} else {
				paddle.setY(RowNum * Starter.BrickHeight - Starter.PaddleHeight);
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

	public final static Boolean throughWall() {
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

	private void bounceWithScreen() {
		if ((ball.getX() + Starter.BallWidth >= myScene.getX()
				+ myScene.getWidth())
				|| (ball.getX() <= 0)) {
			ball.setSpeedX(-ball.getSpeedX());
		}
		if (ball.getY() <= 0) {
			ball.setSpeedY(-ball.getSpeedY());
		}
		if (ball.getY() > myScene.getY() + myScene.getHeight()) {
			ball.reset();
			lifeScore.decreaseLife();
		}
	}

	public void createPowerUp(double posX, double posY) {
		Random random = new Random();
		int choice = (random.nextInt() % 3);
		PowerUp temp;
		if (choice == 0) {
			temp = new PowerThrough(posX, posY, Starter.pictures.get('t'));
			powerUpsFalling.add(new PowerThrough(posX, posY, Starter.pictures.get('t')));
		} else if (choice == 1) {
			temp = new PowerVertical(posX, posY, Starter.pictures.get('v'));
		} else {
			temp = new PowerStick(posX, posY, Starter.pictures.get('s'));
		}
		powerUpsFalling.add(temp);
		root.getChildren().add(temp);
	}

}
