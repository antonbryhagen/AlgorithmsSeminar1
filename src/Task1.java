public class Task1 {

    public static void main(String[] args){
        int[] arr = readNumbers();
        for (int i = 0; i < arr.length; i++){
            System.out.print(arr[i]);
        }
        System.out.println();
        //quickSortRecursive(arr, 0,arr.length-1);
        insertionSortRecursive(arr, arr.length-1);
        for (int i = 0; i < arr.length; i++){
            System.out.print(arr[i]);
        }
    }
    static int[] readNumbers(){
        //int[] numbers = {9,8,7,6,5,4,3,2,1};
        int[] numbers = {5, 7, 3, 2, 6, 1};
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
    public static void quickSortRecursive(int[] numbers, int left, int right){
        if (left < right){
            int pivot = median3(numbers, left, right);
            //begin partitioning
            int i = left, j = right - 1;
            for (;;){
                while (numbers[++i] < pivot) {}
                while (numbers[--j] > pivot) {}
                if (i < j){
                    swapReferences(numbers, i, j);
                }else {
                    break;
                }
            }
            swapReferences(numbers, i, right - 1); //restore pivot
            quickSortRecursive(numbers, left, i-1); //sort small
            quickSortRecursive(numbers, i+1, right); //sort big
        }


    }
    public static int partitioning(int[] numbers, int left, int right){
        int pivot = median3(numbers, left, right);
        //int pivot = numbers[right];
        int i = (left-1); // index of smaller elem
        for (int j = left; j <= right-1; j++) {
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
}
