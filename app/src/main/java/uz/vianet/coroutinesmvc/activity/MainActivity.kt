package uz.vianet.coroutinesmvc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.vianet.coroutinesmvc.adapter.PostAdapter
import uz.vianet.coroutinesmvc.databinding.ActivityMainBinding
import uz.vianet.coroutinesmvc.model.Post
import uz.vianet.coroutinesmvc.network.RetrofitHttp
import uz.vianet.coroutinesmvc.utils.Utils
import uz.vianet.coroutinesmvc.utils.Utils.toast
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(),CoroutineScope {
    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    var posts = ArrayList<Post>()
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        job = Job()
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        binding.floating.setOnClickListener { openCreateActivity() }
        getPostList()

        val extras = intent.extras
        if (extras != null) {
            editPost(extras)
        }
    }
    val handler = CoroutineExceptionHandler { _, exception ->
        toast(this,exception.message.toString())
    }
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
    private fun refreshAdapter(posts: ArrayList<Post>) {
        val adapter = PostAdapter(this, posts)
        binding.recyclerView.setAdapter(adapter)
        binding.pbLoading.visibility = View.GONE
    }
    fun openCreateActivity() {
        val intent = Intent(this@MainActivity, CreateActivity::class.java)
        launchCreateActivity.launch(intent)
    }

    var launchCreateActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.pbLoading.visibility = View.VISIBLE
            val data = result.data
            if (data != null) {
                val new_title = data.getStringExtra("title")
                val new_post = data.getStringExtra("post")
                val new_userId = data.getStringExtra("id_user")
                val post = Post(new_userId!!.toInt(), new_title!!, new_post!!)
                createPost(post)
            }
        } else {
            Toast.makeText(this@MainActivity, "Operation canceled", Toast.LENGTH_LONG).show()
        }
    }
    private fun editPost(extras:Bundle) {
        Log.d("###", "extras not NULL - ")
        val edit_title = extras.getString("title")
        val edit_post = extras.getString("post")
        val edid_userId = extras.getString("id_user")
        val id = extras.getString("id")!!
        val post = Post(id.toInt(),edid_userId!!.toInt(), edit_title!!, edit_post!!)

        updatePost(post)
    }

    fun deletePostDialog(post: Post) {
        val title = "Delete"
        val body = "Do you want to delete?"
        Utils.customDialog(this, title, body, object : Utils.DialogListener {
            override fun onPositiveClick() {
                deletePost(post)
            }

            override fun onNegativeClick() {

            }
        })
    }
    private suspend fun apiPostLists():ArrayList<Post>{

        return async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.listPost().execute()
            return@async response.body()!!
        }.await()
    }
    private fun getPostList() {
        binding.pbLoading.visibility = View.VISIBLE
        launch(Dispatchers.Main + handler) {
            posts.clear()
            val items = apiPostLists()
            for (item in items){
                val post = Post(item.id,item.userId,item.title,item.body)
                posts.add(post)
            }
            refreshAdapter(posts)
        }
    }
    private suspend fun apiPostCreate(post: Post):Post{
        return async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.createPost(post).execute()
            return@async response.body()!!
        }.await()
    }

    private fun createPost(post: Post) {
        binding.pbLoading.visibility = View.VISIBLE
        launch(Dispatchers.Main + handler) {
            apiPostCreate(post)
            Toast.makeText(this@MainActivity,post.title + " Created",Toast.LENGTH_LONG).show()
            getPostList()
        }
    }

    private suspend fun apiPostUpdate(post: Post):Post {
        binding.pbLoading.visibility = View.VISIBLE
        return async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.updatePost(post.id,post).execute()
            return@async response.body()!!
        }.await()
    }
    private fun updatePost(post:Post){
        launch(Dispatchers.Main + handler) {
            apiPostUpdate(post)
            toast(this@MainActivity,post.title +" Updated")
            getPostList()
        }
    }

    private suspend fun apiPostDelete(post: Post):Post {
        return async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.deletePost(post.id).execute()
            return@async response.body()!!
        }.await()

    }
    private fun deletePost(post: Post){

        launch(Dispatchers.Main + handler) {
            apiPostDelete(post)
            toast(this@MainActivity,"${post.title} Deleted")
            getPostList()
        }
    }
}