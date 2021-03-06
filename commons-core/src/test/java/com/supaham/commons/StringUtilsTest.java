package com.supaham.commons;

import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;
import static com.supaham.commons.utils.StringUtils.joinerFunction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

import com.supaham.commons.utils.StringUtils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import javax.annotation.Nullable;

public class StringUtilsTest {

  @Test
  public void asciiASCIIString() {
    assertTrue(StringUtils.isASCII("abc"));
  }

  @Test
  public void asciiUTF8String() {
    assertFalse(StringUtils.isASCII("abc§"));
  }

  @Test(expected = NullPointerException.class)
  public void asciiNullString() {
    assertFalse(StringUtils.isASCII(null));
  }

  @Test
  public void testFormatWhitespace() {
    assertEquals("test", StringUtils.formatWhitespace(32, "test"));
    assertEquals("test", StringUtils.formatWhitespace(32, "test", " "));
    assertEquals("test test", StringUtils.formatWhitespace(9, "test", "test"));
    assertEquals("test  test", StringUtils.formatWhitespace(10, "test", "test"));
    assertEquals("test      test", StringUtils.formatWhitespace(14, "test", "    ", "test"));
    assertEquals("test   t  test", StringUtils.formatWhitespace(14, "test", "  t ", "test"));
  }

  @Test
  public void checkNotNullOrEmptyTest() {
    checkNotNullOrEmpty("abc");
  }

  @Test(expected = NullPointerException.class)
  public void checkNotNullOrEmptyTest2() {
    checkNotNullOrEmpty(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkNotNullOrEmptyTest3() {
    checkNotNullOrEmpty("");
  }

  @Test
  public void testAppendIfPlural() throws Exception {
    assertEquals("school", StringUtils.appendIfPlural(1, "school", false));
    assertEquals("schools", StringUtils.appendIfPlural(2, "school", false));
    assertEquals("cries", StringUtils.appendIfPlural(2, "cry", false));
    assertEquals("crashes", StringUtils.appendIfPlural(2, "crash", false));
    assertEquals("stitches", StringUtils.appendIfPlural(2, "stitch", false));

  }

  @Test
  public void testNormalize() throws Exception {
    Assert.assertEquals("foo_bar", StringUtils.normalizeString("foo bar"));
    Assert.assertEquals("foo_bar_abc_def", StringUtils.normalizeString("foo bar_abc def"));
  }

  @Test
  public void testJoinerFunction() throws Exception {
    String result =
        joinerFunction(Joiner.on(", "), Arrays.asList("A", "B", "C"),
                       new Function<String, String>() {
                         @Nullable @Override public String apply(@Nullable String input) {
                           return input;
                         }
                       });
    assertEquals("A, B, C", result);
  }
}
