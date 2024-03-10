import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.junit.After;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CustomerLoginTest {
    private Customer customer;
    private CustomerClient customerClient;
    private String accessToken;
    private String bearerToken;

    @Before
    public void setUp() {
        customerClient = new CustomerClient();
        customer = Customer.getRandom();
    }

    @Test
    @DisplayName("вход существующим пользователем")
    @Description("Успешный запрос возвращает success: true")
    public void customerCanLogIn() {
        customerClient.create(customer);
        ValidatableResponse response = customerClient.login(CustomerDetails.from(customer));
        accessToken = response.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        int statusCodeSuccessfulLogin = response.extract().statusCode();
        assertThat("User access token is incorrect", bearerToken, is(not("")));
        assertThat(statusCodeSuccessfulLogin, equalTo(200));
    }

    @After
    public void tearDown() {
        CustomerClient.delete(bearerToken);
    }
}