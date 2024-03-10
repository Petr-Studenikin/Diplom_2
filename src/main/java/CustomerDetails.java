import org.apache.commons.lang3.RandomStringUtils;

public class CustomerDetails {
    public String email;
    public String password;
    public CustomerDetails () {
    }
    public CustomerDetails(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public static CustomerDetails from(Customer customer) {
        return new CustomerDetails(customer.email, customer.password);
    }
    public CustomerDetails setEmail (String email){
        this.email = email;
        return this;
    }
    public CustomerDetails setPassword (String password) {
        this.password = password;
        return this;
    }
    public static CustomerDetails getDetailsWithEmailOnly (Customer customer) {
        return new CustomerDetails().setEmail(customer.email);
    }
    public static CustomerDetails getDetailsWithPasswordOnly (Customer customer) {
        return new CustomerDetails().setPassword(customer.password);
    }
    public static CustomerDetails getDetailsWithRandomEmail (Customer customer) {
        return new CustomerDetails(RandomStringUtils.randomAlphabetic(6) + "@mail.ru", customer.password);
    }
    public static CustomerDetails getDetailsWithRandomPassword (Customer customer) {
        return new CustomerDetails(customer.email, RandomStringUtils.randomAlphabetic(6));
    }
}