import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import org.junit.After;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

public class OrderCreateTest {
    private Customer customer;
    private CustomerClient customerClient;
    private Ingredients ingredients;
    public OrderClient orderClient;
    private String accessToken;
    private String bearerToken;

    @Before
    public void setUp() {
        customer = Customer.getRandom();
        customerClient = new CustomerClient();
        ingredients = Ingredients.getRandomBurger();
        orderClient = new OrderClient();
    }

    @Test
    @Description("Заказ. Зарегистрированный пользователь")
    public void orderCreatedAuthUser() {
        ValidatableResponse customerResponse = customerClient.create(customer);
        accessToken = customerResponse.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        ValidatableResponse orderResponse = orderClient.create(bearerToken, ingredients);
        int statusCode = orderResponse.extract().statusCode();
        boolean orderCreated = orderResponse.extract().path("success");
        int orderNumber = orderResponse.extract().path("order.number");
        assertThat("Status code is not correct", statusCode, equalTo(200));
        assertThat("The order has not been created", orderCreated, is(true));
        assertThat("The order number is missing", orderNumber, is(not(0)));
    }

    @Test
    @Description("Заказ. Не зарегистрированный пользователь")
    public void orderCreatedNonAuthUser() {
        bearerToken = "";
        ValidatableResponse orderResponse = orderClient.create(bearerToken, ingredients);
        int statusCode = orderResponse.extract().statusCode();
        boolean orderCreated = orderResponse.extract().path("success");
        int orderNumber = orderResponse.extract().path("order.number");
        assertThat("Status code is not correct", statusCode, equalTo(200));
        assertThat("The order has not been created", orderCreated, is(true));
        assertThat("The order number is missing", orderNumber, is(not(0)));
    }

    @Test
    @Description("Заказ без ингредиентов")
    public void orderCreatedWithOutIngredients() {
        ValidatableResponse customerResponse = customerClient.create(customer);
        accessToken = customerResponse.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        ValidatableResponse orderResponse = orderClient.create(bearerToken, Ingredients.getNullIngredients());
        int statusCode = orderResponse.extract().statusCode();
        boolean orderNotCreated = orderResponse.extract().path("success");
        String errorMessage = orderResponse.extract().path("message");
        assertThat("Status code is not correct", statusCode, equalTo(400));
        assertThat("The order has not been created", orderNotCreated, is(false));
        assertEquals("The error message is not correct", "Ingredient ids must be provided", errorMessage);
    }
    @Test
    @Description("Заказ с несуществующими ингредиентами")
    public void orderCreatedWithIncorrectIngredients() {
        ValidatableResponse customerResponse = customerClient.create(customer);
        accessToken = customerResponse.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        ValidatableResponse orderResponse = orderClient.create(bearerToken, Ingredients.getIncorrectIngredients());
        int statusCode = orderResponse.extract().statusCode();
        assertThat("Status code is not correct", statusCode, equalTo(500));
    }

    @After
    public void tearDown() {
        CustomerClient.delete(bearerToken);
    }
}