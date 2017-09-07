public class Level {
	private int[][] a = new int[6][8];
	double panel_x=0;
	double panel_y=0;
	
	public void buildBrick(int layer, String bricks){
		if(layer>=6){			
		}else{
			for(int i = 0; i<bricks.length();i++){
				char item=bricks.charAt(i);
				if((item-'1'>=0)&&('3'-item>=0)){
					a[layer][i]=item-'0';
				}else if(item=='_'){
					
				}
			}
		}
	}
	
}
