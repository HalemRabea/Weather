package com.hr.musalatask.utilities

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hr.musalatask.R

fun getProcessDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val imgUri = Uri.parse(uri).buildUpon().scheme("https").build()

    val requestOptions = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_broken_image)

    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(imgUri)
        .into(this)
}

fun Context.showPermissionRequestExplanation(
    permission: String,
    message: String,
    retry: (() -> Unit)? = null
) {
    AlertDialog.Builder(this).apply {
        setTitle(getString(R.string.location))
        setMessage(message)
        setPositiveButton("Ok") { _, _ -> retry?.invoke() }
        setNegativeButton("cancel"){dialog, _ -> dialog.dismiss() }
    }.show()
}