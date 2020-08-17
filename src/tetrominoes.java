

public class tetrominoes {
	public int[][][] peices = {
		{	//I cyan
			{0,0,0,0},
			{1,1,1,1},
			{0,0,0,0},
			{0,0,0,0}
		},
		{	//O yellow
			{0,0,0,0},
			{0,1,1,0},
			{0,1,1,0},
			{0,0,0,0}
		},
		{	//L orange
			{1,0,0},
			{1,1,1},
			{0,0,0}
		},
		{	//J blue
			{0,0,1},
			{1,1,1},
			{0,0,0}
		},
		{	//S green
			{0,1,1},
			{1,1,0},
			{0,0,0}
		},
		{	//Z red
			{1,1,0},
			{0,1,1},
			{0,0,0}
		},
		{	//T purple
			{0,1,0},
			{1,1,1},
			{0,0,0}
		}
	};
	
	void rotate(int n) { // https://www.geeksforgeeks.org/inplace-rotate-square-matrix-by-90-degrees/
		int N = (n==0 || n==1) ? 4 : 3;
		for (int x = 0; x < N / 2; x++) {
	        // Consider elements in group 
	        // of 4 in current square 
	        for (int y = x; y < N - x - 1; y++) {
	            // Store current cell in 
	            // temp variable 
	            int temp = peices[n][x][y]; 
	            // Move values from right to top 
	            peices[n][x][y] = peices[n][y][N - 1 - x]; 
	            // Move values from bottom to right 
	            peices[n][y][N - 1 - x] 
	                = peices[n][N - 1 - x][N - 1 - y]; 
	            // Move values from left to bottom 
	            peices[n][N - 1 - x][N - 1 - y] 
	                = peices[n][N - 1 - y][x]; 
	            // Assign temp to left 
	            peices[n][N - 1 - y][x] = temp; 
	        }
		}
//		
	//function end
	}
// class end
}
