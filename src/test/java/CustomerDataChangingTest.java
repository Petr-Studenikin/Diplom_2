import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.junit.After;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CustomerDataChangingTest {
    private Customer customer;
    private Customer updatedCustomer;
    private CustomerClient customerClient;
    private String accessToken;
    private String bearerToken;

    @Before
    public void setUp() {
        customer = Customer.getRandom();
        updatedCustomer = Customer.getRandom();
        customerClient = new CustomerClient();
    }

    @Test
    @DisplayName("Авторизация с изменением данных пользователя")
    @Description("Успешный запрос возвращает success: true")
    public void checkChangingUserData() {
        ValidatableResponse response = customerClient.create(customer);
        accessToken = response.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        ValidatableResponse response2 = customerClient.changeData(updatedCustomer, bearerToken);
        boolean isUserDataChanged = response2.extract().path("success");
        int statusCode = response2.extract().statusCode();
        assertThat("User data is not changed", isUserDataChanged, is(true));
        assertThat("Status code is incorrect", statusCode, equalTo(200));
        assertThat("User access token is incorrect", accessToken, is(not("")));
    }

    @Test
    @DisplayName("Без авторизации с изменением данных пользователя")
    @Description("Успешный запрос возвращает success: false")
    public void checkChangingUserDataWithoutAuth() {
        customerClient.create(customer);
        bearerToken = "";
        ValidatableResponse response = customerClient.changeData(updatedCustomer, bearerToken);
        boolean isUserDataChanged = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertThat("User data is not changed", isUserDataChanged, is(false));
        assertThat("Status code is incorrect", statusCode, equalTo(401));
    }

    @After
    public void tearDown() {
        CustomerClient.delete(bearerToken);
    }
}
