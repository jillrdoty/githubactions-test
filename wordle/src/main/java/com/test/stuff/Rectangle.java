/*
 * (C) Copyright IBM Copr. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.test.stuff;

public class Rectangle {
	class Point {
		int x;
		int y;
		
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private Point coordUpperLeft;
	private Point coordLowerRight;
	
	public Rectangle(int coord1, int coord2, int coord3, int coord4) {
		coordUpperLeft = new Point(coord1, coord2);
		coordLowerRight = new Point(coord3, coord4);
	}
	
	public Point getCoordUpperLeft() {
		return coordUpperLeft;
	}
	public void setCoordUpperLeft(Point coordUpperLeft) {
		this.coordUpperLeft = coordUpperLeft;
	}
	public Point getCoordLowerRight() {
		return coordLowerRight;
	}
	public void setCoordLowerRight(Point coordLowerRight) {
		this.coordLowerRight = coordLowerRight;
	}
	
	
}
