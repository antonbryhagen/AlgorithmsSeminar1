import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Task1 {

    public static void main(String[] args){


        boolean run = true;
        long start = 0;
        long end = 0;
        int[] arr = {};
        int target = 0;
        boolean targetFound = false;
        String pivotMethod = "";

        while(run){
            System.out.println("Select algorithm: ");
            System.out.println("1. QuickSort Recursive" +
                    "\n2. QuickSort Iterative" +
                    "\n3. InsertionSort Recursive" +
                    "\n4. InsertionSort Iterative" +
                    "\n5. BinarySearch Recursive" +
                    "\n6. BinarySearch Iterative" +
                    "\nUse exit to exit the program");
            Scanner input = new Scanner(System.in);
            String option = input.nextLine();
            System.out.println("Enter input size: ");
            int n = input.nextInt();
            System.out.println("Enter number of iterations: ");
            int iterations = input.nextInt();
            long totalTime = 0;

            if(option.equals("1") || option.equals("2")){
                input.nextLine(); // "eat" newline char from int input
                System.out.println("Enter pivot method (first, random or median3)");
                pivotMethod = input.nextLine();
            }

            for (int i = 0; i < iterations; i++){
                switch(option){
                    case "1":
                        arr = readNumbers(n);
                        start = System.nanoTime();
                        quickSortRecursive(arr, 0, arr.length-1, pivotMethod);
                        end = System.nanoTime();
                        break;
                    case "2":
                        arr = readNumbers(n);
                        start = System.nanoTime();
                        quickSortIterative(arr, 0, arr.length-1);
                        end = System.nanoTime();
                        break;
                    case "3":
                        arr = readNumbers(n);
                        start = System.nanoTime();
                        insertionSortRecursive(arr, arr.length-1);
                        end = System.nanoTime();
                        break;
                    case "4":
                        arr = readNumbers(n);
                        start = System.nanoTime();
                        insertionSortIterative(arr);
                        end = System.nanoTime();
                        break;
                    case "5":
                        //binary search using recursive quicksort with median3 pivot as sorting algorithm
                        arr = readNumbers(n);
                        System.out.println("Enter number to search for (0 <= target <= 100): ");
                        target = input.nextInt();
                        quickSortRecursive(arr, 0, arr.length-1, "median3");
                        start = System.nanoTime();
                        targetFound = binarySearchRecursive(arr, target, 0, arr.length-1);
                        end = System.nanoTime();
                        System.out.println("Target found: "+targetFound);
                        break;
                    case "exit":
                        i = iterations; //stop for loop
                        run = false;
                        break;
                }
                if (run){
                    for (int j = 0; j < arr.length; j++){
                        System.out.println(arr[j]);
                    }
                    System.out.println("\nRun time for algorithm: "+(end-start)+" nano seconds");
                    totalTime = totalTime + (end-start);
                }
            }

            if (run){
                System.out.println("Average run time for algorithm: "+(totalTime / iterations));
            }
        }

    }

    static int[] readNumbers(int n){
        int[] numbers = new int[n];
        try{
            File fileObj = new File("numbers.txt");
            Scanner reader = new Scanner(fileObj);
            int i = 0;
            while (reader.hasNextLine()){
                if (i < n){
                    int number = reader.nextInt();
                    numbers[i] = number;
                    i++;
                }else{
                    break;
                }
            }
        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
        return numbers;
    }
    public static void quickSortIterative(int[] numbers, int left, int right){
        // create extra stack/array
        int[] stack = new int[right - left + 1];
        //init top of stacK/array
        int top = -1;

        //push starting values of left and right to stack
        stack[++top] = left;
        stack[++top] = right;

        //pop from stack while not empty
        int p = 0;
        while (top >= 0){
            //pop right and left
            right = stack[top--];
            left = stack[top--];
            //set pivot elem at correct pos in sorted array
            p = partitioning(numbers, left, right);

            //if elem on left side of pivot, push left side to stack
            if (p-1 > left){
                stack[++top] = left;
                stack[++top] = p - 1;
            }
            //if elem on right side of pivot, push right side to stack
            if (p+1 < right){
                stack[++top] = p + 1;
                stack[++top] = right;
            }
        }
        //restore pivot to correct pos
        //swapReferences(numbers, p, right-1);

    }
    public static void quickSortRecursive(int[] numbers, int left, int right, String pivotMethod){

        if (left < right){
            int pivot = pivot(pivotMethod, numbers, left, right);

            //begin partitioning
            int i = left, j = right - 1;    //j = right - 1;
            //for (;;)
            while (i < j) {
                while (numbers[++i] < pivot) {
                }
                while (numbers[--j] > pivot) {
                }
                if (i < j) {
                    swapReferences(numbers, i, j);
                } else {
                    break;
                }
            }
            swapReferences(numbers, i, right - 1); //restore pivot
            quickSortRecursive(numbers, left, i-1, pivotMethod); //sort small
            quickSortRecursive(numbers, i+1, right, pivotMethod); //sort big
        }

    }

    public static int partitioning(int[] numbers, int left, int right){
        int pivot = median3(numbers, left, right);
        //int pivot = numbers[right];
        int i = (left-1); // index of smaller elem
        //for (int j = left; j <= right-1; j++) {
        for (int j = left; j < right-1; j++) {
            //elem smaller or equal to pivot
            if (numbers[j] <= pivot){
                i++;
                swapReferences(numbers, i, j);
            }
        }
        //swap i+1 and right (or pivot)
        swapReferences(numbers, i+1, right-1);

        return i + 1;
    }

    public static int pivot(String pivotMethod, int[] numbers, int left, int right){
        if (pivotMethod.equals("first")){
            return first(numbers, left, right);
        }else if (pivotMethod.equals("random")){
            return random(numbers, left, right);
        }
        return median3(numbers, left, right);
    }

    public static int first(int[] numbers, int left, int right){
        int pivot = numbers[0];
        //store pivot at second to last element
        swapReferences(numbers, 0, right-1);
        return numbers[right-1];
    }

    public static int random(int[] numbers, int left, int right){
        //randomize an index to take pivot value from
        Random rand = new Random();
        int pivot = rand.nextInt(right-left)+left;
        //store pivot at second to last element
        swapReferences(numbers, pivot, right-1);
        return numbers[right-1];
    }

    public static int median3(int[] numbers, int left, int right){
        int center = (left + right) / 2;
        if (numbers[center] < numbers[left]){
            swapReferences(numbers, left, center);
        }
        if (numbers[right] < numbers[left]){
            swapReferences(numbers, left, right);
        }
        if (numbers[right] < numbers[center]){
            swapReferences(numbers, center, right);
        }
        //pivot at pos right-1
        swapReferences(numbers, center, right-1);
        return numbers[right-1];
    }

    public static void swapReferences(int[] numbers, int a, int b){
        int temp = numbers[a];
        numbers[a] = numbers[b];
        numbers[b] = temp;
    }
    public static void insertionSortIterative(int[] numbers){
        int j;
        for (int p = 1; p < numbers.length; p++){
            int tmp = numbers[p];
            for (j = p; j > 0 && (tmp < numbers[j-1]); j--){
                numbers[j] = numbers[j-1];
            }
            numbers[j] = tmp;
        }
    }
    public static void insertionSortRecursive(int[] numbers, int n){
        if (n > 0){
            insertionSortRecursive(numbers, n-1);
            int x = numbers[n];
            int j = n-1;
            while (j >= 0 && numbers[j] > x){
                numbers[j+1] = numbers[j];
                j = j-1;
            }
            numbers[j+1] = x;
        }
    }

    public static boolean binarySearchRecursive(int[] numbers, int target, int low, int high){
        if (high >= low){
            int mid = low + (high - low) / 2;
            if (numbers[mid] == target){
                return true;
            }
            //search left half
            if (numbers[mid] > target){
                return binarySearchRecursive(numbers, target, low, mid-1);
            }
            //search right half
            return binarySearchRecursive(numbers, target, mid+1, high);
        }
        return false;
    }
    
}
