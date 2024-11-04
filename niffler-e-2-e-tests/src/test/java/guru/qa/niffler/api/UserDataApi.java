package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface UserDataApi {

    @GET("/internal/users/current")
    Call<UserJson> getCurrentUser(@Query("username") String username);

    @GET("/internal/users/all")
    Call<List<UserJson>> getAllUsers(@Query("username") String username, @Query("searchQuery") String searchQuery);

    @POST("/internal/users/update")
    Call<UserJson> updateUser(@Body UserJson user);

    @GET("/internal/friends/all")
    Call<List<UserJson>> friends(@Query("username") String username, @Query("searchQuery") String searchQuery);

    @POST("internal/invitations/send")
    Call<UserJson> sendInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/accept")
    Call<UserJson> acceptInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/decline")
    Call<UserJson> declineInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/remove")
    Call<UserJson> removeFriend(@Query("username") String username, @Query("targetUsername") String targetUsername);
}