package com.sneproj.chameleon.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.sneproj.chameleon.R;
import com.sneproj.chameleon.adapter.SliderAdapter;
import com.sneproj.chameleon.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    int[] images = {R.drawable.slider_image1, R.drawable.slider_image1,
            R.drawable.slider_image1

    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container,false);

        setSliderView();
        return  binding.getRoot();
    }
    private void setSliderView() {
        SliderAdapter adapter = new SliderAdapter(images);
        binding.imageSlider.setSliderAdapter(adapter);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycle(true);
    }
}