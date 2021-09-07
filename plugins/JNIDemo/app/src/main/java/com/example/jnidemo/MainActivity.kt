package com.example.jnidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.TextView
import com.example.jnidemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val s = "This is a long string: sdlfdksjflksndhiofuu2893873(*（%￥#@）*&……￥撒肥料开发时傅雷家书那份会计师东方丽景三等奖";

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = getBytesFromJNI(byteArrayOf(1,2,3,4,5,6)).toUByteArray().toString()
        //val byteArray:ByteArray = getBytesFromJNI(byteArrayOf(1,2,3,4,5,6))
        binding.sampleText.setOnClickListener{
            var time = SystemClock.currentThreadTimeMillis()
            for (i in 0 until 10000) {
                getBytesFromJNI(s.toByteArray())
            }
            binding.sampleText.text = "花费了:${SystemClock.currentThreadTimeMillis()-time}"
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    private external fun getBytesFromJNI(byteArray: ByteArray): ByteArray

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}