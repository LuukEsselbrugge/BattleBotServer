# BattleBotServer
De Battlebot Java server

# Front End
De commando's kunnen met behulp van de PHP wrapper (in folder PHPExample) worden verzonden

# Voorbeeld PHP Front End
    <?php
    //Battlebot example code PHP
    require_once "BattleBot.php";

    $botList = new BattleBotList();
    $botArray = $botList->getList();
    var_dump($botArray);

    $battleBot = new BattleBot($botArray[0]);
    $error = $battleBot->sendBotCommand(BOT_FORWARD);
    $error = $battleBot->sendBotCommand(BOT_LEFT);
    $error = $battleBot->sendBotCommand(BOT_STOP);

    echo $error;

    ?>
# Commando's Front End
    BOT_FORWARD
    BOT_BACKWARD
    BOT_LEFT
    BOT_RIGHT
    BOT_AUTO
    BOT_CUSTOM1
    BOT_CUSTOM2
    BOT_STOP

# Commando's Server
    add <MAC> - Add a Battleboi to the list
    remove <MAC> - Remove a Battleboi from the list
    list - Show list of bots
    lookatme - Take control of this ship, you are the captain now
    cmd <MAC> <command> - Sent command to battlebot
    reset <MAC> - Restart connection to Battlebot when not responding
    help - Show this list
