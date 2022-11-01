package dev.ma7.fallingwords.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.ma7.fallingwords.view.util.Navigation

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    open fun observeLiveData() {
        viewModel.navigationLiveData.observe(this, ::navigate)
    }

    private fun navigate(navigation: Navigation) {
        when (navigation) {
            is Navigation.Pop -> findNavController().popBackStack()
            is Navigation.Direction -> findNavController().navigate(navigation.directions)
        }
    }
}