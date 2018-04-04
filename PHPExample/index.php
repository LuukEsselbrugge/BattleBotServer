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