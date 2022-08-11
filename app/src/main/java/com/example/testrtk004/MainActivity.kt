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
import android.view.View
import android.content.Intent
import android.util.Log
import android.content.Context
import android.system.Os.socket

import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi as RequiresApi1

import java.lang.Integer.TYPE
import java.util.UUID
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var result0 : String = ""
    private var mac_address0 : String = ""

    private lateinit var bluetoothManager : BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var t1 : TextView
    private lateinit var t2 : Spinner
    private lateinit var t3 : Button
    lateinit var pairedDevices: Set<BluetoothDevice>
    lateinit var connect_dev : BluetoothDevice
    private val REQUEST_PERMISSIONS= 2

    var MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private val PERMISSIONS = arrayOf(
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN
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

                var socket0 = connect_dev.javaClass.getMethod(
                    "createRfcommSocket"
               , *arrayOf<Class<Int>>(TYPE)).invoke(connect_dev, 1) as BluetoothSocket
                socket0.connect()

                //Toast.makeText(this@MainActivity, "$e" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bluetooth_open(){
        var REQUEST_ENABLE_BT : Int = 1;
        bluetoothManager  = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.getAdapter()
        t1 = findViewById<TextView>(R.id.test)
        t2 = findViewById<Spinner>(R.id.device_list)
        t3 = findViewById<Button>(R.id.conn_bu)

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
                    run0()
                    }
            }
        }
    }
}

