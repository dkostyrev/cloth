package rx.plugins;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Created by Dmitry Kostyrev on 12/10/15
 */
public class RxJavaSchedulersHookResetRule implements TestRule {
    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                resetPlugins();
                RxJavaPlugins.getInstance().registerSchedulersHook(new TestRxJavaSchedulersHook());
                RxAndroidPlugins.getInstance().registerSchedulersHook(new TestRxAndroidSchedulersHook());

                base.evaluate();

                resetPlugins();
            }
        };
    }

    private void resetPlugins() {
        RxJavaPlugins.getInstance().reset();
        RxAndroidPlugins.getInstance().reset();
    }

    private class TestRxAndroidSchedulersHook extends RxAndroidSchedulersHook {
        @Override
        public Scheduler getMainThreadScheduler() {
            return Schedulers.immediate();
        }
    }

    private class TestRxJavaSchedulersHook extends RxJavaSchedulersHook {
        @Override
        public Scheduler getIOScheduler() {
            return Schedulers.immediate();
        }

    }

}
