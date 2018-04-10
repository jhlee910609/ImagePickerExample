package com.example.junheelee.imagepickerexample

import android.content.Context
import android.widget.Toast
import androidx.core.widget.toast

/**
 * Created by junhee.lee on 2018. 4. 2..
 */


fun Int.mkSelectToast(ctx: Context) = ctx.toast("터치된 포지션 : $this", Toast.LENGTH_SHORT).show()
