package se3350.habittracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import se3350.habittracker.R;
import se3350.habittracker.activities.Step1TutorialActivity;
import se3350.habittracker.activities.TutorialIntroductionActivity;

public class InformationFragment extends Fragment {

    private InformationViewModel informationViewModel;
    private Button tutorial;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        informationViewModel =ViewModelProviders.of(this).get(InformationViewModel.class);

        View root = inflater.inflate(R.layout.fragment_information, container, false);
        tutorial = root.findViewById(R.id.begin_tutorial_button);

        // Set add button to open the add habit form
        tutorial.setOnClickListener(event -> {
            Intent intent = new Intent(this.getContext(), TutorialIntroductionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        return root;
    }
}