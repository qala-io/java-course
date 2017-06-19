package io.qala.javatraining;


import org.testng.annotations.Test;

import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

public class FailingTest {
    @Test public void strangeFailure() throws Exception {
        String file = ClassLoader.getSystemResource("myresource.txt").getFile();
        assertEquals(new FileInputStream(file).read(), 49);
    }
}
