package game.map;

import game.map.direction.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Position2DTest {

    @Test
    public void testPositionCreatedWithCorrectXYZeroZero() {
        testPositionCreatedWithCorrectXY(0, 0);
    }

    @Test
    public void testPositionCreatedWithCorrectXYPositiveNegative() {
        testPositionCreatedWithCorrectXY(5, -3);
    }

    @Test
    public void testPositionCreatedWithCorrectXYMinMaxValues() {
        testPositionCreatedWithCorrectXY(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private void testPositionCreatedWithCorrectXY(int x, int y) {
        Position2D position = new Position2D(x, y);

        assertEquals(x, position.getX());
        assertEquals(y, position.getY());
    }

    @Test
    public void testPositionMovedToCorrectXYZeroZero() {
        testPositionMovedToCorrectXY(0, 0);
    }

    @Test
    public void testPositionMovedToCorrectXYPositiveNegative() {
        testPositionMovedToCorrectXY(5, -3);
    }

    @Test
    public void testPositionMovedToCorrectXYMinMaxValues() {
        testPositionMovedToCorrectXY(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private void testPositionMovedToCorrectXY(int x, int y) {
        Position2D position = new Position2D(0, 0);
        position.moveTo(x, y);

        assertEquals(x, position.getX());
        assertEquals(y, position.getY());
    }

    @Test
    public void testPositionDistanceCorrectZeroZero() {
        testPositionDistanceCorrect(0, 0, 0, 0);
    }

    @Test
    public void testPositionDistanceCorrectPositiveNegative() {
        testPositionDistanceCorrect(5, -3, -3, 4);
    }

    @Test
    public void testPositionDistanceCorrectMinMaxValues() {
        testPositionDistanceCorrect(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    private void testPositionDistanceCorrect(int x1, int x2, int y1, int y2) {
        Position2D position1 = new Position2D(x1, y1);
        Position2D position2 = new Position2D(x2, y2);

        double expectedDistance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        assertEquals(expectedDistance, position1.getDistance(position2));
        assertEquals(expectedDistance, position2.getDistance(position1));
    }

    @Test
    public void testPositionNorthCorrect() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D north = new Position2D(0, -1);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(north);

        // Assert
        assertEquals(new North(), direction, "Expected North direction, got +" + direction);
    }
    
    @Test
    public void testPositionNorthCorrectHigherMagnitude() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D north = new Position2D(0, -100);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(north);

        // Assert
        assertEquals(new North(), direction, "Expected North direction, got " + direction);
    }

    @Test
    public void testPositionSouthCorrect() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D south = new Position2D(0, 1);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(south);

        // Assert
        assertEquals(new South(), direction, "Expected South direction, got " + direction);
    }

    @Test
    public void testPositionSouthCorrectHigherMagnitude() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D south = new Position2D(0, 100);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(south);

        // Assert
        assertEquals(new South(), direction, "Expected South direction, got " + direction);
    }

    @Test
    public void testPositionEastCorrect() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D east = new Position2D(1, 0);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(east);

        // Assert
        assertEquals(new East(), direction, "Expected East direction, got " + direction);
    }

    @Test
    public void testPositionEastCorrectHigherMagnitude() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D east = new Position2D(100, 0);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(east);

        // Assert
        assertEquals(new East(), direction, "Expected East direction, got " + direction);
    }

    @Test
    public void testPositionWestCorrect() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D west = new Position2D(-1, 0);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(west);

        // Assert
        assertEquals(new West(), direction, "Expected West direction, got " + direction);
    }

    @Test
    public void testPositionWestCorrectHigherMagnitude() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D west = new Position2D(-100, 0);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(west);

        // Assert
        assertEquals(new West(), direction, "Expected West direction, got " + direction);
    }

    @Test
    public void testPositionNonOrthogonalIncorrectNE() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D northEast = new Position2D(1, -1);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(northEast);

        // Assert
        assertEquals(new NoDirection(), direction, "Expected NoDirection, got " + direction);
    }

    @Test
    public void testPositionNonOrthogonalIncorrectNW() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D northWest = new Position2D(-1, -1);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(northWest);

        // Assert
        assertEquals(new NoDirection(), direction, "Expected NoDirection, got " + direction);
    }

    @Test
    public void testPositionNonOrthogonalIncorrectSE() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D southEast = new Position2D(1, 1);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(southEast);

        // Assert
        assertEquals(new NoDirection(), direction, "Expected NoDirection, got " + direction);
    }

    @Test
    public void testPositionNonOrthogonalIncorrectSW() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D southWest = new Position2D(-1, 1);

        // Act
        OrthogonalDirection direction = center.getDirectionTo(southWest);

        // Assert
        assertEquals(new NoDirection(), direction, "Expected NoDirection, got " + direction);
    }

    @Test
    public void testDirectionNorthCorrect() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int northX = 0;
        int northY = -1;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(northX, northY);

        // Assert
        assertEquals(new North(), direction, "Expected North direction, got " + direction);
    }

    @Test
    public void testDirectionNorthCorrectHigherMagnitude() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int northX = 0;
        int northY = -100;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(northX, northY);

        // Assert
        assertEquals(new North(), direction, "Expected North direction, got " + direction);
    }

    @Test
    public void testDirectionSouthCorrect() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int southX = 0;
        int southY = 1;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(southX, southY);

        // Assert
        assertEquals(new South(), direction, "Expected South direction, got " + direction);
    }

    @Test
    public void testDirectionSouthCorrectHigherMagnitude() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int southX = 0;
        int southY = 100;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(southX, southY);

        // Assert
        assertEquals(new South(), direction, "Expected South direction, got " + direction);
    }

    @Test
    public void testDirectionEastCorrect() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int eastX = 1;
        int eastY = 0;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(eastX, eastY);

        // Assert
        assertEquals(new East(), direction, "Expected East direction, got " + direction);
    }

    @Test
    public void testDirectionEastCorrectHigherMagnitude() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int eastX = 100;
        int eastY = 0;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(eastX, eastY);

        // Assert
        assertEquals(new East(), direction, "Expected East direction, got " + direction);
    }

    @Test
    public void testDirectionWestCorrect() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int westX = -1;
        int westY = 0;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(westX, westY);

        // Assert
        assertEquals(new West(), direction, "Expected West direction, got " + direction);
    }

    @Test
    public void testDirectionWestCorrectHigherMagnitude() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int westX = -100;
        int westY = 0;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(westX, westY);

        // Assert
        assertEquals(new West(), direction, "Expected West direction, got " + direction);
    }

    @Test
    public void testDirectionNonOrthogonalIncorrectNE() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int northEastX = 1;
        int northEastY = -1;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(northEastX, northEastY);

        // Assert
        assertEquals(new NoDirection(), direction, "Expected NoDirection, got " + direction);
    }

    @Test
    public void testDirectionNonOrthogonalIncorrectNW() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int northWestX = -1;
        int northWestY = -1;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(northWestX, northWestY);

        // Assert
        assertEquals(new NoDirection(), direction, "Expected NoDirection, got " + direction);
    }

    @Test
    public void testDirectionNonOrthogonalIncorrectSE() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int southEastX = 1;
        int southEastY = 1;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(southEastX, southEastY);

        // Assert
        assertEquals(new NoDirection(), direction, "Expected NoDirection, got " + direction);
    }

    @Test
    public void testDirectionNonOrthogonalIncorrectSW() {
        // Arrange
        Position2D center = new Position2D(0, 0);
        int southWestX = -1;
        int southWestY = 1;

        // Act
        OrthogonalDirection direction = center.getDirectionTo(southWestX, southWestY);

        // Assert
        assertEquals(new NoDirection(), direction, "Expected NoDirection, got " + direction);
    }
}
