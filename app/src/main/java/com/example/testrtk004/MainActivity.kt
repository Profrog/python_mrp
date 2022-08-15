package com.example.testrtk004

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.bluetooth.*
import android.widget.*
import android.content.pm.PackageManager.*
import android.os.Build.*
import androidx.core.app.ActivityCompat.*
import kotlinx.android.synthetic.main.activity_main.*

import android.Manifest
import android.R.attr
import android.view.View
import android.content.Intent
import android.util.Log
import android.content.Context
import android.system.Os.socket
import android.os.Handler
import android.os.Message

import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi as RequiresApi1

import java.lang.Integer.TYPE
import java.util.UUID
import java.io.IOException
import java.io.InputStream
import android.text.method.ScrollingMovementMethod
import lecho.lib.hellocharts.view.BubbleChartView
import java.io.File
import java.io.FileWriter
import android.R.attr.data

import lecho.lib.hellocharts.model.BubbleChartData
import android.R.attr.shape
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.model.BubbleValue

import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.Chart;


class MainActivity : AppCompatActivity() {

    private var result0 : String = ""
    private var mac_address0 : String = ""
    private val SEND_START = 0

    private lateinit var bluetoothManager : BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var t1 : TextView
    private lateinit var t2 : Spinner
    private lateinit var t3 : Button
    private lateinit var t4 : TextView
    private lateinit var t5 : Button
    private lateinit var t6 : BubbleChartView


    lateinit var pairedDevices: Set<BluetoothDevice>
    lateinit var connect_dev : BluetoothDevice
    lateinit var socket0 : BluetoothSocket
    lateinit var myHandler: MyHandler
    val values: MutableList<BubbleValue> = ArrayList()
    lateinit var data: BubbleChartData


    private fun updateData(lat0 : Float, lon0 : Float, num0 : Int) {
        val value = BubbleValue(
            lat0 , lon0 , 1f
        )
        value.color = num0
        value.setShape(ValueShape.CIRCLE)
        values.add(value)
        t6.setBubbleChartData(data)
    }

    private fun generateData() {

        val chart: BubbleChartView? = null

        val hasAxes = true
        val hasAxesNames = true
        val shape = ValueShape.CIRCLE
        val hasLabels = false
        val hasLabelForSelected = false

        val value = BubbleValue(
            100f, 100f , 100f
        )
        value.color = 0
        value.setShape(ValueShape.SQUARE)
        values.add(value)


        data = BubbleChartData(values)
        data.setHasLabels(hasLabels)
        data.setHasLabelsOnlyForSelected(hasLabelForSelected)
        if (hasAxes) {
            val axisX = Axis()
            val axisY: Axis = Axis().setHasLines(true)
            if (hasAxesNames) {
                axisX.setName("lat")
                axisY.setName("lon")
            }
            data.setAxisXBottom(axisX)
            data.setAxisYLeft(axisY)
        } else {
            data.setAxisXBottom(null)
            data.setAxisYLeft(null)
        }

        t6.setBubbleChartData(data)

    }

    private val REQUEST_PERMISSIONS= 2
    var MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


     inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when (msg.what) {
                SEND_START -> {
                    try {
                    var mes1 = msg.obj.toString()
                    t1.text = mes1.replace("\\s".toRegex(), "") + "\n"
                    t4.text = t4.text.toString() + t1.text.toString()

                    val arr = t1.text.toString().split(",")
                    var lat0 = arr[1].toFloat()
                    var lon0 = arr[2].toFloat()
                    var color0 = arr[3]

                    if (color0 == "A") {
                        updateData(lat0, lon0, ChartUtils.COLOR_RED)
                    }

                    else if(color0 == "B") {
                        updateData(lat0, lon0, ChartUtils.COLOR_BLUE)
                    }

                    else if(color0 == "C") {
                        updateData(lat0, lon0, ChartUtils.COLOR_GREEN)
                    }

                    else if(color0 == "D") {
                        updateData(lat0, lon0, ChartUtils.COLOR_ORANGE)
                    }

                    else{
                        updateData(lat0, lon0, ChartUtils.COLOR_VIOLET)
                    }

                    } catch (e: IOException) {

                    }
                }

                else -> {

                }
            }
        }
    }

    inner class Readdata() : Thread() {

        private val mmInStream: InputStream = socket0.inputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            var numBytes: Int = 1000 // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                try {

                    mmInStream.read(mmBuffer)
                    var message: Message = Message.obtain()
                    message.what = SEND_START
                    message.obj = String(mmBuffer)
                    myHandler.sendMessage(message)

                } catch (e: IOException) {
                    break
                }
            }
        }
    }


    private val PERMISSIONS = arrayOf(
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN,
    )

    private fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (VERSION.SDK_INT >= VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (checkSelfPermission(context, permission)
                    != PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    // Permission 확인
    @RequiresApi1(VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show()
                } else {
                    requestPermissions(permissions, REQUEST_PERMISSIONS)
                    Toast.makeText(this, "Permissions must be granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        connect_dev.createRfcommSocketToServiceRecord(MY_UUID)
        //Toast.makeText(this@MainActivity, "success to " + connect_dev.name, Toast.LENGTH_SHORT).show()
    }

    private fun run0() {
        bluetoothAdapter?.cancelDiscovery()

        mmSocket?.let { socket ->

            try {
                Thread.sleep(500)
                socket.connect()
            } catch (e: IOException) {

                socket0 = connect_dev.javaClass.getMethod(
                    "createRfcommSocket"
               , *arrayOf<Class<Int>>(TYPE)).invoke(connect_dev, 1) as BluetoothSocket
                socket0.connect()

                //Toast.makeText(this@MainActivity, "$e" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bluetooth_open(){
        var REQUEST_ENABLE_BT : Int = 1
        bluetoothManager  = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.getAdapter()
        t1 = findViewById<TextView>(R.id.test)
        t2 = findViewById<Spinner>(R.id.device_list)
        t3 = findViewById<Button>(R.id.conn_bu)
        t4 = findViewById<TextView>(R.id.all_data)
        t4.setMovementMethod(ScrollingMovementMethod())

        t6 = findViewById<BubbleChartView>(R.id.chart)
        generateData()
        //t5 = findViewById<Button>(R.id.save_bu)


        val array_d = ArrayList<String>()
        val sAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array_d)

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this@MainActivity, "adapter fail", Toast.LENGTH_SHORT).show()
        }

        else
        {
            Toast.makeText(this@MainActivity, "adapter success", Toast.LENGTH_SHORT).show()
        }

        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        if(!hasPermissions(this, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, 2)
        }

        pairedDevices  = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            array_d.add(deviceName + ";" + deviceHardwareAddress)
        }

        t2.setAdapter(sAdapter)
        t2.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //position은 선택한 아이템의 위치를 넘겨주는 인자입니다.
                mac_address0 = array_d.get(position).split(";").get(1)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bluetooth_open()

        t3.setOnClickListener {

            pairedDevices?.forEach { device ->

                if(device.address == mac_address0){
                    connect_dev = device
                    Toast.makeText(this@MainActivity, "connect to " + connect_dev.address, Toast.LENGTH_SHORT).show()
                    myHandler = MyHandler()
                    run0()
                    //updateData(100f,100f,ChartUtils.COLOR_RED)
                    }
            }
        }

    }
}

