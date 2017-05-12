package com.example.tomahawk.geoquiz;

/**
 * Created by Tomahawk on 4/16/2017.
 */

public class Question { // Question objects

    private boolean mAnswered;
    private int mTextResId; // Member variable int
    private boolean mAnswerTrue; // Member variable bool

    public int getTextResId() { // Getter/Accessor
        return mTextResId;
    }

    public void setTextResId(int textResId) { // Setter/Mutator
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() { return mAnswerTrue; } // Getter/Accessor

    public void setAnswerTrue(boolean answerTrue) { // Setter/Mutator
        mAnswerTrue = answerTrue;
    }

    public boolean getAnswered() { return mAnswered; }

    public void setAnswered(boolean answered){ mAnswered = answered; }


    public Question(int textResId, boolean answerTrue){ // Constructor of Question object
        mTextResId = textResId; //Is an int, holds the id for the string resource
        mAnswerTrue = answerTrue; //I think this is self explanatory, and, it holds a boolean thus completing the question object. For now.
        mAnswered = false; // Added variable to keep track if questions have been answered or not
    }


}
