# Construkt
 
Construkt is a plugin for Minecraft servers running Craftbukkit or Spigot. It allows a simple solution to building large shapes and areas with few commands. This plugin is being made for a Youtube series over on [my channel](https://www.youtube.com/c/Razz09).
 
## License

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

[Link to full license](LICENSE.md)

[GNU direct link](https://www.gnu.org/licenses/)

## Current Features
**(As of Episode 9)**

- Shape filling
- Shape clearing
- Shape replacement
- Filters
- Lagless area filling (or removal)
- Undo and Redo command
- Permissions
- Limitations
   - Volume limit
   - Axis limit
   - Permission for no limits
   - Permission based limit groups
   - Blacklisted blocks
   
The plugin is almost server ready. There are still some features missing and limitations I 
wish to add before a release version is built (but pre-release versions are available).

**Shapes**

- Cuboid
- Cylinder
   - Axis
   - Hollow
- Hollow Cuboid
   - Outline
- Line
- Overlay
   - Depth (1 - 10)
- Sphere
   - Hollow
- Terrain
   - Octaves (1 - 8)
   - Scale (1 - 10)
   - Seed
   - Random Seed
   - Smooth Edge
   - Smooth Height (0 - 100)
   - Smooth Distance
- Tree
   - Tree Type
- Copy (Early Development)
   - Paste Mode
   
**Filters**

- Checkered
   - Invert
   - Size (1 - 20)
- Noise
   - Octaves (1 - 8)
   - Scale (1 - 10)
   - Limit (1 - 99)
   - Invert
   - Seed
   - Random Seed
   - Smooth Edge
   - Smooth Distance
- Random
   - Chance (1 - 99)
- Slice
   - Axis (x, y, or z)
   - Percent (1, 99)
   - Invert
   
## Commands

- `/construkt` *OR* `/ckt`
   - Toggles build mode on or off.
- `/ckt <shape>`
   - Selects the shape to build in
- `/ckt parameter <parameter> [value]`
   - Sets a value for a shapes parameter
   - Lists the details of the parameter if no value is entered
- `/ckt filter add <filter>`
   - Adds a filter to alter a shape
- `/ckt filter remove <filter>`
   - Removes a filter
- `/ckt filter clear`
   - Clears all filters
- `/ckt filter parameter <parameter> [value]`
   - Sets a value for a filters parameter
   - Lists the details of the parameter if no value is entered
- `/construktundo` *OR* `/cktundo` *OR* `/cktu`
   - Undo a change made with Construkt
- `/construktredo` *OR* `/cktredo` *OR* `/cktr`
   - Redo a previous undo made with Construkt


All commands will come up with tab completion to help. No other in game help information is currently available.
