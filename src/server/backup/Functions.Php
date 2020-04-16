<?php
// input
// TYPE : register
// GROUP : "org" | "user"
// USERNAME : XXXX
// EMAIL : XXXX@XXXXX
// ADDRESS : XXXX (empty when group is user)
// ORGNAME : XXXXXX (empty when group is user)
// process
// check the user input
// send passwd to user email
// store user info to db
// output
// echo any string to notify user
// such as "register success! please go to the e-mail box to get the password"
// or "username has exist"
// or "error : (reason)"
function registerUser($username,$email){
    $db = Database::instance();
    $random_id = randomid();
    $random_password = randomPassword();
    if ($group=='user'){

        if(!isUserExist()){
            $fields = [
                'random_code'=>$random_id,
                'username'=> $username,
                'password'=> $random_password,
                'email' => $email
                //organization
            ];
            
            if($db->insert($table = 'customer', $fields)->id>0){
                echo "Register Success! Please Check your e-mail to get the password";
            }else{
                echo "Registration Unsuccessful! Database Error";
            }
                
        }else{
            echo "customer account exist with username ".$username;
        }
        
    }
    elseif($group=='org') {
        if(!isUserExist()){
            $fields = [
                'random_code'=>$random_id,
                'username'=> $username,
                'password'=> $random_password,
                'email' => $email,
                'address' => $address
            ];
            
            if($db->insert($table = 'organization', $fields)->id>0){
                echo "Register Success! Please Check your e-mail to get the password";
            }else{
                echo "Registration Unsuccessful! Database Error";
            }
        }else{
            echo "Organization account exist with username ".$username;
        }

    }else{
        echo "Registration Unsuccessful";
    }
}

function registerUser($group,$username,$email,$address=false,$orgname=false){
    $db = Database::instance();
    $random_id = randomid();
    $random_password = randomPassword();
    if ($group=='user'){

        if(!isUserExist()){
            $fields = [
                'random_code'=>$random_id,
                'username'=> $username,
                'password'=> $random_password,
                'email' => $email
                //organization
            ];
            
            if($db->insert($table = 'customer', $fields)->id>0){
                echo "Register Success! Please Check your e-mail to get the password";
            }else{
                echo "Registration Unsuccessful! Database Error";
            }
                
        }else{
            echo "customer account exist with username ".$username;
        }
        
    }
    elseif($group=='org') {
        if(!isUserExist()){
            $fields = [
                'random_code'=>$random_id,
                'username'=> $username,
                'password'=> $random_password,
                'email' => $email,
                'address' => $address
            ];
            
            if($db->insert($table = 'organization', $fields)->id>0){
                echo "Register Success! Please Check your e-mail to get the password";
            }else{
                echo "Registration Unsuccessful! Database Error";
            }
        }else{
            echo "Organization account exist with username ".$username;
        }

    }else{
        echo "Registration Unsuccessful";
    }
}



function isUserExist($group,$username){
    if($group=='user'){
        $table = 'customer';
    }elseif($group=='org'){
        $table = 'organization';
    }else{
        $table = 'admin';
    }
    $where = [
        "username" => $username,
    ];
    if($db->select($table, $where)->count>0){
        return True;
    }
}



// input
//     TYPE : login
//     GROUP : "org" | "user" | "admin"
//     USERNAME : XXXX
//     PASSWORD : XXXXX
// process
//     check the user input
//     set session ($_SESSION['login'] = group.":".username)
// output
//     if success : echo "pass" 
//     if fail : echo any string to notify user
//     such as "username or password not match" or "error : (other reason)"

function login($group,$username,$password){
    $db = Database::instance();
    if ($group=='user'){
        $User = 'Customer'; 
    }elseif($group=='org') {
        $User = 'Organization'; 
    }elseif($group=='admin'){
        $User = 'Admin';
    }else{
        echo "Unable to Identify Group";
    }

    if(isUserExist($group,$username)){
        $where = [
            "username" => $username,
            "password" => $password
        ];
        if($db->select($table, $where,$limit=false,$order=false,$where_mode='AND')->count>0){
            session_start();
            $_SESSION['login']=$group.":".$username;
            //logSession($group,$username,$status='ACTIVE'); //TO-DO : create logSession function
            echo "pass";
        }else{
            echo "Login as ".$User." Failed! Please check Password";
        }
    }else{
        echo $User.' username Does Not Exist';
    }   
}



// input
//     TYPE : logout
// process
//     delete session
// output
//     echo "complete"

function logout(){
    //verifySession($_SESSION['login']);  //TO-DO : create verifySession function
    session_destroy();
    echo 'complete';    
}


// input
//     TYPE : get-key
//     ORGNAME : XXXXX(fixed nsysu on first stage)
// process
//     check session
//     get user private key
// output
//     echo private key in string

