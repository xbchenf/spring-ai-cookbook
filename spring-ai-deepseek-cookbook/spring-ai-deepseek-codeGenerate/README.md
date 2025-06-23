

http://localhost:8080/code/ai/generateJavaCode?message=写一个冒泡排序

响应返回：
```
以下是冒泡排序（Bubble Sort）的 Java 实现代码，包含详细注释： ```java public class BubbleSort { /** * 冒泡排序算法 * @param arr 待排序的数组 */ public static void bubbleSort(int[] arr) { // 边界检查 if (arr == null || arr.length <= 1) { return; } int n = arr.length; // 外层循环控制排序轮数（最多需要 n-1 轮） for (int i = 0; i < n - 1; i++) { // 优化标志位：如果某一轮没有发生交换，说明数组已有序 boolean swapped = false; // 内层循环控制每轮比较次数（每轮结束后，最大的元素会沉到最后） for (int j = 0; j < n - 1 - i; j++) { // 如果前一个元素比后一个大，则交换 if (arr[j] > arr[j + 1]) { swap(arr, j, j + 1); swapped = true; } } // 如果没有发生交换，提前结束排序 if (!swapped) { break; } } } /** * 交换数组中两个元素的位置 * @param arr 数组 * @param i 第一个元素的索引 * @param j 第二个元素的索引 */ private static void swap(int[] arr, int i, int j) { int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp; } public static void main(String[] args) { int[] arr = {64, 34, 25, 12, 22, 11, 90}; System.out.println("排序前: " + Arrays.toString(arr)); bubbleSort(arr); System.out.println("排序后: " + Arrays.toString(arr)); } }
```
