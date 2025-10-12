package pojo;
//fields:
//{
//    id:20,--> generate automatically
//    email:String,
//    username:String,
//    password:String,
//    name:{----------------------> will create separate POJO class as its a JSON array
//        firstname:String,
//        lastname:String
//        },
//    address:{
//    city:String,
//    street:String,
//    number:Number,
//    zipcode:String,
//    geolocation:{
//        lat:String,
//        long:String
//        }
//    },
//    phone:String
//}
public class Users {

    private String username;
    private String email;
    private String password;
    private Name name;
    private Address address;
    private  String phonenumber;



    //Constructor
    public Users(String username, String email, String password , Name name, Address address,String phonenumber) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.address = address;
        this.name = name;
        this.password = password;
        this.email = email;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }




}