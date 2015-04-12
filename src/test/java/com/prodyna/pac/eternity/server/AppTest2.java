//package com.prodyna.pac.eternity.server;
//
//import com.prodyna.pac.eternity.server.service.ProjectService;
//import junit.framework.Assert;
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
//import javax.annotation.Resource;
//import javax.inject.Inject;
//
///**
// * Unit test for simple App.
// */
//public class AppTest extends TestCase {
//
//    @Inject
//    private ProjectService service;
//
//    public AppTest() {
//
//    }
//
//    /**
//     * Create the test case
//     *
//     * @param testName name of the test case
//     */
//    public AppTest(String testName) {
//        super(testName);
//    }
//
//    /**
//     * @return the suite of tests being tested
//     */
//    public static Test suite() {
//        return new TestSuite(AppTest.class);
//    }
//
//    /**
//     * Rigourous Test :-)
//     */
//    public void testApp() {
//        assertTrue(true);
//    }
//
//    public void testFindAllProjects() {
//        Assert.assertFalse(service.findAll().isEmpty());
//    }
//}
