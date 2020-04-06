package basic;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvokePhoneNumberTest {
    private static final Logger logger = LoggerFactory.getLogger(InvokePhoneNumberTest.class);

    @Test
    void invokeGetAllTest() {
        logger.info("Invoke Get All customer numbers TEST");
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("operation", "get-all");

        Context context = new TestContext();
        HandlerPhoneNumber handler = new HandlerPhoneNumber();
        String result = handler.handleRequest(event, context);
        logger.info("Result was " + result);
        JsonObject jsonResult = new Gson().fromJson(result, JsonObject.class);
        assertEquals("200", jsonResult.get("status").getAsString());
    }

    @Test
    void invokeGetCustomerTest() {
        logger.info("Invoke Get specific customer numbers TEST");
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("operation", "get");
        event.put("customer", "3b2cb090-12b5-4bb8-aeaf-807b381c821");

        Context context = new TestContext();
        HandlerPhoneNumber handler = new HandlerPhoneNumber();
        String result = handler.handleRequest(event, context);
        logger.info("Result was " + result);
        JsonObject jsonResult = new Gson().fromJson(result, JsonObject.class);
        assertEquals("200", jsonResult.get("status").getAsString());
        assertEquals("0423123123", jsonResult.get("result").getAsJsonArray().get(0).getAsString());
    }

    @Test
    void invokeGetUnknownCustomerTest() {
        logger.info("Invoke Get unknown customer numbers TEST");
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("operation", "get");
        event.put("customer", "foo");

        Context context = new TestContext();
        HandlerPhoneNumber handler = new HandlerPhoneNumber();
        String result = handler.handleRequest(event, context);
        logger.info("Result was " + result);
        JsonObject jsonResult = new Gson().fromJson(result, JsonObject.class);
        assertEquals("400", jsonResult.get("status").getAsString());
    }

    @Test
    void invokeActivateNumberTest() {
        logger.info("Invoke Activate customer number TEST");
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("operation", "activate");
        event.put("customer", "3b2cb090-12b5-4bb8-aeaf-807b381c821");
        event.put("number", "0423031331");

        Context context = new TestContext();
        HandlerPhoneNumber handler = new HandlerPhoneNumber();
        String result = handler.handleRequest(event, context);
        logger.info("Result was " + result);
        JsonObject jsonResult = new Gson().fromJson(result, JsonObject.class);
        assertEquals("200", jsonResult.get("status").getAsString());

    }

}
