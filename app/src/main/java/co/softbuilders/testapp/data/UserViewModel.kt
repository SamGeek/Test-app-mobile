package co.softbuilders.testapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.softbuilders.testapp.model.user

class UserViewModel(application: Application) : AndroidViewModel(application){

    val user : MutableLiveData<user> by lazy {
        MutableLiveData<user>()
    }

}