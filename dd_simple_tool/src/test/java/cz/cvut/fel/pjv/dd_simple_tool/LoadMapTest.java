package cz.cvut.fel.pjv.dd_simple_tool;

import cz.cvut.fel.pjv.dd_simple_tool.model.Chest;
import cz.cvut.fel.pjv.dd_simple_tool.model.Enemy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JavaFxJUnit4ClassRunner.class)
public class LoadMapTest {

    private LoadMap map;

    /**
     * Loading the map before each test.
     */
    @Before
    public void setUp() {
        try {
            map = new LoadMap();
            map.loadMap("map1.json");
        } catch (IOException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }

    /**
     * Tests if the tiles are loaded correctly.
     */
    @Test
    public void testLoadTiles()
    {
        assertEquals(map.getTilesArr().toArray().length, 144);
        assertEquals(map.getTilesArr().get(140).getTerrain().getName(), "Grass");
        assertEquals(map.getTilesArr().get(82).getTerrain().getName(), "Bush");
        assertEquals(map.getTilesArr().get(13).getTerrain().getName(), "Mountain");
        assertEquals(map.getTilesArr().get(60).getTerrain().getName(), "Water");

    }

    /**
     * Tests if the CircleObjects displaying enemies and chests are loaded correctly.
     */
    @Test
    public void testLoadCircleObjects()
    {
        assertEquals(map.getObjectsArr().toArray().length, 10);
        assertEquals(map.getObjectsArr().get(0).getBaseObj().getName(), "Forest chest");
        assertEquals(( (Chest) map.getObjectsArr().get(0).getBaseObj()).getContainer()[0].getName(), "Helmet4");
        assertEquals(( (Chest) map.getObjectsArr().get(0).getBaseObj()).getContainer()[1].getName(), "Helmet5");
        assertEquals(( (Chest) map.getObjectsArr().get(0).getBaseObj()).getContainer()[2].getName(), "Helmet6");

        assertEquals(map.getObjectsArr().get(6).getBaseObj().getName(), "Wolf");
        assertEquals(((Enemy) map.getObjectsArr().get(6).getBaseObj()).getLvl(), 4);
        assertEquals(((Enemy) map.getObjectsArr().get(6).getBaseObj()).getContainer()[0].getName(), "Wolf hide");
    }

}
