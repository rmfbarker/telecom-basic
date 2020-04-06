package basic;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.print.DocFlavor;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class HandlerPhoneNumber implements RequestHandler<Map<String, String>, String> {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        LambdaLogger logger = context.getLogger();

        try {

            // log execution details
            logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
            logger.log("CONTEXT: " + gson.toJson(context));

            // process event
            logger.log("EVENT: " + gson.toJson(event));
            logger.log("EVENT TYPE: " + event.getClass().toString());

            Map database;
            try {

                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream is = classLoader.getResourceAsStream("number-database.json");

                database = new Gson().fromJson(new InputStreamReader(is), java.util.HashMap.class);
                logger.log("Customer database: " + database);

            } catch (Exception e) {
                logger.log("Error init DB" + e.getMessage());
                Map<String, String> result = Map.of("status", "500", "error", e.getMessage());
                return gson.toJson(result);
            }

            String operation = event.get("operation");
            switch (operation) {
                case "get-all": {
                    Map<String, Object> result = Map.of("status", "200", "query", event, "result", database);
                    return gson.toJson(result);
                }
                case "get": {
                    String customer = event.get("customer");
                    logger.log("Get numbers for customer" + customer);
                    Object numbers = database.get(customer);

                    if (numbers == null) {
                        Map<String, Object> result = Map.of("status", "400", "query", event, "error", "unknown customer number");
                        return gson.toJson(result);
                    }

                    Map<String, Object> result = Map.of("status", "200", "query", event, "result", database.get(customer));
                    return gson.toJson(result);

                }
                case "activate": {
                    String customer = event.get("customer");
                    String number = event.get("number");

                    logger.log("Activate number " + number + " for customer" + customer);

                    Map<String, Object> result = Map.of("status", "200", "query", event);
                    return gson.toJson(result);

                }
                default: {
                    Map<String, Object> result = Map.of("status", "400", "query", event, "error", "unknown operation");
                    return gson.toJson(result);
                }
            }

        } catch (RuntimeException e) {
            logger.log(e.toString());
            logger.log("ERROR while executing " + e.getMessage());
            Map<String, Object> result = Map.of("status", "500", "query", event, "error", e.getMessage());
            return gson.toJson(result);
        }

    }
}