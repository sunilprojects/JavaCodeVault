package com.ass;

import java.util.*;

public class GameTask {
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Scanner sc = new Scanner(System.in);
		List<Integer> scoreList = new ArrayList<>();//to get last added value for operations like 'd', 'c', and '+'.
		int sum = 0;

		System.out.println("Enter up the input:");
		String[] inputs = sc.nextLine().trim().split("\\s+");// read from input, remove spaces and split
		// looping through each input
		for (int i = 0; i < inputs.length && i < 100; i++) {
			String token = inputs[i];

			if (token.matches("-?\\d+")) {  // regex is used to validate
				// Input is a number between -999 and 999
				int num = Integer.parseInt(token);
				if (num >= -999 && num <= 999) {
					scoreList.add(num);// Adds the number to the score list
					sum += num;// adds to total score
					System.out.println("Added input: " + num + ", Score: " + sum);
				} else {
					System.out.println("Number out of valid range: " + num);
				}
			}
			//Handles both d and D by ignoring case.
			else if (token.equalsIgnoreCase("d")) {
				if (!scoreList.isEmpty()) {
					int last = scoreList.get(scoreList.size() - 1);//Getting the last number from the list.
					int doubled = last * 2;
					scoreList.add(doubled);
					sum += doubled;
					System.out.println("Doubled input: " + doubled + ", Score: " + sum);
				} else {
					System.out.println("No value to double.");
				}
			}
			//Removing the last entry
			else if (token.equalsIgnoreCase("c")) {
				for (int j = scoreList.size() - 1; j >= 0; j--) {// iterate back to find the last added value
					int removed = scoreList.remove(j);
					sum -= removed;
					System.out.println("Removed input: " + removed + ", Score: " + sum);
					break;
				}
			}
			//adding last two entries
			else if (token.equals("+")) {
				int count = 0, total = 0;

				// iterates from the end and adds last 2 values
				for (int j = scoreList.size() - 1; j >= 0 && count < 2; j--) {
					total += scoreList.get(j);
					count++;
				}
				//If we got 2 valid numbers, add their sum to the list and score.
				if (count == 2) {
					scoreList.add(total);
					sum += total;
					System.out.println("Added by '+': " + total + ", Score: " + sum);
				} else {
					System.out.println("Please add the inputs, not enough to apply '+'.");
				}
			}
			else {
				System.out.println("Invalid input: " + token);
			}
		}

		System.out.println("Total Score: " + sum);
		long endTime = System.currentTimeMillis();//end time
		System.out.println("Time taken:"+ (endTime - startTime)+ "ms");
	}
}
