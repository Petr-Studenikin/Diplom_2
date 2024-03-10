import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
public class OrderClient extends CustomerSpecification {
    private static final String ORDER_PATH = "api/orders";
    @Step ("Make order")
    public ValidatableResponse create (String token,Ingredients ingredients){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }
    @Step ("Order list")
    public ValidatableResponse orderInfo (){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH + "/all")
                .then();
    }
    @Step ("Customer order list")
    public ValidatableResponse customerOrderInfo (String token){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .when()
                .get(ORDER_PATH)
                .then();
    }
}