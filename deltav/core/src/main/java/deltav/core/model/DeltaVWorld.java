package deltav.core.model;

import deltav.core.config.DeltaVGameConfig;
import playn.core.*;
import playn.core.util.Callback;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;

/**
 * This class stores the state of the game world - primarily the objects within
 * it and global state such as the player's score, gravity, etc.
 */
public class DeltaVWorld {

    /**
     * The current set of debris in the world.
     */
    private Set<DeltaVDebris> debrisSet;

    /**
     * The player's ship.
     */
    private DeltaVShip ship;

    /**
     * The player's current score.
     */
    private int score;

    /**
     * A text layer for rendering the scoreboard.
     */
    private GroupLayer textLayer;

    /**
     * Is the game over?
     */
    private boolean gameOver = false;

    public DeltaVWorld() {
        // Initialize the ship.
        ship = new DeltaVShip();
        keyboard().setListener(ship);
        ship.setVelocityInX(0);
        ship.setVelocityInY(0);
        ship.setyPosition(0);
        ship.setxPosition(0);
        ship.setHeading(0);
        ship.setFuelRemaining(DeltaVGameConfig.MAX_FUEL);
        final Image img = assets().getImage("images/spaceship-icon.png");
        ship.setImg(img);
        img.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image image) {
                // We call setImg again once the image loads so that we can
                // properly set the width and height.  This feels gross...
                ship.setImg(img);
            }

            @Override
            public void onFailure(Throwable cause) {
                throw new RuntimeException("Could not load ship asset: ", cause);
            }
        });

        // Initialize the debris field.
        debrisSet = new HashSet<DeltaVDebris>();
        Random r = new Random();
        for (int i = 0; i < DeltaVGameConfig.NUMBER_OF_DEBRIS_PIECES; i++) {
            DeltaVDebris debris = new DeltaVDebris();
            debris.setyPosition(r.nextInt(DeltaVGameConfig.SCREEN_HEIGHT));
            debris.setxPosition(r.nextInt(DeltaVGameConfig.SCREEN_WIDTH));
            final Image debrisImg = assets().getImage("images/mass1.png");
            debris.setImg(debrisImg);
            debrisSet.add(debris);
        }

        // Initialize the scoreboard.
        textLayer = graphics().createGroupLayer();
    }

    /**
     * A convenience method for creating our scoreboard layer.  Shamelessly
     * stolen from the showcases example.
     *
     * @param layout The text to lay out.
     * @param color The color of the text.
     * @return A new Layer containing the text.
     */
    protected Layer createTextLayer(TextLayout layout, int color) {
        CanvasImage image = graphics().createImage((int)Math.ceil(layout.width()),
                (int)Math.ceil(layout.height()));
        image.canvas().setFillColor(color);
        image.canvas().fillText(layout, 0, 0);
        return graphics().createImageLayer(image);
    }

    /**
     * Update the physics for everything in the world.
     *
     * @param delta The number of milliseconds passed since the last call to
     *              update.
     */
    public void updatePhysics(int delta) {
        if (gameOver) {
            return;
        }

        Random r = new Random();

        // Start with the ship's physics and store its bounding box dims.
        ship.updatePhysics(delta);
        double shipTopLeftX = ship.getxPosition() - ship.getImg().width() / 2;
        double shipBottomRightX = ship.getxPosition() + ship.getImg().width() / 2;
        double shipTopLeftY = ship.getyPosition() - ship.getImg().height() / 2;
        double shipBottomRightY = ship.getyPosition() + ship.getImg().height() / 2;

        // Next update each piece of debris.
        for (DeltaVDebris debris : debrisSet) {
            debris.updatePhysics(delta);

            // Collision detection.  Ship first.
            if (debris.isBoundedBy(shipTopLeftX, shipBottomRightX, shipTopLeftY, shipBottomRightY)) {
                // Collision.  Give the player some points, fuel, and respawn
                // this debris piece elsewhere.
                debris.respawn();
                ship.addFuel(r.nextInt(DeltaVGameConfig.MAX_FUEL_PER_DEBRIS - 1) + 1);
                score += DeltaVGameConfig.POINTS_PER_DEBRIS;
            }

            // Now check to see if the debris has fallen into the black hole.
            if (debris.isBoundedBy(DeltaVGameConfig.BLACK_HOLE_TOP_LEFT_X,
                    DeltaVGameConfig.BLACK_HOLE_BOTTOM_RIGHT_X,
                    DeltaVGameConfig.BLACK_HOLE_TOP_LEFT_Y,
                    DeltaVGameConfig.BLACK_HOLE_BOTTOM_RIGHT_Y)) {
                debris.respawn();
            }
        }

        // Check if the ship has fallen into the black hole.
        if(ship.isBoundedBy(DeltaVGameConfig.BLACK_HOLE_TOP_LEFT_X,
                DeltaVGameConfig.BLACK_HOLE_BOTTOM_RIGHT_X,
                DeltaVGameConfig.BLACK_HOLE_TOP_LEFT_Y,
                DeltaVGameConfig.BLACK_HOLE_BOTTOM_RIGHT_Y)) {
            // If it has, start draining fuel.  If fuel reaches 0, the game is
            // over.
            ship.addFuel(DeltaVGameConfig.FUEL_DRAINED_BY_BLACK_HOLE * -1);
            if(ship.getFuelRemaining() <= 0) {
                gameOver = true;
            }
        }
    }

    /**
     * Paint the world to the screen using the provided Surface.
     *
     * @param surface The Surface to paint to.
     */
    public void paint(Surface surface) {
        // Draw the ship and each piece of debris.
        ship.draw(surface);
        for (DeltaVDebris debris : debrisSet) {
            debris.draw(surface);
        }

        // Blank the scoreboard and repopulate it with the current score and
        // fuel.
        textLayer.removeAll();
        Font font = graphics().createFont("Courier", Font.Style.PLAIN, 18);
        TextFormat format = new TextFormat().withFont(font);
        String scoreboardText = "Fuel: " + ship.getFuelRemaining() + "\nScore: " + score;
        if (gameOver) {
            scoreboardText += "\nGAME OVER!";
        }

        // Create a new Layer for the text and append it to the textLayer.
        TextLayout layout = graphics().layoutText(scoreboardText, format);
        Layer layer = createTextLayer(layout, 0xFFFF0000);
        layer.setTranslation(10, 10);
        textLayer.add(layer);

        // Draw the scoreboard.
        surface.drawLayer(textLayer);
    }
}
