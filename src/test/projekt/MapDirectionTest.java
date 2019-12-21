package projekt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void changeDirection() {
        MapDirection dir = MapDirection.N;
        dir = dir.changeDirection(3);
        assertTrue(dir == MapDirection.SE);
        dir = dir.changeDirection(0);
        assertTrue(dir == MapDirection.SE);
        dir = dir.changeDirection(7);
        assertTrue(dir == MapDirection.E);
    }

    @Test
    void toUnitVector() {
        assertTrue(MapDirection.N.toUnitVector().equals(new Vector2d(0,1)));
        assertTrue(MapDirection.NE.toUnitVector().equals(new Vector2d(1,1)));
        assertTrue(MapDirection.E.toUnitVector().equals(new Vector2d(1,0)));
        assertTrue(MapDirection.SE.toUnitVector().equals(new Vector2d(1,-1)));
        assertTrue(MapDirection.S.toUnitVector().equals(new Vector2d(0,-1)));
        assertTrue(MapDirection.SW.toUnitVector().equals(new Vector2d(-1,-1)));
        assertTrue(MapDirection.W.toUnitVector().equals(new Vector2d(-1,0)));
        assertTrue(MapDirection.NW.toUnitVector().equals(new Vector2d(-1,1)));
    }
}