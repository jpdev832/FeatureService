package com.staticvillage.feature.place;

import com.jayway.restassured.RestAssured;
import com.staticvillage.feature.place.Application;
import com.staticvillage.feature.place.controller.PlaceController;
import com.staticvillage.feature.place.model.Feature;
import com.staticvillage.feature.place.model.Place;
import com.staticvillage.feature.place.model.TransactionObject;
import com.staticvillage.feature.place.store.DataStore;
import com.staticvillage.feature.place.store.MongoDBStore;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.hasItems;

/**
 * Created by joelparrish on 3/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:1234")
public class PlaceControllerTest {
    static Place place;
    static Feature feature;
    //private static DataStore<TransactionObject> store;

    //@Value("${local.server.port}")
    static int port = 1234;

    @BeforeClass
    public static void setup(){
        feature = new Feature();
        feature.setId("5504b49e30040f9fcb1fd1cb");
        feature.setName("test feature");
        feature.setCategory("cats");

        place = new Place();
        place.setId("5504b49e30040f9fcb1fd1cc");
        place.setName("test");
        place.setNeighborhood("neigh");
        place.setCity("brooklyn");
        place.setState("new york");
        place.setCountry("usa");
        place.setType("bar");
        place.setLatitude(1.23456);
        place.setLongitude(6.54321);
        place.setFeatures(new Feature[]{ feature });

        //uncomment to generate new test db
        /*store = new MongoDBStore();

        feature = new Feature();
        feature.setName("test feature");
        feature.setCategory("cats");

        place = new Place();
        place.setName("test");
        place.setNeighborhood("neigh");
        place.setCity("brooklyn");
        place.setState("new york");
        place.setCountry("usa");
        place.setType("bar");
        place.setLatitude(1.23456);
        place.setLongitude(6.54321);

        HashMap<String, Object> placeMap = new HashMap<String, Object>();
        placeMap.put(PlaceController.EXTRA_KEY_TARGET, Place.class);

        HashMap<String, Object> featureMap = new HashMap<String, Object>();
        featureMap.put(PlaceController.EXTRA_KEY_TARGET, Feature.class);

        System.out.println("deleting test collections");
        store.deleteAll(placeMap);
        store.deleteAll(featureMap);

        System.out.println("adding new items to test collections");
        feature.setId(store.insert(feature, featureMap));
        place.setFeatures(new Feature[]{ feature });
        place.setId(store.insert(place, placeMap));

        System.out.println(String.format("place id set: %s", place.getId()));
        System.out.println(String.format("feature id set: %s", feature.getId()));*/

        RestAssured.port = port;
    }

    @Test
    public void getPlaceById(){
        System.out.println(String.format("Checking place id: %s", place.getId()));

        get(String.format("/place/%s", place.getId())).
            then().
            assertThat().
                statusCode(HttpStatus.SC_OK).
                body("data." + MongoDBStore.KEY_ID, hasItems(place.getId())).
                body("data." + MongoDBStore.KEY_NAME, hasItems(place.getName())).
                body("data." + MongoDBStore.KEY_NEIGHBORHOOD, hasItems(place.getNeighborhood())).
                body("data." + MongoDBStore.KEY_CITY, hasItems(place.getCity())).
                body("data." + MongoDBStore.KEY_STATE, hasItems(place.getState())).
                body("data." + MongoDBStore.KEY_COUNTRY, hasItems(place.getCountry())).
                body("data." + MongoDBStore.KEY_TYPE, hasItems(place.getType())).
                body("message", Matchers.is("success")).
                body("ok", Matchers.is(true)).
                body("index", Matchers.is("places")).
                body("type", Matchers.is("place"));
    }

    @Test
    public void getPlaceFail(){
        System.out.println(String.format("Checking place id: %s", "FakeId12345"));

        get(String.format("/place/%s", "FakeId12345")).
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                body("ok", Matchers.is(false)).
                body("index", Matchers.is("places")).
                body("type", Matchers.is("place"));
    }

    @Test
    public void getPlaceByName(){
        get(String.format("/place?name=%s", place.getName())).
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                body("data." + MongoDBStore.KEY_ID, hasItems(place.getId())).
                body("data." + MongoDBStore.KEY_NAME, hasItems(place.getName())).
                body("data." + MongoDBStore.KEY_NEIGHBORHOOD, hasItems(place.getNeighborhood())).
                body("data." + MongoDBStore.KEY_CITY, hasItems(place.getCity())).
                body("data." + MongoDBStore.KEY_STATE, hasItems(place.getState())).
                body("data." + MongoDBStore.KEY_COUNTRY, hasItems(place.getCountry())).
                body("data." + MongoDBStore.KEY_TYPE, hasItems(place.getType())).
                body("message", Matchers.is("success")).
                body("ok", Matchers.is(true)).
                body("index", Matchers.is("places")).
                body("type", Matchers.is("place"));
    }

    @Test
    public void getPlaceByType(){
        get(String.format("/place?type=%s", place.getType())).
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                body("data." + MongoDBStore.KEY_ID, hasItems(place.getId())).
                body("data." + MongoDBStore.KEY_NAME, hasItems(place.getName())).
                body("data." + MongoDBStore.KEY_NEIGHBORHOOD, hasItems(place.getNeighborhood())).
                body("data." + MongoDBStore.KEY_CITY, hasItems(place.getCity())).
                body("data." + MongoDBStore.KEY_STATE, hasItems(place.getState())).
                body("data." + MongoDBStore.KEY_COUNTRY, hasItems(place.getCountry())).
                body("data." + MongoDBStore.KEY_TYPE, hasItems(place.getType())).
                body("message", Matchers.is("success")).
                body("ok", Matchers.is(true)).
                body("index", Matchers.is("places")).
                body("type", Matchers.is("place"));
    }

    @Test
    public void getPlaceByNeighborhood(){
        get(String.format("/place?neighborhood=%s", place.getNeighborhood())).
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                body("data." + MongoDBStore.KEY_ID, hasItems(place.getId())).
                body("data." + MongoDBStore.KEY_NAME, hasItems(place.getName())).
                body("data." + MongoDBStore.KEY_NEIGHBORHOOD, hasItems(place.getNeighborhood())).
                body("data." + MongoDBStore.KEY_CITY, hasItems(place.getCity())).
                body("data." + MongoDBStore.KEY_STATE, hasItems(place.getState())).
                body("data." + MongoDBStore.KEY_COUNTRY, hasItems(place.getCountry())).
                body("data." + MongoDBStore.KEY_TYPE, hasItems(place.getType())).
                body("message", Matchers.is("success")).
                body("ok", Matchers.is(true)).
                body("index", Matchers.is("places")).
                body("type", Matchers.is("place"));
    }

    @Test
    public void getPlaceByCity(){
        get(String.format("/place?city=%s", place.getCity())).
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                body("data." + MongoDBStore.KEY_ID, hasItems(place.getId())).
                body("data." + MongoDBStore.KEY_NAME, hasItems(place.getName())).
                body("data." + MongoDBStore.KEY_NEIGHBORHOOD, hasItems(place.getNeighborhood())).
                body("data." + MongoDBStore.KEY_CITY, hasItems(place.getCity())).
                body("data." + MongoDBStore.KEY_STATE, hasItems(place.getState())).
                body("data." + MongoDBStore.KEY_COUNTRY, hasItems(place.getCountry())).
                body("data." + MongoDBStore.KEY_TYPE, hasItems(place.getType())).
                body("message", Matchers.is("success")).
                body("ok", Matchers.is(true)).
                body("index", Matchers.is("places")).
                body("type", Matchers.is("place"));
    }

    @Test
    public void getPlaceByState(){
        get(String.format("/place?state=%s", place.getState())).
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                body("data." + MongoDBStore.KEY_ID, hasItems(place.getId())).
                body("data." + MongoDBStore.KEY_NAME, hasItems(place.getName())).
                body("data." + MongoDBStore.KEY_NEIGHBORHOOD, hasItems(place.getNeighborhood())).
                body("data." + MongoDBStore.KEY_CITY, hasItems(place.getCity())).
                body("data." + MongoDBStore.KEY_STATE, hasItems(place.getState())).
                body("data." + MongoDBStore.KEY_COUNTRY, hasItems(place.getCountry())).
                body("data." + MongoDBStore.KEY_TYPE, hasItems(place.getType())).
                body("message", Matchers.is("success")).
                body("ok", Matchers.is(true)).
                body("index", Matchers.is("places")).
                body("type", Matchers.is("place"));
    }

    @Test
     public void getPlaceByCountry(){
        get(String.format("/place?country=%s", place.getCountry())).
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                body("data." + MongoDBStore.KEY_ID, hasItems(place.getId())).
                body("data." + MongoDBStore.KEY_NAME, hasItems(place.getName())).
                body("data." + MongoDBStore.KEY_NEIGHBORHOOD, hasItems(place.getNeighborhood())).
                body("data." + MongoDBStore.KEY_CITY, hasItems(place.getCity())).
                body("data." + MongoDBStore.KEY_STATE, hasItems(place.getState())).
                body("data." + MongoDBStore.KEY_COUNTRY, hasItems(place.getCountry())).
                body("data." + MongoDBStore.KEY_TYPE, hasItems(place.getType())).
                body("message", Matchers.is("success")).
                body("ok", Matchers.is(true)).
                body("index", Matchers.is("places")).
                body("type", Matchers.is("place"));
    }

    @Test
    public void getFeatureById(){
        get(String.format("/place/feature/%s", feature.getId())).
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                body("data." + MongoDBStore.KEY_ID, hasItems(feature.getId())).
                body("data." + MongoDBStore.KEY_NAME, hasItems(feature.getName())).
                body("data." + MongoDBStore.KEY_CATEGORY, hasItems(feature.getCategory())).
                body("message", Matchers.is("success")).
                body("ok", Matchers.is(true)).
                body("index", Matchers.is("features")).
                body("type", Matchers.is("feature"));
    }

    @Test
    public void getFeatureByName(){
        get(String.format("/place/feature?name=%s", feature.getName())).
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                body("data." + MongoDBStore.KEY_ID, hasItems(feature.getId())).
                body("data." + MongoDBStore.KEY_NAME, hasItems(feature.getName())).
                body("data." + MongoDBStore.KEY_CATEGORY, hasItems(feature.getCategory())).
                body("message", Matchers.is("success")).
                body("ok", Matchers.is(true)).
                body("index", Matchers.is("features")).
                body("type", Matchers.is("feature"));
    }

    @Test
    public void getFeatureByCategory(){
        get(String.format("/place/feature?category=%s", feature.getCategory())).
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                body("data." + MongoDBStore.KEY_ID, hasItems(feature.getId())).
                body("data." + MongoDBStore.KEY_NAME, hasItems(feature.getName())).
                body("data." + MongoDBStore.KEY_CATEGORY, hasItems(feature.getCategory())).
                body("message", Matchers.is("success")).
                body("ok", Matchers.is(true)).
                body("index", Matchers.is("features")).
                body("type", Matchers.is("feature"));
    }

    @Test
    public void getFeatureFail(){
        get(String.format("/place/feature/%s", "fakeid1234")).
                then().
                assertThat().
                statusCode(HttpStatus.SC_OK).
                body("ok", Matchers.is(false)).
                body("index", Matchers.is("features")).
                body("type", Matchers.is("feature"));
    }
}
