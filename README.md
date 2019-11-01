# FurbotNeo

An old discord bot made using Java, filled with references and "features".

Mostly learning ground for writing in java it holds basic functionality, as well as music playback and imageboard searches.

By default operates in SFW mode restricting access to more risky commands.
To change it use command ``!guildcfg nsfw true`` (replace "!" with used prefix).

It uses database to track statistics and per guild configs, in varying state of use or completion.

There is no proper documentation of the code, shamefully commented version of code got lost in transision between pcs.

If this code helps you learn something that's great :3
And if you think its really bad.. well we all have to start somewhere, and skilled advice will be appreciated!


Compilation instructions: 
_Use maven_

Run this command in project root directory:
```bash
mvn package
```
Output .jars will be placed in ``target`` directory.
