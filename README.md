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
Mostly simple chat commands.

| **Command**   |  **Arguments:**   |             **Description:**              |
| :------------ |:-----------------:| :----------------------------------------:|
| about         |   ----            | Prints basic information about the bot. |
| help          |   ----            | Sends private message with list of available commands. |
| joke          | name or mention   | Makes Chuck Noris joke with mentioned name.|
| bigtext       | text              | Replaces normal letters with regional indicators to make given text huge.|
| roll          | number of sides   | Rolls the dice.             |
| profpic       | mention           | Provides link to profile picture of the user. |
| 8ball         |   ----            | Magic 8ball! |

### Images:
NSFW commands only work on servers with nsfw setting enabled, and on channels marked as nsfw.
Results are displayed as single embed with controlls.

On SFW servers NSFW commands are fully hidden and do not produce a response.

| **Command**   | **NSFW?** |  **Arguments:**   |             **Description:**              |
| :------------ |:---------:|:----------:| :----------------------------------------:|
| e621          |  **yes**  |  tags      | Searches for up to 100 pictures on e621. |
| e926          |   *no*    |  tags      | Searches for up to 100 pictures on sfw version of e621 - e926. |
| dan           |  **yes**  |  tags      | Searches for up to 100 pictures on danbooru.|
| gel           |  **yes**  |  tags      | Searches for up to 100 pictures on gelbooru.|
| safe          |   *no*    |  tags      | Searches for up to 100 pictures on safebooru.|
| poke          |  **yes**  |  tags      | Searches for up to 100 pictures on AGNPH. |
| r34           |  **yes**  |  tags      | Searches for up to 100 pictures on r34.xxx.|

### Admin:
Administrative commands, some restricted only to bot owner due to possible abuse.
Remaining commands assume: Moderators can kick members, Admins can ban them.

| **Command**   | **Owner Only** |  **Arguments:**   |             **Description:**              |
| :------------ |:--------------:|:----------:| :----------------------------------------:|
| stats         |  **yes**       |  ----      | Prints backed stats of the bot (ping,mem,etc.) |
| vote          |   *no*         | time topic | Starts a vote lasting specified amount of time, about given topic.|
| info          |   *no*         |  mention   | Displays information about server member mentioned. |
| guildcfg      |   *no*         |  key value | Sets per-guild settings. When ran without arguments, it will print available options, and their values.|
| eval          |  **yes**       |  code      | Directly runs code on the bot. Best only used for debugging.|
| botban        |   *no*         |  mention   | Bans user from using any bot commands on the server.|
| botunban      |   *no*         |  mention   | Removes command ban from user. |
| shutdown      |  **yes**       |  ----      | Shuts down the bot.|

### Music:
Bot joins same voice channel as user who ran the command.
Music commands are only accepted from dedicated channel to prevent spamming main text channels, and can be set in guild configuration (defaults to any channel with "bot" in name).

| **Command**   |  **Arguments:**   |             **Description:**              |
| :------------ |:-----------------:| :----------------------------------------:|
| play          | search or url     | Searches for track and adds it to queue. |
| playnext      | search or url     | Same as above, but track is forced to be next in queue. |
| playshuffle   | search or url     | Same as play, but queue gets shuffled after track is added.|
| volume        | volume (0-1000)   | Sets playback volume for current player.|
| seek          | timestamp         | Skips playback to given timestamp if possible.|
| skip          |   ----            | Skips currently played track. |
| stop          |   ----            | Skips entire queue and stops playback. |
| shuffle       |   ----            | Shuffles the playback queue. |
| queue         |   ----            | Prints current queue for the server. |
| mreset        |   ----            | (debug) Completely resets the player object. |

### Lewd:
All commands are classified as NSFW as such only work on servers with nsfw flag + nsfw channel.
On SFW servers NSFW commands are fully hidden and do not produce a response.

| **Command**   |  **Arguments:**   |             **Description:**              |
| :------------ |:-----------------:| :----------------------------------------:|
| picstat       | mention           | Prints image command usage statistics of mentioned user. |
