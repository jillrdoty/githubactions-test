package com.test.cohort;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.wordle.Untested;

public class UntestedTest {
	private Untested untested = new Untested();
	
	@Test
	public void helloTest() {
		assertEquals(7, untested.doOne(1));
	}
	
	@Test
	public void goodbyeTest() {
		assertEquals(14, untested.doOne(2));
	}
}

