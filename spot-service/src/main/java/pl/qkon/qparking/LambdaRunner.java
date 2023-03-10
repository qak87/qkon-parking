package pl.qkon.qparking;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.qkon.qparking.router.ApiRouter;
import pl.qkon.qparking.router.Logger;
import pl.qkon.qparking.spot.Beans;
import pl.qkon.qparking.validation.ValidationException;
import pl.qkon.qparking.validation.Validator;

import java.util.Optional;


@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class LambdaRunner implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @NonNull
    private final ApiRouter apiRouter;

    public LambdaRunner() {
        this(new ApiRouter(Beans.getSpotService()));
    }

    //https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        Logger.setLambdaLogger(context.getLogger());
        Logger.info("EVENT TYPE: " + event.getClass().toString());
        try {
            return apiRouter.handle(event);
        } catch (ValidationException e) {
            return Validator.mapException(e);
        }
    }

    public static void main(String[] args) {
        String path = Optional.ofNullable(System.getenv("PATH2")).orElse("N/A");
        Logger.info(path);
    }
}