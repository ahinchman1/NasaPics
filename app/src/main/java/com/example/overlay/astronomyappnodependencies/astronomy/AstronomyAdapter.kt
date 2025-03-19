package com.example.overlay.astronomyappnodependencies.astronomy

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.request.transformations
import coil3.size.Scale
import coil3.transform.CircleCropTransformation
import com.example.overlay.astronomyappnodependencies.R
import com.example.overlay.astronomyappnodependencies.databinding.AstronomyListItemBinding
import com.example.overlay.astronomyappnodependencies.network.api.AstronomyPicture

class AstronomyAdapter(
    private var astronomyPictures: List<AstronomyPicture>,
): RecyclerView.Adapter<AstronomyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AstronomyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AstronomyListItemBinding.inflate(inflater, parent, false)
        return AstronomyViewHolder(binding)
    }

    override fun getItemCount(): Int = astronomyPictures.size

    override fun onBindViewHolder(holder: AstronomyViewHolder, position: Int) {
        val astronomyPhoto = astronomyPictures[position]
        holder.bind(astronomyPhoto)
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, astronomyPhoto.title, Toast.LENGTH_SHORT).show()
        }
    }

    fun updatePhotos(updated: List<AstronomyPicture>) {
        val diffCallback = AstronomyDiffCallback(astronomyPictures, updated)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        astronomyPictures = updated
        diffResult.dispatchUpdatesTo(this)
        // only updates changed items instead of the entire list
    }
}

class AstronomyDiffCallback(
    private val oldList: List<AstronomyPicture>,
    private val newList: List<AstronomyPicture>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].url == newList[newItemPosition].url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}

class AstronomyViewHolder(
    private val binding: AstronomyListItemBinding,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(astronomyPicture: AstronomyPicture) {
        Log.d("AstronomyViewHolder", "Loading image URL: ${astronomyPicture.url}")
        Log.d("AstronomyViewHolder", "placeholder: ${R.drawable.baseline_image_24}}")


        binding.astronomyThumbnail.load(astronomyPicture.url) {
            crossfade(true)
            transformations(CircleCropTransformation())
            scale(Scale.FILL)
            placeholder(R.drawable.baseline_image_24)
        }

        binding.astronomyTitle.setTextOrHide(astronomyPicture.title)
    }

    private fun TextView.setTextOrHide(message: String?) {
        isVisible = !message.isNullOrEmpty()
        if (!message.isNullOrEmpty()) text = message
    }
}