package ng.com.babbangona.kymapp.Data.Remote;



import java.util.HashMap;
import java.util.List;

import ng.com.babbangona.kymapp.Data.POJO.Capture;
import ng.com.babbangona.kymapp.Data.POJO.MemberApproved;
import ng.com.babbangona.kymapp.Data.POJO.Members;
import ng.com.babbangona.kymapp.Data.POJO.MembersWhoNeedToFillCardDetails;
import ng.com.babbangona.kymapp.Data.POJO.Verification;
import ng.com.babbangona.kymapp.Model.ServerResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {
//    @GET("/users.json")
//    void contacts(Callback<List<User>> cb);
//
//    @GET("/users.json")
//    void contacts(Callback<Contacts> cb);

    @GET("/kym/members_download.php")
    Call <List<Members>> sendSupervisorId(@Query("mik") String mik, @Query("last_synced_date") String last_synced_date);

    @GET("/kym/new_members_download.php")
    Call <List<MemberApproved>> getApprovedUsers (@Query("mik") String mik);

    @GET("/kym/members_bgcard_download.php")
    Call <List<MembersWhoNeedToFillCardDetails>> getUsersWhoNeedToFillCardDetails (@Query("mik") String mik);

    @FormUrlEncoded
    @POST("/kym/upload_member_template.php")
    Call<List<Capture>> sendCapturedMember(@Field("member_template") String kym_data);

    @FormUrlEncoded
    @POST("/kym/upload_verification.php")
    Call<List<Verification>> sendVerification(@Field("member_verification") String verification_data);

    @Multipart
    @POST("/kym/new_upload.php")
    Call <ServerResponse> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);

}
