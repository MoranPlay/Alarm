package com.example.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.navigation.fragment.findNavController
import com.example.alarm.databinding.FragmentFirstBinding
import java.util.Calendar


class FirstFragment : Fragment() {

    private val calendar = Calendar.getInstance()
    private lateinit var alarmIntent: PendingIntent
    private var alarmManager: AlarmManager? = null
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("ScheduleExactAlarm")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timePicker.setIs24HourView(true)
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let {intent ->
            intent.putExtra("key", "Hello!")
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
        //requestCode изменить если нужно несколько alarm. нужно создать бд для alarm

        binding.button.setOnClickListener {
            calendar.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
            calendar.set(Calendar.MINUTE, binding.timePicker.minute)
            alarmManager?.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }
        binding.button2.setOnClickListener {
            alarmManager?.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP, // + AlarmManager.INTERVAL_HALF_HOUR   Time of start system
                SystemClock.elapsedRealtime(),
                60 * 1000,
                alarmIntent
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}