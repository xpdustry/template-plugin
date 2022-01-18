# TemplatePlugin

[![Jitpack latest version](https://jitpack.io/v/fr.xpdustry/TemplatePlugin.svg)](https://jitpack.io/#fr.xpdustry/TemplatePlugin)
[![Build status](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/Xpdustry/TemplatePlugin/actions/workflows/build.yml)
[![Mindustry 6.0 | 7.0 ](https://img.shields.io/badge/Mindustry-6.0%20%7C%207.0-ffd37f)](https://github.com/Anuken/Mindustry/releases)

## Description

This template features some cool stuff such as:
- [Jitpack](https://jitpack.io/) support.
- Gradle tasks for testing:
  - `gradlew moveJar` Move the output jar to your server mod directory.
  - `gradlew runServer` Start the server in a new cmd.
- GitHub action for easier release and Jitpack usage:
   - You just have to run the `Release` workflow manually,
     it will automatically take the plugin version in your plugin.json file and upload the jar.
   - Changelog support: edit `CHANGELOG.md` before each release, it will be used as the release text.

When using this template, don't forget to change `plugin.json` and `gradle.properties`.

## Building

- `./gradlew jar` for a simple jar that contains only the plugin.
- `./gradlew shadowJar` for a fatJar that contains the plugin and its dependencies (use this for your server).

Thank you for using this template !
