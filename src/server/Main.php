<?php

    require 'PHPMailer/PHPMailerAutoload.php';
    require 'Database.php';
    require 'Functions.php';

    

    $TYPE = get('TYPE');

    //handle requests - done
    if($TYPE == "get-html"){
        //127.0.0.1/DNETServer/Main.php?TYPE=register&GROUP=user&USERNAME=OOO&EMAIL=richardwang1134@gmail.com
        //127.0.0.1/DNETServer/Main.php?TYPE=register&GROUP=org&USERNAME=OOO&EMAIL=richardwang1134@gmail.com&ADDRESS=XXXX&ORGNAME=XXXXX
        $fileName = get('FILENAME');
        echo file_get_contents('html/'.$fileName);
        die();
    }

    if($TYPE == "register"){
        //127.0.0.1/DNETServer/Main.php?TYPE=register&GROUP=user&USERNAME=OOO&EMAIL=richardwang1134@gmail.com
        //127.0.0.1/DNETServer/Main.php?TYPE=register&GROUP=org&USERNAME=OOO&EMAIL=richardwang1134@gmail.com&ADDRESS=XXXX&ORGNAME=XXXXX

        //shared parameter
        $GROUP = get('GROUP');
        $username = get('USERNAME');
        $where = ["username" => $username];
        $email = get('EMAIL');
        $passwd = randomPassword();
        $randomid = randomid();
        //respective parameter
        $table = "";
        $fields = [];
        if($GROUP == "user"){
            $table = 'customer';
            $fields = [
                'random_code'=> $randomid,
                'username'=> $username,
                'password'=> $passwd,
                'email' => $email
            ];
        }
        if($GROUP == "org"){
            $table = 'organization';
            $fields = [
                'random_code'=> $randomid,
                'username'=> $username,
                'password'=> $passwd,
                'email' => $email,
                'address' => get('ADDRESS'),
                'orgname' => get('ORGNAME')
            ];
        }
        //check if exist same username
        if($db->select($table, $where)->count()>0){
            echo "The username have already registered";
            die();
        }
        //send password mail
        $result = sendPasswdMail($passwd,$email);
        if($result!="ok"){
            echo "Send password e-mail failed, please check you e-mail address";
            die();
        }
        //register
        if($db->insert($table, $fields)){
            echo "Register Success! Please Check your e-mail to get the password";
        }else{
            echo "Registration Failed! Database Error";
        }
        die();
    }

    if($TYPE == "login"){
        //127.0.0.1/DNETServer/Main.php?TYPE=login&GROUP=user&USERNAME=OOO&PASSWORD=XXX
        
        //shared parameter
        $GROUP = get('GROUP');
        $USERNAME = get('USERNAME');
        $where = [
            "username" => $USERNAME,
            "password" => get('PASSWORD')
        ];

        //respective parameter
        $table = "";
        if($GROUP == "user"){
            $table = "customer";
        }else if($GROUP == "org"){
            $table = "organization";
        }else if($GROUP == "admin"){
            $table = "administrator";
        }else{
            echo 'Group error';
            die();
        }

        //check if user and password pair exist
        if($db->select($table, $where)->count()<=0){
            echo 'Login failed! Please check you username and password!';
            die();
        }
        echo 'pass';

        //set session
        $last_request = time();
        $prev_rq_id = randomid();
        $_SESSION["username"] = $USERNAME;
        $_SESSION["group"] = $GROUP;
        $_SESSION["last_request"] = time();
        $_SESSION["prev_req_id"] = randomid();
        setcookie(
            'prev_req_id',
            $prev_rq_id,
            ['samesite' => 'Strict']
        );
        die();
    }

    if($TYPE == "logout"){
        setcookie('PreviousRequestId', "", time() - 3600);
        session_destroy();
        echo 'complete';    
        die();
    }

    if($TYPE == "get-org-name"){
        // {TYPE:get-org-name}
        checkSession();
        $username = session('username');
        $orgname = $db->select(
            "organization", 
            ["username" => $username],
            FALSE, // limit
            FALSE, // order
            "AND", // where_mode
            "orgname"//select_fields
        )->result_array();
        if(empty($orgname[0]['orgname'])){
            echo 'Error : get orgname failed';
            die();
        }
        echo $orgname[0]['orgname'];
        die();
    }

    if($TYPE == "get-org-attr"){
        //127.0.0.1/DNETServer/Main.php?TYPE=get-org-attr&ORGNAME=test
        checkSession();
        $ORGNAME = get('ORGNAME');
        //get attribute names
        $names = $db->select(
            "attribute", 
            ["orgname" => $ORGNAME],
            FALSE, // limit
            FALSE, // order
            "AND", // where_mode
            "name" //select_fields
        )->result_array();
        $name_array = [];
        foreach($names as $name){
            array_push($name_array,$name['name']);
        }
        $unique_names = array_unique($name_array);
        //gen attribute name : value array
        $attr_array = [];
        foreach($unique_names as $name){
            $values = $db->select(
                "attribute", 
                [   "orgname" => $ORGNAME,
                    "name" => $name
                ],
                FALSE, // limit
                FALSE, // order
                "AND", // where_mode
                "value" //select_fields
            )->result_array();
            $value_array = [];
            foreach($values as $value){
                array_push($value_array,$value["value"]);
            }
            array_push($attr_array,["name"=>$name,"value"=>$value_array]);
        }
        echo json_encode($attr_array);
        die();
    }

    if($TYPE == "set-org-attr"){
        //127.0.0.1/DNETServer/Main.php?TYPE=set-org-attr&ORGNAME=test&NAME=age&VALUE=10~20,21~30,31~40,41~50,61~70
        checkSession("po");
        $ORGNAME = get('ORGNAME');
        $NAME = get('NAME');
        $VALUE = explode(',',get('VALUE'));
        //delete old attr
        $db->delete(
            "attribute",
            ["name"=>$NAME,"orgname"=>$ORGNAME]
        );
        
        //insert new attr
        foreach($VALUE as $value){
            $db->insert(
                "attribute",
                [   "name"=>$NAME,
                    "orgname"=>$ORGNAME,
                    "value"=> $value,
                    "id"=>randomid()
                ]
            );
        }
        echo 'ok';
        die();
    }

    if($TYPE == "del-org-attr"){
        //127.0.0.1/DNETServer/Main.php?TYPE=del-org-attr&ORGNAME=test&NAME=age
        $ORGNAME = get('ORGNAME');
        $NAME = get('NAME');
        //delete old attr
        $db->delete(
            "attribute",
            ["name"=>$NAME,"orgname"=>$ORGNAME]
        );
        echo 'ok';
        die();
    }

    if($TYPE == "get-join-orgs"){
        //check session
        checkSession("pu");
        //get joined orgname
        $orgs = $db->select(
            "customer_attribute", 
            ["username"=>get('USERNAME')],
            FALSE, // limit
            FALSE, // order
            "AND", // where_mode
            "orgname"//select_fields
        )->result_array();
        $org_array = [];
        foreach($orgs as $item){
            array_push($org_array,$item["orgname"]);
        }
        $org_array = array_unique($org_array);
        $org_array = array_values($org_array);
        if(empty($org_array)){
            echo "[]";
            die();
        }
        //get joined org info
        $orgs = $db->select(
            "organization", 
            ["orgname"=>$org_array],
            FALSE, // limit
            FALSE, // order
            "AND", // where_mode
            "orgname,address,email"//select_fields
        )->result_array();
        echo json_encode($orgs);
        die();
    }

    if($TYPE == "get-notjoin-orgs"){
        //check session
        checkSession("pu");
        //get all orgs
        $all_orgs = $db->select(
            "organization", 
            [],
            FALSE, // limit
            FALSE, // order
            "AND", // where_mode
            "orgname,address,email"//select_fields
        )->result_array();
        //get joined orgs
        $join_orgs = $db->select(
            "customer_attribute", 
            ["username"=>get('USERNAME')],
            FALSE, // limit
            FALSE, // order
            "AND", // where_mode
            "orgname"//select_fields
        )->result_array();
        //get all orgs - joined orgs
        $not_join_orgs = [];
        foreach($all_orgs as $all_org){
            $pass = True;
            foreach($join_orgs as $join_org){
                if($join_org['orgname']==$all_org['orgname']){
                    $pass = False;
                }
            }
            if($pass){
                array_push($not_join_orgs,$all_org);
            }
        }
        echo json_encode($not_join_orgs);
        die();
    }

    if($TYPE == "set-user-attr"){
        checkSession("pu");
        $username = get('USERNAME');
        $orgname = get('ORGNAME');
        $db->delete(
            "customer_attribute",
            [   "username"=>$username,
                "orgname"=>$orgname,
            ]
        );
        $DATA = json_decode(get('DATA'));
        foreach($DATA as $data){
            //get attribute
            $result = $db->select(
                "attribute",
                [   "orgname"=>$orgname,
                    "name"=>$data->name,
                    "value"=>$data->value
                ]
            )->result_array();
            //insert id to cust_attr
            $db->insert(
                "customer_attribute",
                [   "username"=>$username,
                    "orgname"=>$orgname,
                    "attribute_id"=>$result[0]['id']
                ]
            );
        }
        echo 'ok';
        die();
    }

    if($TYPE == "get-user-attr"){
        checkSession("pu");
        $result = $db->select(
            "customer_attribute",
            [   "username"=>get('USERNAME'),
                "orgname"=>get('ORGNAME')
            ]
        )->result_array();
        
        $id_array = [];
        foreach($result as $r){
            array_push($id_array,$r["attribute_id"]);
        }
        $result = $db->select(
            "attribute",
            ["id"=>$id_array]
        )->result_array();
        $array = [];
        foreach($result as $r){
            $item = ["name"=>$r["name"],"value"=>$r["value"]];
            array_push($array,$item);
        }
        echo json_encode($array);
        die();
    }

    if($TYPE == "get-org-user"){
        $orgname = get("ORGNAME");
        $users = $db->select(
            "organization_customer",
            [   "orgname"=>$orgname
            ]
        )->result_array();
        $array = [];
        foreach($users as $user){
            array_push($array,$user["username"]);
        }
        if(empty($array)){
            echo "[]";
            die();
        }
        $users = $db->select(
            "customer",
            [   "username"=>$array
            ]
        )->result_array();
        echo json_encode($users);
        die();
    }

    if($TYPE == "user-join-org"){
        checkSession("pu");
        $username = get("USERNAME");
        $orgname = get("ORGNAME");
        $db->delete(
            "organization_customer",
            [   "orgname"=>$orgname,
                "username"=>$username
            ]
        );
        $db->insert(
            "organization_customer",
            [   "orgname"=>$orgname,
                "username"=>$username
            ]
        );
        echo "ok";
        die();
    }

    if($TYPE == "del-org-user"){
        checkSession();
        $username = get("USERNAME");
        $orgname = get("ORGNAME");
        $db->delete(
            "organization_customer",
            [   "orgname"=>$orgname,
                "username"=>$username
            ]
        );
        $db->delete(
            "customer_attribute",
            [   "orgname"=>$orgname,
                "username"=>$username
            ]
        );
        echo "ok";
        die();
    }

    if($TYPE == "get-orgs"){
        checkSession();
        $result = $db->select("organization",[],False,False,"AND",'address,email,orgname')->result_array();
        echo json_encode($result);
        die();
    }

    if($TYPE == "set-org-policy"){
        checkSession();
        $username = get("USERNAME");
        $orgname = get("ORGNAME");
        $data = json_decode(get("DATA"));
        //delete old
        $db->delete(
            "organization_policy",
            [   "orgname"=>$orgname
            ]
        );
        //set new policy
        foreach($data as $d){
            $db->insert(
                "organization_policy",
                [   "name"=>$d->name,
                    "orgname"=>$orgname,
                    "option1"=>$d->options[0],
                    "option2"=>$d->options[1],
                    "option3"=>$d->options[2]
                ]
            );
        }
        echo("ok");
        die();
    }

    if($TYPE == "get-public-key"){
        //http://127.0.0.1/DNETServer/main.php?TYPE=get-public-key&ORGNAME=test
        //get policy
        $policy = $db->select(
            "organization_policy", 
            ["orgname" => get("ORGNAME")]
        )->result_array();
        //generate command string
        $s = "cd key && java -cp .;jpbc-api-2.0.0.jar.;jpbc-plaf-2.0.0.jar PolicyVector ";
        foreach($policy as $p){
            $s = $s." ".$p["option1"]." ".$p["option2"]." ".$p["option3"];
        }
        //exec java
        exec($s);
        //read mpk,csk,vector
        echo file_get_contents('key/PolicyVector.txt');
        die();
    }

    if($TYPE == "get-mpk"){
        echo file_get_contents('key/mpk.txt');
        die();
    }

    if($TYPE == "get-csk"){
        echo file_get_contents('key/csk.txt');
        die();
    }

    if($TYPE == "get-private-key"){
        //http://127.0.0.1/DNETServer/main.php?TYPE=get-private-key&USERNAME=test&ORGNAME=test
        //get attr
        $result = $db->select(
            "customer_attribute",
            [   "username"=>get('USERNAME'),
                "orgname"=>get('ORGNAME')
            ]
        )->result_array();
        
        $id_array = [];
        foreach($result as $r){
            array_push($id_array,$r["attribute_id"]);
        }
        $attribute = $db->select(
            "attribute",
            ["id"=>$id_array]
        )->result_array();
        //generate command string
        $s = "cd key && java -cp .;jpbc-api-2.0.0.jar.;jpbc-plaf-2.0.0.jar PrivateKey ";
        foreach($attribute as $a){
            $s = $s." ".$a["id"];
        }
        //exec java
        exec($s);
        //read mpk,csk,vector
        echo file_get_contents('key/PrivateKey.txt');
        die();
    }
?>