Goldfish
========

The Instance System

-=(Getting Started)=-

The first thing you want to do is to create your instance prototype. A prototype is the base map that can be edited 
that will affect all future copies (instances) of that prototype.

If you would like to load a pre-existing map as your prototype, throw it in the plugins/Goldfish/prototypes/ folder, then 
type '/goldfish prototype create [map name]' (You can type 'pt' instead of 'prototype' or 'instance' instead of 'goldfish')

If no map exists in the specified folder, a new map will be created. To teleport to your new map, simply type 
'/goldfish prototype warp [map name]', or use a world teleporting method of your choice, but do keep in mind that the 
full names of all prototype maps include the 'plugins/Goldfish/prototypes/' prefix.

In order to activate your prototype, an entrance and an exit must be assigned first. While outside your prototype, type 
'/goldfish prototype entrance [map name]' to assign the entrance, and while in your prototype, simply type 
'/goldfish prototype exit' without the map name to assign the exit.

Once the entrance and exit is assigned, you can activate/deactivate your prototype at any time by typing 
'/goldfish prototype activate [map name]'. That's it, you're done! It's good practice to also update your prototype 
by typing '/goldfish prototype update [map name]' as well, as this saves your meta data associated with the prototype.

If at any time you want to delete your prototype, use the command 'goldfish prototype delete [map name]', this will save 
you the trouble from having to stop the server first to get rid of all the associated files.

-=(Making an instance)=-

When within' 10 blocks of a prototype entrance, type '/goldfish enter', and you will automatically be transported to the 
associated exit. By default, this instance will last until un-populated for 60 seconds. Anything done to this instance 
will not change the prototype. To leave the instance, type '/goldfish leave' near the exit, and it will transport you to 
the entrance.

Multiple entrances/exits are configurable in the prototypedata config file. Read the next section for more details.

-=(Customizing Your Prototype)=-

In the plugins/Goldfish/prototypes/[map name] folder, you'll find a file called 'prototypedata.yml'. This will contain 
a wide assortment of properties specific to that prototype.

The parameters include:

(Limit)

This will limit the map size from one corner to another into any rectangle shape you desire. This will simply stop the 
chunks from loading beyond this contained space, though it is recommended that you limit map traversing via other means.

One such means is to make certain blocks unbreakable, and place them at the perimeter of your map to creatively limit 
the playable space.

A couple things to note here: chunk [0, 0] is a finnicky beast, and won't properly load unless you have at least 20 chunks 
loaded on each side of it. On a similar note, make sure at least 4 chunks are loaded on each side of each potential spawn 
point as well.

Remember that you can press F3 in-game to find your chunk coordinates. Each chunk is 16x16 blocks, and 128 blocks tall.

(Block Permissions)

This parameter will set the permissions of which block IDs are placeable/breakable/useable. If 'allowall' is set to true, 
only the item ids set to false will not be allowed. If 'allowall' is set to false, only the item ids set to true will 
be allowed.

(Respawn)

This will determine if you want to respawn the player at a specific location. If 'respawn: instance' is set to true, then 
the player will respawn at the given location. If set to false, then the player will spawn as normal.

'respawn: counter' is how many times a player can respawn before the instance is deleted. If the respawn location is 
inside the instance, then the player will be booted to the default entrance location instead.

(Instance Timer)

This is a timer that will tick down the second the instance is created. Once the timer hits 0, the players will be booted 
to the default entrance and the instance deleted. You can specify whether the timer keeps ticking down while the instance 
is un-populated with the 'timer: activewhenempty' parameter. Amount set to 0 means 'off'.

(Timeout Timer)

This timer will only tick down when un-populated. Once it hits 0, the instance will be deleted and players kicked to the 
entrance. Amount set to 0 means 'off'. It's also worth noting that if the server disconnects for any reason, this as well 
as the instance timer will deactivate until a player associated with the instance logs back on.

(Static Instance)

If the instance is set to static, then only one instance can exist at a time. All players will enter that same instance, 
and the instance will otherwise act as it normally would. Useful for controlling map resets.

(Equipment)

There are two parameters here, 'store' and 'booty'. If 'store' is set to true, then all items the player enters the instance 
with will be saved and returned to him on exit. If 'booty' is set to true, then all items earned in the instance will be 
dropped at the player's feet when he exits the instance.

If 'store' is set to false, then the player will enter with the equipment they have with them, and will leave with whatever 
they have on them regardless of whether 'booty' is set to true or not.

(Entrances/Exits)

Here you can create as many entrances/exits as you want that don't have the limitations associated with assigning them 
in-game. It is recommended you leave entrance1 and exit1 alone, as Goldfish will use them as mandatory defaults. Make sure 
to number entrances/exits in consecutive order, if you have an entrance4 but no entrance3, it will be ignored.

(Condition)

This will make instance entry conditional. If 'allow' is set to false, then no entry will be admitted at all.
If 'gametime' is set to true, then the player can only enter the instance between the specified times. Note that this 
plugin assumes a 2400 clock, where 600 signifies 6:00 AM (Minecraft dawn) and 2200 signifies 10:00 PM (Minecraft dusk)

'servertime' is similar but depends on what time the server clock is set at. It also uses the 2400 clock.

'itemrequire' means you need a certain amount of a specific item ID in your inventory before it will allow entry.
The number to the left of the colon is the itemID, and the value to the right of the colon is the amount required.