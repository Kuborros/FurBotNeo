<!-- Badges -->
[![GitHub issues](https://img.shields.io/github/issues/Kuborros/FurbotNeo?style=flat-square)](https://github.com/Kuborros/FurbotNeo/issues)
[![GitHub forks](https://img.shields.io/github/forks/Kuborros/FurbotNeo?style=flat-square)](https://github.com/Kuborros/FurbotNeo/network)
[![GitHub stars](https://img.shields.io/github/stars/Kuborros/FurbotNeo?style=flat-square)](https://github.com/Kuborros/FurbotNeo/stargazers)
[![GitHub license](https://img.shields.io/github/license/Kuborros/FurbotNeo?style=flat-square)](https://github.com/Kuborros/FurbotNeo/blob/master/LICENSE)
# FurbotNeo

An old discord bot made using Java, filled with references and "features".

Mostly learning ground for writing in java it holds basic functionality, as well as music playback and imageboard searches.

By default operates in SFW mode restricting access to more risky commands.
To change it use command ``!guildcfg nsfw true`` (replace "!" with used prefix).

It uses database to track statistics and per guild configs, in varying state of use or completion.

There is no proper documentation of the code, shamefully commented version of code got lost in transision between pcs.

If this code helps you learn something that's great :3
And if you think its really bad.. well we all have to start somewhere, and skilled advice will be appreciated!





## Compilation instructions: 

Minimum JDK version required is **11**. (Current LTS release.)

_Use maven_ (Should be avaible in your distribution repositories.)

For simple compilation and package into .jar archives, run this command in project root directory:
```bash
mvn package
```
Output .jars will be placed in ``target`` directory.

Project is set so it can be ran directly without repackaging with 

```bash
mvn exec:java
```

## Supported chat commands:

### General:

| **Command**   |  **Arguments:**   |             **Description:**              |
| :------------ |:-----------------:| :----------------------------------------:|
| joke          | name or mention   | Makes Chuck Noris joke with mentioned name.|
| bigtext       | text              | Replaces normal letters with regional indicators to make given text huge.|
| roll          | number of sides   | Rolls the dice.             |
| profpic       | mention           | Provides link to profile picture of the user. |
| 8ball         |   ---             | Magic 8ball! |
