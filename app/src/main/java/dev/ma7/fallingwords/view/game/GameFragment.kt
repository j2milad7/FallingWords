package dev.ma7.fallingwords.view.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.ma7.fallingwords.databinding.FragmentGameBinding
import dev.ma7.fallingwords.view.base.BaseFragment

@AndroidEntryPoint
class GameFragment : BaseFragment<GameViewModel>() {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = requireNotNull(_binding)

    override val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}