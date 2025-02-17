package com.renter.imager_kotlin

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.BatteryManager
import android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.transition.Visibility
import com.renter.imager_kotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Calendar
import java.util.Date
import kotlin.coroutines.CoroutineContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MainActivity : AppCompatActivity() {

    private lateinit var next: ActivityMainBinding
    private lateinit var text: TextView
    private lateinit var addr: EditText
    private lateinit var timeout: EditText
    private lateinit var launch: Button
    private lateinit var image: ImageView


    @SuppressLint("NewApi", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        next = ActivityMainBinding.inflate(layoutInflater)
        setContentView(next.root)
     
        text = findViewById(R.id.textView2)
        addr = findViewById(R.id.editTextText)
        timeout = findViewById(R.id.editTextNumber)
        launch = findViewById(R.id.button)
        image = findViewById(R.id.imageView)

        image.background = getDrawable(R.drawable.white)
        text.setTextColor(Color.BLACK)
        timeout.setTextColor(Color.BLACK)
        addr.setTextColor(Color.BLACK)
        launch.setBackgroundColor(Color.BLACK)
        launch.setTextColor(Color.WHITE)

        next.fab.setOnClickListener { view ->
            CoroutineScope(Dispatchers.IO).launch {
                val batteryManager = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager

// Получаем уровень заряда батареи
                val batLevel:Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

                val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm")
                var currentDate = sdf.format(Date())
                val url = URL("http://" + addr.text + "/img")

                var bitmap = (getDrawable(R.drawable.white) as BitmapDrawable).getBitmap()
                var stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val white = stream.toByteArray()

                bitmap = (getDrawable(R.drawable.black) as BitmapDrawable).getBitmap()
                stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val black = stream.toByteArray()

                    val imageData = url.readBytes()
                    currentDate = sdf.format(Date())

                    withContext(Dispatchers.Main) {
                        next.fab.visibility = View.INVISIBLE

                        image.setImageBitmap(byteArrayToBitmap(black))
                        text.setTextColor(Color.BLACK)
                        delay(2000)

                        text.visibility = View.VISIBLE
                        text.text = "" + currentDate + " " + batLevel + "%"
                        text.setTextColor(Color.WHITE)
                        delay(3000)


                        image.setImageBitmap(byteArrayToBitmap(white))
                        text.setTextColor(Color.BLACK)
                        delay(3000)

                        text.setTextColor(Color.WHITE)
                        delay(2000)

                        next.fab.visibility = View.VISIBLE
                        text.visibility = View.INVISIBLE
                        image.setImageBitmap(byteArrayToBitmap(imageData))
                    }

            }
        }



        launch.setOnClickListener(View.OnClickListener {

            launch.visibility = View.GONE
            timeout.visibility = View.GONE
            addr.visibility = View.GONE

            CoroutineScope(Dispatchers.IO).launch {
                val batteryManager = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager

// Получаем уровень заряда батареи
                val batLevel:Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

                val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm")
                var currentDate = sdf.format(Date())
                val url = URL("http://" + addr.text + "/img")

                var bitmap = (getDrawable(R.drawable.white) as BitmapDrawable).getBitmap()
                var stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val white = stream.toByteArray()

                bitmap = (getDrawable(R.drawable.black) as BitmapDrawable).getBitmap()
                stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val black = stream.toByteArray()

                while(true) {
                    val imageData = url.readBytes()
                    currentDate = sdf.format(Date())

                    withContext(Dispatchers.Main) {
                        next.fab.visibility = View.INVISIBLE

                        image.setImageBitmap(byteArrayToBitmap(black))
                        text.setTextColor(Color.BLACK)
                        delay(2000)

                        text.visibility = View.VISIBLE
                        text.text = "" + currentDate + " " + batLevel + "%"
                        text.setTextColor(Color.WHITE)
                        delay(3000)


                        image.setImageBitmap(byteArrayToBitmap(white))
                        text.setTextColor(Color.BLACK)
                        delay(3000)

                        text.setTextColor(Color.WHITE)
                        delay(2000)

                        next.fab.visibility = View.VISIBLE
                        text.visibility = View.INVISIBLE
                        image.setImageBitmap(byteArrayToBitmap(imageData))
                    }
                    delay(Integer.parseInt(timeout.text.toString()).toDuration(DurationUnit.MINUTES))
                }
            }
        })
    }

    private fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

}