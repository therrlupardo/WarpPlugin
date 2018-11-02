# WarpPlugin

Bukkit plugin, which allows user to create "warps" - locations, to which player can easly teleport.

### Installation
Just put WarpPlugin.jar into plugins folder and reload your server.

### Usage

Plugin supports following commands:

* ``` setwarp [warp_name]``` - creates warp "warp_name" in current user location.

* ``` warp [warp_name]``` - teleports user to warp "warp_name", if it exist for that user. 

* ``` delwarp [warp_name]``` - deletes warp "warp_name"

* ``` warplist ``` - shows list of available warps for current user.

* ``` warprename [warp_to_rename] [new_name] ``` - renames warp "warp_to_rename" to "new_name"