function getKey($organization){}
function getParameter(){}


//     input
//     TYPE : get-users
// process
//     check session (admin only)
//     get all userlist ((name1,mail1),(name2,mail2))
// output
//     echo user list in JSON string
function getUsers(){
    $db = Database::instance();
    $login_parameters = explode(':',$_SESSION['login']);
    if( strcmp($login_parameters[0],'admin')==0){
        if(isUserExist($login_parameters[0],$login_parameters[1])){
            $select_fields = ["username","email"];
            return json_encode($db->select($table='customer', $where=null, $limit=false, $order=false, $where_mode=null, $select_fields)->result_array());
        }else{
            echo 'Invalid Admin'; 
        }
    }else{
        echo "User does not belong to admin group";
    }
}


// input
//     TYPE : get-org-users
//     TARGET : XXXX(target username)
// process
//     check session (org only)
//     get all userlist ((name1,mail1),(name2,mail2))
// output
//     echo user list in JSON string
function getOrgUsers($username){
    $db = Database::instance();
    $login_parameters = explode(':',$_SESSION['login']);
    if( strcmp($login_parameters[0],'org')==0){
        if(isUserExist($login_parameters[0],$login_parameters[1])){
            $select_fields = ["username","email"];
            return json_encode($db->select($table='customer', $where=null, $limit=false, $order=false, $where_mode=null, $select_fields)->result_array());
        }else{
            echo 'Invalid Organization'; 
        }
    }else{
        echo "User does not belong to Organization group";
    }
}



//     input
//     TYPE : get-orgs
// process
//     check session (admin only)
//     get all orglist ((orgname1,mail1,address1),(orgname2,mail2,address2))
// output
//     echo org list in JSON
function getOrgs(){
    $db = Database::instance();
    $login_parameters = explode(':',$_SESSION['login']);
    if( strcmp($login_parameters[0],'admin')==0){
        if(isUserExist($login_parameters[0],$login_parameters[1])){
            $select_fields = ["username","email","address"];
            return json_encode($db->select($table='organization', $where=null, $limit=false, $order=false, $where_mode=null, $select_fields)->result_array());
        }else{
            echo 'Invalid User'; 
        }
    }else{
        echo "User does not belong to admin group";
    }
}


//     input
//     TYPE : get-join-orgs
//     TARGET : XXXX(target username)
// process
//     check session (user,admin only)
//     get org of paricular user have joined
//     orglist((orgname1,mail1,address1),(orgname2,mail2,address2))
// output
//     echo org list in JSON

function getJoinOrgs($username){
    $db = Database::instance();
    $id = $db->select($table="customer",$where=["username"=>$username])->row_array()['id'];
    $select_fields=['username','email','address'];
    $where = "id IN (SELECT organization_id from customer_org_map where user_id='".$id."')";
    return json_encode($db->select($table='organization', $where,$limit=false,$order=false,$where_mode="OR",$select_fields)->result_array());
}

// input
//     TYPE : get-notjoin-orgs
//     TARGET : XXXX(target username)
// process
//     check session (user,admin only)
//     get org of paricular user not joined
//     orglist((orgname1,mail1,address1),(orgname2,mail2,address2))
// output
//     echo org list in JSON


function getNotJoinOrgs($username){
    $db = Database::instance();
    $id = $db->select($table="customer",$where=["username"=>$username])->row_array()['id'];
    $where = "id NOT IN (SELECT organization_id from customer_org_map where user_id='".$id."')";
    return json_encode($db->select($table='organization', $where,$limit=false,$order=false,$where_mode="OR",$select_fields=['username','email','address']));
}



// input
//     TYPE : get-user-attr
//     USERNAME : XXXX
//     ORGNAME : XXXXX
// process
//     check session
//     get user attr
//     echo user attr JSON string
//     [{
//         name:'age',
//         type:'range',
//         value:30
//     },{
//         name:'department',
//         type:'option',
//         value:'CSE'
//     },{
//         name:'address',
//         type:'text',
//         value:'XXXXXXXXXXXX'
//     },{
//         name:'phone',
//         type:'number'
//         value:'09XXXXXXXX'
//     }]
function getUserOrgAttr($username,$organization){
    $db = Database::instance();
    $user_id = $db->select($table="customer",$where=["username"=>$username])->row_array()['id'];
    $organization_id = $db->select($table="organization",$where=["username"=>$username])->row_array()['id'];
    $query = 'SELECT attribute.attribute_name,attribute.type_id,value.value FROM (
        (customer_value_map INNER JOIN 
    ';//issue
}
function getOrgAttr(){}
function getOrgName(){}
function setUserAttr(){}
function getOrgInfo(){}

?>
