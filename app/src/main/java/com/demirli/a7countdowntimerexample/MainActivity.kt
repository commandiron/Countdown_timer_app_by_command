package com.demirli.a7countdowntimerexample

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.min
import kotlin.time.DurationUnit

class MainActivity : AppCompatActivity() {

    private lateinit var runnableForCurrentDateTime: Runnable
    private lateinit var runnableForRemainingDateTime: Runnable
    private lateinit var handler: Handler

    private var currentDate: String? = null
    private var currentTime: String? = null

    private var yourSelectedDate: String? = null
    private var yourSelectedTime: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        handler = Handler()
        runnableForCurrentDateTime = Runnable {  }
        runnableForRemainingDateTime = Runnable {  }

        currentDateTimeRunnable()

    }

    fun currentDateTimeRunnable(){

        runnableForCurrentDateTime = object: Runnable{
            override fun run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    currentDate = DateTimeFormatter.ofPattern("dd.M.yyyy").withZone(ZoneOffset.UTC).format(Instant.now())
                    currentTime = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneOffset.of("+03:00")).format(Instant.now())
                    current_time_tv.text = "Current Time: " + currentDate + " " + currentTime
                }

                handler.postDelayed(runnableForCurrentDateTime, 1000)
            }
        }
        handler.postDelayed(runnableForCurrentDateTime, 0)
    }

    fun getDatePickerDialog(view: View){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                yourSelectedDate = "$dayOfMonth.${month+1}.$year"
                getTimePickerDialog()

            },year,month,day)
        } else {
            TODO("VERSION.SDK_INT < N")
        }

        dialog.datePicker.minDate = System.currentTimeMillis()
        dialog.show()
    }

    fun getTimePickerDialog(){
        val t = Calendar.getInstance()
        val hour = t.get(Calendar.HOUR_OF_DAY)
        val minute = t.get(Calendar.MINUTE)

        val dialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            yourSelectedTime = "$hourOfDay:$minute"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                your_date_et.text = "Your date: " + yourSelectedDate + " " + yourSelectedTime
                remainingDateTimeRunnable()
            }
        },hour,minute,true)

        dialog.show()
    }

    fun remainingDateTimeRunnable(){

        runnableForRemainingDateTime = object: Runnable{
            override fun run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val fromDateTime = LocalDate.parse(currentDate,DateTimeFormatter.ofPattern("dd.M.yyyy")).atTime(LocalTime.now())
                    val toDateTime = LocalDate.parse(yourSelectedDate,DateTimeFormatter.ofPattern("d.M.yyyy")).atTime(LocalTime.parse(yourSelectedTime,DateTimeFormatter.ofPattern("H:m")))

                    var tempDateTime = LocalDateTime.from(fromDateTime)

                    val years = tempDateTime.until(toDateTime, ChronoUnit.YEARS)
                    tempDateTime = tempDateTime.plusYears(years)

                    val months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS)
                    tempDateTime = tempDateTime.plusMonths(months)

                    val days = tempDateTime.until(toDateTime, ChronoUnit.DAYS)
                    tempDateTime = tempDateTime.plusDays(days)

                    val hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS)
                    tempDateTime = tempDateTime.plusHours(hours)

                    val minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES)
                    tempDateTime = tempDateTime.plusMinutes(minutes)

                    val seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS)
                    tempDateTime = tempDateTime.plusSeconds(seconds)

                    remaining_time_tv.text = "Remaining Time: " + years + " year " + months + " months " + days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds"

                }
                handler.postDelayed(runnableForRemainingDateTime, 1000)
            }
        }
        handler.postDelayed(runnableForRemainingDateTime, 0)
    }
}
