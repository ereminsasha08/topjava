package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.logging.Level;
import java.util.logging.Logger;


public class TImeRunRule implements TestRule {
    static private Logger logger = Logger.getLogger("TImeRunRule logger");

    @Override
    public Statement apply(Statement base, Description description) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long l = System.nanoTime();
                base.evaluate();
                long l1 = System.nanoTime();
                logger.log(Level.INFO, description.getTestClass().getSimpleName() + " TEST: \"" + description.getMethodName() + "\" Time run: " + (l1 - l) / 1000000 + "ms");
            }
        };
    }
}
