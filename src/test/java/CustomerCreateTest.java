import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.junit.After;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CustomerCreateTest {
    private Customer customer;
    private CustomerClient customerClient;
    private String accessToken;
    private String bearerToken;

    @Before
    public void setUp() {
        customer = Customer.getRandom();
        customerClient = new CustomerClient();
    }

    @Test
    @DisplayName("Можно создать пользователя")
    @Description("Успешный запрос возвращает success: true")
    public void checkUserCreated() {
        ValidatableResponse response = customerClient.create(customer);
        int statusCode = response.extract().statusCode();
        boolean isUserCreated = response.extract().path("success");
        accessToken = customerClient.login(CustomerDetails.from(customer)).extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        assertThat("User is not created", isUserCreated, is(true));
        assertThat("Status code is incorrect", statusCode, equalTo(200));
        assertThat("User access token is incorrect", accessToken, is(not("")));
    }

    @Test
    @DisplayName("Нельзя создать одинаковых пользователей")
    @Description("При создании пользователя, который уже есть - возвращается ошибка")
    public void duplicateUserCreated() {
        customerClient.create(customer);
        ValidatableResponse response = customerClient.create(customer);
        accessToken = customerClient.login(CustomerDetails.from(customer)).extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");
        assertThat("Status code is incorrect", statusCode, equalTo(403));
        assertThat("Duplicate user has been created", errorMessage, equalTo("User already exists"));
    }

    @After
    public void tearDown() {
        CustomerClient.delete(bearerToken);
    }
}
