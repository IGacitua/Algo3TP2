package game.map;

import game.map.direction.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Position2DTest {

    private static final double DELTA = 0.1;

    // Creation

    @Test
    public void testPositionCreatedWithCorrectXYZeroZero() {
        assertCreatedWithXY(0, 0);
    }

    @Test
    public void testPositionCreatedWithCorrectXYPositiveNegative() {
        assertCreatedWithXY(5, -3);
    }

    @Test
    public void testPositionCreatedWithCorrectXYMinMaxValues() {
        assertCreatedWithXY(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private void assertCreatedWithXY(int x, int y) {
        Position2D position = new Position2D(x, y);
        assertXY(x, y, position);
    }

    // Moving

    @Test
    public void testPositionMovedToCorrectXYZeroZero() {
        assertMovedToXY(0, 0);
    }

    @Test
    public void testPositionMovedToCorrectXYPositiveNegative() {
        assertMovedToXY(5, -3);
    }

    @Test
    public void testPositionMovedToCorrectXYMinMaxValues() {
        assertMovedToXY(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private void assertMovedToXY(int x, int y) {
        Position2D position = new Position2D(0, 0);
        position.moveTo(x, y);

        assertXY(x, y, position);
    }

    private void assertXY(int expectedX, int expectedY, Position2D actual) {
        assertEquals(expectedX, actual.getX());
        assertEquals(expectedY, actual.getY());
    }

    // Distance

    @Test
    public void testPositionDistanceCorrectZeroZero() {
        assertDistanceCorrect(0, 0, 0, 0);
    }

    @Test
    public void testPositionDistanceCorrectPositiveNegative() {
        assertDistanceCorrect(5, -3, -3, 4);
    }

    @Test
    public void testPositionDistanceCorrectMinMaxValues() {
        assertDistanceCorrect(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    private void assertDistanceCorrect(int x1, int y1, int x2, int y2) {
        Position2D position1 = new Position2D(x1, y1);
        Position2D position2 = new Position2D(x2, y2);

        // Cast to double before subtracting so extreme int values don't overflow
        double dx = (double) x1 - x2;
        double dy = (double) y1 - y2;
        double expectedDistance = Math.sqrt(dx * dx + dy * dy);

        assertEquals(expectedDistance, position1.getDistance(position2), DELTA);
        assertEquals(expectedDistance, position2.getDistance(position1), DELTA);
    }

    // Orthogonal direction

    @Test
    public void testDirectionNorthCorrect() {
        assertDirectionTo(new North(), 0, -1);
    }

    @Test
    public void testDirectionNorthCorrectHigherMagnitude() {
        assertDirectionTo(new North(), 0, -100);
    }

    @Test
    public void testDirectionSouthCorrect() {
        assertDirectionTo(new South(), 0, 1);
    }

    @Test
    public void testDirectionSouthCorrectHigherMagnitude() {
        assertDirectionTo(new South(), 0, 100);
    }

    @Test
    public void testDirectionEastCorrect() {
        assertDirectionTo(new East(), 1, 0);
    }

    @Test
    public void testDirectionEastCorrectHigherMagnitude() {
        assertDirectionTo(new East(), 100, 0);
    }

    @Test
    public void testDirectionWestCorrect() {
        assertDirectionTo(new West(), -1, 0);
    }

    @Test
    public void testDirectionWestCorrectHigherMagnitude() {
        assertDirectionTo(new West(), -100, 0);
    }

    // Direction (Invalid / Non-Orthogonal)

    @Test
    public void testDirectionNonOrthogonalAllDiagonals() {
        assertNoDirectionForAllDiagonals(1);
    }

    @Test
    public void testDirectionNonOrthogonalAllDiagonalsHigherMagnitude() {
        assertNoDirectionForAllDiagonals(100);
    }

    private void assertNoDirectionForAllDiagonals(int magnitude) {
        assertDirectionTo(new NoDirection(), magnitude, -magnitude);   // NE
        assertDirectionTo(new NoDirection(), -magnitude, -magnitude);  // NW
        assertDirectionTo(new NoDirection(), magnitude, magnitude);    // SE
        assertDirectionTo(new NoDirection(), -magnitude, magnitude);   // SW
    }

    // Direction Helper

    private void assertDirectionTo(OrthogonalDirection expected, int x, int y) {
        // Arrange
        Position2D center = new Position2D(0, 0);
        Position2D target = new Position2D(x, y);

        // Act
        OrthogonalDirection fromPosition = center.getDirectionTo(target);
        OrthogonalDirection fromCoordinates = center.getDirectionTo(x, y);

        // Assert
        assertEquals(expected, fromPosition,
                "getDirectionTo(Position2D) for (" + x + ", " + y + "): expected " + expected + ", got " + fromPosition);
        assertEquals(expected, fromCoordinates,
                "getDirectionTo(int, int) for (" + x + ", " + y + "): expected " + expected + ", got " + fromCoordinates);
    }
}