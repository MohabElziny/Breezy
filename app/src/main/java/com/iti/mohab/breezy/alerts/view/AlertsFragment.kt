package com.iti.mohab.breezy.alerts.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.mohab.breezy.R
import com.iti.mohab.breezy.alerts.viewmodel.AlertsViewModel
import com.iti.mohab.breezy.alerts.viewmodel.AlertsViewModelFactory
import com.iti.mohab.breezy.databinding.FragmentAlertsBinding
import com.iti.mohab.breezy.datasource.WeatherRepository
import com.iti.mohab.breezy.dialogs.view.AlertTimeDialog
import com.iti.mohab.breezy.favorites.view.FavoritesFragment
import com.iti.mohab.breezy.model.WeatherAlert
import kotlinx.coroutines.flow.collect

class AlertsFragment : Fragment() {

    private var _binding: FragmentAlertsBinding? = null

    private val viewModel: AlertsViewModel by viewModels {
        AlertsViewModelFactory(WeatherRepository.getRepository(requireActivity().application))
    }

    private lateinit var alertsAdapter: AlertAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backToHomeScreen()

        initFavoritesRecyclerView()

        binding.btnAddAlert.setOnClickListener {
            AlertTimeDialog().show(requireActivity().supportFragmentManager, "AlertDialog")
        }

        viewModel.getFavorites()

        lifecycleScope.launchWhenStarted {
            viewModel.alerts.collect {
                if (!it.isNullOrEmpty()) {
                    binding.textEmptyAlert.visibility = View.GONE
                } else {
                    binding.textEmptyAlert.visibility = View.VISIBLE
                }
                fetchAlertsRecycler(it)
            }
        }
    }

    private fun fetchAlertsRecycler(list: List<WeatherAlert>?) {
        alertsAdapter.alertsList = list ?: emptyList()
        alertsAdapter.notifyDataSetChanged()
    }

    private fun initFavoritesRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(FavoritesFragment().context)
        alertsAdapter = AlertAdapter(this.requireContext(), viewModel)
        binding.alertRecyclerView.layoutManager = linearLayoutManager
        binding.alertRecyclerView.adapter = alertsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun backToHomeScreen() {
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                Navigation.findNavController(v)
                    .navigate(R.id.action_navigation_alerts_to_navigation_home)
                return@OnKeyListener true
            }
            false
        })
    }

}