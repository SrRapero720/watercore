# WATERCoRE | THE SERVER CoRE
Plugins? nah, we cover you
Recreating all EssentialsX commands and popular Bukkit/Spigot/Paper plugins on this mod. adding a powerful API
and administration items

### ¿WHAT DO WATERCORE FOR ME?
- Fix a lot of MemoryLeaks like 
[MemoryLeakFix](https://github.com/fxmorin/MemoryLeakFix) mod but IN FORGE (read incompatibilities section).
- Provides a Lazy API to interact with Forge Registries and other mods APIs (Luckperms included)
- Randomly fix a lot of little issues from other mods.
- Adds a MixinTrace in your crash reports (port wrapped of [CraftyCrashes](https://github.com/Chocohead/Crafty-Crashes) and [MixinTrace](https://github.com/comp500/mixintrace))
- Change language or mipmap level no stun the game.
- Some no editable widgets are now compatible with FancyMenu
- ~~WIP: Replicated PlaceholderAPI~~ (I am doing a different mod for it)


## Features
- Economy Items
- Admin Tool items (Ban hammer, Extra potions, Lightning wand)
- Modify the Players name format
- Customize chat format (With or without Luckperms)
- Customize players Join and Leave format
- Adds 3 dimensions for administrative uses
- Add a lobby/spawn dimension (void)
- LuckPerms support

## Operator commands
- ``/broadcast <message>`` || Broadcast to all player in your server using text format 
- ``/broadcast-raw <message>`` || Broadcast to all players in your server without prefix and using text format
- ``/setlobbyspawn`` || Set your current position as respawn position
- ``/setworldspawn`` || Set your current position as first worldspawn position
- ``/watercore <spawn | lobby>`` || Go to lobby (void) dimension
- ``/watercore back``|| Go back to other player's last position

## Players commands
- `/spawn` || Go to current spawn
- `/back [number]` || Go back to your last position

## Other mods fixes
- [SimpleLogin](https://github.com/SeraphJACK/SimpleLogin/issues/43#issuecomment-1484375642) incompatibility with FANCYMENU

## Special Compatibilities
- [Vanishmod](https://github.com/RedstoneDubstep/Vanishmod) - Uses configured Join/Leave message format
- [FancyMenu](https://github.com/Keksuccino/FancyMenu) - Utility placeholders added

## Incompatibilities
- Saturn (My mod do the same)
- Corail Tombstone (No support)
- FTB Ranks (breaks our chat format and player name format)
- [VMP-Forge](https://github.com/SrRapero720/VMP-forge) (only with WATERCoRE 1.3 and below)
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

## FA~~Q~~
- This mod will be ported to Fabric when reach the highest potential (3.x.x)
- I only give partial support to Arclight and Magma servers (no other forge variations)
- Is no planned to add a Vanish command (uses Vanish Mod instead)
- Suggested (and accepted) features aren't warranted to be added
- Configuration files are located in your world folder (world/serverconfig)
- For developers: Go to Wiki :)
- No allowed respost of the mod
- No planned/wanted support for Quilt (just fabric)
