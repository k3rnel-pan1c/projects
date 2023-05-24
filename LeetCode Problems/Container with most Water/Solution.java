public class Solution {
    public static void main(String[] args){
        int[] height = {1,8,6,2,5,4,8,3,7};
        Solution solution = new Solution();
        System.out.println(solution.maxArea(height));
    }
    public int maxArea(int[] height) {
        int mostWater=0;
        int smallerHeight=0;
        int area;
        for(int i = 0;i<height.length;i++){
            for(int j = i;j<(height.length);j++){
                smallerHeight=Math.min(height[i],height[j]);
                area=area(smallerHeight, j-i);
                if(mostWater<area){
                    mostWater=area;
                }
            }
        }
        return mostWater;
    }

    public int area(int height, int length){
        return height*length;
    }
}
