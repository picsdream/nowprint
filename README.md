# nowprint
The Now Print SDK provides the world’s first mobile SDK to print creative images.

Grab the SDK via Gradle


    ```compile 'com.addazz.picsdream:nowprint:1.0.0'```

Or Maven


    ```<dependency>
      <groupId>com.addazz.picsdream</groupId>
      <artifactId>nowprint</artifactId>
      <version>1.0.0</version>
    </dependency>```


Get an instance of the SDK and initialise. Appropriate place to initialise the SDK is the onCreate() method of your Application class


    ```PicsDream.getInstance()
            .with(application)
            .returnBackActivity(MainActivity.class) 
            .runInSandboxMode(true)
            .initialize("app_key");```


    application : Application object eg. activity.getApplication()
    returnBackActivity : Activity to return when user completes a purchase eg. your homeactivity
    runInSandboxMode : Test SDK in sadbox mode. Payments are skipped in Sandbox mode.
    The SDK will always run in Sandbox mode if your API_KEY is not approved.


Launch the SDK using 


    ```PicsDream.getInstance().withImageUri(uri).launch(context);```

Be sure to initialise the SDK before you launch.

The SDK picks the `colorPrimary` (for highlights/buttons), `colorPrimaryDark` (for status bar) and `colorAccent` (for controls) from your application theme.
