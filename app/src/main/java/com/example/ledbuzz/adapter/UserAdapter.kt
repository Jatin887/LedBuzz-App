package com.example.ledbuzz.adapter

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ledbuzz.R
import com.example.ledbuzz.model.Subject
import com.example.ledbuzz.model.UserCard
import com.example.ledbuzz.notification.Notification
import com.example.ledbuzz.notification.messageExtra
import com.example.ledbuzz.notification.notificationID
import com.example.ledbuzz.notification.titleExtra
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter(val context: Context,val userList : ArrayList<Subject>):RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    var priority = ""
    var names = " "
    var newTime:Long = 0
    private lateinit var database: DatabaseReference
    lateinit var mAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.M)
    inner class UserViewHolder(val v: View):RecyclerView.ViewHolder(v){

        var name: TextView
        var mpriority:TextView
        var mMenus: ImageView
        var time :TextView

        init {
            name = v.findViewById(R.id.mTitle)
            mpriority = v.findViewById(R.id.mSubTitle)
            mMenus = v.findViewById(R.id.mMenus)
            time = v.findViewById(R.id.mTime)
            mMenus.setOnClickListener { popupMenus(it) }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun popupMenus(v:View) {
            val position = userList[adapterPosition]
            val popupMenus = PopupMenu(context,v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editText->{

                        val Dialogview = View.inflate(context, R.layout.add_item, null)
                        val builder = AlertDialog.Builder(context)
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
                            position.subjectName = names
                            position.priority = priority
                            position.time = time
                            notifyDataSetChanged()
                            mAuth = FirebaseAuth.getInstance()
                            database = Firebase.database.getReference()

                            val uid = mAuth.uid


                            if(uid!=null) {
                                database.child("users").child(uid).child("Data").child(names).setValue(Subject(time, priority, "True", names))
                            }

                            Toast.makeText(context,"User Information is Edited",Toast.LENGTH_SHORT).show()

                            dialog.dismiss()
                        }
                        true
                    }
                    R.id.delete->{
                        /**set delete*/
                        AlertDialog.Builder(context)
                            .setTitle("Delete")
                            .setIcon(R.drawable.ic_warning)
                            .setMessage("Are you sure delete this Information")
                            .setPositiveButton("Yes"){
                                    dialog,_->
                                val subjectName = userList[adapterPosition].subjectName
                                mAuth = FirebaseAuth.getInstance()
                                database = Firebase.database.reference
                                val uid = mAuth.uid
                                if (uid != null) {
                                    if (subjectName != null) {
                                        database.child("users").child(uid).child("Data").child(subjectName).removeValue()
                                    }
                                }
                                userList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(context,"Deleted this Information",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else-> true
                }

            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v  = inflater.inflate(R.layout.user_card,parent,false)
        return UserViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val newList = userList[position]
        val date = newList.time?.let { Date(it) }
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(context)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(context)
        holder.name.text = newList.subjectName
        holder.time.text = timeFormat.format(date)
        holder.mpriority.text = newList.priority+"                "+dateFormat.format(date)
    }

    override fun getItemCount(): Int {
        return  userList.size
    }

}