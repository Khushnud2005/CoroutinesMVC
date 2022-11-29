package uz.vianet.coroutinesmvc.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import uz.vianet.coroutinesmvc.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        val extras = intent.extras
        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            binding.tvTitle.setText(extras.getString("title")!!.uppercase())
            binding.tvBody.setText(extras.getString("body"))
        }
    }
}