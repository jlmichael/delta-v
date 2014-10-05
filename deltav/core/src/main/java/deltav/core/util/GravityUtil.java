package deltav.core.util;

import deltav.core.config.DeltaVGameConfig;
import deltav.core.model.DeltaVObject;

/**
 * A simple utility lib for doing gravity calculations.
 */
public class GravityUtil {

    /**
     * A class for composing the force of gravity in the X and Y directions.
     */
    public static class ForceOfGravity {
        public double forceInX;
        public double forceInY;
    }

    /**
     * A class for containing the various geometric measurements for objects in
     * space.
     */
    protected static class Geometry {
        public double centerX;
        public double centerY;
        public double deltaXFromCenter;
        public double deltaYFromCenter;
        public double distanceFromCenter;
    }

    /**
     * A class for getting the Geometry of an object with respect to the
     * screen.
     *
     * @param object The object to get the Geometry for.
     * @return A Geometry object storing the measurements for the object.
     */
    protected static Geometry getGeometryOfObject(DeltaVObject object) {
        double centerX = DeltaVGameConfig.SCREEN_WIDTH * 0.5;
        double centerY = DeltaVGameConfig.SCREEN_HEIGHT * 0.5;
        double deltaX = centerX - object.getxPosition();
        double deltaY = centerY - object.getyPosition();
        double distanceFromCenter = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        Geometry geometry = new Geometry();
        geometry.centerX = centerX;
        geometry.centerY = centerY;
        geometry.deltaXFromCenter = deltaX;
        geometry.deltaYFromCenter = deltaY;
        geometry.distanceFromCenter = distanceFromCenter;
        return geometry;
    }

    /**
     * Get the force of gravity acting on the object in both the X and Y
     * directions.
     *
     * @param object The object under the influence of gravity.
     * @return A ForceOfGravity object storing the x and y components of the
     *         force.
     */
    public static ForceOfGravity getForceOfGravityOnObject(DeltaVObject object) {
        // Get our distance from the center of the world.
        Geometry geometry = getGeometryOfObject(object);

        // Force of gravity is inversely proportional to the square of the
        // distance.
        double denominator = Math.pow(geometry.distanceFromCenter, 2);
        if (denominator < 0.01) {
            denominator = 0.01;
        }

        // We need to cap the acceleration of gravity or the physics become
        // a bit unstable near the black hole.
        double forceOfGravity = Math.min(
                DeltaVGameConfig.GRAVITY / denominator,
                DeltaVGameConfig.MAX_ACCEL_OF_GRAVITY
        );

        // Decompose the force into its X and Y components.  We need to account
        // for the dimensions of our screen as well, so we normalize each
        // component based on the ratio of screen dimension along the axis of
        // the component to the screen dimension along the orthogonal axis.
        // This gives us a nice, smooth pull towards the center of the screen.
        double angleOfForceVector = Math.atan(geometry.deltaYFromCenter / geometry.deltaXFromCenter);
        double accelerationInX = forceOfGravity * Math.sin(angleOfForceVector) * (geometry.centerX / geometry.centerY);
        double accelerationInY = forceOfGravity * Math.cos(angleOfForceVector) * (geometry.centerY / geometry.centerX);

        // Normalize the direction of the acceleration vectors.
        if ((object.getxPosition() > geometry.centerX && accelerationInX > 0) ||
                (object.getxPosition() <= geometry.centerX && accelerationInX < 0)) {
            accelerationInX *= -1;
        }
        if ((object.getyPosition() > geometry.centerY && accelerationInY > 0) ||
                (object.getyPosition() <= geometry.centerY && accelerationInY < 0)) {
            accelerationInY *= -1;
        }

        ForceOfGravity force = new ForceOfGravity();
        force.forceInX = accelerationInX;
        force.forceInY = accelerationInY;
        return force;
    }
}
