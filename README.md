# BattleBotServer
De Battlebot Java server

# Front End
Poort voor TCP verbinding PHP 1337. De commando's kunnen met behulp van de PHP wrapper of eigen TCP socket implementatie worden verstuurd.

# Commando Beschrijving Front End 
    getConnectedBots Geeft een JSON array met alle verbonden bots (BotMACAdrr’s)
    BotMac,1000000 Vooruit
    BotMac,0100000 Achteruit
    BotMac,0010000 Links
    BotMac,0001000 Rechts
    BotMac,0000100 Autonoom rijden
    BotMac,0000010 Commando voor eigen gebruik
    BotMac,0000001 Commando voor eigen gebruik

# Commando's Server
    add <MAC> - Add a Battleboi to the list
    remove <MAC> - Remove a Battleboi from the list
    list - Show list of bots
    lookatme - Take control of this ship, you are the captain now
    cmd <MAC> <command> - Sent command to battlebot
    reset <MAC> - Restart connection to Battlebot when not responding
    help - Show this list
