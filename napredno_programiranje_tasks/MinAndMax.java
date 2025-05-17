//package midterm1;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {
    private T min;
    private T max;
    private int counter;
    private int minCounter;
    private int maxCounter;

    public MinMax() {
        min = null;
        max = null;
        counter = 0;
        minCounter = 1;
        maxCounter = 1;
    }
    public T max() {
        return max;
    }
    public T min() {
        return min;
    }
    public void update(T element) {
        if (min == null) {
            min = element;
            max = element;
            minCounter = 1;
            maxCounter = 1;
        } else if (min.compareTo(element) == 0) {
            if (min == max) {
                maxCounter++;
            }
            minCounter++;
        } else if (max.compareTo(element) == 0) {
            maxCounter++;
        } else if (min.compareTo(element) > 0) {
            if (min == max) {
                minCounter = 0;
            }
            min = element;
            counter += minCounter;
            minCounter = 1;
        } else if (max.compareTo(element) < 0) {
            if (max == min) {
                maxCounter = 0;
            }
            max = element;
            counter += maxCounter;
            maxCounter = 1;
        } else {
            counter++;
        }
    }
    @Override
    public String toString() {
        return min + " " + max + " " + counter + "\n";
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
