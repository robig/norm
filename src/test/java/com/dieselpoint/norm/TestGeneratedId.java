package com.dieselpoint.norm;

import java.util.List;

import javax.persistence.*;

import org.junit.*;


public class TestGeneratedId {

    private Database db;

    @Before
    public void setUp() {
	Setup.setSysProperties();
        db = new Database();

	db.sql("drop table if exists pojo").execute();

	db.createTable(NormPojo.class);
    }
	
    @Test
    public void testCreate() {
        NormPojo np = new NormPojo();
        np.setId("MyID");
        np.setName("My name");
        db.generatedKeyReceiver(np, "id").insert(np);
        Assert.assertNotNull(np.getDatabaseId());
    }
    
    @Test
    public void testRetrieval() {
	testCreate();

        List<NormPojo> npList = null;
        npList = db.results(NormPojo.class);
        Assert.assertNotNull(npList);
        Assert.assertTrue(npList.size() > 0);
    }
    
    @Table(name = "pojo") 
    public static class NormPojo {

        /** Database record ID. */
        private Integer databaseId;

        /** Unique identifier of the object. */
        private String id;

        /** Human readable name. */
        private String name;

        /**
         * @return the id
         */
        @Column(name = "object_id", unique = true)
        public String getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return the name
         */
        @Column(name = "name")
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        @Id
        @GeneratedValue
        @Column(name = "id")
        public Integer getDatabaseId() {
            return databaseId;
        }

        public void setDatabaseId(Integer databaseId) {
            this.databaseId = databaseId;
        }   

    }

    
    
}
