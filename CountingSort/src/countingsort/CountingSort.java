package countingsort;

public class CountingSort {

    public static void main(String[] args) {

        int[] numbers = {4, 7, 2, 1, 2, 8, 6, 7, 5, 3, 0, 9};
        int[] sorted = countingSort(numbers);
        System.out.println("Result:");
        for (int num : sorted){
            // : is IN for each number in array numbers
            System.out.print(num + " ");
        }
        System.out.println();
    }
    
    public static int[] countingSort(int[] nums){
        
        int[] count = new int[nums.length];
        int[] result = new int[nums.length];
        
        for(int i = 0; i < nums.length - 1; i++){
            for(int j = i + 1; j < nums.length; j++){
                if(nums[i] < nums[j])
                    count[j]++;
                else
                    count[i]++;
                
            }
        }
        
        for (int i = 0; i < nums.length; i++){
            result[count[i]] = nums[i];
        }
        return result;
    }
    
}
