package com.example.ledbuzz.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ledbuzz.R
import com.example.ledbuzz.adapter.UserAdapter
import com.example.ledbuzz.databinding.ActivityUserBinding
import com.example.ledbuzz.model.Subject
import com.example.ledbuzz.notification.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class UserActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBinding
    lateinit var userList:ArrayList<Subject>
    lateinit var userAdapter: UserAdapter
    var names = ""
    var priority:String? =""
    var newTime:Long= 0
    lateinit var database: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        //        mDatabase = FirebaseDatabase.getInstance()
        database = Firebase.database.reference
        userList = ArrayList()
        userAdapter = UserAdapter(this,userList)
        val uid = mAuth.uid
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        if (uid != null) {
            database.child("users").child(uid).child("Data").get().addOnSuccessListener {
                if(it.exists()) {
                    it.children.forEach { i->
                        val temp = i.getValue(Subject::class.java)
                        if (temp != null) {
                            userList.add(temp)
                        }

                    }

                    binding.mRecycler.layoutManager = LinearLayoutManager(this)
                    binding.mRecycler.adapter = userAdapter
                    userAdapter.notifyDataSetChanged()
                }
            }
        }


        binding.addingBtn.setOnClickListener {
            addInfo()
        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun addInfo() {
        val Dialogview = View.inflate(this, R.layout.add_item, null)
        val builder = android.app.AlertDialog.Builder(this)
        builder.setView(Dialogview)
        val dialog = builder.create()

        dialog.show()

        Dialogview.findViewById<RadioButton>(R.id.high).setOnClickListener{
            priority = "High"

        }

        Dialogview.findViewById<RadioButton>(R.id.low).setOnClickListener{
            priority = "Low"
        }

        Dialogview.findViewById<Button>(R.id.btn_submit).setOnClickListener{
            val subjectName = Dialogview.findViewById<EditText>(R.id.subjectName)

            val minute  = Dialogview.findViewById<TimePicker>(R.id.timePicker).minute
            val hour = Dialogview.findViewById<TimePicker>(R.id.timePicker).hour
            val day  = Dialogview.findViewById<DatePicker>(R.id.datePicker).dayOfMonth
            val month = Dialogview.findViewById<DatePicker>(R.id.datePicker).month
            val year  = Dialogview.findViewById<DatePicker>(R.id.datePicker).year

            val calendar =  Calendar.getInstance()
            calendar.set(year, month, day, hour, minute)
            val time = calendar.timeInMillis
            newTime = time
            Log.e("Time","$newTime")
            names = subjectName.text.toString().trim()
            userList.add(Subject(time,priority,"True",names))
            userAdapter.notifyDataSetChanged()

            val uid = mAuth.uid


            if(uid!=null) {
                database.child("users").child(uid).child("Data").child(names).setValue(Subject(time, priority, "True", names))


                Log.e("database","${database.child("users").get()}")
            }

            Toast.makeText(this, "Adding User Information Success", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, Notification::class.java)

            intent.putExtra(titleExtra, names)
            intent.putExtra(messageExtra, priority)

            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                newTime,
                pendingIntent
            )
            dialog.dismiss()
        }


    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}