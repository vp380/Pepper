package com.storm.pepper;

import android.util.Log;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.BodyLanguageOption;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.QiChatVariable;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.aldebaran.qi.sdk.util.FutureUtils;
import com.storm.posh.BaseBehaviourLibrary;
import com.storm.posh.plan.planelements.Sense;
import com.storm.posh.plan.planelements.action.ActionEvent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PepperIntroductionLibrary extends BaseBehaviourLibrary {
    private static final String TAG = PepperIntroductionLibrary.class.getSimpleName();

    private boolean humanReady;


    @Override
    public void reset() {
        super.reset();

        humanReady = false;
    }

    @Override
    public boolean getBooleanSense(Sense sense) {
        //        pepperLog.appendLog(TAG, String.format("Getting boolean sense: %s", sense));
        boolean senseValue;

        switch (sense.getNameOfElement()) {
            case "HumanReady":
                senseValue = humanReady;
                break;

            default:
                senseValue = super.getBooleanSense(sense);
                break;
        }

        pepperLog.checkedBooleanSense(TAG, sense, senseValue);

        return senseValue;
    }

    @Override
    public void executeAction(ActionEvent action) {
        pepperLog.appendLog(TAG, "Performing action: " + action);

        if (action.getNameOfElement() == currentAction) {
            // already performing this action
            pepperLog.appendLog(TAG, String.format("Action still in progress: %s", currentAction));
            return;
        }

        switch (action.getNameOfElement()) {

            case "IntroduceToHuman":
                introduceToHuman();
                break;

            default:
                super.executeAction(action);
                break;
        }
    }

    public void introduceToHuman() {
        pepperLog.appendLog(TAG, "Starts introducing to human");
        if (!humanPresent) {
            pepperLog.appendLog(TAG, "Cannot approach when no human present");
            return;
        } else {
            FutureUtils.wait(0, TimeUnit.SECONDS).andThenConsume((ignore) -> {
                Say say = SayBuilder.with(qiContext) // Create the builder with the context.
                        .withText("I am cool. Are you though?") // Set the text to say.
                        .withBodyLanguageOption(BodyLanguageOption.NEUTRAL)
                        .build(); // Build the say action.
                say.run();
            });
        }
    }
}
