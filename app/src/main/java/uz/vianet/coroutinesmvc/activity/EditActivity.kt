package uz.vianet.coroutinesmvc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import uz.vianet.coroutinesmvc.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    lateinit var id: String
    lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }
    fun initViews(){
        val extras = intent.extras

        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            binding.etUserId.setText(extras.getString("user_id"))
            binding.etTitle.setText(extras.getString("title"))
            binding.etText.setText(extras.getString("body"))
            id = extras.getString("id")!!
        }
        binding.btnSubmit.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val post = binding.etText.text.toString().trim { it <= ' ' }
            val id_user = binding.etUserId.text.toString().trim { it <= ' ' }
            val intent = Intent(this@EditActivity, MainActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("post", post)
            intent.putExtra("id_user", id_user)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}