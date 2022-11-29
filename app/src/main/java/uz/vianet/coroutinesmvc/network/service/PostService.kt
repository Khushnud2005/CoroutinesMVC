package uz.vianet.coroutinesmvc.network.service

import retrofit2.Call
import retrofit2.http.*
import uz.vianet.coroutinesmvc.model.Post

interface PostService {

    @Headers(
        "Content-type:application/json"
    )

    @GET("posts")
    fun listPost(): Call<ArrayList<Post>>

    @GET("posts/{id}")
    fun detailPost(@Path("id") id: Int): Call<Post>

    @POST("posts")
    fun createPost(@Body post: Post): Call<Post>

    @PUT("posts/{id}")
    fun updatePost(@Path("id") id: Int, @Body post: Post): Call<Post>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<Post>
}