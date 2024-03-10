import org.apache.commons.lang3.RandomStringUtils;

public class Customer {
    public String email;
    public String password;
    public String name;
    public Customer () {
    }
    public Customer(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public static Customer getRandom() {
        final String email = RandomStringUtils.randomAlphabetic(6) + "@mail.ru";
        final String password = RandomStringUtils.randomAlphabetic(6);
        final String name = RandomStringUtils.randomAlphabetic(6);
        return new Customer(email, password, name);
    }
    public Customer setEmail(String email) {
        this.email = email;
        return this;
    }
    public Customer setPassword(String password) {
        this.password = password;
        return this;
    }
    public Customer setName(String name) {
        this.name = name;
        return this;
    }
    public static Customer getCustomerWithoutName() {
        return new Customer().setEmail(RandomStringUtils.randomAlphabetic(6) + "@mail.ru").setPassword(RandomStringUtils.randomAlphabetic(6));
    }
    public static Customer getCustomerWithoutPassword() {
        return new Customer().setEmail(RandomStringUtils.randomAlphabetic(6) + "@mail.ru").setName(RandomStringUtils.randomAlphabetic(6));
    }
    public static Customer getCustomerWithoutEmail() {
        return new Customer().setPassword(RandomStringUtils.randomAlphabetic(6)).setName(RandomStringUtils.randomAlphabetic(6));
    }
}