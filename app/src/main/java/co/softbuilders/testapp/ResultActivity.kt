package co.softbuilders.testapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.softbuilders.testapp.model.user
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    private lateinit var userIntent: user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initUI()
    }

    private fun initUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(intent.extras!=null){
            userIntent = Gson().fromJson(intent.extras.getString("computeResponse"),user::class.java)
        }

        if(::userIntent.isInitialized){
            container.setBackgroundColor(Color.parseColor(userIntent.color))
            welcome.text = "Welcome ${userIntent.name}"
            message.text = "You are ${userIntent.age} years old sam \n The appropriate color for you is ${userIntent.colorName} set in background"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return true
    }
}
