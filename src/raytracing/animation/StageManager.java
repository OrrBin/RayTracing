package raytracing.animation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class StageManager {
    @Getter
    private int framesNumber;
    @Getter
    private int framesPerSecond;
    private final List<Transition> transitions = new ArrayList<>();
    private int currentFrame = 0;

    public void transitionScene() {
        transitions.forEach(transition -> transition.act(currentFrame));

        currentFrame++;
    }

    public void init(final int framesNumber, final int framesPerSecond) {
        this.framesNumber = framesNumber;
        this.framesPerSecond = framesPerSecond;
    }

    public void addTransition(final Transition transition) {
        this.transitions.add(transition);
    }

}
