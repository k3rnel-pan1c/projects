public class Solution {
    public static void main(String[] args){
        Solution solution = new Solution();
        int[][]  grid = {{1,1,1,1,1},{1,0,0,0,1},{1,0,1,0,1},{1,0,0,0,1},{1,1,1,1,1}};
        solution.shortestBridge(grid);
    }
    public int shortestBridge(int[][] grid) {
        int gridSize = grid.length*grid[0].length;
        int shortestPath;
        for(int i =0;i<grid.length;i++){
            for(int j =0;i<grid[i].length;i++){
                if(grid[i][j]==1){

                }
            }
        }
        return 0;
    }

    public int pathFinder(int [][] grid,int x,int y){
        int maxPathLength = (2* grid.length)-3;
        for(int i = 0;i<maxPathLength;i++){

        }

        return 0;
    }

    public boolean allOnesConect(int grid[][]){
        int numberOfOnes=0;
        for(int i =0;i<grid.length;i++){
            for(int j =0;i<grid[i].length;i++){
                if(grid[i][j]==1){
                    numberOfOnes++;
                }
            }
        }
        boolean visited[][] = new boolean[grid.length][grid[0].length];
        int connectedOnes=0;
        for(int i =0;i<grid.length;i++){
            for(int j =0;i<grid[i].length;i++){
                if(grid[i][j]==1){
                    
                }
            }
        }



    }
}
