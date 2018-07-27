package com.example.hlarbi.firebaseappbeta1.AccountActivity.Oauth_Api.api;

import com.example.hlarbi.firebaseappbeta1.AccountActivity.Oauth_Api.api.objects.Oauth.AccessToken;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APIClient {



    @Headers({
            "Authorization: Basic MjJDWFhEOjU0YzUwZjk1YzliMmRhYjU0NzFmMzE1OThlMzI4Yzkw",
                    "Content-Type: application/x-www-form-urlencoded"
    })
    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<AccessToken> getNewAccessToken(

            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("redirect_uri") String redirectUri,
            @Field("code") String code,
            @Field("code_verifier") String base);

    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<AccessToken> getRefreshAccessToken(
            @Field("refresh_token") String refreshToken,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri,
            @Field("grant_type") String grantType);




}



