/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-07 The eXist Project
 *  http://exist-db.org
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 *  $Id$
 */
package org.exist.xquery;

import org.exist.storage.DBBroker;
import org.exist.xmldb.DatabaseInstanceManager;
import org.exist.xmldb.IndexQueryService;
import org.exist.util.XMLFilenameFilter;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;
import org.junit.*;

import java.io.File;
import java.io.IOException;

/**
 * 
 */
public class OptimizerTest {

    private final static String OPTIMIZE = "declare option exist:optimize 'enable=yes';";
    private final static String NO_OPTIMIZE = "declare option exist:optimize 'enable=no';";
    private final static String NAMESPACES = "declare namespace mods='http://www.loc.gov/mods/v3';";
    
    private final static String XML =
            "<root>" +
            "   <a><b>one</b></a>" +
            "   <a><c><b>one</b></c></a>" +
            "</root>";

    private final static String COLLECTION_CONFIG =
        "<collection xmlns=\"http://exist-db.org/collection-config/1.0\">" +
    	"	<index xmlns:mods=\"http://www.loc.gov/mods/v3\">" +
    	"		<fulltext default=\"none\">" +
        "           <create qname=\"LINE\"/>" +
        "           <create qname=\"SPEAKER\"/>" +
        "           <create qname=\"mods:title\"/>" +
        "           <create qname=\"mods:topic\"/>" +
        "		</fulltext>" +
    	"		<create qname=\"b\" type=\"xs:string\"/>" +
        "        <create qname=\"SPEAKER\" type=\"xs:string\"/>" +
        "        <create qname=\"mods:internetMediaType\" type=\"xs:string\"/>" +
        "	</index>" +
    	"</collection>";

    private static Collection testCollection;

    @Test
    public void nestedQuery() {
        execute("/root/a[descendant::b = 'one']", true, "Inner b node should be returned.", 2);
        execute("/root/a[b = 'one']", true, "Inner b node should not be returned.", 1);
        execute("/root/a[b = 'one']", false, "Inner b node should not be returned.", 1);
    }

    @Test
    public void simplePredicates() {
        int r = execute("//SPEECH[LINE &= 'king']", false);
        execute("//SPEECH[LINE &= 'king']", true, "Optimized query should return same number of results.", r);
        r = execute("//SPEECH[SPEAKER = 'HAMLET']", false);
        execute("//SPEECH[SPEAKER = 'HAMLET']", true, "Optimized query should return same number of results.", r);
        r = execute("//LINE[. &= 'king']", false);
        execute("//LINE[. &= 'king']", true, "Optimized query should return same number of results.", r);
        r = execute("//SPEECH/LINE[. &= 'king']", false);
        execute("//SPEECH/LINE[. &= 'king']", true, "Optimized query should return same number of results.", r);
        r = execute("//*[LINE &= 'king']", false);
        execute("//*[LINE &= 'king']", true, "Optimized query should return same number of results.", r);
    }

    @Test
    public void namespaces() {
        int r = execute("//mods:mods/mods:titleInfo[mods:title &= 'ethnic']", false);
        execute("//mods:mods/mods:titleInfo[mods:title &= 'ethnic']", true, "Optimized query should return same number of results.", r);
        r = execute("//mods:mods/mods:physicalDescription[mods:internetMediaType &= 'application/pdf']", false);
        execute("//mods:mods/mods:physicalDescription[mods:internetMediaType &= 'application/pdf']", true, "Optimized query should return same number of results.", r);
        r = execute("//mods:mods/mods:*[mods:title &= 'ethnic']", false);
        execute("//mods:mods/mods:*[mods:title &= 'ethnic']", true, "Optimized query should return same number of results.", r);
    }

    @Test
    public void simplePredicatesRegex() {
        int r = execute("//SPEECH[LINE &= 'nor*']", false);
        execute("//SPEECH[LINE &= 'nor*']", true, "Optimized query should return same number of results.", r);
        r = execute("//SPEECH[LINE &= 'skirts nor*']", false);
        execute("//SPEECH[LINE &= 'skirts nor*']", true, "Optimized query should return same number of results.", r);
        r = execute("//SPEECH[near(LINE, 'skirts nor*', 2)]", false);
        execute("//SPEECH[near(LINE, 'skirts nor*', 2)]", true, "Optimized query should return same number of results.", r);

        r = execute("//SPEECH[match-all(LINE, 'skirts', 'nor.*')]", false);
        execute("//SPEECH[match-all(LINE, 'skirts', 'nor.*')]", true, "Optimized query should return same number of results.", r);
        execute("//SPEECH[text:match-all(LINE, ('skirts', 'nor.*'))]", false, "Query should return same number of results.", r);

        r = execute("//SPEECH[match-any(LINE, 'skirts', 'nor.*')]", false);
        execute("//SPEECH[match-any(LINE, 'skirts', 'nor.*')]", true, "Optimized query should return same number of results.", r);
        execute("//SPEECH[text:match-any(LINE, ('skirts', 'nor.*'), 'w')]", false, "Query should return same number of results.", r);
        execute("//SPEECH[text:match-any(LINE, ('skirts', 'nor.*'), 'w')]", true, "Optimized query should return same number of results.", r);
        execute("//SPEECH[text:match-any(LINE, ('skirts', '^nor.*$'))]", true, "Optimized query should return same number of results.", r);

        r = execute("//SPEECH[matches(SPEAKER, '^HAM.*')]", false);
        execute("//SPEECH[matches(SPEAKER, '^HAM.*')]", true, "Optimized query should return same number of results.", r);
        r = execute("//SPEECH[starts-with(SPEAKER, 'HAML')]", false);
        execute("//SPEECH[starts-with(SPEAKER, 'HAML')]", true, "Optimized query should return same number of results.", r);
        r = execute("//SPEECH[ends-with(SPEAKER, 'EO')]", false);
        execute("//SPEECH[ends-with(SPEAKER, 'EO')]", true, "Optimized query should return same number of results.", r);
    }

