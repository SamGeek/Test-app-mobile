package co.softbuilders.testapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.softbuilders.testapp.data.UserViewModel
import co.softbuilders.testapp.databinding.ActivityResultBinding
import co.softbuilders.testapp.model.user
import co.softbuilders.testapp.utils.Functions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    private lateinit var userIntent: user
    lateinit var model: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityResultBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_result)

        model = ViewModelProviders.of(this).get(UserViewModel::class.java)
        // Bind layout with ViewModel
        binding.uservm = model
        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

        initUI()
    }

    private fun initUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(intent.extras!=null){
            userIntent = Gson().fromJson(intent.extras.getString("computeResponse"),user::class.java)
            userIntent = computeColorWithAge(userIntent)
            model.user.value = userIntent
        }

        this.model.user.observe(this, Observer { user: user ->

            if(user.age <=0){
                message.text = "You are less than a year old"
            }
        });



        if(::userIntent.isInitialized){
            container.setBackgroundColor(Color.parseColor(userIntent.color))
        }
    }

    private fun computeColorWithAge(myUser: user) : user {

        if(myUser.age>=1 && myUser.age<=20){
            myUser.color =  "#03A9F4"
            myUser.colorName = "LightBlue"
        }else if(myUser.age<=50 && myUser.age>20){
            myUser.color = "#ef5350"
            myUser.colorName = "LightRed"
        }else{
            myUser.color = "#9E9E9E"
            myUser.colorName = "Gray"
        }

        return myUser
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return true
    }
}
