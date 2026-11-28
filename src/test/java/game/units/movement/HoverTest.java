package game.units.movement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HoverTest extends MovementTest {

    @Override
    protected Movement createMovement(int maxDistance) {
        return new Hover(maxDistance);
    }

    @Test
    public void testCanBeContaminated() {
        // Arrange
        Movement movement = new Hover(3);

        // Act
        boolean result = movement.canBeContaminated();

        // Assert
        Assertions.assertTrue(result, "Hover units should be affected by acid.");
    }

    @Test
    public void testCanMoveToWater() {
        // Arrange
        Movement movement = new Hover(3);

        // Act
        boolean result = movement.canMoveToWater(false);

        // Assert
        Assertions.assertTrue(result, "Hover units should be able to move into water.");
    }

    @Test
    public void testCanBePushedToWater() {
        // Arrange
        Movement movement = new Hover(3);

        // Act
        boolean result = movement.canMoveToWater(true);

        // Assert
        Assertions.assertTrue(result, "Hover units should be able to be pushed into water.");
    }

    @Test
    public void testDoesNotDieOnWater() {
        // Arrange
        Movement movement = new Hover(3);

        // Act
        boolean result = movement.diesOnWater();

        // Assert
        Assertions.assertFalse(result, "Hover units should not die on water.");
    }

    @Test
    public void testCannotAttackFromWater() {
        // Arrange
        Movement movement = new Hover(3);

        // Act
        boolean result = movement.canAttackFromWater();

        // Assert
        Assertions.assertFalse(result, "Hover units should not attack from water.");
    }
}