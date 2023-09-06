package com.example.dogspetmanagement

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dogspetmanagement.model.AppViewModel
import java.io.File
import androidx.navigation.fragment.findNavController

class ListAdapter(private val context: Context, private val shareViewModel: AppViewModel): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    class ListViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val dogImageView: ImageView? = view.findViewById<ImageView>(R.id.dogImageView)
        val dogNameView: TextView? = view.findViewById<TextView>(R.id.dogNameView)
        val dogBreedView: TextView? = view.findViewById<TextView>(R.id.dogBreedView)
        val dogDescriptionView: TextView? = view.findViewById<TextView>(R.id.dodDescriptionView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        layout.setOnClickListener {
            layout.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        return ListViewHolder(layout)
    }

    override fun getItemCount(): Int = shareViewModel.dogList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dogData = shareViewModel.dogList[position]
        val absolutePath = File(dogData.imagePath).absolutePath
        val bitmapImage = BitmapFactory.decodeFile(absolutePath)

        holder.dogImageView?.setImageBitmap(bitmapImage)
        holder.dogNameView?.text = dogData.name
        holder.dogBreedView?.text = dogData.breed
        holder.dogDescriptionView?.text = dogData.description
        holder.itemView.setOnClickListener {
            shareViewModel.selectedDogInfo = dogData
            it.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}