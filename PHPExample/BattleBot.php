<?php
//Created by: Luuk
//Date: 04-04-2018
define("BOT_FORWARD", "1000000");
define("BOT_BACKWARD", "0100000");
define("BOT_LEFT", "0010000");
define("BOT_RIGHT", "0001000");
define("BOT_AUTO", "0000100");
define("BOT_CUSTOM1", "0000010");
define("BOT_CUSTOM2", "0000001");
define("BOT_STOP", "0000000");

class BattleBot {

    private $PORT = 1337;
    private $SERVER = "127.0.0.1";
    private $MAC;

    function __construct($macAdd = "")
    {
        $this->MAC = $macAdd;
    }

    function sendBotCommand($Command)
    {
        $fp = fsockopen($this->SERVER, $this->PORT, $errno, $errstr, 10);
        $result = "";
        if (!$fp) {
            return "$errstr ($errno)";
        } else {
            fwrite($fp, $this->MAC.",".$Command."\n");
            while (!feof($fp)) {
                $result = fgets($fp, 128);
            }
            fclose($fp);
            if($result == "OK"){
                return false;
            }else{
                return $result;
            }
        }
    }
}

class BattleBotList {
    private $PORT = 1337;
    private $SERVER = "127.0.0.1";

    function getList()
    {
        $fp = fsockopen($this->SERVER, $this->PORT, $errno, $errstr, 10);
        $result = "";
        if (!$fp) {
            return "$errstr ($errno)";
        } else {
            fwrite($fp, "getConnectedBots"."\n");
            while (!feof($fp)) {
                $result = fgets($fp, 128);
            }
            fclose($fp);
            return json_decode($result, true);
        }
    }
}

?>