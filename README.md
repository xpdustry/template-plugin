# TemplatePlugin
 
Template stolen from **Anuken/ExamplePlugin**.

This template also features some cool stuff such as:
- [Jitpack](https://jitpack.io/) support
- Gradle tasks for testing:
  - `gradlew install` Move the output jar to your server mod directory
  - `gradlew run` Start the server in a new cmd
  - `gradlew deploy` Executes `install` and `run`
- GitHub action for easier release and Jitpack usage
   - You just have to run the `Release` workflow manually, it will automatically take the plugin version in your plugin.json file and upload the jar
    
For faster testing, I recommend you to add an exit statement at the end of your `run_server.bat` such as:

```bat
@echo off
java -jar server.jar
exit
```

If you want to use the gradle tasks, don't forget to change the server path in `build.gradle`
