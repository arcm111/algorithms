import java.util.Arrays;

/**
 * Finds maximum acheivable profit of a stock price given in advance over a 
 * set period of time, where it is only allowed to buy once and to sell again 
 * at a later date during that period.
 */
public class MaximumSubarray {
    /**
     * Divide and conquer algorithm.
     * Split in half, find max profit in each half, then find max profit 
     * crossing the middle and return the largest of three.
     * Running time is <em>O(nlgn)</em>.
     * Space complexity is <em>O(n)</em>.
     * @param prices the array of stock prices over a period of time
     * @return maximum-profit subarray (buying date, selling date, and profit)
     */
    public static int[] findMaximumSubarray(int[] prices) {
        int[] change = getPriceDifference(prices);
        int[] maxSubarray = findMaximumSubarray(change, 0, change.length - 1);
        if (maxSubarray[2] <= 0) return new int[] {-1, -1, 0};
        return new int[] {maxSubarray[0], maxSubarray[1] + 1, maxSubarray[2]};
    }

    private static int[] findMaximumSubarray(int[] a, int low, int high) {
        if (high == low) return new int[] {low, high, a[low]};
        int mid = (high + low) / 2;
        int[] left = findMaximumSubarray(a, low, mid);
        int[] right = findMaximumSubarray(a, mid + 1, high);
        int[] crossing = findMaxCrossingSubarray(a, low, mid, high);
        if (left[2] >= right[2] && left[2] >= crossing[2]) return left;
        if (right[2] >= left[2] && right[2] >= crossing[2]) return right;
        return crossing;
    }

    private static int[] 
            findMaxCrossingSubarray(int[] a, int low, int mid, int high) {
        int leftSum = Integer.MIN_VALUE;
        int maxLeft = -1;
        int sum = 0;
        for (int i = mid; i >= low; i--) {
            sum += a[i];
            if (sum > leftSum) {
                leftSum = sum;
                maxLeft = i;
            }
        }
        int rightSum = Integer.MIN_VALUE;
        int maxRight = -1;
        sum = 0;
        for (int j = mid + 1; j <= high; j++) {
            sum += a[j];
            if (sum > rightSum) {
                rightSum = sum;
                maxRight = j;
            }
        }
        return new int[] {maxLeft, maxRight, leftSum + rightSum};
    }

    /**
     * Finds maximum subarray using <b>Kadane's</b> algorithm.
     * Running time is <em>O(n)</em>.
     * Space complexity is <em>O(1)</em>.
     * @param prices the array of stock prices over a period of time
     * @return maximum-profit subarray (buying date, selling date, and profit)
     */
    public static int[] findMaximumSubarrayLinear(int[] prices) {
        int[] change = getPriceDifference(prices);
        int totalMax = change[0];
        int curMax = change[0];
        int maxBuying = -1;
        int maxSelling = -1;
        int buying = 0;
        for (int i = 1; i < change.length; i++) {
            curMax += change[i];
            if (curMax < 0) {
                curMax = 0;
                buying = i + 1;
            } else if (curMax > totalMax) {
                totalMax = curMax;
                maxBuying = buying;
                maxSelling = i;
            }
        }
        if (totalMax <= 0) return new int[] {-1, -1, 0};
        return new int[] {maxBuying, maxSelling + 1, totalMax};
    }

    /**
     * Converts list of prices to list of differences between every two
     * consecutive items.
     * @param prices the list of prices
     * @return the list of consecutive price differences
     */
    private static int[] getPriceDifference(int[] prices) {
        int[] change = new int[prices.length - 1];
        for (int i = 0; i < prices.length - 1; i++) {
            change[i] = prices[i + 1] - prices[i];
        }
        return change;
    }

    public static void main(String[] args) {
        int[] prices = {100, 113, 110, 85, 105, 102, 86, 63, 81, 101, 94, 106, 
                101, 79, 94, 90, 97};
        System.out.println(Arrays.toString(prices));
        System.out.println("=> " + Arrays.toString(getPriceDifference(prices)));
        int[] max = findMaximumSubarray(prices);
        System.out.println(max[0] + ":" + max[1] + " => " + max[2]);
        max = findMaximumSubarrayLinear(prices);
        System.out.println(max[0] + ":" + max[1] + " => " + max[2]);

        System.out.println("Testing all positive integers: ");
        prices = new int[] {1, 2, 3, 4, 5, 6, 7};
        System.out.println(Arrays.toString(prices));
        System.out.println("=> " + Arrays.toString(getPriceDifference(prices)));
        max = findMaximumSubarray(prices);
        System.out.println(max[0] + ":" + max[1] + " => " + max[2]);
        max = findMaximumSubarrayLinear(prices);
        System.out.println(max[0] + ":" + max[1] + " => " + max[2]);

        System.out.println("Testing all negative integers: ");
        prices = new int[] {7, 6, 5, 4, 3, 2, 1};
        System.out.println(Arrays.toString(prices));
        System.out.println("=> " + Arrays.toString(getPriceDifference(prices)));
        max = findMaximumSubarray(prices);
        System.out.println(max[0] + ":" + max[1] + " => " + max[2]);
        max = findMaximumSubarrayLinear(prices);
        System.out.println(max[0] + ":" + max[1] + " => " + max[2]);
    }
}
