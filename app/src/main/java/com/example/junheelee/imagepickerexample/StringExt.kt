package com.example.junheelee.imagepickerexample

import android.content.Context
import android.widget.Toast

/**
 * Created by junhee.lee on 2018. 4. 2..
 */

fun Int.mkRemoveToast(ctx: Context) = Toast.makeText(ctx, "삭제된 포지션 : $this", Toast.LENGTH_SHORT).show()

fun Int.mkSelectToast(ctx: Context) = Toast.makeText(ctx, "터치된 포지션 : $this", Toast.LENGTH_SHORT).show()