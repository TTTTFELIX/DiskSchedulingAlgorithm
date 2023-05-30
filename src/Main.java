import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

//      create variables
        Scanner scanner = new Scanner(System.in);
        int data_option = -1;
        String file_name = "input.txt";

//      ask user for options
        System.out.println("Which way you want to provide the data? \n1.Generate 1000 Random Number\n2.Provide a file path that include 1000 numbers");
        data_option = scanner.nextInt();

//      generate random number into a file and read numbers from that file that just created
//      check if user choose number 1 option
        if (data_option == 1){
//          create variables
            Random ran = new Random();
            int number = 0;

//          try and catch error for writing 1000 random numbers into the input.txt file
            try (PrintWriter file = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter("input.txt")));
            ) {

//              use loop to generate 1000 random numbers
                for (int i = 1; i <= 1000; i++) {
                    number = ran.nextInt(4999);
                    file.println(number);
                }
            }

//          catch IOException and print it
            catch (IOException e) {
                e.printStackTrace();
            }

//          print file creation successful
            System.out.println("File input.txt has been created it has 1000 random numbers!");
        }

//      user choose option 2
        else {

//          ask for file path
            System.out.println("Enter file path (make sure the file path is correct): ");
            file_name = scanner.next();
        }


//      read 1000 numbers from a file which called input.txt or from the file path that user provided
        Scanner scan_file = new Scanner(new File(file_name));
        int[] disk_positions = new int[1000];
        int i = 0;
        while (scan_file.hasNextInt()) {
            disk_positions[i++] = scan_file.nextInt();
        }


//      create variables
        boolean flag = true;
        String option = "";
        int head = -1;
        int prev_head = -1;
        int order = -1;
//      create a int array to store the result, index 0 is total movement, index 1 is total change direction
        int[] final_result = new int[2];

