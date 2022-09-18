package com.degitalcon.passthings.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.degitalcon.passthings.databinding.FragmentHomeBinding

class HomeFragment : Fragment(),ItemRecyclerAdapter.OnItemListener {

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val v: View = binding!!.root

        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}