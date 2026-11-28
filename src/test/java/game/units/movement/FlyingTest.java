package game.units.movement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FlyingTest extends MovementTest {

    @Override
    protected Movement createMovement(int maxDistance) {
        return new Flying(maxDistance);
    }

    @Test
    public void testCannotBeContaminated() {
        // Arrange
        Movement movement = new Flying(3);

        // Act
        boolean result = movement.canBeContaminated();

        // Assert
        Assertions.assertFalse(result, "Flying units should not be affected by acid.");
    }

    @Test
    public void testCanMoveToWater() {
        // Arrange
        Movement movement = new Flying(3);

        // Act
        boolean result = movement.canMoveToWater(false);

        // Assert
        Assertions.assertTrue(result, "Flying units should be able to move over water.");
    }

    @Test
    public void testCanBePushedToWater() {
        // Arrange
        Movement movement = new Flying(3);

        // Act
        boolean result = movement.canMoveToWater(true);

        // Assert
        Assertions.assertTrue(result, "Flying units should be able to be pushed over water.");
    }

    @Test
    public void testDoesNotDieOnWater() {
        // Arrange
        Movement movement = new Flying(3);

        // Act
        boolean result = movement.diesOnWater();

        // Assert
        Assertions.assertFalse(result, "Flying units should not die on water.");
    }

    @Test
    public void testCanAttackFromWater() {
        // Arrange
        Movement movement = new Flying(3);

        // Act
        boolean result = movement.canAttackFromWater();

        // Assert
        Assertions.assertTrue(result, "Flying units should not be affected by water when attacking.");
    }
}