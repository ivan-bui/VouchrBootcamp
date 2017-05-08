package com.example.ivanbui.twittersearch;


import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.Map;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by markhetherington on 2017-05-02.
 */
public class TwitterServiceBuilder {

    public static final String TOKEN = "";
    public static final String SECRET = "";

    public static class AuthToken {
        @SerializedName("token_type")
        private String tokenType;
        @SerializedName("access_token")
        private String accessToken;

        public String getBearer() {
            return "Bearer " + accessToken;
        }
    }


    public interface TwitterService {

        @GET("/1.1/search/tweets.json")
        Call<SearchResponse> page(@Query("q") String search,
                                  @Query("max_id") String maxId,
                                  @Query("since_id") String sinceId);

        @GET("/1.1/search/tweets.json?include_entities=true")
        Call<SearchResponse> search(@Query("q") String search);

        @POST("/oauth2/token?grant_type=client_credentials")
        Call<AuthToken> auth(@Header("Authorization") String credentials);
    }

    private static TwitterService twitterService;
    private static AuthToken savedAuthToken = new AuthToken();

    public static TwitterService getTwitterService() {
        if (twitterService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor())
                    .authenticator(new AuthAuthenticator())
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl("https://api.twitter.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            twitterService = retrofit.create(TwitterService.class);
        }
        return twitterService;
    }

    /**
     * Adds an Authorization Header to each request made
     */
    private static class AuthInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder().addHeader("Authorization", savedAuthToken.getBearer()).build();
            return chain.proceed(request);
        }
    }

    /**
     * If a network call returns an HTTP 401 this method is called,
     * it will then create request an auth token, save it and retry the request
     */
    private static class AuthAuthenticator implements Authenticator {

        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            savedAuthToken = getTwitterService().auth(Credentials.basic(TOKEN, SECRET)).execute().body();
            return response.request().newBuilder().header("Authorization", savedAuthToken.getBearer()).build();
        }
    }

}
