package com.raisetech.ecalculo.zorbistore.data.api


import com.raisetech.ecalculo.zorbistore.ui.auth.login.body.ResetPasswordBody
import com.raisetech.ecalculo.zorbistore.ui.auth.login.body.UpdateCustomerBody
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.body.LoginBody
import com.raisetech.ecalculo.zorbistore.ui.auth.login.orders.orderDetails.response.OrderDetailsResponse
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.CustomerOrderResponse
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.CustomerResponse
import com.raisetech.ecalculo.zorbistore.ui.auth.login.response.UpdateCustomerResponse
import com.raisetech.ecalculo.zorbistore.ui.auth.register.body.RegisterBody
import com.raisetech.ecalculo.zorbistore.ui.auth.register.response.RegisterResponse
import com.raisetech.ecalculo.zorbistore.ui.main.cart.checkout.body.OrderBody
import com.raisetech.ecalculo.zorbistore.ui.main.cart.checkout.response.OrderResponse
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.CategoriesResponse
import com.raisetech.ecalculo.zorbistore.ui.main.categories.response.SubCategoryResponse
import com.raisetech.ecalculo.zorbistore.ui.main.shop.response.NewProductResponseItem
import retrofit2.Response
import retrofit2.http.*

interface zorbiApiServices {

    var categoryId: Int

    @GET("/ecommerce/categoryList/5")
    //   @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun getCategories(): Response<CategoriesResponse>

    @GET("/ecommerce/itemList/5")
    //   @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun getProduct(): Response<List<com.raisetech.ecalculo.zorbistore.ui.main.shop.response.Data>>

    @POST("/ecommerce/registerCustomer/5")
    //   @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun register(
            @Body params: RegisterBody
    ): Response<RegisterResponse>

    @POST("/ecommerce/saveOrder/5")
    @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun order(
            @Body params: OrderBody
    ): Response<OrderResponse>

    @GET("/ecommerce/itemListByCategoryList/30")
//    @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun getFeaturedProduct(): Response<NewProductResponseItem>

    @GET("/ecommerce/latestProductItemList/5")
    //  @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun getLatestProducts(): Response<NewProductResponseItem>

    @GET("/ecommerce/itemListByCategoryList/29")
    //   @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun getOnSaleProducts(): Response<NewProductResponseItem>

    @GET("/wp-json/wc/v3/products?category=88")
    @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun getBanner(): Response<List<NewProductResponseItem>>

    @GET("/ecommerce/itemListByCategoryList/{id}")
    // @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun getProductCategoryWise(
            @Path(value = "id") id: Int?,
            @Query("per_page") per_page: Int?
    ): Response<NewProductResponseItem>

    @GET("wp-json/wc/v3/customers")
    @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun getCustomerDetails(
            @Query("email") email: String?
    ): Response<List<CustomerResponse>>

    @GET("ecommerce/orderListByCustomerId/{id}")
//    @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun getCustomersOrder(
            @Path(value = "id") id: Int?
    ): Response<CustomerOrderResponse>


    @GET("/ecommerce/itemList/5")
    @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun getProductPage(
            @Query("page") page: Int?,
            @Query("per_page") per_page: Int?
    ): Response<NewProductResponseItem>


    @POST("ecommerce/customerLogin/5")
//    @Headers("Authorization:Basic Y2tfZGIwNjI0MjE2YmE2Y2I0MDJmMjM5NWU2MDgyMWI1ZTk3MjQ3MDUxNjpjc18yZDJlZjU0OTUwNDJjNGQ3ZDc2Y2Q4MDFjNzk4YWM5OGI4MDZlMzNj")
    suspend fun getCustomer(@Body params: LoginBody): Response<CustomerResponse>


    @GET("ecommerce/orderDetailListByOrderId/{id}")
    suspend fun getOrderDetails(
            @Path(value = "id") id: Int?
    ): Response<OrderDetailsResponse>

    @GET("ecommerce/itemSubCategoryList/5")
    suspend fun getSubCategory(): Response<SubCategoryResponse>


    @PUT("ecommerce/updateCustomer/{id}")
    suspend fun updateCustomer(
            @Path(value = "id") id: Int?,
            @Body params: UpdateCustomerBody

    ): Response<UpdateCustomerResponse>

    @POST("ecommerce/resetPassword/{id}/{cPassword}/{nPassword}")
    suspend fun resetPassword(
            @Path(value = "id") id: Int?,
            @Path(value = "cPassword") cPassword: String?,
            @Path(value = "nPassword") nPassword: String?,
            @Body params: ResetPasswordBody
    ): Response<UpdateCustomerResponse>


    @GET("/ecommerce/itemListBySubCategoryList/{id}")
    suspend fun getSubCategory(
            @Path(value = "id") id: Int?
    ): Response<NewProductResponseItem>

}
