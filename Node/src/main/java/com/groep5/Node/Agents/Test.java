package com.groep5.Node.Agents;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many items do you want to print?");
        int items, previous, next;
        items = scanner.nextInt();
        previous = 0;
        next = 1;

        for (int i = 1; i <= items; ++i)
        {
            System.out.println(previous);
            int sum = previous + next;
            previous = next;
            next = sum;
        }
    }
}