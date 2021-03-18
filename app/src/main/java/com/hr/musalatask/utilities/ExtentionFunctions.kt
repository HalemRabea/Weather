package com.hr.musalatask.utilities

import android.content.Context
import android.net.Uri
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hr.musalatask.R
import com.hr.musalatask.model.WeatherResponseModel
import java.util.*

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
//        .error(R.drawable.ic_broken_image)

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

fun TextView.setTimeFromDateTimeStamp(timestamp: Long){
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = timestamp * 1000L
    this.text= DateFormat.format("hh:mm aa", calendar).toString()
}

fun TextView.WindowDirectionFromDegree(degree: Int){
    this.text=when (degree) {
        in 0..45 -> "E"
        in 45..135 -> "N"
        in 135..225 -> "W"
        in 225..315 -> "S"
        in 315..360 -> "E"
        else -> ""
    }
}