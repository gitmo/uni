package uebung4.aufgabe2;

import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;

import uebung4.aufgabe1.Task;

public class MandelTask implements Task<Segment, Segment> {

	private static final long serialVersionUID = 8385192269821267847L;

	private static double x_from = -2.2;
	private static double x_to = 1.0;

	private static double y_from = -1.15;
	private static double y_to = 1.25;

	private static double dx = (x_to - x_from);
	private static double dy = (y_to - y_from);

	public static double dx_dy = dx / dy;

	private int maxIterations;
	private int width, heigth;

	public MandelTask(int width, int heigth, int iterations) {
		this.width = width;
		this.heigth = heigth;
		this.maxIterations = iterations;
	}

	private int calculatePixelColor(double x0, double y0) {

		// Scaling
		x0 = dx / width * x0 + x_from;
		y0 = -(dy / heigth * y0 + y_from);

		double x = 0;
		double y = 0;
		double xtemp;

		int iteration = 0;

		while ((x * x + y * y <= 2 * 2) && iteration < maxIterations) {
			xtemp = x * x - y * y + x0;
			y = 2 * x * y + y0;
			x = xtemp;
			iteration++;
		}

		if (iteration >= maxIterations) {
			return 0;
		} else {
			float hue = (iteration / (float) maxIterations) + (180 / 360.0f);
			return Color.HSBtoRGB(hue, 1.0f, 0.9f);
		}
	}

	private Segment computeMandel(Segment r) {
		// Setting color pixel by pixel
		int[] rgbArray = r.getRgbArray();
		int color;
		for (int x = 0; x < r.getWidth(); ++x) {
			for (int y = 0; y < r.getHeigth(); ++y) {
				color = calculatePixelColor(r.getX() + x, r.getY() + y);
				rgbArray[y * r.getWidth() + x] = color;
			}
		}
		return r;
	}

	@SuppressWarnings("unused")
	private void trace(Segment r) {
		try {
			System.out.println(InetAddress.getLocalHost()
					+ ": MandelTask.exec(): Segment " + r);
		} catch (UnknownHostException e) {
		}
	}

	public Segment exec(Segment r) {
		// trace(r);
		return computeMandel(r);
	}
}
