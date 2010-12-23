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
		this.rgbArray = new int[width * heigth];
	}

	/*
	 * Getters & Setters
	 */

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int[] getRgbArray() {
		return rgbArray;
	}

	public int getWidth() {
		return width;
	}

	public int getHeigth() {
		return heigth;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "][" + width + "," + heigth + "]";
	}
}
