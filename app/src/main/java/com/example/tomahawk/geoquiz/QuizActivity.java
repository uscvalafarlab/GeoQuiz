package com.example.tomahawk.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; // Use for having a button with just an image
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String QUESTION_LIST = "question_list";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mBackButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private double Correct = 0;
    private int TotalAnswered = 0;
    private double Score = 0;


    /**
     * Getting things set up and ready to be inspected.
     * Green code ares like this will be the challenges commented out, except for this first block
     * Grey code will be comments on the methods and code itself about how things work
     * Log statements to follow how everything works and for debugging purposes
     * There were some bugs that the BNR did not discuss in any way,
     * like hitting the back button once reaching the first index would crash the app.
     */

    /**
     * Challenge: Disable a question after it has been answered
     *
     * To disable the buttons to complete the challenge...
     * Add extra bool variable to the question object.
     * Mark if question was answered.---> requires some logic and algorithms
     * Store marked questions in array or hash map to cross check if answered
     * Create methods to check if question at given index has been answered and if so disable button, if not enable button.
     *
     * Challenge complete
     */

    /**
     * Challenge: Display game score toast once all questions have been answered
     * Create a couple variables and keep track of them in the button presses
     * Display score once all questions have been answered
     *
     * Challenge complete, very easy...
     */

    // This was a pretty basic app that re-wet my feet from the first book to here...
    // This app is done, it can be tweeked in many ways

    private Question[] mQuestionBank = new Question[] { // An array of question objects
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);// Calling this method to inflate the layout, use resource id to specify which layout

        if (savedInstanceState != null){
            Log.d(TAG, "Checking savedInstanceState 1");
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            Log.d(TAG, "Checking savedInstanceState 2");
            /**
            * // This is where the saved answers get re-initialized up onCreate() being called after an onDestroy() being called
            boolean[] mQuestionAnswerArray = savedInstanceState.getBooleanArray(QUESTION_LIST);
            //assert mQuestionAnswerArray != null;
            for(int i = 0; i < mQuestionBank.length; i++){
                mQuestionBank[i].setAnswered(mQuestionAnswerArray[i]);
            }
            */
        }

        Log.d(TAG, "QuestionTextView");
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        // This is a challenge that did not get commented out because it's hidden and kinda fun to use
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){ // Set an onClickListener to the TextView
            public void onClick(View v){ // Allows the user to change questions by clicking the text
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length; // Calculates index
                int question = mQuestionBank[mCurrentIndex].getTextResId();
                mQuestionTextView.setText(question);
            }
        });

        Log.d(TAG, "True Button");
        // BNR uses anonymous classes to keep things simple,
        // the new class is the listener(OnClickListener), and we pass its entire implementation to setOnClickListener
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkAnswer(true); // Calling the checkAnswer method to check the answer
                //Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show(); // This is how we make a toast, one way at least
            }
        });

        Log.d(TAG, "False Button");
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(QuizActivity.this, R.string.wrong_toast, Toast.LENGTH_SHORT).show()
                checkAnswer(false); // Same as above
            }
        });

        Log.d(TAG, "Next Button");
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){ // OnClickListener
            public void onClick(View v){
                Log.d(TAG, "Next Button listener");
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length; // Calculates question index
                //int question = mQuestionBank[mCurrentIndex].getTextResId(); // Uses index to extract res id and sets to question
                //mQuestionTextView.setText(question); // Uses question (the text res id), to set text to the textView
                mIsCheater = false;
                updateQuestion(); // Call to the update method and clean up code from inside the onClickListener
                /**
                setButtons(); // Update required to enable/disable buttons according to mCurrentIndex
                */
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button); // Allows the user to cheat if they want to
        mCheatButton.setOnClickListener(new View.OnClickListener(){ // If they click cheat then it starts a new activity to display the answer
            public void onClick(View v){
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent tent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue); // Calling the cheat intent method, this is how the info gets passed
                //startActivity(tent); // Starting the activity with the intent
                startActivityForResult(tent, REQUEST_CODE_CHEAT);// Starting activity with ForResult, allows info to be passed back to the parent activity
            }
        });

        Log.d(TAG, "Back Button");
        // This is a challenge but it does not change the main function of the app so I didn't comment it out,
        // I did however fix the crash problem with the back button, hence the extra logic below
        mBackButton = (Button) findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.d(TAG, "Back Button listener");
                if(mCurrentIndex == 0){
                    mCurrentIndex = 0;
                } else {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                    //int question = mQuestionBank[mCurrentIndex].getTextResId();
                    //mQuestionTextView.setText(question);
                    updateQuestion(); // Call to the update method to clean up code and further abstract our logic
                }
                /**
                setButtons(); // Update required to enable/disable buttons according to mCurrentIndex
                */
            }
        });

        updateQuestion(); // Call to the updateQuestion method
    }

    // This method handles decoding the results from the CheatActivity, this is how the activities communicate and "do things" based on that communication
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    /**
    // This method sets the buttons to enabled or disabled according to the question being answered or not
    // Part of the a challenge
    private void setButtons() {
        Log.d(TAG, "1st setButtons called");
       if (mQuestionBank[mCurrentIndex].getAnswered()){ // Simple check to see if the question at a particular index has been answered
           mTrueButton.setEnabled(false); // If yes it disables the buttons
           mFalseButton.setEnabled(false);
       }else{
           Log.d(TAG, "2nd setButtons called");
           mTrueButton.setEnabled(true); // Otherwise it enables the buttons
           mFalseButton.setEnabled(true);
        }
    }
    */

    private void updateQuestion(){ // Method that gets a question based on index and sets it to the TextView
        Log.d(TAG, "updateQuestion() called");
        int question = mQuestionBank[mCurrentIndex].getTextResId(); // Gets the Resource Id for the question at current index
        mQuestionTextView.setText(question); // Sets the question in the text view
        /**
        setButtons();
        */
    }

    // Method that checks if the user got the answer right or wrong
   private void checkAnswer(boolean userPressedTrue){
       boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue(); // Retrieves the boolean from the question and sets it to answerIsTrue

       int messageResId = 0;

       if(mIsCheater){
           messageResId = R.string.judgment_toast;
       } else {
           if (userPressedTrue == answerIsTrue) { // Logic to check if user answered correctly
               /**
                mQuestionBank[mCurrentIndex].setAnswered(true);
                Correct += 1;
                TotalAnswered += 1;
                */
               messageResId = R.string.correct_toast;
           } else {
               /**
                mQuestionBank[mCurrentIndex].setAnswered(true);
                TotalAnswered += 1;
                */
               messageResId = R.string.wrong_toast;
           }
         }
       /**
       setButtons();
       checkScore();
       */
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
   }

    /**
    // Challenge: This method checks if all the questions have been answered and if so calculates the score and displays a toast
    public void checkScore(){ // It can be modified to reset the quiz to be retaken
        if (TotalAnswered == mQuestionBank.length){
            Score = (Correct / mQuestionBank.length) * 100;
            String str = String.format("%.2f", Score);
            //R.string.quiz_score + Score
            Toast.makeText(this, "Your score is: " + str , Toast.LENGTH_LONG).show(); //Displays the players score, Challenge
        }
    }
    */

    // Overriding these methods to learn logging and stack tracing
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        Log.d(TAG, "onSaveInstanceState");

        /**
        // To save the answered questions upon onDestroy() method being called
         // Boolean array to store the true/false attributes (not to be confused with widget attributes), of the questions set to the length of question bank
        boolean[] mQuestionAnswerArray = new boolean[mQuestionBank.length];
        for(int i = 0; i < mQuestionBank.length; i++){
         // For loop iterates through question bank at given index and gets the answer and sets it in the array
            mQuestionAnswerArray[i] = mQuestionBank[i].getAnswered();
            Log.d(TAG, " inside for loop onSaveInstanceState");
        }
        savedInstanceState.putBooleanArray(QUESTION_LIST, mQuestionAnswerArray);
        */
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

}
