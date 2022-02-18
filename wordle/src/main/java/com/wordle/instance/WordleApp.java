/*
 * (C) Copyright IBM Copr. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wordle.instance;

import java.util.List;

import org.openqa.selenium.WebElement;

public interface WordleApp {
	
	public enum LetterState {
		ABSENT,
		CORRECT,
		ELSEWHERE
	}
	
	public String getUrl();
	public WebElement getGame();
	public List<WebElement> getRows(WebElement game);
	
	public boolean isRowLockedIn(WebElement row);
	public List<WebElement> getLetters(WebElement row);
	public LetterState getLetterState(WebElement letter);
	public String getLetter(WebElement letter);
	
}
