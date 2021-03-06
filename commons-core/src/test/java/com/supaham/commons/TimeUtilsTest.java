package com.supaham.commons;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.supaham.commons.exceptions.DurationParseException;
import com.supaham.commons.utils.TimeUtils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ali on 09/02/2015.
 */
public class TimeUtilsTest {

  @Test
  public void testElapsed() throws Exception {
    assertFalse(TimeUtils.elapsed(System.currentTimeMillis(), 400));
    assertTrue(TimeUtils.elapsed(System.currentTimeMillis() - 1001, 1000));
  }

  @Test
  public void testParse() throws Exception {
    long duration = TimeUtils.parseDuration("1h");
    Assert.assertEquals(3600, duration);
    duration = TimeUtils.parseDuration("1d1h1m1s");
    Assert.assertEquals(86400 + 3600 + 60 + 1, duration);
  }

  @Test
  public void testParseMs() throws Exception {
    long duration = TimeUtils.parseDurationMs("1h");
    Assert.assertEquals(3600 * 1000, duration);
    duration = TimeUtils.parseDurationMs("1d1h1m1s3ms");
    Assert.assertEquals((86400 + 3600 + 60 + 1) * 1000 + 3, duration);
    duration = TimeUtils.parseDurationMs("0.5s");
    Assert.assertEquals((0.5) * 1000, duration, 0.000001);
  }

  @Test(expected = DurationParseException.class)
  public void testParseMsError() throws Exception {
    long duration = TimeUtils.parseDurationMs("1x");
  }
}
