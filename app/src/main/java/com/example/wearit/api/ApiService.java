package com.example.wearit.api;

import com.example.wearit.api.response.AddressResponse;
import com.example.wearit.api.response.AllProductResponse;
import com.example.wearit.api.response.CategoryResponse;
import com.example.wearit.api.response.DashResponse;
import com.example.wearit.api.response.LoginResponse;
import com.example.wearit.api.response.OrderHistoryResponse;
import com.example.wearit.api.response.RegisterResponse;
import com.example.wearit.api.response.SingleProductResponse;
import com.example.wearit.api.response.SliderResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("ecommerce/api/v1/login")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("ecommerce/api/v1/register")
    Call<RegisterResponse> register(@Field("name") String name, @Field("email") String email, @Field("password") String password_hash,@Field("phone_no") Integer phone_no);

    @FormUrlEncoded
    @POST("ecommerce/api/v1/cart")
    Call<RegisterResponse> addToCart(@Header("api_key") String apikey, @Field("p_id") int p, @Field("quantity") int q);


    @GET("ecommerce/api/v1/slider")
    Call<SliderResponse> getSliders();

    @GET("ecommerce/api/v1/cart")
    Call<AllProductResponse> getMyCart(@Header("api_key") String apikey);

    @GET("ecommerce/api/v1/wishlist")
    Call<AllProductResponse> wishlist(@Header("api_key") String apikey);

    @DELETE("ecommerce/api/v1/cart")
    Call<RegisterResponse> deleteFromCart(@Header("api_key") String apikey, @Query("c_id") int cartID);

    @DELETE("ecommerce/api/v1/wishlist")
    Call<RegisterResponse> deleteFromWishlist(@Header("api_key") String apikey, @Query("c_id") int cartID);

    @GET("ecommerce/api/v1/get-categories")
    Call<CategoryResponse> getCategories();

    @GET("ecommerce/api/v1/get-all-products")
    Call<AllProductResponse> getAllProducts();


    @GET("ecommerce/api/v1/get-products-by-category")
    Call<AllProductResponse> getProductsByCategory(@Query("c_id") int catID);

    @FormUrlEncoded
    @POST("ecommerce/api/v1/order")
    Call<RegisterResponse> order(@Header("api_key") String apikey,
                                 @Field("p_type") int p_type,
                                 @Field("address_id") int address_id,
                                 @Field("payment_reference") String paymentRefrence);

    @Multipart
    @POST("/api/v1/upload-product")
    Call<RegisterResponse> uploadProduct(
            @Header("api_key") String apikey,
            @Part MultipartBody.Part[] files,
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("description") RequestBody description,
            @Part("quantity") RequestBody quantity,
            @Part("discount_price") RequestBody discount_price,
            @Part("categories") RequestBody categories

    );

    @GET("ecommerce/api/v1/order")
    Call<OrderHistoryResponse> orderHistory(@Header("api_key") String apikey
    );

    @GET("ecommerce/api/v1/allOrder")
    Call<OrderHistoryResponse> allOrder(@Header("api_key") String apikey
    );

    @GET("ecommerce/api/v1/get-all-products")
    Call<SingleProductResponse> getProductById(@Query("id") int c_id);

    @GET("ecommerce/api/v1/address")
    Call<AddressResponse> getMyAddresses(@Header("api_key") String apikey);

    @FormUrlEncoded
    @POST("ecommerce/api/v1/address")
    Call<AddressResponse> address(@Header("api_key") String apikey,
                                    @Field("city") String city,
                                    @Field("street") String street,
                                    @Field("description") String description,
                                    @Field("province") String province

    );


    @GET("/api/v1/dash")
    Call<DashResponse> getDash(@Header("api_key") String apikey);

    @Multipart
    @POST("/api/v1/upload-category")
    Call<RegisterResponse> uploadCategory(
            @Header("api_key") String apikey,
            @Part MultipartBody.Part file,
            @Part("name") RequestBody name

    );

    @DELETE("/api/v1/category")
    Call<RegisterResponse> deleteCategory(@Header("Apikey") String apikey, @Query("c_id") int id);

}



