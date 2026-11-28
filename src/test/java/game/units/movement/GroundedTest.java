package game.units.movement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GroundedTest extends MovementTest {

    @Override
    protected Movement createMovement(int maxDistance) {
        return new Grounded(maxDistance);
    }

    @Test
    public void testCanBeContaminated() {
        // Arrange
        Movement movement = new Grounded(3);

        // Act
        boolean result = movement.canBeContaminated();

        // Assert
        Assertions.assertTrue(result, "Grounded units should be affected by acid.");
    }

    @Test
    public void testCannotMoveToWater() {
        // Arrange
        Movement movement = new Grounded(3);

        // Act
        boolean result = movement.canMoveToWater(false);

        // Assert
        Assertions.assertFalse(result, "Grounded units should not walk into water by themselves.");
    }

    @Test
    public void testCanBePushedToWater() {
        // Arrange
        Movement movement = new Grounded(3);

        // Act
        boolean result = movement.canMoveToWater(true);

        // Assert
        Assertions.assertTrue(result, "Grounded units should be able to be pushed into water.");
    }

    @Test
    public void testDiesOnWater() {
        // Arrange
        Movement movement = new Grounded(3);

        // Act
        boolean result = movement.diesOnWater();

        // Assert
        Assertions.assertTrue(result, "Grounded units should die on water.");
    }

    @Test
    public void testCannotAttackFromWater() {
        // Arrange
        Movement movement = new Grounded(3);

        // Act
        boolean result = movement.canAttackFromWater();

        // Assert
        Assertions.assertFalse(result, "Grounded units should not attack from water.");
    }
}