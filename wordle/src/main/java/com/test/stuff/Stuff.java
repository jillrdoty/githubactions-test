package com.test.stuff;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * (C) Copyright IBM Copr. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

public class Stuff {
	public static boolean isPalindrome(String word) {
		int length = word.length();
		
		for(int i = 0; i < length / 2; i++) {
			if(word.charAt(i) != word.charAt(length - i - 1)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isPalindrome(int num) {
		//convert to string, call above
		String word = "";
		while(num > 0) {
			word += String.valueOf(num % 10);
			num = num / 10;
		}
		
		return isPalindrome(word);
	}
	
	public static void printPerm(String word, String ans) {
		if(word.length() == 0) {
			System.out.println(ans);
		}
		
		for(int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			String sub = word.substring(0, i) + word.substring(i+1);
			printPerm(sub, ans+ch);
		}
	}
	
	// o(n^2)
	public static void printPrimesTo(int max) {
		for(int i = 1; i <= max; i++) {
			if(isPrime(i))
			{
				System.out.println(i);
			}
		}
	}
	
	private static boolean isPrime(int num) {
		// Can divide by 2 since any number is not divisible by anything greater than half of itself
		for(int i = 2; i < num/2; i++) {
			
			//first imp
//			if((num/i) == ((double)num / (double)i)) {
//				return false;
//			}
			
			if(num % i == 0) {
				return false;
			}
		}
		return true;
	}
	
//	public static boolean rectanglesOverlap(Rectangle one, Rectangle two) {
//		
//		if(two.getCoordLowerRight().x < one.getCoordUpperLeft().x || one.getCoordLowerRight().x < two.getCoordUpperLeft().x)
//			return false;
//		}
//		
//	}
	
	public static void printFib(int max) {
		System.out.println(1);
		printFib(1, 1, max);
	}
	
	private static void printFib(int prev, int current, int max) {
		System.out.println(current);
		
		if((prev + current) < max) {
			printFib(current, prev + current, max);
		}
		
	}
	
	public static void printFibIter(int max) {
		System.out.println(1);
		for(int i = 1; i < max; i+=i) {
			System.out.println(i);
		}
	}
	
	public static boolean isArmstrongNumber(int num) {
		Set<Integer> numbers = new HashSet<>();
		
		int copyNum = num;
		while(copyNum != 0) {
			numbers.add(copyNum % 10);
			copyNum = copyNum / 10;
		}
		
		int sum = 0;
		for(Integer n : numbers) {
			sum += n*n*n;
		}
		
		if(num == sum) {
			return true;
		}
		return false;
	}
	
	public static <T extends Object> List<T> removeDuplicates(List<T> items) {
//		Set<T> unqiue = new HashSet<T>();
		
		return items.stream().distinct().collect(Collectors.toList());
	}
}
