import java.io.*;
import java.lang.Object;
import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Starter extends Application{

	public static HashMap<Character, Image> pictures = new HashMap<Character, Image> ();
	private static HashMap<Integer, Level> levels = new HashMap<Integer, Level> ();
	private static Stage s;
	public static LevelRunning currentLevel;
	static int currentNum;
	
	public static double PaddleHeight;
	public static double PaddleWidth;
	public static double BrickHeight;
	public static double BrickWidth;
	public static double BallHeight;
	public static double BallWidth;
	public static final Paint BACKGROUND = Color.WHITE;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	
	private static void loadFile(int level,String fileName){
		
		Level newLevel;
		File file = new File("block_config",fileName);
		BufferedReader reader = null;
		try{
            reader = new BufferedReader(new FileReader(file));
            String[] temp=reader.readLine().split(" ");
            System.out.printf("%d, %d\n",temp[0].charAt(0)-'0',temp[1].charAt(0)-'0');
            newLevel=new Level(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
            String tempString = null;  
            int line = 0;  
            // 一次读入一行，直到读入null为文件结束  
            while ((tempString = reader.readLine()) != null) {  
                // 显示行号 
            	newLevel.buildBrick(line,tempString);
                System.out.println("line " + line + ": " + tempString);  
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
		if(name=='1'){
			this.BrickHeight=image.getHeight();
			this.BrickWidth=image.getWidth();
		}else if(name=='_'){
			this.PaddleHeight=image.getHeight();
			this.PaddleWidth=image.getWidth();
		}else if(name=='.'){
			this.BallHeight=image.getHeight();
			this.BallWidth=image.getWidth();
		}
		pictures.put(name, image);
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
		currentLevel=new LevelRunning(levels.get(1));
		this.s=s;
		s.setScene(currentLevel.getScene(BACKGROUND));
        setStage(s);
     // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY/100.0),
        		e -> {
        			if(!anounce)
        				currentLevel.refresh(SECOND_DELAY*10);
        		});
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

	private static void setStage(Stage s) {
		s.setTitle("K-on!");
        s.show();
        currentLevel.myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
	}
	
	static void addLevel(){
		if(levels.get(currentNum+1)==null){
			goFinal();
		}else{
			currentNum++;
			winFlag=false;
			reset();
		}
	}
	
	static Boolean winFlag=false;
	static Boolean anounce=false;
	
	private static void handleKeyInput (KeyCode code) {
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
        if(code == KeyCode.SPACE){
        	currentLevel.ball.reset();
        }
    }
	
	public static void lose(){
		anounce=true;
		winFlag=false;
		Text t=new Text("You lose this level!\n" + "Press ENTER to restart");
		printText(t);
	}
	
	public static void win(){
		anounce=true;
		winFlag=true;
		Text t=new Text("You win this level!\n" + "Press ENTER to go to next level");
		printText(t);
	}
	
	public static void goFinal(){
		anounce=true;
		winFlag=true;
		Text t=new Text("You win the whole game!\n" + "Goodbye!");
		printText(t);
	}

	private static void printText(Text t) {
		t.setX(0);
		t.setY(0+36);
		t.setFill(Color.GREEN);
		t.setFont(Font.font(null, FontWeight.NORMAL, 36));
		currentLevel.root.getChildren().clear();
		currentLevel.root.getChildren().add(t);
	}
	
	public static void reset(){
		s.setScene(
		currentLevel.reset(levels.get(currentNum)));
		setStage(s);
	}

	
	public static void main(String[] args) {
		launch(args);
	}

}
