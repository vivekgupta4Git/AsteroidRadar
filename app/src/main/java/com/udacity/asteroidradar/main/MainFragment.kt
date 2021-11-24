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
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

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

        val adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener{
            viewModel.displayDetailFragment(it)
        })

        binding.asteroidRecycler.adapter = adapter
   //    binding.asteroidRecycler.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))

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
            R.id.show_saved-> MenuItemFilter.SAVED
            R.id.show_today -> MenuItemFilter.SHOW_TODAY
            else-> MenuItemFilter.SHOW_WEEK
        }
        )
        return true
    }


}
