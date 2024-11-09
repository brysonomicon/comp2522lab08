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

/**
 * A simple JavaFX-based quiz application that loads questions from a file and allows
 * the user to answer a limited set of questions, tracking their score along the way.
 */
public class QuizApp extends Application
{
    private static final double VBOX_SPACING    = 10.0;
    private static final double SCENE_WIDTH     = 400.0;
    private static final double SCENE_HEIGHT    = 300.0;
    private static final int NUM_QUESTION_PARTS = 2;
    private static final int NUM_QUESTIONS_PER_GAME = 10;
    private static final int QUESTION_INDEX = 0;
    private static final int ANSWER_INDEX   = 1;
    private static final int NONE = 0;

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

    /**
     * Initializes the quiz application components and fields.
     * Sets up the path for loading questions and the UI elements for user interaction.
     */
    public QuizApp()
    {
        questionsAndAnswers = new ArrayList<>();
        questionsPath = Path.of("resources/quiz.txt");
        questionLabel = new Text("Press 'Start Quiz' to begin.");
        scoreLabel    = new Text("Score: 0");
        messageLabel  = new Text();
        submitButton  = new Button("Submit");
        startButton   = new Button("Start Quiz");
        answerField   = new TextField();
        answerField.setPromptText("Enter answer here:");
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(final String[] args)
    {
        launch(args);
    }

    /**
     * Starts the JavaFX application, setting up the layout, loading questions,
     * and configuring the event handlers for the UI components.
     *
     * @param stage The primary stage for this JavaFX application.
     * @throws Exception if there is an issue starting the application.
     */
    @Override
    public void start(final Stage stage) throws Exception
    {
        final VBox vbox;
        final Scene scene;

        // Set up layout with UI components
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

        // Load questions into the questionsAndAnswers array
        loadQuestions();

        // Set event handlers
        startButton.setOnAction(click -> startQuiz());
        submitButton.setOnAction(click -> processAnswer());
        answerField.setOnKeyPressed(press ->
        {
            if (press.getCode() == KeyCode.ENTER)
            {
                processAnswer();
            }
        });

        // Set up the stage
        stage.setTitle("QuizApp");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads questions from a file into the questionsAndAnswers list.
     * Each question and answer pair is split by a pipe (|) delimiter.
     */
    private void loadQuestions()
    {
        try
        {
            final List<String> lines;

            lines = Files.readAllLines(questionsPath);

            for (final String line : lines)
            {
                final String[] qAndA;

                qAndA = line.split("\\|");

                if (qAndA.length == NUM_QUESTION_PARTS)
                {
                    questionsAndAnswers.add(qAndA);
                }
            }
            Collections.shuffle(questionsAndAnswers);
        }
        catch (final IOException e)
        {
            messageLabel.setText("Failed to load questions");
        }
    }

    /**
     * Starts a new quiz game by resetting the score, enabling input fields,
     * and displaying the first question.
     */
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

    /**
     * Processes the user's answer by comparing it with the correct answer.
     * Updates the score and displays feedback, then moves to the next question.
     */
    private void processAnswer()
    {
        final String userAnswer;
        final String correctAnswer;

        userAnswer = answerField.getText().trim().toLowerCase();
        correctAnswer = questionsAndAnswers.get(questionIndex - 1)[ANSWER_INDEX].toLowerCase();

        if (userAnswer.equals(correctAnswer))
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

    /**
     * Displays the next question to the user or ends the quiz if there are no more questions.
     */
    private void nextQuestion()
    {
        if (questionIndex < NUM_QUESTIONS_PER_GAME)
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

    /**
     * Ends the quiz by disabling input fields, showing the final score,
     * and allowing the user to start a new quiz.
     */
    private void endQuiz()
    {
        questionLabel.setText("Quiz finished. Final score is " + score + "/10");
        answerField.setDisable(true);
        submitButton.setDisable(true);
        startButton.setDisable(false);
        messageLabel.setText("Press 'Start Quiz' to begin.");
    }
}
