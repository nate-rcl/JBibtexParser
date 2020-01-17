package com.JBibtexParser.tests;

import com.JBibtexParser.util.LeveledString;
import com.JBibtexParser.util.exceptions.ParseErrorException;
import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Created by Aleksander on 28.11.2017.
 */
class LeveledStringTest {
    @Test
    void leveledStringWhithoutBracketsShouldBeCreated() throws ParseErrorException {
        LeveledString s1= new LeveledString("foobar");
        assertEquals ("foobar",s1.getEntry());
        assertArrayEquals(new int[]{0,0,0,0,0,0},s1.getLevels());
    }
    @Test
    void leveledStringWithBracketsShouldBeCreated() throws ParseErrorException {
        LeveledString s1= new LeveledString("fo{o{b}a}r");
        assertEquals ("fo{o{b}a}r",s1.getEntry());
        assertArrayEquals(new int[]{0,0,0,1,1,2,1,1,0,0},s1.getLevels());
    }

    @Test
    void joinShouldWorkCorrectly() throws ParseErrorException {
        LeveledString s1= new LeveledString("f{}oo");
        LeveledString s2= new LeveledString("bar");
        assertEquals (new LeveledString("f{}oobar"), s1.join(s2));
    }

    @Test
    void substituteOnLevelShouldCorrectlySubstituteBrackets() throws ParseErrorException {
        LeveledString s1 = new LeveledString("t{e{s{t}i}n}g");
          s1=s1.substituteOnLevel(0,new String[]{"{","}"},"");

        assertEquals(new LeveledString("te{s{t}i}ng"),s1);
        
    }
    @Test
    void substituteOnLevelShouldCorrectlySubstituteBrackets2() throws ParseErrorException {
        LeveledString s1 = new LeveledString("a{b{c{d{e}f}g}h}i");
        s1=s1.substituteOnLevel(2,new String[]{"{","}"},"");

        assertEquals(new LeveledString("abcd{e}fghi"),s1);
        
    }

    @Test
    void findAllOccurencesShouldWork() throws ParseErrorException {
        LeveledString s1 = new LeveledString("atestbtestctest");
        assertEquals(3,LeveledString.findAllOccurences(s1.getEntry(),"test").size());
    }

    @Test
    void getSubstringsOfLevelShouldWorkCorrectlyWithBrackets() throws ParseErrorException {
        LeveledString s1 = new LeveledString("a,b,{c,{test},d}");
        assertArrayEquals(new String[]{"test"},s1.getSubstringsOfLevel(2, (x, y) -> -y + x).stream().map(LeveledString::getEntry).toArray());

       }
    @Test
    void getSubstringsOfLevelShouldWorkCorrectlyWithQuotes() throws ParseErrorException {
        LeveledString s1 = new LeveledString("\"test\"");
        assertArrayEquals(new String[]{"test"},s1.getSubstringsOfLevel(1, (x, y) -> x-y).stream().map(LeveledString::getEntry).toArray());

    }

    @Test
    void mismatchedBracketsCauseParseErrorException() {
        Executable executable = () -> new LeveledString("test{");
        assertThrows(ParseErrorException.class, executable);
    }

    @Test
    void toStringWithLevels() {
    }

    @Test
    void splitOnLevel() throws ParseErrorException {
        LeveledString s1 = new LeveledString("a,b,{c,d}");
        assertArrayEquals(new String[]{"a","b","{c,d}"},s1.splitOnLevel(',',0).stream().map(LeveledString::getEntry).toArray());

    }

    @Test
    void splitIntoKeyValuePairs() throws ParseErrorException {
        LeveledString s1 = new LeveledString("{k1=vl,k2=v2}");
        List<MutablePair<LeveledString, LeveledString>> pairs = s1.splitIntoKeyValuePairs();
        assertEquals("k1",pairs.get(0).getKey().getEntry());
        assertEquals("v2",pairs.get(1).getValue().getEntry());
    }

    @Test
    void length() throws ParseErrorException {
        LeveledString s1 = new LeveledString("jdtcpssodd");
        assertEquals(10,s1.length());
    }

    @Test
    void trimShouldReturnCorrectLegth() throws ParseErrorException {
            LeveledString s1 = new LeveledString("  test ,test   ");
            LeveledString s2 = new LeveledString("test ,test");
            
            assertEquals(s1.trim(),s2);
        }
    @Test
    void trimShouldReturnCorrectLeveledString() throws ParseErrorException {
        LeveledString s1 = new LeveledString(" t{e{s{t}i}n}g  ");
        LeveledString s2 = new LeveledString("t{e{s{t}i}n}g");
        
        assertEquals(s1.trim(),s2);
    }
}