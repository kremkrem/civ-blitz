# Civ Blitz Readme

Welcome to Civ Blitz! This is a web application used to generated custom 
factions for Sid Meier's Civilization VI by mixing and matching abilities
found in the base game and official expansions and DLC. Of course, if you try
to use something from some DLC you do not own, it will not work.

# The app is available right now at: https://civ6blitz.app/

Go ahead and check it out!

--------------------------------------------------------------------------------

## Standalone deployment

This readme is provided to give instructions on how to run Civ Blitz on your
own machine, as the webapp hosting of the original Civ Blitz site
is no longer free, and there was not quite enough demand to be worth paying for it.

### Prerequisites

This is a web application written in Java, so all you really need is to
have a Java runtime installed to run the packaged file provided.

I recommend downloading the latest version of Adoptium's OpenJDK distribution from:

### https://adoptium.net/

There should be a button labelled "Latest LTS Release" or similar. The version should be 17.x.x or later.
Download and install that. Be sure to have the "Add to PATH" option enabled 
(it is enabled by default so don't worry) and "Associate .jar".

Once done, open a command prompt anywhere and run `java -version`, and you should get 
output somewhat like the following:
```
openjdk version "17.0.5" 2022-10-18
OpenJDK Runtime Environment Temurin-17.0.5+8 (build 17.0.5+8)
OpenJDK 64-Bit Server VM Temurin-17.0.5+8 (build 17.0.5+8, mixed mode, sharing)
```

If not, you have not installed Java and made it available on your system PATH. 
Either follow the steps above again or refer to Google.

### Downloading and Running

- Go to the Civ Blitz releases page at https://github.com/rossturner/civ-blitz/releases
- Download civblitz.jar from the latest release (the one to the top of the page)
- Put this somewhere on your computer and open a command prompt to this location
- Run `java -jar civblitz.jar` to launch the jar file using your Java (17) runtime
- If  successful, you should see a bunch of stuff printed to the command line, beginning with a funky CivBlitz ascii art
banner
- Go to http://localhost:8080/ in your browser

You *could* just double-click the .jar file to run it, but this will leave Civ Blitz running as a 
background process until you either restart your computer, or kill the process called "OpenJDK Platform binary".

Note that you'll have to come back and manually download any new versions of Civ Blitz,
if we create any (such as for new DLC leaders).

--------------------------------------------------------------------------------

## Development and running from source

Feel free to make pull requests to this repository.

The entire project builds using Maven wrapper `mvnw` (that calls upon stuff like frontend tools when necessary).

### Run the site - quick and easy edition

This will run a version of the site with only the ModTester and Single Draft enabled.

To run the project from source, do the following:
```shell
export
./mvnw spring-boot:run -P dev,offline
```

### Run the site - full and tricky edition

If you'd like to develop on a full deployment of the website, prepare the following: 

1. Deploy a database (preferably a Postgres database, on port 5432).
    * If needed, make a new database called 'imperium' and a new schema called 'imperium'. Leave the schema empty.
    * If you insisted on using a database other than Postgres, please edit `src/main/resources/application-dev.yml` file
    to use proper DB driver, url and credentials for the database.
2. Create a new app on Discord development website: https://discord.com/developers/applications.
3. Create a Google Cloud project (hopefully we'll cut this dependency out soon). 
    * You can create a project FOR FREE at https://cloud.google.com
    * Your dev deployment does NOT need any server or service running in the cloud, so it doesn't generate any costs.
      This way you can KEEP THE PROJECT FREE OF CHARGE. 
    * **Important:** activate Google Sheets API (in the "APIs & Services" tab). By default, the project has most APIs 
      disabled (to keep the chaos, confusion and costs at minimum).
    * Create credentials (in the "APIs & Services > Credentials" tab).
4. Set all credentials in environment variables:
    * `SPRING_DATASOURCE_PASSWORD` for password to the database created in step 1.
    * `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_DISCORD_CLIENT_ID` for APPLICATION ID of your Discord app, as seen on 
      "General Information" tab.
    * `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_DISCORD_CLIENT_SECRET` for CLIENT SECRET of your Discord app, obtainable
      from the "OAuth2" tab (don't lose it and PROTECT IT; it's like a master password to your app; once obtained, the dev
      site won't show it again, and only lets you reset it if you lose it).
    * `GOOGLE_API_KEY` from "APIs & Services > Credentials" in your Google Cloud project.

Finally, run:
```shell
export
./mvnw spring-boot:run -P dev
```

This will have all features enabled - registering new accounts, setting preferences, opening card packs, and - if you 
manually set field `is_admin` to 'true' for someone in the `player` table of the database - create, run and resolve 
Blitz matches.