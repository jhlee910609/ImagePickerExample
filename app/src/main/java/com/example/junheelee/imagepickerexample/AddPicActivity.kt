package com.example.junheelee.imagepickerexample

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.activity_add_pic.*
import kotlinx.android.synthetic.main.image_holder.*

class AddPicActivity : AppCompatActivity() {

    val REQUEST_CODE_CHOOSE = 101
    var itemList: MutableList<Uri> = mutableListOf()
    lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pic)

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
        galleryAdapter = GalleryAdapter(itemList)

        preview_recycler.apply {
            layoutManager = GridLayoutManager(baseContext, 3)
            adapter = galleryAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            itemList = Matisse.obtainResult(data)
            galleryAdapter.itemList = Matisse.obtainResult(data)
            galleryAdapter.notifyDataSetChanged()
        }
    }

    inner class GalleryAdapter(var itemList: MutableList<Uri>) : RecyclerView.Adapter<GalleryAdapter.ImageHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder = ImageHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.image_holder, parent, false)
        )


        override fun getItemCount(): Int = itemList?.size ?: 0

        override fun onBindViewHolder(holder: ImageHolder, position: Int) = holder.bind(itemList[position])


        inner class ImageHolder(val v: View) : RecyclerView.ViewHolder(v) {

            var imageView: ImageView

            init {
                imageView = v.findViewById<ImageView>(R.id.image_view)
            }


            fun bind(data: Uri) {
                Glide.with(v.context)
                        .load(data)
                        .into(imageView)

            }
        }
    }
}
