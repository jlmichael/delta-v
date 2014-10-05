package deltav.core.config;

/**
 * A convenience class for centralizing game configuration elements, such as
 * how many pieces of debris to maintain in the world, etc.
 */
public class DeltaVGameConfig {
    /**
     * The gravitational constant in our game.  The force of gravity is
     * inversely proportional to the distance between the game object and the
     * center of the screen, and this value represents a kind of scaling factor
     * for tweaking the acceleration.
     */
    public static final float GRAVITY = 50000.0F;

    /**
     * The width and height of our world.
     */
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 850;

    /**
     * We need to put bounds on our acceleration to keep the physics from
     * becoming unstable near the black hole.
     */
    public static final double MAX_ACCEL_OF_GRAVITY = 100D;

    /**
     * The number of debris pieces to maintain the world.
     */
    public static final int NUMBER_OF_DEBRIS_PIECES = 20;

    /**
     * The strength of the ship's thruster.
     */
    public static final float THRUSTER_THRUST = 2.0F;

    /**
     * The speed at which the ship can rotate.
     */
    public static final double ROTATION_SPEED = Math.PI / 64;

    /**
     * The max amount of fuel the ship can carry.
     */
    public static final int MAX_FUEL = 500;

    /**
     * The max amount of fuel that any piece of debris can give when captured.
     */
    public static final int MAX_FUEL_PER_DEBRIS = 10;

    /**
     * The amount of fuel the black hole drains per tick in contact.
     */
    public static final int FUEL_DRAINED_BY_BLACK_HOLE = 10;

    /**
     * The number of points per piece of debris captured.
     */
    public static final int POINTS_PER_DEBRIS = 1;

    /**
     * The width of the black hole in the center of the world.
     */
    public static final int BLACK_HOLE_WIDTH = 50;

    /**
     * The height of the black hole in the center of the world.
     */
    public static final int BLACK_HOLE_HEIGHT = 50;

    /**
     * Using the above, we can compute some black hole geometry to use with
     * collision detection.
     */
    public static final double BLACK_HOLE_TOP_LEFT_X = SCREEN_WIDTH / 2 - BLACK_HOLE_WIDTH / 2;
    public static final double BLACK_HOLE_BOTTOM_RIGHT_X = SCREEN_WIDTH / 2 + BLACK_HOLE_WIDTH / 2;
    public static final double BLACK_HOLE_TOP_LEFT_Y = SCREEN_HEIGHT / 2 - BLACK_HOLE_HEIGHT / 2;
    public static final double BLACK_HOLE_BOTTOM_RIGHT_Y = SCREEN_HEIGHT / 2 + BLACK_HOLE_HEIGHT / 2;

}

