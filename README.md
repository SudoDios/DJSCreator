# DJSCreator
DJSCreator is a summary of "Desktop jar shortcut creator".
This is a simple app to create a launcher for your jar application in Unix Desktops.
## How does it work?
1. Get from user a icon, name, jar executable path.
2. Create app folder in `~/apps`.
3. Copy jar file and icon if exist to app directory.
4. Create a shell script file in folder called `runner`
```shell
cd home/yourprofile/apps/appfolder
java -jar appname.jar
```
5. Create a appname.desktop file in `~/.local/share/applications`
6. And the last step link files in .desktop file.
```shell
Name=appName
Exec=shellScriptPath
Terminal=true or false
Categories=inputCategory
Type=Application
Comment=userInputComment
```
7. You see your desktop icon in apps menu
## License
Copyright (C) 2021 Sudo Dios

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
