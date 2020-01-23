package ng.com.babbangona.kymapp.Data.Remote;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://apps.babbangona.com/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
