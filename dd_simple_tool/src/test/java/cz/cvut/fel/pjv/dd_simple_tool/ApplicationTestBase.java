package cz.cvut.fel.pjv.dd_simple_tool;

import javafx.scene.Group;
import javafx.scene.Scene;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is a sample test class for java fx tests.
 */
@RunWith(JavaFxJUnit4ClassRunner.class)
public class ApplicationTestBase
{
    /**
     * Test which would normally fail without running on the JavaFX thread.
     */
    @Test
    public void testNeedsJavaFX()
    {
        Scene scene = new Scene(new Group());
        assertTrue(true);
    }
}


