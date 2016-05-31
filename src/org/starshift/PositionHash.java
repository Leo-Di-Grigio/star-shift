package org.starshift;

public class PositionHash {

	private static final int HASH_CELL_RADIUS = (int)(Const.STAR_RADIUS)*2;
	
	public static int hashCode(float x, float y) {
		return ((int)x / HASH_CELL_RADIUS) * 361275 | ((int)y / HASH_CELL_RADIUS) * 6328548;
	}
}
