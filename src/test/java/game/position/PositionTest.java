package game.position;

import game.position.direction.OrthogonalDirection;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

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
        Position position = new Position(x, y);
        assertXY(x, y, position);
    }

    private void assertXY(int expectedX, int expectedY, Position actual) {
        assertEquals(expectedX, actual.getX());
        assertEquals(expectedY, actual.getY());
    }

    // Moving

    @Test
    public void testGetMovedZeroDelta() {
        assertMovedBy(5, -3, 0, 0, 5, -3);
    }

    @Test
    public void testGetMovedPositiveDelta() {
        assertMovedBy(0, 0, 5, 3, 5, 3);
    }

    @Test
    public void testGetMovedNegativeDelta() {
        assertMovedBy(0, 0, -5, -3, -5, -3);
    }

    @Test
    public void testGetMovedMixedDelta() {
        assertMovedBy(5, -3, -2, 4, 3, 1);
    }

    @Test
    public void testGetMovedMinMaxValues() {
        assertMovedBy(0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Test
    public void testGetMovedDoesNotModifyOriginal() {
        Position original = new Position(1, 2);

        Position moved = original.getMoved(10, 20);

        assertNotSame(original, moved);
        assertXY(1, 2, original);
        assertXY(11, 22, moved);
    }

    private void assertMovedBy(int x, int y, int dx, int dy, int expectedX, int expectedY) {
        Position position = new Position(x, y);
        Position moved = position.getMoved(dx, dy);
        assertXY(expectedX, expectedY, moved);
    }

    // Adjacent

    @Test
    public void testGetAdjacentAppliesDirectionDeltas() {
        assertAdjacent(1, -1, 5, 5, 6, 4);
    }

    @Test
    public void testGetAdjacentNegativeCoordinatesAndDeltas() {
        assertAdjacent(-2, 3, -3, -3, -5, 0);
    }

    @Test
    public void testGetAdjacentDoesNotModifyOriginal() {
        Position original = new Position(5, 5);

        Position adjacent = original.getAdjacent(mockDirection(1, 0));

        assertNotSame(original, adjacent);
        assertXY(5, 5, original);
        assertXY(6, 5, adjacent);
    }

    private void assertAdjacent(int deltaX, int deltaY, int x, int y, int expectedX, int expectedY) {
        Position position = new Position(x, y);

        Position adjacent = position.getAdjacent(mockDirection(deltaX, deltaY));

        assertXY(expectedX, expectedY, adjacent);
    }

    private OrthogonalDirection mockDirection(int deltaX, int deltaY) {
        OrthogonalDirection direction = Mockito.mock(OrthogonalDirection.class);
        Mockito.when(direction.getDeltaX()).thenReturn(deltaX);
        Mockito.when(direction.getDeltaY()).thenReturn(deltaY);
        return direction;
    }

    // Diagonal distance

    @Test
    public void testDiagonalDistanceCorrectZeroZero() {
        assertDiagonalDistanceCorrect(0, 0, 0, 0);
    }

    @Test
    public void testDiagonalDistanceCorrectPositiveNegative() {
        assertDiagonalDistanceCorrect(5, -3, -3, 4);
    }

    @Test
    public void testDiagonalDistanceCorrectMinMaxValues() {
        assertDiagonalDistanceCorrect(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    private void assertDiagonalDistanceCorrect(int x1, int y1, int x2, int y2) {
        Position position1 = new Position(x1, y1);
        Position position2 = new Position(x2, y2);

        // Cast to double before subtracting so extreme int values don't overflow
        double dx = (double) x1 - x2;
        double dy = (double) y1 - y2;
        double expectedDistance = Math.sqrt(dx * dx + dy * dy);

        assertEquals(expectedDistance, position1.getDiagonalDistance(position2), DELTA);
        assertEquals(expectedDistance, position2.getDiagonalDistance(position1), DELTA);
        assertEquals(expectedDistance, position1.getDiagonalDistance(x2, y2), DELTA);
        assertEquals(expectedDistance, position2.getDiagonalDistance(x1, y1), DELTA);
    }

    // Orthogonal distance

    @Test
    public void testOrthogonalDistanceCorrectZeroZero() {
        assertOrthogonalDistanceCorrect(0, 0, 0, 0, 0);
    }

    @Test
    public void testOrthogonalDistanceCorrectPositiveNegative() {
        assertOrthogonalDistanceCorrect(5, -3, -3, 4, 15);
    }

    @Test
    public void testOrthogonalDistanceCorrectSameRow() {
        assertOrthogonalDistanceCorrect(2, 7, 10, 7, 8);
    }

    @Test
    public void testOrthogonalDistanceCorrectSameColumn() {
        assertOrthogonalDistanceCorrect(7, 2, 7, -10, 12);
    }

    @Test
    public void testOrthogonalDistanceCorrectLargeValues() {
        assertOrthogonalDistanceCorrect(-1_000_000, 1_000_000, 1_000_000, -1_000_000, 4_000_000);
    }

    private void assertOrthogonalDistanceCorrect(int x1, int y1, int x2, int y2, double expectedDistance) {
        Position position1 = new Position(x1, y1);
        Position position2 = new Position(x2, y2);

        assertEquals(expectedDistance, position1.getOrthogonalDistance(position2), DELTA);
        assertEquals(expectedDistance, position2.getOrthogonalDistance(position1), DELTA);
        assertEquals(expectedDistance, position1.getOrthogonalDistance(x2, y2), DELTA);
        assertEquals(expectedDistance, position2.getOrthogonalDistance(x1, y1), DELTA);
    }

    // Ordering (isCloserToOrigin): lower row first, then lower column

    @Test
    public void testCloserToOriginLowerRowIsCloser() {
        assertCloserToOrigin(5, 1, 0, 2);
    }

    @Test
    public void testCloserToOriginLowerRowWinsOverColumn() {
        // Lower row wins even if its column is much higher
        assertCloserToOrigin(100, 1, 0, 2);
    }

    @Test
    public void testCloserToOriginSameRowLowerColumnIsCloser() {
        assertCloserToOrigin(1, 3, 2, 3);
    }

    @Test
    public void testCloserToOriginNegativeCoordinates() {
        assertCloserToOrigin(-5, -5, -5, -4);
        assertCloserToOrigin(-6, -4, -5, -4);
    }

    @Test
    public void testCloserToOriginMinMaxValues() {
        assertCloserToOrigin(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private void assertCloserToOrigin(int closerX, int closerY, int fartherX, int fartherY) {
        Position closer = new Position(closerX, closerY);
        Position farther = new Position(fartherX, fartherY);

        assertTrue(closer.isCloserToOrigin(farther));
        assertTrue(closer.isCloserToOrigin(fartherX, fartherY));

        assertFalse(farther.isCloserToOrigin(closer));
        assertFalse(farther.isCloserToOrigin(closerX, closerY));
    }

    // Equality

    @Test
    public void testEqualsNullIsFalse() {
        Position position = new Position(1, 2);

        assertNotEquals(null, position);
        assertNotEquals(null, position);
    }

    @Test
    public void testEqualsSameInstanceIsTrue() {
        Position position = new Position(1, 2);

        assertEquals(position, position);
    }

    @Test
    public void testEqualsDifferentInstancesSameCoordinatesIsTrue() {
        assertEquals(new Position(1, 2), new Position(1, 2));
        assertEquals(new Position(0, 0), new Position(0, 0));
        assertEquals(new Position(-5, -7), new Position(-5, -7));
    }

    @Test
    public void testEqualsMinMaxValuesIsTrue() {
        assertEquals(
                new Position(Integer.MIN_VALUE, Integer.MAX_VALUE),
                new Position(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void testEqualsDifferentXIsFalse() {
        assertNotEquals(new Position(1, 2), new Position(3, 2));
    }

    @Test
    public void testEqualsDifferentYIsFalse() {
        assertNotEquals(new Position(1, 2), new Position(1, 3));
    }

    @Test
    public void testEqualsSwappedCoordinatesIsFalse() {
        assertNotEquals(new Position(1, 2), new Position(2, 1));
    }

    @Test
    public void testEqualsAdjacentMinMaxValuesIsFalse() {
        assertNotEquals(
                new Position(Integer.MAX_VALUE, Integer.MAX_VALUE),
                new Position(Integer.MAX_VALUE - 1, Integer.MAX_VALUE));
    }
}