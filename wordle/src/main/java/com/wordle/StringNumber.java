/*
 * (C) Copyright IBM Copr. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wordle;

public class StringNumber implements Comparable<StringNumber>{
	private Integer number;
	private String str;
	
	public StringNumber(String str, Integer number) {
		this.str = str;
		this.number = number;
	}

	@Override
	public int compareTo(StringNumber arg0) {
		return -number.compareTo(arg0.getNumber());
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
	
	
}
