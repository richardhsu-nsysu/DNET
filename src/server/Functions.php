<?php

    $expire_time = 300;
    start_session($expire_time);

    //connect to database
    $dbhost = "localhost";
    $dbusername = "root";
    $dbpassword = "";
    $database_name = "dnetdummytable"; 
    $db = new Database($database_name, $dbusername, $dbpassword, $dbhost);
    
    function get($p){
        if(isset($_GET[$p]) && !empty($_GET[$p])){
            return $_GET[$p];
        } else {
            echo "Error : parameter ".$p." not set or empty";
            die();
        }
    }

    function session($p){
        if(isset($_SESSION[$p]) && !empty($_SESSION[$p])){
            return $_SESSION[$p];
        } else {
            echo "Error : session ".$p." not set or empty";
            die();
        }
    }

    function cookie($p){
        if(isset($_SESSION[$p]) && !empty($_SESSION[$p])){
            return $_SESSION[$p];
        } else {
            echo "Error : cookie ".$p." not set or empty";
            die();
        }
    }
    
    function start_session($expire = 0)
    {
        if ($expire == 0) {
            $expire = ini_get('session.gc_maxlifetime');
        } else {
            ini_set('session.gc_maxlifetime', $expire);
        }
    
        if (empty($_COOKIE['PHPSESSID'])) {
            session_set_cookie_params($expire);
            session_start();
        } else {
            session_start();
            setcookie('PHPSESSID', session_id(), time() + $expire);
        }
    }

    function checkSession($p = 'n'){
    //mode n  : normal
    //mode po : session orgname match request orgname
    //mode pu : session username match request username
        if(session("prev_req_id") != cookie("prev_req_id")){
            echo "Session : prev_req_id not match";
            die();
        }
        global $expire_time;
        if(session("last_request")+$expire_time < time()){
            echo "Session : prev_req_id expired";
            die();
        }
    }

    function randomid() {
        $alphabet = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $pass = array(); //remember to declare $pass as an array
        $alphaLength = strlen($alphabet) - 1; //put the length -1 in cache
        for ($i = 0; $i < 8; $i++) {
            $n = rand(0, $alphaLength);
            $pass[] = $alphabet[$n];
        }
        return implode($pass); //turn the array into a string
    }

    function randomPassword() {
        $alphabet = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890';
        $pass = array(); //remember to declare $pass as an array
        $alphaLength = strlen($alphabet) - 1; //put the length -1 in cache
        for ($i = 0; $i < 8; $i++) {
            $n = rand(0, $alphaLength);
            $pass[] = $alphabet[$n];
        }
        return implode($pass); //turn the array into a string
    }
    
    function sendPasswdMail($passwd,$email){
        $mail = new PHPMailer;
        $mail->isSMTP();                                    // Set mailer to use SMTP
        $mail->Host = gethostbyname('smtp.gmail.com');      // Specify main and backup SMTP servers
        $mail->SMTPAuth = true;                             // Enable SMTP authentication
        $mail->Username = 'iccsl.nsysu@gmail.com';          // SMTP username
        $mail->Password = 'profrichiccsl';                  // SMTP password
        $mail->SMTPSecure = 'tls';                          // Enable TLS encryption, `ssl` also accepted
        $mail->Port = 587;                                  // TCP port to connect to
        $mail->SMTPAutoTLS = false;
        $mail->SMTPOptions = array(
            'ssl' => array(
                'verify_peer' => false,
                'verify_peer_name' => false,
                'allow_self_signed' => true
            )
        );
        $mail->setFrom('iccsl.nsysu@gmail.com', 'NETS');
        $mail->addReplyTo('iccsl.nsysu@gmail.com', 'NETS');
        $mail->addAddress($email);                          // Add a recipient
        $mail->isHTML(true);                                // Set email format to HTML
        $bodyContent = '<b>Thankyou for the registration.</b>';
        $bodyContent .= '<br/><b>Password for your account is : &nbsp;&nbsp;&nbsp;</b>';
        $bodyContent .= $passwd;
        $mail->Subject = 'Email from NETS';
        $mail->Body    = $bodyContent;

        if($mail->send()){
            return 'ok';
        }else{
            return $mail->ErrorInfo;
        }
    }



    

?>
