Specification of 1.6.2 Rev1 and later sound settings Little Maid Mob


By changing the description of the cfg file in littleMaidMob,
It is possible to play on the sound of any voice that is associated with each action.
In addition, various kinds of sound is possible to set a texture pack, made in each color.
I will explain how to write a sound settings that are set in the cfg file in this text.

Note for the location of the resources have changed since 1.6.2.


Available audio format
	1.6.2 client currently available formats are as follows.
		. Ogg Ogg.Vorbis

		※ I have described the format codec confirmed.


Voice-defined variables
	string starting there in the first half of the cfg file with a '#' was written about the function of each variable.
	It is possible to describe the sound settings that are associated with the various operations in the variable starting with "se_".

	

Audio Settings string
	How to set the voice that can be written in "se_" are as follows.

	• Example 1
		se_living_daytime = mob.ghast.moan
			In the case of this description, is included in the AppData% / .mincraft / assets / sound / mob / ghast%,
			moan1.ogg ~ moan7.ogg will play at random.

	• Example 2
		se_living_daytime = littleMaidMob.live_d
			In the case of this description, is included in the AppData% / .mincraft / assets / sound / littleMaidMob%,
			live_d?. ogg will be played at random.
			(The number of 1-9 is?)

	• Example 3
		se_living_daytime = mob.ghast.moan
		se_living_night = ^
			In the case of this description, you will in the same manner as in Example 1 for se_living_daytime,
			The contents of se_living_night will also be similar to the se_living_daytime.
			You can set it to the same content as the setting value high priority internally by describing the "^".



Texture specifies that
	By describing the specification plus separated, textures specify that by "," audio settings string above,
	It is possible to define a sound corresponding texture pack, made in color.

	· How to Write
		Texture pack name; maid color; voice: delimiter; is "".
	

	Texture pack name
		It becomes the name of the texture pack being loaded.
		The delimiter of the folder name that contains the texture exactly
		It will be interpreted as an ".".
		It applies to the texture All packs if you do not enter a value.
			Texture pack directory name
			/ Mob / littleMaid / ALTERNATIVE /: ALTERNATIVE
			/ Mob / littleMaid / okota / Hituji /: okota.Hituji

	Made color
		I will be a decimal number from 0 to 15.
		Please refer to the corresponding texture for each color.
		It applies to all colors of the texture pack if it is omitted or set to "-1".


	And audio
		A description same as that described in the audio settings string.


	• Example
		se_living_daytime = mob.ghast.moan, okota.Hituji; -1; littleMaid.live_d,; 3; littleMaid.livealt_d
			The default voice: mob.ghast.moan
			okota.Hituji Pack: littleMaid.live_d
			LittleMaid.livealt_d: color 3 pack of all

	· Priority
		Texture pack + color specified> texture pack + all the colors specified> color specified> default
		It refers to the value in the order of the above, please be careful to do any complicated settings.


Sound Pack
	I is compatible with sound pack from b1.8.1-4.
	It should also be noted for location of resources have changed since 1.6.2.
	It is included in the "AppData% / .mincraft / assets / sound / littleMaidMob /%"
	You can set the voice reads the configuration file.

	How to write configuration file is similar to that of the normal cfg file,
	Some rules have been added.


	· How to Write
		Made color; voice: delimiter; is "".
		Audio: maid color specification is optional.

	Texture pack name
		It becomes the name of the texture pack configuration file name is set.
			Texture pack name sound pack name
			ALTERNATIVE: ALTERNATIVE.cfg
			okota.Hituji: okota.Hituji.cfg

	Special configuration file
		If there is a thing of "littleMaidMob.cfg" the file name,
		The contents of this will be loaded in place of the current audio settings in the cfg file normal.

	· Priority
		If there is a littleMaidMob.cfg, it will be applied instead of the audio settings of mod_littleMaidMob.cfg.
		After this, the setting of the sound pack is loaded, I will override the settings of each.

	It is similar to that of the cfg file other than the above.



Description
	Sound pack cfg
		Damage is the voice: se_hurt.
		It is a voice when I hit the snow ball: se_hurt_snow.
		It is the voice of when damaged by fire: se_hurt_fire.
		It is a voice at the time of the attack can guard: se_hurt_guard.
		It is a voice when subjected to falling damage: se_hurt_fall.
		It is the voice of when there is no damage, even under attack: se_hurt_nodamege.
		death is the voice: se_death.
		is an attack voice: se_attack.
		It is the voice of attack when you are hungry for blood: se_attack_bloodsuck.
		It is the voice of shooting those requiring reservoir: se_shoot.
		It is the voice of fire those that do not require a reservoir: se_shoot_burst.
		It is the voice of when I posed a fire weapon: se_sighting.
		It is the voice of the child when the system is Bloodsucker defeat the enemy: se_laughter.
		It is the voice of when you ingest sugar: se_eatSugar.
		It is a voice when I eat sugar strength until the maximum: se_eatSugar_MaxPower.
		It is the voice of the contract: se_getCake.
		It is the voice of re-contract: se_Recontract.
		It is a voice when you have added fuel to the furnace: se_addFuel.
		It is a voice when you turn on the material in furnace: se_cookingStart.
		It is a voice when I take out the finished product from the furnace: se_cookingOver.
		It is the voice of when you restore the main: se_healing.
		It is the voice of the potion when using: se_healing_potion.
		It is a voice when you have installed the torch: se_installation.
		It is the voice of when to collect the snow: se_collect_snow.

		It is a normal voice when you have found the enemy body: se_findTarget_N.
		It is the voice of the child when the Bloodsucker system found enemy: se_findTarget_B.
		It is a voice when you have found the item: se_findTarget_I.
		It is a voice when you have found the gloom: se_findTarget_D.
		It is the voice of TNT-D upon activation: se_TNT_D.

		It is normal 啼声: se_living_daytime.
		It is 啼声 morning when you have a watch: se_living_morning.
		It is 啼声 night when you have a watch: se_living_night.
		It is 啼声 when you are weak: se_living_whine.
		It is the voice of defunct when it is raining: se_living_rain. Priority is higher than the change over time.
		It is the voice of defunct when the snow is falling: se_living_snow. Priority is higher than the change over time.
		It is the voice of defunct when you are in the cold biomes: se_living_cold.
		It is the voice of defunct when you are in hot biomes: se_living_hot.
		It is a greeting of good morning when you have a watch: se_goodmorning.
		This is the greeting of the day off when you have a watch: se_goodnight.

		I set the incidence of 啼声 everyday: LivingVoiceRate. (It is not currently used)




	· Mod_littleMaidMob.cfg
		I set the incidence of 啼声 everyday: LivingVoiceRate. 1.0 = 100%, 0.5 = 50%, 0.0 = 0%



Note
	-There is a possibility to be changed in the future specification.



History
	Change the description of the resource position by 20,130,829.1 client change.


	Describes the audio description of 20,120,621.1 Additions
	Describes the audio description of 20,111,125.1 Additions
	Describes the audio description of 20,111,104.1 Additions
	Add a description of 20,111,006.1 Sound Pack
	Add a description of the configuration parameters 20,110,905.1
	Corresponding to a texture pack of 20,110,817.1 voice