package com.example.personalquizcontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class QFragment extends Fragment implements View.OnClickListener {

    TextView queText;
    RadioButton opt1;
    RadioButton opt2;
    RadioButton opt3;
    RadioButton opt4;
    RadioGroup rg;
    Button next;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question, container, false);

        queText = (TextView) view.findViewById(R.id.que);
        rg = (RadioGroup) view.findViewById(R.id.rg);
        opt1 = (RadioButton) view.findViewById(R.id.opt1);
        opt2 = (RadioButton) view.findViewById(R.id.opt2);
        opt3 = (RadioButton) view.findViewById(R.id.opt3);
        opt4 = (RadioButton) view.findViewById(R.id.opt4);
        next = (Button) view.findViewById(R.id.next);

        Bundle bundle = getArguments();
        queText.setText(bundle.getString("que", ""));
        opt1.setText(bundle.getString("opt1", ""));
        opt2.setText(bundle.getString("opt2", ""));
        opt3.setText(bundle.getString("opt3", ""));
        opt4.setText(bundle.getString("opt4", ""));
        next.setText(bundle.getString("next", ""));
        next.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Listener listener = (Listener) getActivity();
        listener.send(getArguments().getInt("qid")+1, rg.indexOfChild(getActivity().findViewById(rg.getCheckedRadioButtonId())));
    }
}
