package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.network.AsteroidFilter
import kotlinx.coroutines.channels.Channel

class MainFragment : Fragment() {

    /*
    using solution of Recycler view update list
    https://knowledge.udacity.com/questions/577992
     */
// Declare an adapter for submitting list later
private lateinit var asteroidListAdapter: AsteroidAdapter

    private lateinit var binding: FragmentMainBinding

    private lateinit var viewModel : MainViewModel

   @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
       val application = requireNotNull(activity).application

       val factory = MainViewModel.Factory(application)
       viewModel = ViewModelProvider(this,factory).get(MainViewModel::class.java)
        binding.viewModel = viewModel

       //using solution from knowledge center ->https://knowledge.udacity.com/questions/577992
        val adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener{
            viewModel.displayDetailFragment(it)
        }).apply {
            asteroidListAdapter = this
        }

        binding.asteroidRecycler.adapter = adapter
       binding.asteroidRecycler.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))

        viewModel.navigateToDetailFrgament.observe(viewLifecycleOwner, Observer {
            if(null!= it)
            {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayDetailFragmentComplete()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(when(item.itemId){
            R.id.show_saved-> AsteroidFilter.SHOW_SAVED
            R.id.show_today -> AsteroidFilter.SHOW_TODAY
            else-> AsteroidFilter.SHOW_WEEKLY
        }
        )
ObserveList()
        return true
    }

    private fun ObserveList(){
        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            asteroidListAdapter.submitList(it)
        })
    }
}
