/*
 * (C) Copyright IBM Copr. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.test.cohort;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.test.stuff.Stuff;

public class TestPalindrome {
	@Test
	public void testEvenPalindrome() {
		assertTrue(Stuff.isPalindrome("maddam"));
	}
	
	@Test
	public void testOddPalindrome() {
		assertTrue(Stuff.isPalindrome("madam"));
		assertTrue(Stuff.isPalindrome("d"));
	}
	
	@Test
	public void testNotPalindrome() {
		assertFalse(Stuff.isPalindrome("blah"));
		assertFalse(Stuff.isPalindrome("da"));
		assertFalse(Stuff.isPalindrome("dalgad"));
	}
	
	@Test
	public void testIntegerPalindrome() {
		assertFalse(Stuff.isPalindrome(1234567));
		assertFalse(Stuff.isPalindrome(12));
		assertTrue(Stuff.isPalindrome(123454321));
		assertTrue(Stuff.isPalindrome(1));
		assertTrue(Stuff.isPalindrome(222));
	}
	
//	@Test
//	public void testPrimes() {
//		Stuff.printPrimesTo(14);
//	}
//	
//	@Test
//	public void testPermutations() {
//		Stuff.printPerm("blog", "");
//	}
	
//	@Test
//	public void testFib() {
//		Stuff.printFib(25);
//	}
	
	@Test
	public void testArmstrongNumber() {
		assertTrue(Stuff.isArmstrongNumber(153));
		assertFalse(Stuff.isArmstrongNumber(152));
	}
	
	@Test
	public void testRemoveDups() {
		List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 3, 2);
		
		assertEquals(Lists.newArrayList(1,2,3,4), Stuff.removeDuplicates(intList));
	}
}
