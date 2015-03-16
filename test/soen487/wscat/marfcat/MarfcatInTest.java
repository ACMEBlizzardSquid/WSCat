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
            String path = marfcatIn.getPath();
            marfcatIn.write();
            
            marfcatIn = new MarfcatIn(path);
            item1 = marfcatIn.getItemById(1);
            item2 = marfcatIn.getItemById(2);
        } catch (Exception e) {
            System.out.println(e);
            fail();
        }
    }
}
