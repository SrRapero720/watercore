# WATERCoRE | THE SERVER CoRE
Plugins? nah, we cover you
Recreating all EssentialsX commands and popular Bukkit/Spigot/Paper plugins on this mod. adding a powerful API
and administration items

### ¿WHAT DO WATERCORE FOR ME?
- Fix a lot of MemoryLeaks like 
[MemoryLeakFix](https://github.com/fxmorin/MemoryLeakFix) mod but IN FORGE (read incompatibilities section).
- Provides a Lazy API to interact with Forge Registries and other mods APIs (Luckperms included)
- Randomly fix a lot of little issues from other mods.
- Adds a MixinTrace in your crash reports (like [CraftyCrashes](https://github.com/Chocohead/Crafty-Crashes) or [MixinTrace](https://github.com/comp500/mixintrace))
- Change language or mipmap level no stun the game.
- WIP: Connect Bukkit plugins with forge (only for devs)
- WIP: Make all widgets compatible with FancyMenu
- WIP: Replicated PlaceholderAPI


## Features
- Economy Items
- Modify the Players name format
- Customize chat format (With or without Luckperms)
- Customize players Join and Leave format
- Adds 3 dimensions for administrative purposes
- Add a lobby/spawn dimension (void)
- LuckPerms support
- MemoryLeak Fixes
- Language and MipMap no reloads all resources.


## Operator commands
- ``/broadcast <message>`` || Broadcast to all in your server using a fancy format 
- ``/setlobbyspawn`` || Set current position like server spawn position
- ``/spawnlobby`` || Go to lobby(void) dimension

## Players commands
- `/spawn` || Go to current spawn

## Incompatibilities
- Saturn (My mod do the same)
- Corail Tombstone (No support)
- FTB Ranks (breaks our chat format and player name format)
- MemoryLeakFix (My mod do the same)

## MemoryLeakFix Disclaimer
No, I no C&P MemoryLeakFix code or wrap my own port here. Before they release a forge version 
I ask [if can I make a port of the mod](https://github.com/fxmorin/MemoryLeakFix/issues/66) using the same name and keep the same
code (just porting stuff), and the owner says no, because forge maybe fix leaks, so I made my own fixes in forge. But currently they
release a Forge version... and no, I don't want to delete all my hard work because they release a forge version...

WATERCoRE do the same leak fixes for 1.18.2, if you have WATERCoRE you don't need that mod

***
<img style="display:block; width: 100%; height: auto; margin: 0 auto;" src="https://media.discordapp.net/attachments/1076151535291088916/1076656790986559538/WATERCoRE.png">

***

## SUPPORT AND DISCORD
- This mod is developed by ReplicatedStudios in association with WATERMiNE
- Need support for: Mod safe and trusts, Developer Contributor, Pull Request, API Access. [Join to my Discord](https://discord.gg/cuYAzzZ)

## FAQ
- This mod will be ported to Fabric when reach the highest potential (3.x.x)
- I only give partial support to Arclight and Magma servers (no other forge variations)
- Is no planned to add a Vanish command (uses Vanish Mod instead)
- Suggested (and accepted) features aren't warranted to be added
- Configuration files are located in your world folder (world/serverconfig)
- For developers: Go to Wiki :)
