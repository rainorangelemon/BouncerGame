import java.util.HashSet;
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
	public static Scene myScene;
	private static LifeScore lifeScore=new LifeScore();
	public Group root;
	Paddle paddle=new Paddle(0,0,Starter.pictures.get('_'));
	Ball ball=new Ball(0,0,Starter.pictures.get('.'));
	
	LevelRunning(Level source){
		create(source);
	}
	
	public void create(Level source){
		RowNum=source.rowNum;
		ColumnNum=source.columnNum;
		setBricks(source);
		paddle.reset((ColumnNum/2)*Starter.PaddleWidth, 
					RowNum*Starter.BrickHeight-Starter.PaddleHeight);
		ball.reset(paddle.getX()+Starter.PaddleWidth/2-Starter.BallWidth/2,paddle.getY()-(Starter.BallHeight)-1);
		lifeScore.reset();
		System.out.printf("size: %d\n",graphs.size());
	}
	
	public Scene reset(Level source){
		removeAll2Root();
		graphs.clear();
		create(source);
		return getScene(background);
	}

	private void setBricks(Level source) {
		for(int i=0;i<RowNum;i++){
			for(int j=0;j<ColumnNum;j++){
				char temp=source.getChar(i, j);
				if((temp>='1')&&(temp<='3')){
					switch(temp){
						case '1':{
							Brick newBrick =new Brick1(Starter.pictures.get(temp),i,j);
							graphs.add(newBrick);
							break;
						}
						case '2':{
							Brick newBrick =new Brick2(Starter.pictures.get(temp),i,j);
							graphs.add(newBrick);
							break;
						}
						case '3':{
							Brick newBrick =new Brick3(Starter.pictures.get(temp),i,j);
							graphs.add(newBrick);
							break;
						}
					}
				}
			}
		}
	}
	
	public void removeAll2Root(){
		root.getChildren().clear();
	}
	
	public Scene getScene (Paint background) {
        this.background=background;
		// create one top level collection to organize the things in the scene
        root = new Group();
        // create a place to see the shapes
        myScene = new Scene(root, ColumnNum*(Starter.BrickWidth+1), RowNum*(Starter.BrickHeight), background);
        // make some shapes and set their properties
        addAll2Root();
        return myScene;
    }

	private void addAll2Root() {
		for(Brick graph:graphs){
        	root.getChildren().add(graph);
        }
        root.getChildren().add(ball);
        root.getChildren().add(paddle);
        root.getChildren().add(lifeScore);
	}
	
	public void refresh(double elapsedTime){
		for(Brick brick:graphs){
			if(bounceWithBrick(brick)==true)
				break;
		}
		bounceWithPaddle();
		bounceWithScreen();
		ballRefresh(elapsedTime);
		if(graphs.isEmpty()&&(lifeScore.getLife()>=0)){
			Starter.win();
		}
	}

	private Boolean bounceWithBrick(Brick brick) {
		if(ball.intersects(brick.getBoundsInParent())){
			double origSpeedX=ball.getSpeedX();
			double origSpeedY=ball.getSpeedY();
			ball.setSpeedX(brick.ChangeX(ball.getX(), ball.getY(), ball.getSpeedX(),ball.getSpeedY()));
			ball.setSpeedY(brick.ChangeY(ball.getX(), ball.getY(), ball.getSpeedX(),ball.getSpeedY()));
			if((ball.getSpeedX()==origSpeedX)&&(ball.getSpeedY()==origSpeedY)){
				ball.setSpeedY(-ball.getSpeedY());
			}
			brick.addHit();
			if(brick.Disappear()==true){
				graphs.remove(brick);
				root.getChildren().remove(brick);
			}
			return true;
		}
		return false;
	}

	private void ballRefresh(double elapsedTime) {
		ball.setX(ball.getX() + ball.getSpeedX() * elapsedTime);
        ball.setY(ball.getY() + ball.getSpeedY() * elapsedTime);
	}
	
	private double max(double a, double b){
		return a>b?a:b;
	}
	
	private double min(double a, double b){
		return a<b?a:b;
	}
	
	public void paddleRefresh(KeyCode code) {
		if (code == KeyCode.RIGHT) {
            paddle.setX( min( (paddle.getX() + Math.abs(paddle.getSpeedX())), myScene.getX()+myScene.getWidth()-Starter.PaddleWidth));
        }
        else if (code == KeyCode.LEFT) {
            paddle.setX( max( (paddle.getX() - Math.abs(paddle.getSpeedX())), myScene.getX()));
        }
        /*else if (code == KeyCode.UP) {
            paddle.setY(paddle.getY() - Math.abs(paddle.getSpeedX()));
        }
        else if (code == KeyCode.DOWN) {
            paddle.setY(paddle.getY() + Math.abs(paddle.getSpeedX()));
        }*/

	}

	private void bounceWithPaddle() {
		if(ball.intersects(paddle.getBoundsInParent())){
			ball.setSpeedX(paddle.ChangeX(ball.getX(), ball.getY(), ball.getSpeedX(),ball.getSpeedY()));
			ball.setSpeedY(paddle.ChangeY(ball.getX(), ball.getY(), ball.getSpeedX(),ball.getSpeedY()));
		}
	}

	private void bounceWithScreen() {
		if((ball.getX()+Starter.BallWidth>=myScene.getX()+myScene.getWidth())||
           (ball.getX()<=0)){
        	ball.setSpeedX(-ball.getSpeedX());
        }
		if(ball.getY()<=0){
     		ball.setSpeedY(-ball.getSpeedY());
        }
		if(ball.getY()>myScene.getY()+myScene.getHeight()){
			ball.reset();
			lifeScore.decreaseLife();
		}
	}
	
}
