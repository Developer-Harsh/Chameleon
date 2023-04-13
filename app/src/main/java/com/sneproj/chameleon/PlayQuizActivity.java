package com.sneproj.chameleon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sneproj.chameleon.databinding.ActivityPlayQuizBinding;
import com.sneproj.chameleon.model.LangModal;
import com.sneproj.chameleon.model.QuestionModal;

import java.util.ArrayList;

public class PlayQuizActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityPlayQuizBinding binding;
    ArrayList<QuestionModal> list;
    int index = 0;
    QuestionModal questionModal;
    private boolean textViewClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(PlayQuizActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(PlayQuizActivity.this.getColor(R.color.bg_main));
        }

        list = new ArrayList<>();



        list.add(new QuestionModal("How are ___", "i", "are", "you", "your", "you"));
        list.add(new QuestionModal("I am __", "sine", "line", "fine", "dine", "fine"));
        list.add(new QuestionModal("I am __", "sine", "line", "fine", "dine", "fine"));
        list.add(new QuestionModal("I am __", "sine", "line", "fine", "dine", "fine"));
        list.add(new QuestionModal("I am __", "sine", "line", "fine", "dine", "fine"));
        list.add(new QuestionModal("I am __", "sine", "line", "fine", "dine", "fine"));
        list.add(new QuestionModal("I am __", "sine", "line", "fine", "dine", "fine"));
        list.add(new QuestionModal("I am __", "sine", "line", "fine", "dine", "fine"));

        binding.Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetQuestion();
                if (index <list.size()){
                    index++;
                    textViewClicked = false;
                    setNextQuestions();
                }else{
                    Toast.makeText(PlayQuizActivity.this, "Finish", Toast.LENGTH_SHORT).show();
                    textViewClicked = false;
                }
            }
        });
      setNextQuestions();
    }
    void setNextQuestions(){
        if (index<list.size()){
            binding.questionno.setText(String.format("%d/%d", (index+1), list.size()));
             questionModal = list.get(index);
            binding.question.setText(questionModal.getQuestion());
            binding.option1text.setText(questionModal.getOption1());
            binding.option2text.setText(questionModal.getOption2());
            binding.option3text.setText(questionModal.getOption3());
            binding.option4text.setText(questionModal.getOption4());
        }
    }
    public void onClick(View view){
        TextView selected;
        ImageView check;
        if (textViewClicked) {
            return;
        }
        switch (view.getId()){

            case R.id.option1text:
                ImageView check1 = binding.check1;
                selected = (TextView) view;
                checkAnswer(selected, check1);
                textViewClicked = true;
                break;
            case R.id.option2text:
                selected = (TextView) view;
                ImageView check2 = binding.check2;
                checkAnswer(selected, check2);
                textViewClicked = true;
                break;
            case R.id.option3text:
                selected = (TextView) view;
                ImageView check3 = binding.check3;
                checkAnswer(selected,check3);
                textViewClicked = true;
                break;
            case R.id.option4text:
                selected = (TextView) view;
                ImageView check4 = binding.check4;
                checkAnswer(selected, check4);
                textViewClicked = true;
                break;
        }
    }
    public void checkAnswer(TextView textView, ImageView check){
        String selectedAnswer = textView.getText().toString();

        textView.setBackground(getResources().getDrawable(R.drawable.quiz_option_selected));
        if (selectedAnswer.equals(questionModal.getAnswer())){

            check.setVisibility(View.VISIBLE);
            check.setBackground(getResources().getDrawable(R.drawable.right));
        }else{
            check.setVisibility(View.VISIBLE);
            check.setBackground(getResources().getDrawable(R.drawable.wrong));
            showAnswer();

        }
    }
    public void resetQuestion(){
        binding.option1text.setBackground(getResources().getDrawable(R.drawable.quiz_unselected));
        binding.option2text.setBackground(getResources().getDrawable(R.drawable.quiz_unselected));
        binding.option3text.setBackground(getResources().getDrawable(R.drawable.quiz_unselected));
        binding.option4text.setBackground(getResources().getDrawable(R.drawable.quiz_unselected));
        binding.check1.setVisibility(View.GONE);
        binding.check2.setVisibility(View.GONE);
        binding.check3.setVisibility(View.GONE);
        binding.check4.setVisibility(View.GONE);
    }
    public void showAnswer(){
        if (questionModal.getAnswer().equals(binding.option1text.getText().toString())){
            binding.check1.setVisibility(View.VISIBLE);
            binding.check1.setBackground(getResources().getDrawable(R.drawable.right));
        }else if (questionModal.getAnswer().equals(binding.option2text.getText().toString())){
            binding.check2.setVisibility(View.VISIBLE);
            binding.check2.setBackground(getResources().getDrawable(R.drawable.right));
        }else if (questionModal.getAnswer().equals(binding.option3text.getText().toString())){
            binding.check3.setVisibility(View.VISIBLE);
            binding.check3.setBackground(getResources().getDrawable(R.drawable.right));
        }else if (questionModal.getAnswer().equals(binding.option4text.getText().toString())){
            binding.check4.setVisibility(View.VISIBLE);
            binding.check4.setBackground(getResources().getDrawable(R.drawable.right));
        }
    }
}