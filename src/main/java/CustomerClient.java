import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class CustomerClient extends CustomerSpecification {
    private static final String CUSTOMER_PATH = "api/auth/";
    @Step ("Create Customer")
    public ValidatableResponse create(Customer customer) {
        return given()
                .spec(getBaseSpec())
                .body(customer)
                .when()
                .post(CUSTOMER_PATH + "register")
                .then();
    }
    @Step ("Customer system login")
    public ValidatableResponse login(CustomerDetails Details) {
        return given()
                .spec(getBaseSpec())
                .body(Details)
                .when()
                .post(CUSTOMER_PATH + "login")
                .then();
    }
    @Step ("Change Customer data")
    public ValidatableResponse changeData(Customer customer, String bearerToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(bearerToken)
                .body(customer)
                .when()
                .patch(CUSTOMER_PATH + "user")
                .then();
    }
    @Step ("Delete Customer")
    public static ValidatableResponse delete(String bearerToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(bearerToken)
                .when()
                .delete(CUSTOMER_PATH + "user")
                .then();
    }
}