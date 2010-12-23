package uebung4.aufgabe2;

import java.io.Serializable;

public class Segment implements Serializable {

	private static final long serialVersionUID = 1L;

	private int x, width;
	private int y, heigth;

	private int[] rgbArray;

	public Segment(int x, int y, int width, int heigth) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.heigth = heigth;
		this.setRgbArray(new int[width * heigth]);
	}

	public int getHeigth() {
		return heigth;
	}

	/*
	 * Getters & Setters
	 */

	public int[] getRgbArray() {
		return rgbArray;
	}

	public int getWidth() {
		return width;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}

	public void setRgbArray(int[] rgbArray) {
		this.rgbArray = rgbArray;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "][" + width + "," + heigth + "]";
	}
}
