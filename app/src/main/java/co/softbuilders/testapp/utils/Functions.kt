package co.softbuilders.testapp.utils

import android.app.ProgressDialog
import android.content.Context
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.widget.EditText
import android.widget.Toast
import java.text.NumberFormat
import java.util.*


/**
 * Created by softbuilders on 18/01/17.
 */

object Functions {

    internal var i = 0
    private val timer: CountDownTimer? = null
    private val CHARS = "1234567890"
    var load: ProgressDialog? = null

    fun spacer(text: StringBuilder): StringBuilder {
        return StringBuilder("")
    }

    fun toastLong(mContext: Context, message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
    }

    fun toastShort(mContext: Context, message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    fun isEmpty(edt: EditText): Boolean {
        return if (edt.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)) {
            //            edt.setError("Veuillez remplir ce champ");
            false
        } else
            true
    }
    fun convertTimeStampToDate(timestamp: Long, format : String): String {
        val cal = Calendar.getInstance(Locale.FRENCH)
        cal.timeInMillis = timestamp
        return DateFormat.format(format, cal).toString()
    }


    fun showProgress(mContext: Context) {
        load = ProgressDialog(mContext)
        if (null == load) {
            load = ProgressDialog(mContext)
        }
        load!!.setCancelable(false)
        load!!.show()
    }

    fun hideProgress() {
        if (null != load) load!!.dismiss()
        load = null
    }


    fun formatAmount(nbr: Double): String {
        return NumberFormat.getInstance(Locale.ENGLISH).format(nbr).replace(",".toRegex(), ".")
    }
    fun atEndOfDay(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }

    fun atStartOfDay(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }



}
