I created an app to convert MapTools Basic generic Tokens into DandDads Tokens
This little java app reads a source that is either :
A driectory where a MapTools cmpgn file has been extracted
A MapTools campaign (foo.cmpgn) file.
It reads in the content.xml, an wherever there is a "net.rptools.maptool.model.Token" Element with "Basic" properties, it replaces the Basic properties with the "NPC" properties from the DandDads ruleset.
Actually, it replaces the Basic property set with any propertyMap you define, so if you want to create a Monster propertyMap, a or a Townsfolk propertyMap, you just select that  as the "Replacement XML file:" in the GUI. The limitation is that currently, it is looking to replace only in tokens with The "Basic" propertyType. You can cool at the example "danddads_properties_npc_civilian.xml" to see the default set, that I based on Dusty's "ShieldMan"

The Output is a directory called "danddads_processed", with the new content.xml file, which you can edit further. It also produces a dnddads.cmpgn, which you can open in MapTool.
So it if you wanted to, you can create some Tokens in MapTool, save your campaign, run the TokenProcessor on the "cmpgn" file, and open the output, without messing around with any other editors or anything. However it would probably make more sense to rename the inputs "cmpgn" files before and after processing to minimize confusion.
