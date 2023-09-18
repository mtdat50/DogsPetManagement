package com.example.dogspetmanagement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogspetmanagement.database.AppDatabase
import com.example.dogspetmanagement.database.Dog
import com.example.dogspetmanagement.database.DogDao
import com.example.dogspetmanagement.databinding.FragmentFirstBinding
import com.example.dogspetmanagement.model.AppViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private val sharedViewModel: AppViewModel by activityViewModels()
    private var _binding: FragmentFirstBinding? = null
    private lateinit var dogDAO: DogDao

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dogDAO = AppDatabase.getInstance(requireContext()).dogDao()

        lifecycleScope.launch {
            Log.i("=======", dogDAO.getAll().size.toString())
        }

//        sharedViewModel.dogList.add(AppViewModel.DogInfo(0, "", "AAAAA", "BBBB", "recycleView test"))
//        sharedViewModel.dogList.add(AppViewModel.DogInfo(1, "", "AAAAA", "BBBB", "recycleView test2"))


//        (activity as AppCompatActivity).supportActionBar?.hom


        val dogListView = binding.dogList
        val adapter = ListAdapter(sharedViewModel)
        dogListView.layoutManager = LinearLayoutManager(requireContext())
        dogListView.adapter = adapter


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(v: RecyclerView, h: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
            override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int) {
                if (sharedViewModel.viewSearchResult) {
                    val deletedDogInSearchResult = sharedViewModel.searchResult[h.adapterPosition]
                    lifecycleScope.launch {
                        dogDAO.delete(Dog(deletedDogInSearchResult.id, deletedDogInSearchResult.imagePath, deletedDogInSearchResult.name, deletedDogInSearchResult.breed, deletedDogInSearchResult.description))
                    }

                    val id: Int = sharedViewModel.searchResult[h.adapterPosition].id
                    for (dog in sharedViewModel.dogList)
                        if (dog.id == id) {
                            sharedViewModel.dogList.remove(dog)
                            break
                        }

                    sharedViewModel.searchResult.removeAt(h.adapterPosition)
                }
                else {
                    val deletedDogInList = sharedViewModel.dogList[h.adapterPosition]
                    lifecycleScope.launch {
                        dogDAO.delete(Dog(deletedDogInList.id, deletedDogInList.imagePath, deletedDogInList.name, deletedDogInList.breed, deletedDogInList.description))
                    }
                    sharedViewModel.dogList.removeAt(h.adapterPosition)
                }
                adapter.notifyItemRemoved(h.adapterPosition)
            }
        }).attachToRecyclerView(dogListView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.searchResult.clear()
        _binding = null
    }
}