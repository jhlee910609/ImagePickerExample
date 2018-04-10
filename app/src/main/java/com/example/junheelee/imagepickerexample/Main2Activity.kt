package com.example.junheelee.imagepickerexample

import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activitiy_main2_1.*

class Main2Activity : AppCompatActivity() {

    val imgUrl = "https://ilyricsbuzz.com/wp-content/uploads/2018/01/OH-MY-GIRL-Secret-Garden.jpg"
    var isChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitiy_main2_1)

        Glide.with(this)
                .load(imgUrl)
                .into(imageView)

        val constSet_1 = ConstraintSet()
                .apply { clone(constraint_1) }

        val constSet_2 = ConstraintSet()
                .apply { clone(this@Main2Activity, R.layout.activity_main2) }


        imageView.setOnClickListener {
            val transition = android.support.transition.AutoTransition()
                    .apply {
                        duration = 750
                    }

            android.support.transition.TransitionManager.beginDelayedTransition(constraint_1, transition)
            val constraint = if (isChanged) constSet_1 else constSet_2
            constraint.applyTo(constraint_1)
            isChanged = !isChanged
        }

    }
}
