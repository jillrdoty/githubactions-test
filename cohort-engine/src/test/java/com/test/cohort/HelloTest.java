package com.test.cohort.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HelloTest {
	public static final String helloString = "Hello actions";
	public static final String goodbyeString = "Goodbye actions";

	
	@Test
	public void helloTest() {
		assertEquals("Hello actions", helloString);
	}
	
	@Test
	public void goodbyeTest() {
		assertTrue(!"Hello actions".equals(goodbyeString));
	}
}

