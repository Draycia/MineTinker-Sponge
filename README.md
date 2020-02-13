# MineTinker-Sponge  
Release builds can be found [here](https://ore.spongepowered.org/Vicarious/MineTinker-Sponge/versions).

#### Overview
MineTinker is an enchanting overhaul and tool leveling system.
  
Instead of enchanting gear, you modify it with modifiers.  
Modifiers can be crafted or obtained via staff commands.

Currently all vanilla enchantments are supported (the corresponding modifiers apply them internally).  
There are also custom modifiers, and an API to add more via addon plugins.

If anyone wants support for any specific mods / plugins feel free to reply to the discussion or make an issue on github.

If you have [TeslaPowered](https://ore.spongepowered.org/Simon_Flash/TeslaPowered) installed, the plugin will auto generate a GUI to show modifiers and their recipes in `/mt mods`.

Items existing before installation of this plugin must be converted with `/mt convert`.

### Getting started
Upon crafting any vanilla tool or armor, it'll automatically be MTS compatible.  
With default settings, all items start off with 1 modifier slot.  
Each modifier has a recipe by default but recipes are not required!  
Once you've picked a mod and crafted it, you can apply it to your item.

#### Applying modifiers to items
To apply modifiers to items you must hold the item in your off-hand, the modifier item in your main-hand, and right click a bookshelf.  
Each modifier applied takes up a modifier slot.

#### More about slots and item leveling
Items gain one slot each time you level up the item.  
As you dig blocks, attack mobs, etc, your item will gain XP.
Once it gains enough XP it'll level up and gain another slot.

#### Options
All changes to gameplay like disabling enchanting tables, mobs dropping MTS loot, etc, can be disabled in the plugin's configs.
Each modifier can be enabled/disabled individually, their recipes changed, their max levels changed, name, etc.

### Mixins
The plugin utilizes mixins in order to implement some of its functionality.  
Currently, the only change is to LootTables, to convert generated loot.  
If you'd like to opt out of this, you can set `convertLootTables` to `false` in the config.

### TODO:
- User configurable list of actions to give XP for each item group (pickaxes, swords, etc)
- Option to have mobs randomly drop a (configurable) list of modifiers with drop chances
- Allow modifiers to require other modifiers to be applied first or be at a specific level

### Screenshots
Sword after being crafted:  
![](https://i.imgur.com/hedmPoC.png)  
Sword with mods applied:  
![](https://i.imgur.com/FQwgUoh.png)
