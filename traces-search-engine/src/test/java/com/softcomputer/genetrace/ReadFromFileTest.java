package com.softcomputer.genetrace;

import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;



public class ReadFromFileTest {

    ReadFromFile readFromFile = new ReadFromFile();
    List<String> sciezki = new ArrayList<String>();

    public List<String> init() {
        readFromFile.setPathOffset("src\\test\\resources\\wl\\domains\\PROD2\\servers");
        List<String> sciezki = readFromFile.checkFiles();
        readFromFile.setLinesBefore(5);
        readFromFile.setLinesAfter(5);
        return sciezki;
    }
/*   @Test
    public void nextWithPhraseTest() throws IOException, ParseException {

        readFromFile.setPhrase("at com");
        readFromFile.set(init().get(0));
       System.out.println(readFromFile.amountPhraseWithDate("10-08-2018 16:56:02","10-08-2018 16:56:25", false));

       readFromFile.set(init().get(0));
       for(int j=0;j<18;j++) {
           List<String> temp2 = readFromFile.nextWithDate("10-08-2018 16:56:02","10-08-2018 16:56:25", false);
           for (int i = 0; i < temp2.size(); i++) {
               if (i == readFromFile.getMiddle() - 1) {
                   System.out.println("******************************");
                   System.out.println(temp2.get(i));
                   System.out.println("******************************");
               } else
                   System.out.println(temp2.get(i));

           }System.out.println();
       }
    }*/

    @Test
    public void nextLineTest() throws IOException {
        readFromFile.setPhrase("clinic");
        readFromFile.set(init().get(0));

        List<String> temp2 = readFromFile.nextWithPhrase(false);

        List<String> nextLineList = readFromFile.nextOneLine();
        assertEquals("Here is test for search next line: ", nextLineList.get(5), "10-08-2018 16:56:02 ERROR [[ACTIVE] ExecuteThread: '6' for queue: 'weblogic.kernel.Default (self-tuning)'] [com.scc.gcmintegration.inbound.TechNonTechUpdateHelper.logInterpretations(TechNonTechUpdateHelper.java:177)] - ");

    }

    @Test
    public void amountPhraseWithDateTest() throws IOException, ParseException {
        readFromFile.setPhrase("error");
        readFromFile.set(init().get(0));
        int liczba = readFromFile.amountPhraseWithDate("10-08-2018 16:56:02", "10-08-2018 16:58:07", false);
        assertTrue("Amount phrases between dates should return TRUE.", liczba == 29);
        assertFalse("Amount phrases between dates should return FALSE", liczba == 10);
    }

    @Test
    public void objectIsNullTest() throws IOException {
        ReadFromFile readFromFile = new ReadFromFile();
        readFromFile.setPathOffset("rak");
        assertNull(readFromFile.getPhrase());
    }

    @Test
    public void notExistPhraseTest() throws IOException {
        readFromFile.setPhrase("brak");
        readFromFile.set(init().get(0));
        List<String> temp2 = readFromFile.nextWithPhrase(false);
        assertNull(temp2);
    }

    @Test
    public void containLinesBetweenDatesTest() throws IOException, ParseException {
        readFromFile.setPhrase("Exception");
        readFromFile.set(init().get(0));

        List<String> temp2 = readFromFile.nextWithDate("10-08-2018 16:56:02", "10-08-2018 16:56:25", false);
        assertEquals("Here is test for contains first search line between dates: ", temp2.get(0), ") ");
        assertEquals("Here is test for contains last search line between dates: ", temp2.get(10),
                "        at com.scc.gcmrbs.GcmRbsMgr.getRbsSystemInfo(GcmRbsMgr.java:190)");
    }
}
