import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizApp
    extends Application
{
    private static final double VBOX_SPACING = 10.0;
    private static final double SCENE_WIDTH  = 400.0;
    private static final double SCENE_HEIGHT = 300.0;
    private static final int    NUM_QUESTION_PARTS     = 2;
    private static final int    NUM_QUESTIONS_PER_GAME = 10;
    private static final int    QUESTION_INDEX         = 0;
    private static final int    ANSWER_INDEX           = 1;
    private static final int    NONE = 0;

    private final List<String[]> questionsAndAnswers;
    private final Path      questionsPath;
    private final Text      questionLabel;
    private final Text      scoreLabel;
    private final Text      messageLabel;
    private final Button    submitButton;
    private final Button    startButton;
    private final TextField answerField;
    private int score;
    private int questionIndex;

    public QuizApp()
    {
        questionsAndAnswers = new ArrayList<>();
        questionsPath       = Path.of("resources/quiz.txt");
        questionLabel       = new Text("Press 'Start Quiz' to begin.");
        scoreLabel          = new Text("Score: 0");
        messageLabel        = new Text();
        submitButton        = new Button("Submit");
        startButton         = new Button("Start Quiz");
        answerField         = new TextField();
        answerField.setPromptText("Enter answer here:");
    }

    public static void main(final String[] args)
    {
        launch(args);
    }

    @Override
    public void start(final Stage stage)
            throws Exception
    {
        final VBox  vbox;
        final Scene scene;

        // add objects to the scene source
        vbox = new VBox(VBOX_SPACING,
                questionLabel,
                answerField,
                scoreLabel,
                messageLabel,
                submitButton,
                startButton);

        scene = new Scene(vbox, SCENE_WIDTH, SCENE_HEIGHT);

        answerField.setDisable(true);
        submitButton.setDisable(true);

        // load questions into the questionsAndAnswers array
        loadQuestions();

        // set event handlers
        startButton.setOnAction(click -> startQuiz());
        submitButton.setOnAction(click -> processAnswer());
        answerField.setOnKeyPressed(press ->
        {
            if(press.getCode() == KeyCode.ENTER)
            {
                processAnswer();
            }
        });

        // set the stage
        stage.setTitle("QuizApp");
        stage.setScene(scene);
        stage.show();
    }

    private void loadQuestions()
    {
        try
        {
            final List<String> lines;

            lines = Files.readAllLines(questionsPath);

            for(final String line : lines)
            {
                final String[] qAndA;

                qAndA = line.split("\\|");

                if(qAndA.length == NUM_QUESTION_PARTS)
                {
                    questionsAndAnswers.add(qAndA);
                }
            }
            Collections.shuffle(questionsAndAnswers);
        }
        catch(final IOException e)
        {
            messageLabel.setText("Failed to load questions");
        }
    }

    private void startQuiz()
    {
        score         = NONE;
        questionIndex = NONE;

        scoreLabel.setText("Score: " + score);
        messageLabel.setText("");

        startButton.setDisable(true);
        answerField.setDisable(false);
        submitButton.setDisable(false);

        Collections.shuffle(questionsAndAnswers);

        nextQuestion();
    }

    private void processAnswer()
    {
        final String userAnswer;
        final String correctAnswer;

        userAnswer = answerField.getText().trim().toLowerCase();
        correctAnswer = questionsAndAnswers.get(questionIndex - 1)[ANSWER_INDEX].toLowerCase();

        if(userAnswer.equals(correctAnswer))
        {
            score++;
            messageLabel.setText("Correct!");
        }
        else
        {
            messageLabel.setText("Wrong! The correct answer was: " + correctAnswer);
        }

        scoreLabel.setText("Score: " + score);
        nextQuestion();
    }

    private void nextQuestion()
    {
        if(questionIndex < NUM_QUESTIONS_PER_GAME)
        {
            final String[] qa;

            qa = questionsAndAnswers.get(questionIndex);
            questionLabel.setText(qa[QUESTION_INDEX]);
            answerField.clear();
            questionIndex++;
        }
        else
        {
            endQuiz();
        }
    }

    private void endQuiz()
    {
        questionLabel.setText("Quiz finished. Final score is " + score + "/10");
        answerField.setDisable(true);
        submitButton.setDisable(true);
        startButton.setDisable(false);
        messageLabel.setText("Press 'Start Quiz' to begin.");
    }
}
