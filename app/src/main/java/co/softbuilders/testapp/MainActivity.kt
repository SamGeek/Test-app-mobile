package co.softbuilders.testapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import co.softbuilders.testapp.model.user
import co.softbuilders.testapp.utils.Functions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded


class MainActivity : AppCompatActivity() {
    private lateinit var service : TestAppService
    private lateinit var mDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRetrofit()
        submit.setOnClickListener {
            if(!checkFields()) makeRequest()
        }

    }
    fun checkFields(): Boolean {

        var verif = false

        if (name.getText().toString().trim { it <= ' ' }.equals("", ignoreCase = true)) {
            name.setError(getString(R.string.fill_field))
            verif = true
        }

        if (age.getText().toString().trim { it <= ' ' }.equals("", ignoreCase = true)) {
            age.setError(getString(R.string.fill_field))
            verif = true
        }
        return verif
    }

    private fun makeRequest() {

        Functions.showProgress(this)
        mDisposable = CompositeDisposable()
        try {
            mDisposable.add(
                service.computeData(name.text.toString(),age.text.toString()).subscribeOn(
                    Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe({ user ->
                    Functions.hideProgress()
                    Log.e("", Gson().toJson(user))
                    Functions.toastLong(this, "Computing successfull")
                    //traitement approprié
                    //pass it to the next activity
                    startActivity(Intent(this, ResultActivity::class.java).putExtra("computeResponse", Gson().toJson(user)))

                },
                { error ->
                    Functions.hideProgress()
                    Functions.toastLong(this, "Error on computing data. Please Retry")
                })
            )
        } catch (ex: Exception) {
            Functions.hideProgress()
            Functions.toastLong(this, "Error on computing data. Please Retry")
        }

    }

    private fun initRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://207.180.224.107:1337/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        service = retrofit.create<TestAppService>(TestAppService::class.java!!)
    }
}

interface TestAppService {
    @POST("user/computeData")
    @FormUrlEncoded
    fun computeData(@Field("name")  name: String,
                    @Field("age") age: String): Observable<user>
}
