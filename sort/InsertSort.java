import java.lang.System;
import java.util.Arrays;

public class InsertSort
{
	private static final int[] a = {90, 34, 5, 42, 8, 51, 20, 23, 2, 3, 79, 33, 17, 88, 71, 41, 7, 12, 87, 4, 26, 83, 73, 9, 60, 21, 67, 40, 45, 57, 94, 91, 72, 55, 25, 35, 95, 46, 92, 59, 39, 16, 63, 6, 22, 75, 10, 84, 58, 56, 76, 32, 29, 44, 80, 65, 1, 66, 69, 82, 31, 37, 30, 81, 61, 47, 11, 19, 18, 99, 70, 54, 68, 97, 27, 62, 49, 13, 52, 93, 86, 89, 85, 78, 74, 38, 48, 43, 64, 96, 24, 77, 14, 36, 50, 15, 98, 53, 28};

	private static void sort()
	{
		for (int j = 1; j < a.length; j++)
		{
			int key = a[j];
			int i = j - 1;
			while (i >= 0 && a[i] > key)
			{
				a[i + 1] = a[i];
				i--;
			}
			a[i + 1] = key;
		}
	}

	private static void printArray (int[] a)
	{
		System.out.println (Arrays.toString (a));
	}

	public static void main (String[] args)
	{
		System.out.println ("Unsorted: ");
		printArray (a);
		sort();
		System.out.println ("Sorted: ");
		printArray (a);
	}
}
