package com.udacity.asteroidradar

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidAdapter
import com.udacity.asteroidradar.main.Asteroid_Status


@BindingAdapter("listData")
fun bindRecylerView(recyclerView: RecyclerView,listOfAsteroids: List<Asteroid>?)
{
    Log.i("Asteroid","in BindingAdapter :" + listOfAsteroids.toString())
     val   adapter = recyclerView.adapter as AsteroidAdapter
        adapter.submitList(listOfAsteroids)

}

@BindingAdapter("imageUrl")
fun bindToImageView(imageView: ImageView,url : String?)
{

    url?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Picasso.with(imageView.context).load(imgUri).into(imageView)
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}


@BindingAdapter("androidApiStatus")
fun bindStatus(progressBar : ProgressBar, status : Asteroid_Status)
{
    when(status)
    {
        Asteroid_Status.LOADING ->
        {
            progressBar.visibility = View.VISIBLE
            progressBar.progress = 50

        }
        Asteroid_Status.DONE ->
        {
            progressBar.visibility = View.GONE
        }
        Asteroid_Status.ERROR->
        {
            progressBar.visibility = View.VISIBLE
            progressBar.progress = 0
        }

    }

}






