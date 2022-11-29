package uz.vianet.coroutinesmvc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import uz.vianet.coroutinesmvc.databinding.ActivityCreateBinding

class CreateActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.btnSubmitCreate.setOnClickListener(View.OnClickListener {
            val title: String = binding.etTitleCreate.getText().toString()
            val post: String = binding.etTextCreate.getText().toString().trim { it <= ' ' }
            val id_user: String = binding.etUserIdCreate.getText().toString().trim { it <= ' ' }
            if (title.isNotEmpty() && post.isNotEmpty() && id_user.isNotEmpty()){
                val intent = Intent()
                intent.putExtra("title", title)
                intent.putExtra("post", post)
                intent.putExtra("id_user", id_user)
                setResult(RESULT_OK, intent)
                super@CreateActivity.onBackPressed()
            }

        })
    }
}