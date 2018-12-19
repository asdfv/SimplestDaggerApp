package by.grodno.vasili.simplestdaggerapp;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjection;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    Dependency dependencyOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView label = findViewById(R.id.label);
        String text = dependencyOne.getName();
        label.setText(text);
    }
}

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, ActivityBuilder.class})
interface AppComponent {

    void inject(App app);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
}

@Module
class AppModule {
    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

}

@Module
abstract class ActivityBuilder {
    @SubScope
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();
}

@Module
interface MainActivityModule {
    @Binds
    @SubScope
    Dependency provideDependencyOne(DependencyOne dependencyOne);
}

interface Dependency {
    String getName();
}

@SubScope
class DependencyOne implements Dependency {
    @Inject
    DependencyOne() {
    }

    @Override
    public String getName() {
        return "DependencyOne!!!";
    }
}
