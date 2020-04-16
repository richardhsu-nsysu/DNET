<?php

require_once('Database.php');

$host = "localhost";
$username = "onkar";
$password = "1234";
$database_name = "dnetdummytable"; 

$db = new Database($database_name, $username, $password, $host);

// if(getOrganizationByUser(2)->count()>0){
//     echo "found org";
//     echo json_encode(getOrganizationByUser(2)->result_array());
//     echo json_encode(getOrganizationByUser(2)->row_array());
//     echo getOrganizationByUser(2)->result_array()[0]['organization_id'];
// }else{
//     echo "org not found";
    
// }
// echo json_encode(getUserAttributesByOrganization(2)->result_array());

// if(getJoinedOrganization(2)->count()>0){
//     echo "success";
    
// }else{
//     echo "failed";
// }
echo checkquery();

function checkquery(){
    $db = Database::instance();
    $query="select * from customer";
    echo $query;
    return json_encode($db->query($query)->result_array());
}


function loginAsCustomer($username,$password){
    if(getCustomer($username,$password)->count()>0){
        echo "login successful";
    }else{
        echo "unsuccessful";
    }
}



function loginAsOrganization(){
    if(getOrganization($username,$password)->count()>0){
        echo "login successful";
    }else{
        echo "unsuccessful";
    }
}

function loginAsAdmin(){
    if(getAdmin($username,$password)->count()>0){
        echo "login successful";
    }else{
        echo "unsuccessful";
    }
}

function getorgs(){
    return json_encode(getAllOrganization());
}

function getjoinorgs($id){
    return json_encode(getJoinedOrganization($id));
}


function getNOTjoinorgs($id){
    return json_encode(getNOTJoinedOrganization($id));
}

function getUserOrgAttr($customer_username,$organization_name){
    return json_encode(getUserAttributesByOrganization($customer_username,$organization_name));
}

function setCustomer($user_name,$user_email) {
    $table = 'customer';
    $random_id = randomid();
    $user_password = randomPassword();
    $fields = [
        'random_code'=>$random_id,
        'username'=> $user_name,
        'password'=> $user_password,
        'email' => $user_email
    ];
    $db = Database::instance();
    $db->insert($table, $fields);
}


function setOrganization($organization_name,$address,$email) {
    $table = 'organization';
    $random_id = randomid();
    $organization_password = randomPassword();
    $fields = [
        'random_code'=>$random_id,
        'organization_name'=> $organization_name,
        'organization_password'=> $organization_password,
        'address'=> $address,
        'email' => $email
    ];
    $db = Database::instance();
    $db->insert($table, $fields);
}


function setAdmin($admin_username,$admin_password,$admin_email) {
    $table = 'adminlist';
    $random_id = randomid();
    $fields = [
        'random_code'=>$random_id,
        'username'=> $admin_username,
        'password'=> $admin_password,
        'email' => $admin_email
    ];
    $db = Database::instance();
    $db->insert($table, $fields);
}


function getCustomer($username,$password) {
    $table = 'user';
    $where = [
        "username" => $username,
        "password" => $password
    ];

    $db = Database::instance();
    return $db->select($table, $where);
}

function getCustomerInfo($id)
{
    $table = 'user';
    $where = ['id'=>$id];
    $limit = false;
    $where_mode = false;
    $select_fields = ["id","username","email"];

    $db = Database::instance();
    return json_encode($db->select($table, $where, $limit, $order, $where_mode, $select_fields)->row_array());
}

function getAllCustomers() {
    $table = 'user';
    $where = false;
    $limit = false;
    $where_mode = false;
    $select_fields = ["id","username","email"];

    $db = Database::instance();
    return json_encode($db->select($table, $where, $limit, $order, $where_mode, $select_fields)->result_array());
}


function getAdmin($username,$password) {
    $table = 'adminlist';
    $where = [
        "username" => $username,
        "password" => $password
    ];

    $db = Database::instance();
    return $db->select($table, $where);
}

function getAllAdmin() {
    $table = 'admin';
    $where = false;
    $limit = false;
    $where_mode = false;
    $select_fields = ["id","username","email"];

    $db = Database::instance();
    return json_encode($db->select($table, $where, $limit, $order, $where_mode, $select_fields)->result_array());
}

function getOrganization($username,$password) {
    $table = 'organization';
    $where = [
        "organization_name" => $username,
        "organization_password" => $password
    ];

    $db = Database::instance();
    return $db->select($table, $where);
}

function getAllOrganization() {
    $table = 'organization';
    $where = false;
    $limit = false;
    $where_mode = false;
    $select_fields = ["id","username","email","address"];

    $db = Database::instance();
    return $db->select($table, $where, $limit, $order, $where_mode, $select_fields)->result_array();
}


function getJoinedOrganization($id){
    $table = 'organization';
    $where = "id in (SELECT organization_id from customer_org_map where user_id='".$id."')";
    $db = Database::instance();
    return $db->select($table, $where, false,false,'OR');
}

function getNOTJoinedOrganization($id){
    $table = 'organization';
    $where = "id NOT IN (SELECT organization_id from customer_org_map where user_id='".$id."')";
    $db = Database::instance();
    return $db->select($table, $where, false,false,'OR');
}


function getUserAttributesByOrganization($customer_username,$organization_name){
    $db = Database::instance();
    $organization_id = $db->select($table = 'organization' , $where = ["organization_name"=>$organization_name])->row_array();
    echo $organization_id;
    $organization_attributes = $db->select($table = 'attribute' , $where = ['organization_id'=>$organization_id], false,false,'OR');
}


function getUserByOrganization($id){
    $table = 'customer_org_map';
    $where = ["organization_id" => $id];
    $limit = false;
    $order = false;
    $where_mode = false;
    $select_fields = ["user_id"];

    $db = Database::instance();
    return $db->select($table, $where, $limit, $order, $where_mode, $select_fields)->result_array();
}

function getOrganizationByUser($id){
    $table = 'customer_org_map';
    $where = ["user_id" => $id];
    $limit = false;
    $order= false;
    $where_mode = false;
    $select_fields=$select_fields = ["organization_id",];

    $db = Database::instance();
    return $db->select($table, $where, $limit, $order, $where_mode, $select_fields);
}


function addOrganization() {
    $table = 'organization';
    $random_id = randomid();
    $organization_password = randomPassword();
    $fields = [
        'random_code'=>$random_id,
        'organization_name'=> $organization_name,
        'organization_password'=> $organization_password,
        'address'=> $address,
        'email' => $email
    ];
    $db = Database::instance();
    $db->insert($table, $fields);
}

function updateOrganization() {
}

function deleteOrganization() {
}

function banOrganization() {
}


function addCustomer() {
}

function updateUser() {
}

function deleteUser() {
}

function banUser() {
}

function setAttributes() {
}

function setAttributeValues() {
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



?>