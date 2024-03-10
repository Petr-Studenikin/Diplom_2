import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import org.junit.After;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class OrderInfoTest {
    private Customer customer;
    private CustomerClient customerClient;
    public OrderClient orderClient;
    private String accessToken;
    private String bearerToken;

    @Before
    public void setUp() {
        customer = Customer.getRandom();
        customerClient = new CustomerClient();
        orderClient = new OrderClient();
    }

    @Test
    @Description("Список заказов")
    public void orderInfoGet() {
        bearerToken = "";
        ValidatableResponse orderInfo = orderClient.orderInfo();
        int statusCode = orderInfo.extract().statusCode();
        boolean orderInfoGet = orderInfo.extract().path("success");
        List<Map<String, Object>> ordersList = orderInfo.extract().path("orders");
        assertThat("Status code is not correct", statusCode, equalTo(200));
        assertThat("Information about orders has not been received", orderInfoGet, is(true));
        assertThat("Orders list empty", ordersList, is(not(0)));
    }

    @Test
    @Description("Список заказов авторизованного пользователя")
    public void orderInfoGetAuth() {
        customerClient.create(customer);
        ValidatableResponse login = customerClient.login(CustomerDetails.from(customer));
        accessToken = login.extract().path("accessToken");
        bearerToken = accessToken.substring(7);
        ValidatableResponse orderInfo = orderClient.customerOrderInfo(bearerToken);
        int statusCode = orderInfo.extract().statusCode();
        boolean orderCreated = orderInfo.extract().path("success");
        List<Map<String, Object>> ordersList = orderInfo.extract().path("orders");
        assertThat("Status code is not correct", statusCode, equalTo(200));
        assertThat("Information about orders has not been received", orderCreated, is(true));
        assertThat("Orders list empty", ordersList, is(not(0)));
    }

    @Test
    @Description("Список заказов не авторизованного пользователя")
    public void orderInfoGetNonAuth() {
        bearerToken = "";
        ValidatableResponse orderInfo = orderClient.customerOrderInfo(bearerToken);
        int statusCode = orderInfo.extract().statusCode();
        boolean orderInfoNotGet = orderInfo.extract().path("success");
        String errorMessage = orderInfo.extract().path("message");
        assertThat("Status code is incorrect", statusCode, equalTo(401));
        assertThat("Information about orders has not been received", orderInfoNotGet, is(false));
        assertEquals("The error message is not correct", "You should be authorised", errorMessage);
    }

    @After
    public void tearDown() {
        CustomerClient.delete(bearerToken);
    }
}