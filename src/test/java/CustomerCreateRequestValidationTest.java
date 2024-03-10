import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CustomerCreateRequestValidationTest {
    private final Customer customer;
    private final int expectedStatus;
    private final String expectedMessageError;

    public CustomerCreateRequestValidationTest(Customer customer, int expectedStatus, String expectedMessageError) {
        this.customer = customer;
        this.expectedStatus = expectedStatus;
        this.expectedMessageError = expectedMessageError;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {Customer.getCustomerWithoutName(), 403, "Email, password and name are required fields"},
                {Customer.getCustomerWithoutPassword(), 403, "Email, password and name are required fields"},
                {Customer.getCustomerWithoutEmail(), 403, "Email, password and name are required fields"}
        };
    }

    @Test
    @DisplayName("Создать пользователя не заполнив одно обязательное поле")
    @Description("Вернётся код ответа 403 Forbidden")
    public void requestIsNotAllowed() {
        ValidatableResponse response = new CustomerClient().create(customer);
        String actualMessage = response.extract().path("message");
        int code = response.extract().statusCode();
        assertEquals(expectedMessageError, actualMessage);
        assertEquals(expectedStatus, code);
    }
}