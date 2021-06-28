# TemplatePlugin

## Description

Template stolen from **Anuken/ExamplePlugin** lol...

This template features some cool stuff such as:
- [Jitpack](https://jitpack.io/) support.
- Gradle tasks for testing:
  - `gradlew install` Move the output jar to your server mod directory.
  - `gradlew run` Start the server in a new cmd.
  - `gradlew deploy` Executes `install` and `run`.
- GitHub action for easier release and Jitpack usage:
   - You just have to run the `Release` workflow manually,
     it will automatically take the plugin version in your plugin.json file and upload the jar.

## Tips and nice things to know

- For faster testing, I recommend you to add an exit statement at the end of your startup script such as:

`run_server.bat` 
```batch
@echo off
java -jar server.jar
exit
```

`run_server.sh`
```shell
#!/usr/bin/env bash
java -jar server.jar
exit
```

- If you want to use the gradle tasks, don't forget to change the server path in `build.gradle`.
- The plugin compiles to java 8 for compatibility reasons but nothing keeps you to change the compiler target or source to a higher jdk.
