package me.bahadir.scramba;

import java.util.Random;

public class Util {
	/**
	 * Inclusive random integer generator
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randInt(int min, int max) {

	    // Usually this should be a field rather than a method variable so
	    // that it is not re-seeded every call.
	    Random rand = new Random();

	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
}
