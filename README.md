# This mod is inspired by [Potion Fingers](https://github.com/VazkiiModsArchive/PotionFingers).
## This mod requires Baubles and AutoRegLib.
### This mod can make a single ring with multiple effects.
### For Modpack Creators
Basic Ring (no effects): <effectring:ring:0>

Ring with effect: <effectring:ring:0>.withTag({effects:[{"id":"EFFECT ID HERE","amplifier":EFFECT AMPLIFIER HERE}]})

e.g.

Ring with Night Vision(level:1): <effectring:ring:0>.withTag({effects:[{"id":"minecraft:night_vision","amplifier":1}]})

Ring with Regeneration(level:1) and Haste(level:1): <effectring:ring:0>.withTag({effects:[{"id":"minecraft:regeneration","amplifier":1},{"id":"minecraft:haste","amplifier":1}]})