//      using while loop to loop the options
        while (flag) {

//          ask for algorithm options
            System.out.println("Which Disk-Scheduling Algorithms: ");
            System.out.println(
                    "A    FCFS\n" +
                    "B    SSTF\n" +
                    "C    SCAN\n" +
                    "D    C-SCAN\n" +
                    "E    Exit");
            option = scanner.next();

//          if option is e or E then exit the program
            if (option.equals("E") || option.equals("e")) {
                flag = false;
                break;
            }
//            System.out.println("Enter Head Position: ");
//            head = scanner.nextInt();

//          get head input from command line arguments
            head = Integer.parseInt(args[0]);


//          if option is FCFS OR SSTF
            if (option.equals("A") || option.equals("B") || option.equals("a") || option.equals("b")){

//              FCFS
                if (option.equals("a") || option.equals("A")){

//                  using a array to store the result
                    final_result = FCFS(disk_positions, head);

//                  print result
                    System.out.println("FCFS result:");
                    System.out.println("The total movement is " + final_result[0] + "\nThe total number of change of direction is " + final_result[1] + "\n");
                }

//              SSTF
                else{

//                  using a array to store the result
                    final_result = SSTF(disk_positions, head);

//                  print result
                    System.out.println("SSTF result:");
                    System.out.println("The total movement is " + final_result[0] + "\nThe total number of change of direction is " + final_result[1] + "\n");
                }
            }

//          else gonna be SCAN or C-SCAN
            else {

//              ask for previous position
                System.out.println("Enter Previous Position: ");
                prev_head = scanner.nextInt();

//              determine the order of the algorithms
                if (head >= prev_head){

//                  moving from smaller to larger and set order to 1
                    order = 1;
                }
                else {

//                  moving from larger to smaller and set order to 0
                    order = 0;
                }

//              SCAN
                if (option.equals("C") || option.equals("c")){

//                  using a array to store the result
                    final_result = SCAN(disk_positions, head, order);

//                  print result
                    System.out.println("SCAN result:");
                    System.out.println("The total movement is " + final_result[0] + "\nThe total number of change of direction is " + final_result[1] + "\n");
                }

//              C-SCAN
                else {

//                  using a array to store the result
                    final_result = C_SCAN(disk_positions, head, order);

//                  print result
                    System.out.println("C-SCAN result:");
                    System.out.println("The total movement is " + final_result[0] + "\nThe total number of change of direction is " + final_result[1] + "\n");
                }
            }


        }
    }

    public static int[] FCFS (int[] arr, int head){

//      create variables
        int[] result = new int[2];
        int arr_order = -1;
        int total_movement = 0;
        int total_change_direction = 0;
        int res = -1;

//      determine the array order to help to record the total change of direction
        if (head <= arr[0]){
            arr_order = 1;

//          add movement between the first element and head
            total_movement += arr[0] - head;
        }else{
            arr_order = 0;
            total_movement += head - arr[0];
        }

//      using for loop to go through the array
        for (int i = 1; i < arr.length; i++){
//          calculate the the result
            res = arr[i] - arr[i - 1];
//          check if the res is greater than 0
            if (res > 0){
//              check if the direction is not equal to 1 then add 1 to total direction and change the array order to 1
                if (arr_order != 1){
                    arr_order = 1;
                    total_change_direction +=1;
                }
            }
//          check if the res is smaller than 0
            else if (res < 0){
                if (arr_order != 0){
                    arr_order = 0;
                    total_change_direction += 1;
                }
            }
            total_movement += Math.abs(res);
        }

//      add total_movement and total_change_direction to array and ready to return
        result[0] = total_movement;
        result[1] = total_change_direction;

//     return result
       return result;
    }

    public static int[] SSTF(int[] arr, int head) {

//        create variables
        int[] result = new int[2];
        int total_movement = 0;
        int total_change_direction = 0;
        int arr_order = -1;
        int left_index = -1;
        int right_index = -1;
        int left = -1;
        int right = -1;

//      sort the array and call the find function to find the nearest number index of the head and set right index to the return index
        Arrays.sort(arr);
        right_index = find(arr, head);

//      check if head index equal to 0 if not then left index will equal to head index - 1 otherwise it will equal to -1
        if (right_index != 0){

//          set the left index to right index - 1
            left_index = right_index - 1;
        }

//      using while loop to go through the array
        while (left_index >= 0 && right_index <= arr.length - 1){

//          using left and right to store the movement between the head and the right and left
            left = head - arr[left_index];
            right = arr[right_index] - head;

//          if right smaller than left the go right
            if (right < left){

//              add the movement to the total movement
                total_movement += right;

//              set the head to the right index in the array
                head = arr[right_index];

//              determine if the array order equal to 1
                if (arr_order != 1){

//                  if not then set array order equal to 1 and add 1 to total change direction
                    arr_order = 1;
                    total_change_direction += 1;
                }

//              move right index 1 more
                right_index += 1;
            }

//          go left
            else{

//              add the left movement to total movement
                total_movement += left;

//              set the head to the left index in the array
                head = arr[left_index];

//              determine if the array order equal to 0

                if (arr_order != 0){

//                  if not then set array order equal to 0 and add 0 to total change direction
                    arr_order = 0;
                    total_change_direction += 1;
                }

//              move left index 1 less
                left_index -= 1;
            }
        }

//      determine if the left run out of range
        if (left_index < 0){

//          if not then use while loop to loop the rest of the right part of the array
            while (right_index <= arr.length - 1){

//              do the calculation
                right = arr[right_index] - head;
                total_movement += right;
                head = arr[right_index];

//              check if the array order is 1
                if (arr_order != 1){
                    arr_order = 1;
                    total_change_direction += 1;
                }

//              move the right index 1 more
                right_index += 1;
            }
        }

//      determine if the right run out of range
        if (right_index > arr.length - 1){

//          if not then use while loop to loop the rest of the left part of the array
            while (left_index >= 0){

//              do calculation
                left = head - arr[left_index];
                total_movement += left;
                head = arr[left_index];

//              check if the array order is 0
                if (arr_order != 0){
                    arr_order = 0;
                    total_change_direction += 1;
                }

//              move the left index 1 less
                left_index -= 1;
            }
        }

//      put the total movement at index 0 and total change direction at index 1
        result[0] = total_movement;
        result[1] = total_change_direction - 1;

//      return result
        return result;
    }

    public static int find(int[] arr, int target) {

//      using for loop to find the index that number near the head
        for (int i = 0; i < arr.length; i++){
            if (arr[i] >= target){
                return i;
            }
        }

//      if not find return the last index of the array
        return arr.length - 1;
    }

    public static int[] SCAN(int[] arr, int head, int order){

//      create variables
        int[] result = new int[2];
        int total_movement = 0;
        int total_change_direction = 0;
        int right_index = -1;
        int left_index = -1;
        int res = -1;

//      sort array and get the right index by find function and set the left index to 1 less from the right index
        Arrays.sort(arr);
        right_index = find(arr, head);
        left_index = right_index - 1;

//      move from smaller to larger
        if (order == 1){
//          go to 4999 first
            while (right_index < arr.length){

//              do calculation
                res = arr[right_index] - head;
                total_movement += res;
                head = arr[right_index];
                right_index += 1;
            }

//          consider if the last element is not 4999 then do the calculation and set head to 4999
            if (arr[arr.length-1] != 4999){
                head = 4999;
                res = head - arr[arr.length - 1];
                total_movement += res;
            }

//          after 4999 go to left index all the way to index 0
//          determine if there has any elements in the left
            if (left_index >= 0) {

//              if yes then add a change of direction otherwise the change of direction is 0
                total_change_direction += 1;

//              go all the way to the left until the first element
                while (left_index >= 0){

//                  do calculation
                    res = head - arr[left_index];
                    total_movement += res;
                    head = arr[left_index];
                    left_index -= 1;
                }
            }
        }

//      move from larger to smaller
        else {

//          go to 0 first
            while (left_index >= 0){

//              do calculation
                res = head - arr[left_index];
                total_movement += res;
                head = arr[left_index];
                left_index -= 1;
            }

//          if the first element is not 0 then do the calculation and set head to 0
            if (arr[0] != 0){
                head = 0;
                total_movement += arr[0];
            }

//          after 4999 go to left index all the way to index 0
//          determine if there has any elements in the left
            if (right_index < arr.length) {

//              if yes then add a change of direction otherwise the change of direction is 0
                total_change_direction += 1;

//              go all the way to the right until the last element
                while (right_index < arr.length){

//                  do calculation
                    res = arr[right_index] - head;
                    total_movement += res;
                    head = arr[right_index];
                    right_index += 1;
                }
            }

        }

//      put the total movement at index 0 and total change direction at index 1
        result[0] = total_movement;
        result[1] = total_change_direction;

//      return result
        return result;
    }

    public static int[] C_SCAN(int[] arr, int head, int order){

//      create variables
        int[] result = new int[2];
        int total_movement = 0;
        int total_change_direction = 0;
        int right_index = -1;
        int left_index = -1;
        int res = -1;
        int index = -1;

//      sort array and get the right index by find function and set the left index to 1 less from the right index
        Arrays.sort(arr);
        right_index = find(arr, head);
        left_index = right_index - 1;

//      move from smaller to larger
        if (order == 1){

//          go to 4999 first
            while (right_index < arr.length){

//              do calculation
                res = arr[right_index] - head;
                total_movement += res;
                head = arr[right_index];
                right_index += 1;
            }

//          consider if the last element is not 4999 then do the calculation and set head to 4999
            if (arr[arr.length-1] != 4999){

//              do calculation
                head = 4999;
                res = head - arr[arr.length - 1];
                total_movement += res;
            }

//          after 4999 go to left index all the way to index 0
//          determine if there has any elements in the left
            if (left_index >= 0) {

//              total movement will increase by 4999 because 4999 - 0 and set head to 0
                total_movement += 4999;
                head = 0;

//              if yes then add a change of direction otherwise the change of direction is 0
                total_change_direction += 1;

//              set index to the first of the array
                index = 0;

//              go all the way to the left until the first element
                while (index <= left_index){

//                  do calculation
                    res = arr[index] - head;
                    total_movement += res;
                    head = arr[index];
                    index += 1;
                }
            }
        }

//      move from larger to smaller
        else {

//          go to 0 first
            while (left_index >= 0){

//              do calculation
                res = head - arr[left_index];
                total_movement += res;
                head = arr[left_index];
                left_index -= 1;
            }

//          if the first element is not 0 then do the calculation and set head to 0
            if (arr[0] != 0){
                head = 0;
                total_movement += arr[0];
            }

//          after 4999 go to left index all the way to index 0
//          determine if there has any elements in the left
            if (right_index < arr.length) {

//              total movement will increase by 4999 because 0 - 4999 and set head to 4999
                total_movement += 4999;
                head = 4999;

//              if yes then add a change of direction otherwise the change of direction is 0
                total_change_direction += 1;

//              set index to the last index of the array
                index = arr.length - 1;

//              go all the way to the right until the last element
                while (index >= right_index){

//                  do calculation
                    res = head - arr[index];
                    total_movement += res;
                    head = arr[index];
                    index -= 1;
                }
            }

        }

//      put the total movement at index 0 and total change direction at index 1
        result[0] = total_movement;
        result[1] = total_change_direction;

//      return result
        return result;
    }

}