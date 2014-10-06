package deltav.core.model;

import deltav.core.config.DeltaVGameConfig;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeltaVObjectTest {

    /**
     * This test will exercise our GravityUtil methods as well.
     * @throws Exception
     */
    @Test
    public void testUpdatePhysics() throws Exception {
        DeltaVObject testObject = new DeltaVObject() {
        };

        double initialX = DeltaVGameConfig.SCREEN_WIDTH / 2 + 100.0D;
        double initialY = DeltaVGameConfig.SCREEN_HEIGHT / 2 + 100.0D;
        testObject.setxPosition(initialX);
        testObject.setyPosition(initialY);
        testObject.setVelocityInX(0.0D);
        testObject.setVelocityInY(0.0D);

        // Update the object's physics.
        testObject.updatePhysics(1000);

        // Distance from center before update was 100 * sqrt(2).  So the force
        // of gravity should be our GRAVITY constant / 20000.  That means that
        // we should have moved GRAVITY / 20000 * .707 in the negative x and y
        // directions, scaled to the screen dimensions.
        double expectedVelocityInX = DeltaVGameConfig.GRAVITY / 20000
                * Math.cos(Math.PI / 4)
                * ((double)DeltaVGameConfig.SCREEN_WIDTH / (double)DeltaVGameConfig.SCREEN_HEIGHT)
                * -1D;
        double expectedVelocityInY = DeltaVGameConfig.GRAVITY / 20000
                * Math.sin(Math.PI / 4)
                * ((double)DeltaVGameConfig.SCREEN_HEIGHT / (double)DeltaVGameConfig.SCREEN_WIDTH)
                * -1D;
        assertEquals(expectedVelocityInX, testObject.getVelocityInX(), 1e-15);
        assertEquals(expectedVelocityInY, testObject.getVelocityInY(), 1e-15);
        assertEquals(initialX + expectedVelocityInX, testObject.getxPosition(), 1e-15);
        assertEquals(initialY + expectedVelocityInY, testObject.getyPosition(), 1e-15);
    }

    @Test
    public void testCollisionDetection() throws Exception {
        DeltaVObject testObject = new DeltaVObject() {
        };
        testObject.setxPosition(100);
        testObject.setyPosition(100);

        assertTrue(testObject.isBoundedBy(0, 200, 0, 200));
        assertFalse(testObject.isBoundedBy(200, 400, 200, 400));
    }
}
