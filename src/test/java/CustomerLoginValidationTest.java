import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.junit.After;
import io.restassured.response.ValidatableResponse;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CustomerLoginValidationTest {
    private CustomerClient customerClient = new CustomerClient();
    private static Customer customer = Customer.getRandom();
    private final CustomerDetails customerDetails;
    private final int expectedStatus;
    private final String expectedMessageError;
    private String accessToken;
    private String bearerToken;

    public CustomerLoginValidationTest(CustomerDetails customerDetails, int expectedStatus, String expectedMessageError) {
        this.customerDetails = customerDetails;
        this.expectedStatus = expectedStatus;
        this.expectedMessageError = expectedMessageError;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {CustomerDetails.getDetailsWithPasswordOnly(customer), 401, "email or password are incorrect"},
                {CustomerDetails.getDetailsWithEmailOnly(customer), 401, "email or password are incorrect"},
                {CustomerDetails.getDetailsWithRandomEmail(customer), 401, "email or password are incorrect"},
                {CustomerDetails.getDetailsWithRandomPassword(customer), 401, "email or password are incorrect"},
        };
    }

    @Test
    @DisplayName("Надо передать обязательные поля")
    @Description("1. Email с ошибкой" +
            "2. Password с ошибкой" +
            "3. отсутствует Email" +
            "4. отсутствует Password")
    public void CustomerLoginValidation() {
        ValidatableResponse response = customerClient.create(customer);
        ValidatableResponse login = new CustomerClient().login(customerDetails);
        accessToken = response.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        int ActualStatusCode = login.extract().statusCode();
        String actualMessage = login.extract().path("message");
        assertEquals("Status code is incorrect", expectedStatus, ActualStatusCode);
        assertEquals("User access token is incorrect", expectedMessageError, actualMessage);
    }

    @After
    public void tearDown() {
        CustomerClient.delete(bearerToken);
    }
}