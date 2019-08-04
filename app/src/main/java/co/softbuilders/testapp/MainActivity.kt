package co.softbuilders.testapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
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
import org.threeten.bp.LocalDate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var service : TestAppService
    private lateinit var mDisposable: CompositeDisposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        birth = findViewById<View>(R.id.birthDate) as EditText
        initRetrofit()
        submit.setOnClickListener {
            if(!checkFields()) makeRequest()
        }

    }

    override fun onResume() {
        super.onResume()
        birthDate.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                showDatePickerDialog(view)
            }

            true
        })

        birthDate.setOnFocusChangeListener(View.OnFocusChangeListener { view, b -> if (b) showDatePickerDialog(view) })
    }

    fun showDatePickerDialog(v: View) {
        DatePickerFragment().show(fragmentManager, "datePicker")
    }

    fun checkFields(): Boolean {

        var verif = false

        if (name.getText().toString().trim { it <= ' ' }.equals("", ignoreCase = true)) {
            name.setError(getString(R.string.fill_field))
            verif = true
        }

        if (birthDate.getText().toString().trim { it <= ' ' }.equals("", ignoreCase = true)) {
            birthDate.setError(getString(R.string.fill_field))
            verif = true
        }
        return verif
    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            val dpd = DatePickerDialog(getActivity(), this, year, month, day)
            dpd.getDatePicker().setMaxDate(Date().getTime())

            return dpd
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            // Do something with the date chosen by the user

            //the date choosen could not be greatter or equals today
            val chosenDate = LocalDate.of(year, month+1, day)
            val today = LocalDate.now()

            if (chosenDate.isAfter(today)){
                Functions.toastLong( activity, "Your birthday can not be after today")
            }else {
                birth!!.setText(day.toString() + "/" + (month + 1) + "/" + year)
            }
        }
    }

    private fun makeRequest() {

        Functions.showProgress(this)
        mDisposable = CompositeDisposable()
        try {
            mDisposable.add(
                service.computeData(name.text.toString(),birthDate.text.toString()).subscribeOn(
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

    companion object{
        private var birth:EditText? = null
    }
}

interface TestAppService {
    @POST("process/age")
    @FormUrlEncoded
    fun computeData(@Field("name")  name: String,
                    @Field("birthDate") age: String): Observable<user>
}
