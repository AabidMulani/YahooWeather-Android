package in.abmulani.yahooweather.interfaces;

import com.bankoncube.android.app.requestModel.bankAccounts.OpenAccountRequest;
import com.bankoncube.android.app.requestModel.money.DepositMoneyRequest;
import com.bankoncube.android.app.responseModel.UMOtpResponseModel;
import com.bankoncube.android.app.responseModel.UMTokenResponseModel;
import com.bankoncube.android.app.responseModel.UMUserDataResponseModel;
import com.bankoncube.android.app.responseModel.bankAccounts.AccountCreationResponse;
import com.bankoncube.android.app.responseModel.bankAccounts.UserBankAccounts;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by aabid on 3/29/16.
 */
public interface YahooWeatherRetrofitInterface {

    String IMPLICIT_AUTH = "Basic Y3ViZWFwcDpjdWJlYXBwJDEyMw==";

    @FormUrlEncoded
    @POST("/v1/user/otp")
    Call<UMOtpResponseModel> generateOTP(@Header("access-token") String accessToken,
                                         @Header("Authorization") String authorization,
                                         @FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("/v1/api/oauth/token")
    @Headers({
            "Authorization:" + IMPLICIT_AUTH
    })
    Call<UMTokenResponseModel> tokenGenerationCall(@FieldMap HashMap<String, String> map);


    @GET("/v1/user")
    Call<UMUserDataResponseModel> getUserDataByMobile(@Query("mobile") String mobile);


    @POST("/v1/user/accounts/deposit")
    Call<AccountCreationResponse> depositMoney(@Body DepositMoneyRequest map);

    @GET("/v1/user/accounts")
    Call<List<UserBankAccounts>> getAccounts(@Query("uuid") String uuid);

    @POST("/v1/account/bank")
    Call<AccountCreationResponse> openAccount(@Body OpenAccountRequest map);


}
