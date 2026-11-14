package game.map;

import game.map.direction.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrthogonalDirectionTest {
    @Test
    public void testEastXYCorrect() {
        // Arrange
        OrthogonalDirection direction = new East();

        // Act
        int x = direction.getDeltaX();
        int y = direction.getDeltaY();

        // Assert
        assertEquals(1, x, "Expected deltaX to be 1, is " + x);
        assertEquals(0, y, "Expected deltaY to be 0, is " + y);
    }

    @Test
    public void testInvertedEastIsWest() {
        // Arrange
        OrthogonalDirection direction = new East();

        // Act
        OrthogonalDirection invertedDirection = direction.invert();

        // Assert
        assertEquals(new West(), invertedDirection, "Expected inverted direction to be West, is " + invertedDirection);
    }

    @Test
    public void testEastMovesPositionCorrectly() {
        // Arrange
        OrthogonalDirection direction = new East();
        Position2D position = Mockito.mock(Position2D.class);

        // Act
        direction.movePosition(position);

        // Assert
        Mockito.verify(position).moveTo(1, 0);
    }

    @Test
    public void testNorthXYCorrect() {
        // Arrange
        OrthogonalDirection direction = new North();

        // Act
        int x = direction.getDeltaX();
        int y = direction.getDeltaY();

        // Assert
        assertEquals(0, x, "Expected deltaX to be 0, is " + x);
        assertEquals(-1, y, "Expected deltaY to be -1, is " + y);
    }

    @Test
    public void testInvertedNorthIsSouth() {
        // Arrange
        OrthogonalDirection direction = new North();

        // Act
        OrthogonalDirection invertedDirection = direction.invert();

        // Assert
        assertEquals(new South(), invertedDirection, "Expected inverted direction to be South, is " + invertedDirection);
    }

    @Test
    public void testNorthMovesPositionCorrectly() {
        // Arrange
        OrthogonalDirection direction = new North();
        Position2D position = Mockito.mock(Position2D.class);

        // Act
        direction.movePosition(position);

        // Assert
        Mockito.verify(position).moveTo(0, -1);
    }

    @Test
    public void testSouthXYCorrect() {
        // Arrange
        OrthogonalDirection direction = new South();

        // Act
        int x = direction.getDeltaX();
        int y = direction.getDeltaY();

        // Assert
        assertEquals(0, x, "Expected deltaX to be 0, is " + x);
        assertEquals(1, y, "Expected deltaY to be 1, is " + y);
    }

    @Test
    public void testInvertedSouthIsNorth() {
        // Arrange
        OrthogonalDirection direction = new South();

        // Act
        OrthogonalDirection invertedDirection = direction.invert();

        // Assert
        assertEquals(new North(), invertedDirection, "Expected inverted direction to be North, is " + invertedDirection);
    }

    @Test
    public void testSouthMovesPositionCorrectly() {
        // Arrange
        OrthogonalDirection direction = new South();
        Position2D position = Mockito.mock(Position2D.class);

        // Act
        direction.movePosition(position);

        // Assert
        Mockito.verify(position).moveTo(0, 1);
    }

    @Test
    public void testWestXYCorrect() {
        // Arrange
        OrthogonalDirection direction = new West();

        // Act
        int x = direction.getDeltaX();
        int y = direction.getDeltaY();

        // Assert
        assertEquals(-1, x, "Expected deltaX to be -1, is " + x);
        assertEquals(0, y, "Expected deltaY to be 0, is " + y);
    }

    @Test
    public void testInvertedWestIsEast() {
        // Arrange
        OrthogonalDirection direction = new West();

        // Act
        OrthogonalDirection invertedDirection = direction.invert();

        // Assert
        assertEquals(new East(), invertedDirection, "Expected inverted direction to be East, is " + invertedDirection);
    }

    @Test
    public void testWestMovesPositionCorrectly() {
        // Arrange
        OrthogonalDirection direction = new West();
        Position2D position = Mockito.mock(Position2D.class);

        // Act
        direction.movePosition(position);

        // Assert
        Mockito.verify(position).moveTo(-1, 0);
    }

    @Test
    public void testNoDirectionXYCorrect() {
        // Arrange
        OrthogonalDirection direction = new NoDirection();

        // Act
        int x = direction.getDeltaX();
        int y = direction.getDeltaY();

        // Assert
        assertEquals(0, x, "Expected deltaX to be 0, is " + x);
        assertEquals(0, y, "Expected deltaY to be 0, is " + y);
    }

    @Test
    public void testInvertedNoDirectionIsNoDirection() {
        // Arrange
        OrthogonalDirection direction = new NoDirection();

        // Act
        OrthogonalDirection invertedDirection = direction.invert();

        // Assert
        assertEquals(new NoDirection(), invertedDirection, "Expected inverted direction to be NoDirection, is " + invertedDirection);
    }

    @Test
    public void testNoDirectionDoesNotMovePosition() {
        // Arrange
        OrthogonalDirection direction = new NoDirection();
        Position2D position = Mockito.mock(Position2D.class);

        // Act
        direction.movePosition(position);

        // Assert
        Mockito.verify(position, Mockito.times(0)).moveTo(Mockito.any(int.class), Mockito.any(int.class));
    }
}
