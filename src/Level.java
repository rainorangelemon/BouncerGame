public class Level {
	
	private char[][] numbers;
	public int rowNum,columnNum;
	
	Level(int rowNum, int columnNum){
		numbers=new char[rowNum][columnNum];
		this.rowNum=rowNum;
		this.columnNum=columnNum;
	}
	
	public void buildBrick(int layer, String bricks){
		if(layer>=rowNum){
		}else{
			for(int i = 0; (i<columnNum) && (i<bricks.length());i++){
				char item=bricks.charAt(i);
				if(((item-'1'>=0)&&('3'-item>=0))||(item=='_')){
					numbers[layer][i]=item;
				}
			}
		}
	}
	
	public char getChar(int row, int column){
		return numbers[row][column];
	}
	
}
