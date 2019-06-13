
package com.fry.tetris;

import java.awt.Image;

public class Cell {
	private int row;
	private int col;
	private Image image;

	public Cell() {
	}

	public Cell(int row, int col, Image image) {
		this.row = row;
		this.col = col;
		this.image = image;
	}

	public int getRow() {
		return this.row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return this.col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void moveRight() {
		this.col += 1;
	}

	public void moveLeft() {
		this.col -= 1;
	}

	public void moveDown() {
		this.row += 1;
	}

	public String toString() {
		return "[" + this.row + "," + this.col + "]";
	}
}
