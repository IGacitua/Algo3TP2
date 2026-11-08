package game.map.test;

import game.map.Position2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Position2DTest {

    @Test
    public void testPositionCreatedWithCorrectXYZeroZero(){
        testPositionCreatedWithCorrectXY(0, 0);
    }

    @Test
    public void testPositionCreatedWithCorrectXYPositiveNegative(){
        testPositionCreatedWithCorrectXY(5, -3);
    }

    @Test
    public void testPositionCreatedWithCorrectXYMinMaxValues(){
        testPositionCreatedWithCorrectXY(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private void testPositionCreatedWithCorrectXY(int x, int y) {
        Position2D position = new Position2D(x, y);

        assertEquals(x, position.getX());
        assertEquals(y, position.getY());
    }

    @Test
    public void testPositionMovedToCorrectXYZeroZero(){
        testPositionMovedToCorrectXY(0, 0);
    }

    @Test
    public void testPositionMovedToCorrectXYPositiveNegative(){
        testPositionMovedToCorrectXY(5, -3);
    }

    @Test
    public void testPositionMovedToCorrectXYMinMaxValues(){
        testPositionMovedToCorrectXY(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private void testPositionMovedToCorrectXY(int x, int y) {
        Position2D position = new Position2D(0, 0);
        position.moveTo(x, y);

        assertEquals(x, position.getX());
        assertEquals(y, position.getY());
    }

    @Test
    public void testPositionDistanceCorrectZeroZero(){
        testPositionDistanceCorrect(0, 0, 0, 0);
    }

    @Test
    public void testPositionDistanceCorrectPositiveNegative(){
        testPositionDistanceCorrect(5, -3, -3, 4);
    }

    @Test
    public void testPositionDistanceCorrectMinMaxValues(){
        testPositionDistanceCorrect(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    private void testPositionDistanceCorrect(int x1, int x2, int y1, int y2) {
        Position2D position1 = new Position2D(x1, y1);
        Position2D position2 = new Position2D(x2, y2);

        double expectedDistance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        assertEquals(expectedDistance, position1.getDistance(position2));
        assertEquals(expectedDistance, position2.getDistance(position1));
    }
}
