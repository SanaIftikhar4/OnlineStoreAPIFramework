package payloads;
import pojo.*;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class Payload {

    //Creating Object For Fake Data to generate random Data
    private static final Faker faker = new Faker();

    //Creating Category so our dat can select from the following option
    private  static  final String[] categories = {"Appliances","Furniture","Clothing","Sports/Outdoor"};
    //create random class so our data can pick randomly from our options
    private static Random random = new Random();

    ////Product Module
    public static Product productPayload(){

        String title = faker.commerce().productName();
        BigDecimal price = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 1000));

        String description = faker.lorem().sentence();
        String image = "https://i.pravatar.cc/300";

        //Three ways to handle random category:

        //#1
           //String category = categories[random.nextInt(categories.length)];


        //#2 This is an alternative way to randomly pick one category from predefined list.
             //FAKER.options() gives you access to Faker’s Options utility.
             //.option(categories) picks one random value from the array you pass in.
              // String category = FAKER.options().option(categories);
        //3 Thread-safe — perfect for parallel test execution.

             String category = categories[ThreadLocalRandom.current().nextInt(categories.length)];


        return new Product(title,price,description,category,image);

    }

    ////User Module
    public static Users usersPayload() {

        //Name
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        Name name = new Name(firstName, lastName); // Object for Name

        //GeoLocation:
        String lat = faker.address().latitude();
        String lng = faker.address().longitude();

        Geolocation geolocation = new Geolocation(lat, lng);

        //Address

        String city = faker.address().city();
        String street = faker.address().streetName();
        int number = faker.number().numberBetween(1, 9999);
        String zipcode = faker.address().zipCode();

        Address address = new Address(city, street, number, zipcode, geolocation);

        String email = faker.internet().emailAddress();
        String username = faker.internet().username();
        String password = faker.internet().password();
        String phonenumber = faker.phoneNumber().cellPhone();
        return new Users(email, username, password, name, address, phonenumber);
    }
        ////Cart Module
    public static Cart cartPayload(){


        //Generating fakeId:
        int userId = faker.number().numberBetween(1,10);

        //Generating fake Date
        String date = LocalDate.now().minusDays(faker.number().numberBetween(0, 2000))
                .format(DateTimeFormatter.ISO_LOCAL_DATE);


        // Generate a list of ProductItem objects

        List<ProductItems> products = new ArrayList<>();

        int numProducts = faker.number().numberBetween(1, 4);
        for (int i = 0; i < numProducts; i++) {
            int productId = faker.number().numberBetween(1, 20);
            int quantity = faker.number().numberBetween(1, 5);
            //Adding products to the cart:
            products.add(new ProductItems(productId, quantity));
        }

        //Create and return a Cart object with all these fields
        return new Cart(userId,date,products);

    }


        ////Auth Module

        public static Auth authPayload(){

        String username = faker.internet().username();
        String password = faker.internet().password();


        return new Auth(username,password);

    }


}
