/*
 * Copyright notice: CreativeHummers@gmail.com Restricted for now. Will consider any other license after Project completion
 * @author: Sudhakar Krishnamachari
 */
package tests.com.dbtransactions.tests;

import com.dbtransactions.DatabaseTransactions;
import com.dbtransactions.enums.DBName;
import com.dbtransactions.enums.ResultFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class DBBasicTests {

    static DatabaseTransactions instance = null;

    @Before
    public void setUp(){
        //PostgreSQLJDBC.setWorkingDirectory("src/main/resources");
        instance = new DatabaseTransactions(DBName.PostGreSQLJDBC, "default");
    }

    @Test
    public void testBasicQuery() throws JSONException {

        String jsonResp = instance.handleRequest("100001",  new ArrayList(), ResultFormat.JSONObjectsList);
        JSONObject json = new JSONObject(jsonResp);
        JSONArray dataRow = (JSONArray)((JSONArray) json.get("data_rows")).get(0);

        Assert.assertTrue( "Expected to contain : data_rows ",((String) dataRow.get(2)).contains("skrish#123"));
    }

    @Test
    public void testUserTestQuery() throws JSONException {

        String jsonResp = instance.handleRequest("100010" ,  Arrays.asList(new String[] {"skrish01"}), ResultFormat.JSONObjectsList );
        JSONObject json = new JSONObject(jsonResp);
        JSONArray dataRow = (JSONArray)((JSONArray) json.get("data_rows")).get(0);

        Assert.assertTrue( "Expected to contain : data_rows ",((String) dataRow.get(2)).contains("skrish#123"));
    }

    @Test
    public void testUserTest02Query() throws JSONException {

        String[] params = new String[] { "skrish01"};
        String jsonResp = instance.handleRequest( "100110" , Arrays.asList(params) ,ResultFormat.JSONObjectsList);
        JSONObject json = new JSONObject(jsonResp);
        JSONArray dataRow = (JSONArray)((JSONArray) json.get("data_rows")).get(0);
        Assert.assertTrue( "Expected to contain : data_rows ",((String) dataRow.get(0)).contains("skrish#123"));
        //Assert.assertTrue( "Expected to contain : data_rows ",resultArray[0][1].contains("SUPERADMIN"));
    }

    @Test
    public void testMultipleRowsQuery() throws JSONException {

        String jsonResp = instance.handleRequest("100100",  new ArrayList(), ResultFormat.JSONObjectsList);
        JSONObject json = new JSONObject(jsonResp);
        JSONArray dataRows = (JSONArray) json.get("data_rows");
        Assert.assertTrue( "Expected to contain : data_rows ",( dataRows.length())> 1);
    }

    @Test
    public void testInsertTestUser() throws JSONException {
        String uuid = "USER_2023_"+ new java.util.Random().nextInt(9999) +"_"+new java.util.Random().nextInt(9999);
        String phone = "+91-"+ new java.util.Random().nextInt(9999) +"-"+new java.util.Random().nextInt(999999);
        int user_random_suffix = new java.util.Random().nextInt(9999);
        String last_name = "User_"+ user_random_suffix;
        String userid = "testuser"+user_random_suffix;
        String profile_code = "USPR_2022_DEC_XRFE-"+user_random_suffix;
        String[] params = new String[] { uuid, "Test" , last_name , userid , "pwd#1234" , "adjda@gmail.com" , phone, "USPR_2022_DEC_XRFE_A129_XE12" , "Unit Test Data Creation 01" };
        String resultJSON = instance.handleRequest( "100500" , Arrays.asList(params) , ResultFormat.JSONObjectsList);
        JSONObject json = new JSONObject(resultJSON);
        JSONArray data_row = (JSONArray)((JSONArray) json.get("data_rows")).get(0);
        Assert.assertTrue( "Expected to contain : data_rows ",((int)data_row.get(0))==1 );
    }

    @Test
    public void testUpdateTestUser() throws JSONException {
        int user_random_suffix = new java.util.Random().nextInt(9999);
        String pwd = "pwd#" + user_random_suffix;
        String[] params = new String[] {  pwd , "testuser2755" };
        String resultJSON = instance.handleRequest( "100700" , Arrays.asList(params) , ResultFormat.JSONObjectsList);
        JSONObject json = new JSONObject(resultJSON);
        JSONArray data_row = (JSONArray)((JSONArray) json.get("data_rows")).get(0);
        Assert.assertTrue( "Expected to contain : data_rows ", (int)data_row.get(0)==1 );
    }


    @Test
    public void testUpdateTestUserError() throws JSONException {
        int user_random_suffix = new java.util.Random().nextInt(9999);
        String pwd = "pwd#" + user_random_suffix;
        String[] params = new String[] {  pwd , "testuser2755" };
        String resultJSON = instance.handleRequest( "100710" , Arrays.asList(params) , ResultFormat.JSONObjectsList);
        JSONObject json = new JSONObject(resultJSON);
        JSONArray data_row = (JSONArray)((JSONArray) json.get("data_rows")).get(0);
        Assert.assertTrue( "Expected to contain : data_rows ",((String)data_row.get(0)).equals("ERROR: column \"userid\" does not exist") );
    }


}
