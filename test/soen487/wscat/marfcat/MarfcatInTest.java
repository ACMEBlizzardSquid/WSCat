/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soen487.wscat.marfcat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author connorbode
 */
public class MarfcatInTest {
    
    public MarfcatInTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void readTest () {
        try {
            MarfcatIn marfcatIn = new MarfcatIn();
            MarfcatInItem item1 = new MarfcatInItem();
            MarfcatInItem item2 = new MarfcatInItem();
            item1.setPath("test/soen487/wscat/marfcat/test1.txt");
            item2.setPath("test/soen487/wscat/marfcat/test2.txt");
            item1.loadFileInfo();
            item2.loadFileInfo();
            marfcatIn.addItem(item1);
            marfcatIn.addItem(item2);
            String path = marfcatIn.getPath();
            marfcatIn.write();
            
            marfcatIn = new MarfcatIn(path);
            MarfcatInItem item1loaded = marfcatIn.getItemById(1);
            MarfcatInItem item2loaded = marfcatIn.getItemById(2);
            
            assertEquals(item1loaded.getPath(), item1.getPath());
            assertEquals(item2loaded.getPath(), item2.getPath());
            assertEquals(item1loaded.getCVE(), item1.getCVE());
            assertEquals(item2loaded.getCVE(), item2.getCVE());
            assertEquals(item1loaded.getLines(), item1.getLines());
            assertEquals(item2loaded.getLines(), item2.getLines());
            assertEquals(item1loaded.getWords(), item1.getWords());
            assertEquals(item2loaded.getWords(), item2.getWords());
            assertEquals(item1loaded.getBytes(), item1.getBytes());
            assertEquals(item2loaded.getBytes(), item2.getBytes());
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }
    
    @Test
    public void updateTest () {
        try {
            MarfcatIn marfcatIn = new MarfcatIn();
            
            StringBuilder sb = new StringBuilder();
            sb.append("<file id=\"1\" path=\"apache-tomcat-5.5.13-src/build/resources/tomcat.spec\">")
                    .append("<meta><type>ASCII English text</type><length lines=\"89\" words=\"341\" bytes=\"2565\"/></meta>")
                    .append("<location line=\"\" fraglines=\"\">")
                        .append("<meta><cve/><name cweid=\"\"/></meta>")
                        .append("<fragment></fragment>")
                        .append("<explanation></explanation>")
                    .append("</location>")
                .append("</file>");
            
            MarfcatInItem marfcatInItem = marfcatIn.updateItemById(1, sb.toString());
            assertEquals(marfcatInItem.toString(), sb.toString());
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }
}
