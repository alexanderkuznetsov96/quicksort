package dev.skz;

import dev.skz.Main.QuickSort.PartitionType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
	      int[] arr1 = getArray();
        int[] arr2 = arr1.clone();
        int[] arr3 = arr1.clone();

	      QuickSort qsTestFirst = new QuickSort(PartitionType.FIRST_INDEX);
        QuickSort qsTestFinal = new QuickSort(PartitionType.FINAL_INDEX);
        QuickSort qsTestMedian = new QuickSort(PartitionType.MEDIAN_OF_THREE);

        qsTestFirst.sort(arr1);
        qsTestFinal.sort(arr2);
        qsTestMedian.sort(arr3);

        // for (int i: arr1) {
        //     System.out.print(i + " ");
        // }
        System.out.println("Type: First numComparisons: " + qsTestFirst.numComparisons);

        // for (int i: arr2) {
        //     System.out.print(i + " ");
        // }
        System.out.println("Type: Final numComparisons: " + qsTestFinal.numComparisons);

        // for (int i: arr3) {
        //     System.out.print(i + " ");
        // }
        System.out.println("Type: Median of three numComparisons: " + qsTestMedian.numComparisons);
    }

    public static int[] getArray() {
        return readFileIntoIntegerList("IntegerArray.txt").stream().mapToInt(i -> i).toArray();
    }

    public static List<Integer> readFileIntoIntegerList(String file) {
        return readFileIntoList(file).stream()
            .map(Integer::parseInt).collect(Collectors.toList());
    }

    public static List<String> readFileIntoList(String file) {
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static class QuickSort {

        public final PartitionType partitionType;

        public QuickSort(PartitionType partitionType) {
            this.partitionType = partitionType;
        }

        public enum PartitionType {
            FIRST_INDEX,
            FINAL_INDEX,
            MEDIAN_OF_THREE,
        }

        public int numComparisons = 0;

        public void sort(int[] arr) {
            sort(arr, 0, arr.length - 1);
        }

        public void sort(int[] arr, int startIndex, int endIndex) {
            int length = endIndex - startIndex + 1;
            if (length < 2) {
                return;
            }
            numComparisons += length - 1;

            // choose pivot
            int pivotIndex;
            switch (partitionType) {
                case FINAL_INDEX:
                    pivotIndex = endIndex;
                    break;
                case MEDIAN_OF_THREE:
                    int i = startIndex;
                    int j = endIndex;
                    int k = (startIndex + endIndex)/2;
                    if (arr[i] > arr[j]) {
                        i ^= j;
                        j ^= i;
                        i ^= j;
                    }
                    if (arr[k] < arr[i]) {
                        pivotIndex = i;
                    } else if (arr[k] > arr[j]) {
                        pivotIndex = j;
                    } else {
                        pivotIndex = k;
                    }
                    break;
                default: // same as FIRST_INDEX
                    pivotIndex = startIndex;
            }

            swap(arr, startIndex, pivotIndex);
            int pivotValue = arr[startIndex];

            int startOfGreaterThanPivotPartitionIndex = startIndex + 1;
            for (int startOfUnknownPartition = startOfGreaterThanPivotPartitionIndex; startOfUnknownPartition <= endIndex; startOfUnknownPartition++) {
                if (arr[startOfUnknownPartition] < pivotValue) {
                    swap(arr, startOfUnknownPartition, startOfGreaterThanPivotPartitionIndex);
                    startOfGreaterThanPivotPartitionIndex++;
                }
            }
            // swap pivot back
            swap(arr, startIndex, startOfGreaterThanPivotPartitionIndex - 1);

            sort(arr, startIndex, startOfGreaterThanPivotPartitionIndex - 2);
            sort(arr, startOfGreaterThanPivotPartitionIndex, endIndex);
        }

        public void swap(int[] arr, int i, int j) {
            if (i == j) {
                return;
            }
            arr[i] ^= arr[j]; // x = x^y
            arr[j] ^= arr[i]; // y = y^(x^y) = x
            arr[i] ^= arr[j]; // x = (x^y)^(x) = y
        }
    }
}
