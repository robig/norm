package com.dieselpoint.norm;

import static org.junit.Assert.fail;

import java.util.*;

import javax.persistence.*;

import org.junit.Before;
import org.junit.Test;

public class TestAnnotations {
    Database db;

    @Before
    public void setUp() {

        Setup.setSysProperties();

        db = new Database();

        db.sql("drop table if exists rooms").execute();
        db.sql("drop table if exists users").execute();
        db.sql("drop table if exists addresses").execute();

        db.createTable(Address.class);
        db.createTable(Room.class);
        db.createTable(User.class);

        Address a = new Address();
        a.houseNumber = "123a";
        a.street = "1st Street";
        db.insert(a);

        User u = new User();
        u.id = 1;
        u.name = "Mickey Mouse";
        db.insert(u);

        Room row = new Room();
        row.id = 101;
        row.name = "Room 101";
        row.user = u;
        row.address = a;
        db.insert(row);
    }

    @Test
    public void test() {

        // primitive
        Long myId = db.sql("select id from rooms").first(Long.class);
        if (myId != 101) {
            fail();
        }

        // map
        Map myMap = db.table("rooms").first(LinkedHashMap.class);
        String str = myMap.toString();
        if (!str.equals("{id=101, name=Room 101, resident_id=1, address_id=0}")) {
            fail();
        }

        // pojo
        Room myRoom = db.first(Room.class);
        String myRowStr = myRoom.toString();
        if (!myRowStr.equals("101Room 1011Mickey Mouse")) {
            fail();
        }

        myRoom = db.where("id=?", 101).first(Room.class);
        myRowStr = myRoom.toString();
        if (!myRowStr.equals("101Room 1011Mickey Mouse")) {
            fail();
        }

        Address adr = db.first(Address.class);
        List<Room> rooms = adr.getRooms();
        rooms.get(0);

    }

    // TODO test @ColumnOrder

    // POJO with getter/setters
    @Table(name = "rooms")
    public static class Room {
        @Id
        @Column(unique = true)
        private long id;

        @Column(name = "name")
        private String name;

        @OneToOne
        @Column(name = "resident_id")
        private User user;

        @ManyToOne
        @Column(name = "address_id")
        private Address address;

        @Override
        public String toString() {
            return id + name + (user != null ? user.toString() : "");
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    @Table(name = "users")
    public static class User {
        @Id
        @Column(unique = true)
        private long id;

        @Column(name = "name")
        private String name;

        @Override
        public String toString() {
            return id + name;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Table(name = "addresses")
    public static class Address {
        @Id
        @Column(unique = true)
        private long id;

        @Column(name = "street")
        private String street;


        private String houseNumber;

        @OneToMany(targetEntity = Room.class)
        private List<Room> rooms;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        @Column(name = "number") // Issue in xbt
        public String getHouseNumber() {
            return houseNumber;
        }

        public void setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
        }

        public List<Room> getRooms() {
            return rooms;
        }

        public void setRooms(List<Room> rooms) {
            this.rooms = rooms;
        }
    }

}
