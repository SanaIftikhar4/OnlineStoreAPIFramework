package routes;

import utils.ConfigReader;

public class Routes {

    //public static final String base_URL = "https://fakestoreapi.com/";
    public static final String base_URL = ConfigReader.getProperty("baseURI");

    //------------------------Products-----------------------

    //Get all products-->Retrieve a list of all available products.
    public static final String get_allProducts = "/products";
    //Add a new product-->Create a new product.
    public static final String post_newProduct = "/products";
    //Get a single product-->Retrieve details of a specific product by ID
    public static final String get_productById = "/products/{id}";
    //Update a product--> Update an existing product by ID.
    public static final String put_updateProductById = "/products/{id}";
    //Delete a product -- > Delete a specific product by ID.
    public static final String delete_productById ="/products/{id}";

    //------------------------Carts-----------------------

    //Get all carts--> Retrieve a list of all available carts.
    public static final String get_allCarts = "/carts";
    //Add a new cart --> Create a new cart.
    public static final String post_newCart = "/carts";
   //Get a single cart --> Retrieve details of a specific cart by ID.
    public static final String get_cartById = "/carts/{id}";
    //Update a cart --> Update an existing cart by ID.
    public static final String put_updateCartById = "/carts/{id}";
    //Delete a cart -- > Delete a specific cart by ID.
    public static final String delete_cartById ="/carts/{id}";

    //------------------------Users-----------------------

    // Get all users --> Retrieve a list of all users.
    public static final String get_allUsers = "/users";

    // Get all users (limited) --> Retrieve a limited number of users.
    public static final String get_allUsers_Limit = "/users?limit={limit}";

    // Get all users (sorted) --> Retrieve users in ascending or descending order.
    public static final String get_allUsers_Sorted = "/users?sort={order}"; // order = asc | desc

    // Get a single user --> Retrieve details of a specific user by ID.
    public static final String get_userById = "/users/{id}";

        // Add a new user --> Create a new user.
    public static final String post_newUser = "/users";

    // Update a user --> Update an existing user by ID (PUT replaces all fields).
    public static final String put_updateUserById = "/users/{id}";

    // Partially update a user --> Update specific user fields (PATCH).
    public static final String patch_updateUserById = "/users/{id}";

    // Delete a user --> Delete a specific user by ID.
    public static final String delete_userById = "/users/{id}";

    //----------------AUTH Login--------------------

    //Login --> Authenticate a user.
    public static final String post_auth = "/auth/login";

















}