    @Test
    public void twoPredicates() {
        int r = execute("//SPEECH[LINE &= 'king'][SPEAKER='HAMLET']", false);
        execute("//SPEECH[LINE &= 'king'][SPEAKER='HAMLET']", true, "Optimized query should return same number of results.", r);
        r = execute("//SPEECH[SPEAKER='HAMLET'][LINE &= 'king']", false);
        execute("//SPEECH[SPEAKER='HAMLET'][LINE &= 'king']", true, "Optimized query should return same number of results.", r);
    }

    @Ignore("not correctly optimized yet")
    @Test
    public void complexPaths() {
        int r = execute("//mods:mods[mods:titleInfo/mods:title &= 'ethnic']", false);
        execute("//mods:mods[mods:titleInfo/mods:title &= 'ethnic']", true, "Optimized query should return same number of results.", r);
    }

    @Ignore("not correctly optimized yet")
    @Test
    public void booleanOperator() {
        int r = execute("//SPEECH[LINE &= 'king' and SPEAKER='HAMLET']", false);
        execute("//SPEECH[LINE &= 'king' and SPEAKER='HAMLET']", true, "Optimized query should return same number of results.", r);
        r = execute("//SPEECH[LINE &= 'king' or SPEAKER='HAMLET']", false);
        execute("//SPEECH[LINE &= 'king' or SPEAKER='HAMLET']", true, "Optimized query should return same number of results.", r);
    }

    private int execute(String query, boolean optimize) {
        try {
            System.out.println("--- Query: " + query + "; Optimize: " + Boolean.toString(optimize));
            XQueryService service = (XQueryService) testCollection.getService("XQueryService", "1.0");
            query = NAMESPACES + query;
            if (optimize)
                query = OPTIMIZE + query;
            else
                query = NO_OPTIMIZE + query;
            ResourceSet result = service.query(query);
            return (int) result.getSize();
        } catch (XMLDBException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        return 0;
    }

    private void execute(String query, boolean optimize, String message, int expected) {
        try {
            System.out.println("--- Query: " + query + "; Optimize: " + Boolean.toString(optimize));
            XQueryService service = (XQueryService) testCollection.getService("XQueryService", "1.0");
            if (optimize)
                query = NAMESPACES + OPTIMIZE + query;
            else
                query = NAMESPACES + NO_OPTIMIZE + query;
            ResourceSet result = service.query(query);
            Assert.assertEquals(message, expected, result.getSize());
        } catch (XMLDBException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
    
    @BeforeClass
    public static void initDatabase() {
		try {
			// initialize driver
			Class cl = Class.forName("org.exist.xmldb.DatabaseImpl");
			Database database = (Database) cl.newInstance();
			database.setProperty("create-database", "true");
			DatabaseManager.registerDatabase(database);

			Collection root =
				DatabaseManager.getCollection("xmldb:exist://" + DBBroker.ROOT_COLLECTION, "admin",	null);
			CollectionManagementService service =
				(CollectionManagementService) root.getService("CollectionManagementService", "1.0");
			testCollection = service.createCollection("test");
			Assert.assertNotNull(testCollection);

            IndexQueryService idxConf = (IndexQueryService) testCollection.getService("IndexQueryService", "1.0");
            idxConf.configureCollection(COLLECTION_CONFIG);
            
            XMLResource resource = (XMLResource) testCollection.createResource("test.xml", "XMLResource");
            resource.setContent(XML);
            testCollection.storeResource(resource);

            String existHome = System.getProperty("exist.home");
            File existDir = existHome==null ? new File(".") : new File(existHome);
            File dir = new File(existDir, "samples/shakespeare");
            if (!dir.canRead())
                throw new IOException("Unable to read samples directory");
            File[] files = dir.listFiles(new XMLFilenameFilter());
            for (File file : files) {
                resource = (XMLResource) testCollection.createResource(file.getName(), "XMLResource");
                resource.setContent(file);
                testCollection.storeResource(resource);
            }

            dir = new File(existDir, "samples/mods");
            if (!dir.canRead())
                throw new IOException("Unable to read samples directory");
            files = dir.listFiles(new XMLFilenameFilter());
            for (File file : files) {
                resource = (XMLResource) testCollection.createResource(file.getName(), "XMLResource");
                resource.setContent(file);
                testCollection.storeResource(resource);
            }
        } catch (Exception e) {
			e.printStackTrace();
            Assert.fail(e.getMessage());
        }
	}

    @AfterClass
    public static void shutdownDB() {
        try {
            CollectionManagementService service =
                    (CollectionManagementService) testCollection.getService("CollectionManagementService", "1.0");
            service.removeCollection(".");

            Collection system = DatabaseManager.getCollection("xmldb:exist:///db/system/config/db", "admin", null);
            service = (CollectionManagementService) system.getService("CollectionManagementService", "1.0");
            service.removeCollection(".");

            DatabaseInstanceManager dim =
                (DatabaseInstanceManager) testCollection.getService(
                    "DatabaseInstanceManager", "1.0");
            dim.shutdown();
        } catch (XMLDBException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        testCollection = null;

		System.out.println("tearDown PASSED");
	}
}
