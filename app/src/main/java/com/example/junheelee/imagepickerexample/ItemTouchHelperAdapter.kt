package com.example.junheelee.imagepickerexample

/**
 * Created by junhee.lee on 2018. 4. 1..
 */
interface ItemTouchHelperAdapter {

     fun onItemMove(fromPosition: Int, toPosition: Int) : Boolean
     fun onItemDismiss(position: Int)
     fun resetNumber()
}