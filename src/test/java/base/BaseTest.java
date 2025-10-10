package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import routes.Routes;
import utilities.ConfigReader;

public class BaseTest {
    ConfigReader configReader;
    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = Routes.base_URL;

        configReader=new ConfigReader();


    }

}
