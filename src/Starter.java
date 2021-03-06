import java.io.*;
import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Starter extends Application{

	private HashMap<Character, Image> pictures = new HashMap<Character, Image> ();
	private HashMap<Integer, Level> levels = new HashMap<Integer, Level> ();
	private HashMap<KeyCode, Integer> keyValue= new HashMap<KeyCode, Integer>() {{
		put(KeyCode.DIGIT0,0);
		put(KeyCode.DIGIT1,1);
		put(KeyCode.DIGIT2,2);
		put(KeyCode.DIGIT3,3);
		put(KeyCode.DIGIT4,4);
		put(KeyCode.DIGIT5,5);
		put(KeyCode.DIGIT6,6);
		put(KeyCode.DIGIT7,7);
		put(KeyCode.DIGIT8,8);
		put(KeyCode.DIGIT9,9);
	}};
	private Stage s;
	private LevelRunning currentLevel;
	private int currentNum;
	private Boolean winFlag=false;
	private Boolean anounce=true;
	
	private final Paint BACKGROUND = Color.WHITE;
	private double BrickHeight;
	private double BrickWidth;
	private double PaddleHeight;
	private double PaddleWidth;
	private double BallHeight;
	private double BallWidth;
	private double PowerUpHeight;
	private double PowerUpWidth;
	static final int FRAMES_PER_SECOND = 60;
    static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	
	private void loadFile(int level,String fileName){
		
		Level newLevel;
		File file = new File("block_config",fileName);
		BufferedReader reader = null;
		try{
            reader = new BufferedReader(new FileReader(file));
            String[] temp=reader.readLine().split(" ");
            newLevel=new Level(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
            String tempString = null;  
            int line = 0;  
            // 一次读入一行，直到读入null为文件结束  
            while ((tempString = reader.readLine()) != null) {  
                // 显示行号 
            	newLevel.buildBrick(line,tempString);
                line++;  
            }  
            reader.close();
            levels.put(level, newLevel);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private final void loadPicture(char name, String path){
		Image image =  new Image(getClass().getClassLoader().getResourceAsStream(path));
		pictures.put(name, image);
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
	
	public void loadData(){
		loadFile(1,"1.txt");
		loadFile(2,"2.txt");
		loadFile(3,"3.txt");
		loadPicture('1', "brick9.gif");
		loadPicture('2', "brick2.gif");
		loadPicture('3', "brick3.gif");
		loadPicture('_', "paddle.gif");
		loadPicture('.',"ball.gif");
		loadPicture('t',"extraballpower.gif");
		loadPicture('v',"laserpower.gif");
		loadPicture('s',"pointspower.gif");
	}
	
	@Override
    public void start (Stage s) {
		loadData();
		currentNum=1;
		currentLevel=new LevelRunning(levels.get(currentNum),pictures,currentNum);
		this.s=s;
		reset();
		goHelp();
     // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY/100.0),
        		e -> {
        			if(!anounce){
        				String temp=currentLevel.refresh(SECOND_DELAY*10);
        				if(temp=="win"){
        					win();
        				}else if(temp=="lose"){
        					lose();
        				}
        			}
        		});
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

	private void setStage(Stage s) {
		s.setTitle("K-on!");
        s.show();
        s.sizeToScene();
        currentLevel.getMyScene().setOnKeyPressed(e -> handleKeyInput(e.getCode()));
	}
	
	void goHelp(){
		anounce=true;
		winFlag=false;
		currentLevel.getRoot().getChildren().clear();
	    designText("Hey!"+" Press ENTER to begin\n",0,0+12,Color.GREEN,Font.font(null, FontWeight.NORMAL, 12));
	    ImageView picture;
		picture = addPicture('.', 0, 12, BallWidth, BallHeight,"This is ball\n");
		picture = addPicture('1', 0, picture.getY()+BallHeight+4, BrickWidth, BrickHeight,"This is brick1. You can break it within one hit.");
		picture = addPicture('2', 0, picture.getY()+BrickHeight+8, BrickWidth, BrickHeight,"This is brick2. You can break it within three hits, and then it will drop a random PowerUp.");		
		picture = addPicture('3', 0, picture.getY()+BrickHeight+8, BrickWidth, BrickHeight,"This is brick3. You cannot break it.");
		picture = addPicture('_', 0, picture.getY()+BrickHeight+8, PaddleWidth, PaddleHeight,"This is paddle. You can use ←， → to make it move horizontally,\n"
							+ "and use ↑，↓ to make it move horizontally when having related PowerUp.");
		picture = addPicture('t', 0, picture.getY()+BrickHeight+12, PowerUpWidth, PowerUpHeight,"This is the PowerUp to break through the wall.\nAfter you have this, it can go throught any brick3.");
		picture = addPicture('s', 0, picture.getY()+PowerUpHeight+16, PowerUpWidth, PowerUpHeight,"This is the PowerUp to stick the ball.\nAfter you have this, you can press SPACE to catch the ball in any time, \nand shoot the ball again after releasing SPACE.");
		picture = addPicture('v', 0, picture.getY()+PowerUpHeight+30, PowerUpWidth, PowerUpHeight,"This is the PowerUp to move paddle vertically.\nAfter you have this, you can move the paddle vertically.");
		designText("Every powerUp will be caught only when it touches the paddle, and it will last for about 20 seconds.", 0, picture.getY()+PowerUpHeight+30, Color.GREEN, Font.font(null, FontWeight.NORMAL, 12));
		designText("The paddle has a permanent ability. When ball hits paddle from the left into left part of the paddle, its horizontal direction will reverse. \nSame logic to the ball from right hit into the right part of paddle.", 0, picture.getY()+PowerUpHeight+50, Color.GREEN, Font.font(null, FontWeight.NORMAL, 12));
		designText("You can press i in [1,n] to go to the ith level.", 0, picture.getY()+PowerUpHeight+80, Color.GREEN, Font.font(null, FontWeight.NORMAL, 12));
		s.setHeight(picture.getY()+PowerUpHeight+120);
		s.setWidth(800);
	}

	private ImageView addPicture(char c, double posX, double posY, double width, double height, String desc) {
		ImageView image=new ImageView(pictures.get(c));
		image.setX(posX);
		image.setY(posY);
		currentLevel.getRoot().getChildren().add(image);
		designText(desc, width, image.getY()+height, Color.GREEN, Font.font(null, FontWeight.NORMAL, 12));
		return image;
	}
	
	void goLevel(int i){
		if(levels.get(i)==null){
		}else{
			currentNum=i;
			winFlag=false;
			reset();
		}
	}
	
	void addLevel(){
		if(levels.get(currentNum+1)==null){
			goFinal();
		}else{
			currentNum++;
			winFlag=false;
			reset();
		}
	}
	
	private void handleKeyInput (KeyCode code) {
        currentLevel.paddleRefresh(code);
        if (code == KeyCode.ENTER) {
        	anounce=false;
        	if(winFlag==true){
        		addLevel();
        	}else{
        		reset();
        	}
        }
        if(code == KeyCode.N){
        	win();
        }
        if((code == KeyCode.SPACE)&&(currentLevel.stickBall())){
        	currentLevel.resetBall();
        }
        if(code.isDigitKey()){
        	goLevel(keyValue.get(code));
        }
    }
	
	public void lose(){
		anounce=true;
		winFlag=false;
		printText("You lose this level!\n" + "Press ENTER to restart");
	}
	
	public void win(){
		anounce=true;
		winFlag=true;
		printText("You win this level!\n" + "Press ENTER to go to next level");
	}
	
	public void goFinal(){
		anounce=true;
		winFlag=true;
		printText("You win the whole game!\n" + "Goodbye!");
	}

	private void printText(String passage) {
		currentLevel.getRoot().getChildren().clear();
		designText(passage,0,0+36,Color.GREEN,Font.font(null, FontWeight.NORMAL, 36));
	}
	
	private Text designText(String passage, double posX, double posY, Color color, Font font){
		Text t=designText(passage, color, font);
		t.setX(posX);
		t.setY(posY);
		return t;
	}
	
	private Text designText(String passage, Color color, Font font){
		Text t=new Text();
		t.setText(passage);
		t.setFill(color);
		t.setFont(font);
		currentLevel.getRoot().getChildren().add(t);
		return t;
	}
	
	
	public void reset(){
		s.setScene(currentLevel.reset(levels.get(currentNum),currentNum));
		setStage(s);
	}
	
	public final Image getPicture(char ch){
		return pictures.get(ch);
	}

	
	public static void main(String[] args) {
		launch(args);
	}

}
