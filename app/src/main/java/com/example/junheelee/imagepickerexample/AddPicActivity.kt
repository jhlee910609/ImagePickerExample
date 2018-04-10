package com.example.junheelee.imagepickerexample

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.MotionEventCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.widget.toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.activity_add_pic.*
import kotlinx.android.synthetic.main.image_holder.*
import java.util.*


class AddPicActivity : AppCompatActivity(), OnDragListener {

    lateinit var itemTouchHelper: ItemTouchHelper

    val REQUEST_CODE_CHOOSE = 101
    var itemList: MutableList<Uri> = mutableListOf()
    lateinit var galleryAdapter: GalleryAdapter
    lateinit var mStorageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pic)

        mStorageRef = FirebaseStorage.getInstance("gs://imagepickerexample.appspot.com/").reference

        btn_upload.setOnClickListener { uploadFiles() }

        add_pic.setOnClickListener {
            val perms = RxPermissions(this)
            perms.request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe {
                        if (it) {
                            Matisse.from(this)
                                    .choose(MimeType.allOf())
                                    .countable(true)
                                    .theme(R.style.Matisse_Dracula)
                                    .maxSelectable(10)
                                    .imageEngine(GlideEngine())
                                    .thumbnailScale(0.8f)
                                    .forResult(REQUEST_CODE_CHOOSE)
                        }
                    }
        }



        galleryAdapter = GalleryAdapter(itemList, this)

        preview_recycler.apply {
            layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
            adapter = galleryAdapter
        }

        val simpleCallback = SimpleItemTouchHelperCallback(galleryAdapter)
        itemTouchHelper = ItemTouchHelper(simpleCallback).apply {
            attachToRecyclerView(preview_recycler)
        }
    }

    override fun onDrag(vh: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(vh)
    }

    fun uploadFiles() {
        itemList.forEach {
            val file = it
            val imgRef = mStorageRef.child(it.path)
            imgRef.putFile(file)
                    .addOnSuccessListener {
                        Log.e("AddpicActivity", it.downloadUrl.toString())
                        Toast.makeText(AddPicActivity@ this, "업로드 성공!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { Toast.makeText(AddPicActivity@ this, "업로드 실패!", Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            itemList = Matisse.obtainResult(data)
            galleryAdapter.itemList = Matisse.obtainResult(data)
            galleryAdapter.notifyDataSetChanged()
            total_count.text = """
                |[ 총 개수 ${itemList.size} 입니다. ]
                |드래그 하시면 순서를 변경하실 수 있습니다.
            """.trimMargin()

            doExample { println("Hi") }


        }
    }

    inline fun doExample(func: () -> Unit) {
        println("The start")
        func.invoke()
        println("The end")
    }

    inner class GalleryAdapter(var itemList: MutableList<Uri>, var listener: OnDragListener) : RecyclerView.Adapter<GalleryAdapter.ImageHolder>(), ItemTouchHelperAdapter {
        override fun resetNumber() {
            notifyDataSetChanged()
        }

        private lateinit var mCtx: Context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.image_holder, parent, false)
            this.mCtx = parent.context
            return ImageHolder(v)
        }

        override fun onBindViewHolder(holder: ImageHolder, position: Int) {
            holder.bind(itemList[position], position, { pos: Int -> removeItem(pos) })
        }

        override fun getItemCount(): Int = itemList.size

        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            Collections.swap(itemList, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
            Toast.makeText(mCtx, "before [ $fromPosition ] -> after : [ $toPosition ] ", Toast.LENGTH_SHORT).show()
            move_item_info.text = "before [ $fromPosition ] -> after : [ $toPosition ] "
            return true
        }

        override fun onItemDismiss(position: Int) {
            itemList.removeAt(position)
            notifyItemRemoved(position)
        }

        private fun removeItem(pos: Int) {
            itemList.removeAt(pos)
            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos, itemList.size - pos)

            if (itemList.isEmpty())
                baseContext.toast("모두 삭제됨", Toast.LENGTH_SHORT).show()
        }


        inner class ImageHolder(val vh: View) : AndroidExtensionsViewHolder(vh), ItemTouchHelperHolder, GestureDetector.OnGestureListener {

            override fun onItemSelected() {
                vh.setBackgroundColor(Color.LTGRAY)
            }

            override fun onItemClear() {
                vh.setBackgroundColor(0)
            }

            fun bind(data: Uri, pos: Int, removeItem: (pos: Int) -> Unit) {

                vh.setOnClickListener {
                    pos.mkSelectToast(vh.context)
                }
                Glide.with(vh.context)
                        .load(data)
                        .into(image_view)

                num_tv.text = pos.toString()
            }

            override fun onShowPress(e: MotionEvent?) {

            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return false
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return false
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                return false
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                return false
            }

            override fun onLongPress(e: MotionEvent?) {
                if (MotionEventCompat.getActionMasked(e) == MotionEvent.ACTION_DOWN) {
                    listener.onDrag(this)
                }
            }
        }


    }
}
