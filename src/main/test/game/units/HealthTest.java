package game.units;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HealthTest {

    @Test
    public void testHealthStartsAtMaximum() {
        Health health = new Health(3);

        assertEquals(3, health.getCurrent());
        assertEquals(3, health.getMaximum());
        assertTrue(health.isAlive());
    }

    @Test
    public void testReceivingDamageReducesHealth() {
        // Arrange: create a 3 hp unit
        Health health = new Health(3);

        // Act: receive 2 of damaged
        health.receiveDamage(2);

        // Assert: check of the unit status
        assertEquals(1, health.getCurrent());
        assertTrue(health.isAlive());
    }

    @Test
    public void testHealthNeverGoesBelowZero() {
        // Arrange
        Health health = new Health(3);

        // Act
        health.receiveDamage(5);

        // Assert
        assertEquals(0, health.getCurrent());
        assertFalse(health.isAlive());
    }

    @Test
    public void testConsecutiveDamageAccumulates() {
        // Arrange
        Health health = new Health(5);

        // Act
        health.receiveDamage(2);
        health.receiveDamage(1);

        // Assert
        assertEquals(2, health.getCurrent());
        assertTrue(health.isAlive());
    }

    @Test
    public void testHealthCannotStartWithInvalidMaximum() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Health(0);
        });
    }

    @Test
    public void testNegativeDamageIsRejected() {
        // Arrange
        Health health = new Health(3);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            health.receiveDamage(-2);
        });

        // The health should be the same as before the failed action
        assertEquals(3, health.getCurrent());
    }
}
