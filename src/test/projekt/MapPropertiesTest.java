package projekt;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

class MapPropertiesTest {

    @Test
    void testMapProperties(){
        try {
            MapProperties mapProperties = new MapProperties(new FileReader("parameters.json"));
            assertEquals(200, mapProperties.getHeight());
            assertEquals(200, mapProperties.getWidth());
            assertEquals(50, mapProperties.getStartEnergy());
            assertEquals(1, mapProperties.getMoveEnergy());
            assertEquals(5, mapProperties.getPlantEnergy());
            assertEquals(0.25,mapProperties.getJungleRatio());
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        }
}