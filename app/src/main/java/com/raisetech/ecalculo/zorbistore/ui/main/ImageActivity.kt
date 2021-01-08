package com.raisetech.ecalculo.zorbistore.ui.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.raisetech.ecalculo.R
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.recyclerview_cartitems.view.*

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        var image=intent.getStringExtra("images")


        Glide.with(image_view)
            .load(image)
            .placeholder(R.drawable.ic_empty_image)
            .into(image_view)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}