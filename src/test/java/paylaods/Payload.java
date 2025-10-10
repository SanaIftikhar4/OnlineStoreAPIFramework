package paylaods;
import com.github.javafaker.Faker;
import pojo.Product;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class Payload {

    //Creating Object For Fake Data to generate random Data
    private static final Faker faker = new Faker();

    //Creating Category so our dat can select from the following option
    private  static  final String[] categories = {"Appliances","Furniture","Clothing","Sports/Outdoor"};
    //create random class so our data can pick randomly from our options
    private static Random random = new Random();

    public static Product productPayload(){

        String title = faker.commerce().productName();
        double price = faker.number().randomDouble(2, 1, 1000);
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
}
