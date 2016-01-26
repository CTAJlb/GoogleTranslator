package utils;

import org.jbehave.core.model.*;
import org.jbehave.core.reporters.StoryReporter;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.events.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Serhii_Pirohov
 */
public class AllureReporter implements StoryReporter {

    private Allure allure = Allure.LIFECYCLE;
    private final Map<String, String> suites = new HashMap<>();
    private String uid;

    public void beforeStory(Story story, boolean givenStory) {
        uid = generateSuiteUid(story);
        TestSuiteStartedEvent event = new TestSuiteStartedEvent(uid, story.getName());
        event.withLabels(AllureModelUtils.createTestFrameworkLabel("JBehave"));
        event.withTitle(story.getName());
        allure.fire(event);
    }

    public void afterStory(boolean givenStory) {
        allure.fire(new TestSuiteFinishedEvent(uid));
    }

    public void beforeScenario(String scenarioTitle) {
        allure.fire(new TestCaseStartedEvent(uid, scenarioTitle));
        allure.fire(new ClearStepStorageEvent());
    }

    public void beforeStep(String step) {
        allure.fire(new StepStartedEvent(step).withTitle(step));
    }

    public void successful(String step) {
        allure.fire(new StepFinishedEvent());
    }

    public void ignorable(String step) {
        allure.fire(new StepCanceledEvent());
    }

    public void notPerformed(String step) {
        allure.fire(new StepCanceledEvent());
    }

    public void failed(String step, Throwable cause) {
        allure.fire(new StepFinishedEvent());
        allure.fire(new StepFailureEvent().withThrowable(cause.getCause()));
        allure.fire(new TestCaseFailureEvent().withThrowable(cause.getCause()));
    }


    public void pending(String step) {
        allure.fire(new StepCanceledEvent());
        allure.fire(new TestCasePendingEvent().withMessage("PENDING"));
    }


    public void afterScenario() {
        allure.fire(new TestCaseFinishedEvent());
    }


    public void storyNotAllowed(Story story, String filter) {
        //TODO
        //Allure doesn't support this
    }


    public void storyCancelled(Story story, StoryDuration storyDuration) {

    }

    @Override
    public void narrative(Narrative narrative) {
        //TODO
        //Allure doesn't support this
    }

    @Override
    public void lifecyle(Lifecycle lifecycle) {
        //TODO
        //Allure doesn't support this
    }

    @Override
    public void scenarioNotAllowed(Scenario scenario, String filter) {
        //TODO
        //Allure doesn't support this
    }

    @Override
    public void scenarioMeta(Meta meta) {
        //TODO
        //Allure doesn't support this
    }

    @Override
    public void givenStories(GivenStories givenStories) {
        //TODO
        //Allure doesn't support this
    }

    @Override
    public void givenStories(List<String> storyPaths) {
        //TODO
        //Allure doesn't support this
    }

    @Override
    public void beforeExamples(List<String> steps, ExamplesTable table) {
        //TODO
        //Allure doesn't support this
    }

    @Override
    public void example(Map<String, String> tableRow) {
        //TODO
        //Allure doesn't support this
    }

    @Override
    public void afterExamples() {
        //TODO
        //Allure doesn't support this
    }

    @Override
    public void failedOutcomes(String step, OutcomesTable table) {

    }

    @Override
    public void restarted(String step, Throwable cause) {
        //TODO
        //Allure doesn't support this
    }

    @Override
    public void dryRun() {
        //TODO
        //Allure doesn't support this
    }

    @Override
    public void pendingMethods(List<String> methods) {
        //TODO
        //Allure doesn't support this
    }

    /**
     * Generate suite uid.
     *
     * @param story the story
     * @return the string
     */
    public String generateSuiteUid(Story story) {
        String uId = UUID.randomUUID().toString();
        synchronized (getSuites()) {
            getSuites().put(story.getPath(), uId);
        }
        return uId;
    }

    public Map<String, String> getSuites() {
        return suites;
    }

    @Override
    public void restartedStory(Story story, Throwable cause) {

    }
}