package com.milesilac.citylifescan.view;

import android.app.Dialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.milesilac.citylifescan.databinding.ImageViewDialogBinding;

public class CityPhotoDialogFragment extends DialogFragment {

    ImageViewDialogBinding binding;
    String imageUrl;
    SpannableString personAndSite;
    String license;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = ImageViewDialogBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);

        addDetailsToImageView();

        return dialog;
    }

    private void addDetailsToImageView() {
        Glide.with(this)
                .load(imageUrl)
                .into(binding.photoZoomed);

        binding.imagePhotographer.setText(personAndSite);
        binding.imagePhotographer.setMovementMethod(LinkMovementMethod.getInstance());

        binding.imageAttribution.setText(license);
    }

    public void setImageDetails(String imageUrl, SpannableString personAndSite, String license) {
        this.imageUrl = imageUrl;
        this.personAndSite = personAndSite;
        this.license = license;
    }

}
