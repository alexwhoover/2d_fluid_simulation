package com.example;

import com.example.Utils.MathUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BilInterpTest {
    @Test
    public void testBilInterp() {
        // Test 1: Corner values (s=0, t=0) should return v00
        double result = MathUtils.bilInterp(1.0, 2.0, 3.0, 4.0, 0, 0);
        assertEquals(1.0, result, 0.001);

        // Test 2: Corner values (s=1, t=0) should return v10
        result = MathUtils.bilInterp(1.0, 2.0, 3.0, 4.0, 1.0, 0);
        assertEquals(3.0, result, 0.001);

        // Test 3: Corner values (s=0, t=1) should return v01
        result = MathUtils.bilInterp(1.0, 2.0, 3.0, 4.0, 0, 1.0);
        assertEquals(2.0, result, 0.001);

        // Test 4: Corner values (s=1, t=1) should return v11
        result = MathUtils.bilInterp(1.0, 2.0, 3.0, 4.0, 1.0, 1.0);
        assertEquals(4.0, result, 0.001);

        // Test 5: Center point (s=0.5, t=0.5) should return average
        result = MathUtils.bilInterp(1.0, 2.0, 3.0, 4.0, 0.5, 0.5);
        assertEquals(2.5, result, 0.001);

        // Test 6: Random point (s=0.4, t=0.6)
        result = MathUtils.bilInterp(1.0, 2.0, 3.0, 4.0, 0.4, 0.6);
        assertEquals(2.4, result, 0.001);
    }
}
