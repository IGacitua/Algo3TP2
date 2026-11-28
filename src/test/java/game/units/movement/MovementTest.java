package game.units.movement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// Hago que todas las clases de test de movimiento hereden de este.
// Las subclases 'solo' testean getters pero no veo por que no tener los tests extra.
public abstract class MovementTest {

    protected abstract Movement createMovement(int maxDistance);

    // Checks

    @Test
    public void testCanMoveIsTrueWhenMovementIsEnough() {
        // Arrange
        Movement movement = createMovement(5);

        // Act
        boolean result = movement.canMove(false, 3.0);

        // Assert
        Assertions.assertTrue(result, "Should be able to move when the cost is below the remaining movement.");
    }

    @Test
    public void testCanMoveIsTrueWhenCostEqualsRemainingMovement() {
        // Arrange
        Movement movement = createMovement(5);

        // Act
        boolean result = movement.canMove(false, 5.0);

        // Assert
        Assertions.assertTrue(result, "Should be able to move when the cost equals the remaining movement.");
    }

    @Test
    public void testCanMoveIsFalseWhenCostExceedsRemainingMovement() {
        // Arrange
        Movement movement = createMovement(5);

        // Act
        boolean result = movement.canMove(false, 5.5);

        // Assert
        Assertions.assertFalse(result, "Should not be able to move when the cost exceeds the remaining movement.");
    }

    @Test
    public void testCanMoveWithZeroCostIsTrueWhenNoMovementRemains() {
        // Arrange
        Movement movement = createMovement(0);

        // Act
        boolean result = movement.canMove(false, 0.0);

        // Assert
        Assertions.assertTrue(result, "A free move should always be possible.");
    }

    @Test
    public void testCanMoveDoesNotConsumeMovement() {
        // Arrange
        Movement movement = createMovement(5);

        // Act
        movement.canMove(false, 5.0);
        boolean result = movement.canMove(false, 5.0);

        // Assert
        Assertions.assertTrue(result, "Checking if a move is possible should not reduce the remaining movement.");
    }

    // Pushing

    @Test
    public void testCanMoveIsTrueWhenForcedEvenIfCostExceedsRemainingMovement() {
        // Arrange
        Movement movement = createMovement(1);

        // Act
        boolean result = movement.canMove(true, 10.0);

        // Assert
        Assertions.assertTrue(result, "Forced movement should ignore the movement cost.");
    }

    @Test
    public void testCanMoveIsTrueWhenForcedAndNoMovementRemains() {
        // Arrange
        Movement movement = createMovement(2);
        movement.reduceMovement(2.0);

        // Act
        boolean result = movement.canMove(true, 1.0);

        // Assert
        Assertions.assertTrue(result, "Forced movement should be possible with no movement left.");
    }

    // Modifying

    @Test
    public void testReduceMovementLowersRemainingMovement() {
        // Arrange
        Movement movement = createMovement(5);

        // Act
        movement.reduceMovement(3.0);
        boolean canMoveRemaining = movement.canMove(false, 2.0);
        boolean canMoveMore = movement.canMove(false, 2.5);

        // Assert
        Assertions.assertTrue(canMoveRemaining, "Should be able to spend exactly the remaining movement.");
        Assertions.assertFalse(canMoveMore, "Should not be able to spend more than the remaining movement.");
    }

    @Test
    public void testReduceMovementAccumulates() {
        // Arrange
        Movement movement = createMovement(5);

        // Act
        movement.reduceMovement(2.0);
        movement.reduceMovement(2.0);
        boolean canMoveRemaining = movement.canMove(false, 1.0);
        boolean canMoveMore = movement.canMove(false, 1.5);

        // Assert
        Assertions.assertTrue(canMoveRemaining, "One movement point should remain.");
        Assertions.assertFalse(canMoveMore, "Only one movement point should remain.");
    }

    @Test
    public void testResetRemainingMovementRestoresMaximum() {
        // Arrange
        Movement movement = createMovement(5);
        movement.reduceMovement(5.0);

        // Act
        movement.resetRemainingMovement();
        boolean canMoveMaximum = movement.canMove(false, 5.0);
        boolean canMoveMore = movement.canMove(false, 5.5);

        // Assert
        Assertions.assertTrue(canMoveMaximum, "All movement should be restored after a reset.");
        Assertions.assertFalse(canMoveMore, "Reset should not grant more than the maximum.");
    }
}