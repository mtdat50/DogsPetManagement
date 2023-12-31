package com.example.dogspetmanagement

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dogspetmanagement.model.AppViewModel
import java.io.File

class ListAdapter(private val sharedViewModel: AppViewModel): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    class ListViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val dogImageView: ImageView? = view.findViewById(R.id.dogImageView)
        val dogNameView: TextView? = view.findViewById(R.id.dogNameView)
        val dogBreedView: TextView? = view.findViewById(R.id.dogBreedView)
        val dogDescriptionView: TextView? = view.findViewById(R.id.dodDescriptionView)
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

    override fun getItemCount(): Int =
        if (sharedViewModel.viewSearchResult)
            sharedViewModel.searchResult.size
        else
            sharedViewModel.dogList.size


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dogData:AppViewModel.DogInfo =
            if (sharedViewModel.viewSearchResult)
                sharedViewModel.searchResult[position]
            else
                sharedViewModel.dogList[position]


        val absolutePath = File(dogData.imagePath).absolutePath
        val bitmapImage = BitmapFactory.decodeFile(absolutePath)

        holder.dogImageView?.setImageBitmap(bitmapImage)
        holder.dogNameView?.text = dogData.name
        holder.dogBreedView?.text = dogData.breed
        holder.dogDescriptionView?.text = dogData.description
        holder.itemView.setOnClickListener {
            sharedViewModel.selectedDogInfo = dogData
            it.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}