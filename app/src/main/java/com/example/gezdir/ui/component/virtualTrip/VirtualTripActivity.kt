package com.example.gezdir.ui.component.virtualTrip

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.gezdir.R
import com.example.gezdir.databinding.ActivityVirtualTripBinding
import com.example.gezdir.ui.base.BaseActivity
import com.example.gezdir.ui.component.meet.MeetActivity
import com.example.gezdir.util.UserManager
import java.util.UUID

class VirtualTripActivity : BaseActivity() {
    private lateinit var binding: ActivityVirtualTripBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_virtual_trip)

        binding.buttonCreateMeet.setOnClickListener {
            val uuid: UUID = UUID.randomUUID()
            binding.textViewMeetLink.visibility = android.view.View.VISIBLE
            binding.imageViewCopy.visibility = android.view.View.VISIBLE
            binding.textViewMeetLink.text = uuid.toString()

        }

        binding.buttonJoinMeet.setOnClickListener {
            if (binding.editTextMeetID.text.toString().isEmpty()) {
                Toast.makeText(this, "Önce bir link girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val intent = Intent(this, MeetActivity::class.java)
                intent.putExtra("callID", binding.editTextMeetID.text.toString())
                startActivity(intent)
            }
        }

        // TextView tıklanabilir yapma
        binding.imageViewCopy.setOnClickListener {
            if (binding.textViewMeetLink.text.toString().isEmpty()) {
                Toast.makeText(this, "Önce bir link oluşturun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                copyTextToClipboard(binding.textViewMeetLink.text.toString())
                Toast.makeText(this, "Metin kopyalandı", Toast.LENGTH_SHORT).show()
            }
        }

        if (UserManager.currentUserGezdiren) {
            binding.buttonCreateMeet.visibility = android.view.View.VISIBLE
        }

    }

    // Metni panoya kopyala
    private fun copyTextToClipboard(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Copied Text", text)

        clipboardManager.setPrimaryClip(clipData)
    }
}