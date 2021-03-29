# Minecraft HTTP mod

## Installation

- Download Minecraft Forge at the official website.
- Make sure you double check your version, so you download the correct one. The list of versions can be found along the left hand side.
- Open the downloaded installer, which will bring up the installation process. Be sure to confirm with ‘Install Client’ still selected and press ‘OK’.
- Place the plugin jar archive (you can download it [here](https://github.com/Thyvador/minecraft-http-mod/releases/download/1.0.0/twitvhhttpmod-1.0.jar)) in `%AppData%\.minecraft\mods\` for Windows users or `~/.minecraft/mods/` for linux users

## Develop

See the Forge Documentation online for more detailed instructions:
http://mcforge.readthedocs.io/en/latest/gettingstarted/

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: You're left with a choice.
If you prefer to use Eclipse:
1. Run the following command: "gradlew genEclipseRuns" (./gradlew genEclipseRuns if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder 
   or run "gradlew eclipse" to generate the project.
(Current Issue)
4. Open Project > Run/Debug Settings > Edit runClient and runServer > Environment
5. Edit MOD_CLASSES to show [modid]%%[Path]; 2 times rather then the generated 4.

If you prefer to use IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: "gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not affect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.
or the Forge Project Discord discord.gg/UvedJ9m
