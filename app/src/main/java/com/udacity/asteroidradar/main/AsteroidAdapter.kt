package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ItemViewBinding


class AsteroidAdapter : ListAdapter<Asteroid,AsteroidAdapter.AsteroidViewHolder>(DiffCallBack){
    companion object DiffCallBack : DiffUtil.ItemCallback<Asteroid>(){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
                return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }


    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AsteroidViewHolder {
    return AsteroidViewHolder(ItemViewBinding.inflate(LayoutInflater.from(parent.context)))

    }

    class AsteroidViewHolder(var binding: ItemViewBinding ): RecyclerView.ViewHolder(binding.root) {

            fun bind(asteroid: Asteroid)
            {
                binding.asteroidProperty = asteroid
                binding.executePendingBindings()
            }

    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val singleAsteroid = getItem(position)
        holder.bind(singleAsteroid)
    }


}


